package com.followorkback.followorkback.controller;

import com.followorkback.followorkback.entity.*;
import com.followorkback.followorkback.repository.EtudeAnalysisRepository;
import com.followorkback.followorkback.repository.EtudeRepository;
import com.followorkback.followorkback.repository.MonitorRepository;
import com.followorkback.followorkback.service.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/etude")
@RequiredArgsConstructor
@Slf4j
public class EtudeController {

    private final EtudeServiceImpl etudeService;
    private final UserService userService;
    private final EtudeAnalysisServiceImpl etudeAnalysisService;
    private final EtudeAnalysisRepository etudeAnalysisRepository;
    private final EtudeRepository etudeRepository;
    private final MonitorServiceImpl monitorService;
    private final MonitorRepository monitorRepository;

    @GetMapping("/etudes")
    public ResponseEntity<List<?>> getEtudes(){
        log.info("Getting all etudes ");


        return ResponseEntity.ok().body(etudeRepository.findAllJoinAnalysis());
    }

    @GetMapping("/etudes/analyst/{username}")
    public ResponseEntity<List<?>> getUserEtudes(@PathVariable String username){

        List<?> allUserEtudes = etudeRepository.findAnalystJoinAnalysis(username);

        return ResponseEntity.ok().body(allUserEtudes);
    }


    @DeleteMapping(value= "/delete/{genericCode}/{username}")
    public ResponseEntity<List<?>> deleteEtude(@PathVariable String genericCode, @PathVariable String username){
        // Find corresponding

        log.info("Deleting records for dossier id {}", genericCode);
        try{
            etudeRepository.deleteAllByGenericCode(genericCode);
            etudeAnalysisRepository.deleteAllByEtudeGeneric(genericCode);
            monitorRepository.deleteAllByDossierId(genericCode);
        }catch (Exception e){
            log.info("Error deleting. Error:  {}", e.getMessage());
            return ResponseEntity.status(500).body(new ArrayList<>());
        }

        List<?> allUserEtudes = etudeRepository.findAnalystJoinAnalysis(username);
        log.info("Getting all etudes for user {} ", username);

        return ResponseEntity.ok().body(allUserEtudes);

    }

    @PostMapping("/analysis/update/{username}/{action}")
    public ResponseEntity<List<?>> updateAnalysis(@RequestBody Analysis analysis, @PathVariable String username, @PathVariable String action){

        log.info("Updating analysis");

        String generic_code = analysis.getEtudeGeneric();
        EtudeAnalysis etudeAnalysis = etudeAnalysisRepository.findByEtudeGeneric(generic_code);

        String comment = analysis.getComment();

        etudeAnalysis.setAmount_given(analysis.getAmount_given());
        etudeAnalysis.setDate_comity_dri(analysis.getDate_comity_dri());
        etudeAnalysis.setDeliverables(analysis.getDeliverables());
        etudeAnalysis.setPerspectives(analysis.getPerspectives());
        etudeAnalysis.setLink(analysis.getLink());

        if ( action == "close"){
            etudeAnalysis.setStatus(Status.CLOSE_DEMAND);

            if (comment != null){
                Monitor monitor = new Monitor();
                monitor.setUsername(etudeAnalysis.getUsername());
                monitor.setDossierId(generic_code);
                monitor.setComment(comment);
                monitor.setStatus(Status.CLOSE_DEMAND);

                monitorRepository.save(monitor);
            }
        }

        etudeAnalysisRepository.save(etudeAnalysis);


        return ResponseEntity.ok().body(etudeRepository.findAnalystJoinAnalysis(username));
    }


    @PostMapping("/update/{username}")
    public ResponseEntity<List<?>> updateEtude(@RequestBody EtudeEdit etude, @PathVariable String username){
        log.info("Updating etude {}", etude);

        User userAnalyst = userService.getUser(etude.getAnalystEdit());
        User userManager = userService.getUser(etude.getUsernameManagerEdit());
        String generic_code = etude.getGenericCodeEdit();
        Etude new_etude = etudeRepository.findByGenericCode(generic_code);

        new_etude.setGenericCode(generic_code);
        new_etude.setUserManager(userManager);
        new_etude.setUnit(etude.getUnitEdit());
        new_etude.setUserAnalyst(userAnalyst);
        new_etude.setName(etude.getNameEdit());
        new_etude.setNeededAmount(etude.getNeededAmountEdit());
        new_etude.setRecipients(etude.getTypeEdit());
        new_etude.setDomain(etude.getDomainEdit());
        new_etude.setStart_date(etude.getStart_dateEdit());
        new_etude.setExpected_end_date(etude.getExpected_end_dateEdit());
        new_etude.setOrigin(etude.getOriginEdit());
        new_etude.setFirst_cote(etude.getFirst_coteEdit());
        new_etude.setType(etude.getTypeEdit());

        ArrayList<User> supports = new ArrayList();

        ArrayList supportRequest = etude.getSupportsEdit();

        if (supportRequest != null)
        {
            etude.getSupportsEdit().forEach((support) -> {

                // Adding potential supports
                try{
                    supports.add(userService.getUser((String)support));
                }catch (Exception e){
                    log.info("Erreur get support : {]", e.getMessage());
                }
            });

            new_etude.setSupports(supports);
        }

        try{
            etudeService.saveEtude(new_etude);
        }catch (Exception e){
            log.info("error saving {}", e.getMessage());
            return ResponseEntity.status(500).body(new ArrayList<>());
        }

        return ResponseEntity.ok().body(etudeRepository.findAnalystJoinAnalysis(username));
    }


    @PostMapping("/save/{username}")
    public ResponseEntity<List<?>> saveEtude(@RequestBody EtudeSaveNew etude, @PathVariable String username){
        log.info("Saving new etude {}", etude);

        // 1- Saving Etude

        Etude new_etude = new Etude();
//        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String timeStampDate = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        String[] analyst = etude.getAnalyst().split("_");

        List<?> alletudes = etudeRepository.findAnalystJoinAnalysis(analyst[0] +  "_" + analyst[1]);

        String generic_code = analyst[1].charAt(0) + "" + analyst[0].charAt(0) + "-etud" +  "-" + (alletudes.size() + 1) + "-" + timeStampDate;

        log.info("manager : {}", etude.getUsernameManager());
        log.info("analyst : {}", etude.getAnalyst());

        log.info("generic code: {}", generic_code);

        User userAnalyst = userService.getUser(etude.getAnalyst());
        User userManager = userService.getUser(etude.getUsernameManager());

        new_etude.setGenericCode(generic_code);
        new_etude.setUserManager(userManager);
        new_etude.setUnit(etude.getUnit());
        new_etude.setUserAnalyst(userAnalyst);
        new_etude.setName(etude.getName());
        new_etude.setNeededAmount(etude.getNeededAmount());
        new_etude.setRecipients(etude.getRecipients());
        new_etude.setDomain(etude.getDomain());
        new_etude.setStart_date(etude.getStart_date());
        new_etude.setExpected_end_date(etude.getExpected_end_date());
        new_etude.setOrigin(etude.getOrigin());
        new_etude.setFirst_cote(etude.getFirst_cote());
        new_etude.setType(etude.getType());

        ArrayList<User> supports = new ArrayList();

        ArrayList supportRequest = etude.getSupports();

        log.info("passed here {}", new_etude.getUserAnalyst());

        if (supportRequest != null)
        {
            etude.getSupports().forEach((support) -> {

                // Adding potential supports
                try{
                    supports.add(userService.getUser((String)support));
                }catch (Exception e){
                    log.info("Erreur get support : {]", e.getMessage());
                }
            });

            new_etude.setSupports(supports);
        }

        try{
            etudeService.saveEtude(new_etude);
        }catch (Exception e){
            log.info("error saving {}", e.getMessage());
             return ResponseEntity.status(500).body(new ArrayList<>());
        }

        try {

            // Register new analysis
            Etude etudeSaved = etudeRepository.findByGenericCode(generic_code);

            EtudeAnalysis etudeAnalysis = new EtudeAnalysis();

            etudeAnalysis.setEtudeGeneric(etudeSaved.getGenericCode());
            etudeAnalysis.setUsername(username);

            etudeAnalysis.setStatus(Status.INIT_DEMAND);


            etudeAnalysisService.saveEtudeAnalysis(etudeAnalysis);

            // Registering new monitor
            Monitor monitor =  new Monitor();

            monitor.setDossierId(etudeSaved.getGenericCode());
            monitor.setStatus(Status.INIT_DEMAND);
            monitor.setUsername(userAnalyst.getUsername());
            monitorService.saveMonitor(monitor);

        }catch (Exception e){
            log.info("Exception e {}", e.getMessage());
        }

        // 2- Saving EtudeAnalysis

        return ResponseEntity.ok().body(etudeRepository.findAnalystJoinAnalysis(username));

    }

}

@Data
class EtudeSaveNew {
    private String unit;
    private String name;
    private String recipients;
    private String analyst;
    private String usernameManager;
    private String type;
    private long neededAmount;
    private String origin;
    private String domain;
    private String first_cote;
    private Date expected_end_date;
    private Date start_date;
    private ArrayList supports;
}

@Data
class EtudeEdit {
    private String unitEdit;
    private String genericCodeEdit;
    private String nameEdit;
    private String recipientsEdit;
    private String analystEdit;
    private String usernameManagerEdit;
    private String typeEdit;
    private long neededAmountEdit;
    private String originEdit;
    private String domainEdit;
    private String first_coteEdit;
    private Date expected_end_dateEdit;
    private Date start_dateEdit;
    private ArrayList supportsEdit;
}

@Data
class Analysis {
    private String comment;
    private String deliverables;
    private String etudeGeneric;
    private long amount_given;
    private String link;
    private String perspectives;
    private Date date_comity_dri;
}
