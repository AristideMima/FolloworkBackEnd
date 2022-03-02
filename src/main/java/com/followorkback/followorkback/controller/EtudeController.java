package com.followorkback.followorkback.controller;

import com.followorkback.followorkback.entity.*;
import com.followorkback.followorkback.repository.EtudeAnalysisRepository;
import com.followorkback.followorkback.repository.EtudeRepository;
import com.followorkback.followorkback.repository.MonitorRepository;
import com.followorkback.followorkback.service.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    @GetMapping("/stats/all")
    public ResponseEntity<?> getCreditsStatsAll(){

        long total = etudeAnalysisRepository.count();
        long init_all = etudeAnalysisRepository.countAllByStatus(Status.INIT_DEMAND);
        long progress = etudeAnalysisRepository.countAllByStatus(Status.IN_PROGRESS);
        long close = etudeAnalysisRepository.countAllByStatus(Status.CLOSE_REJECT) + etudeAnalysisRepository.countAllByStatus(Status.CLOSE_SUCCESS);

        JSONObject allStats = new JSONObject();
        allStats.put("total", total);
        allStats.put("init_all", init_all);
        allStats.put("progress", progress);
        allStats.put("close", close);

        return ResponseEntity.ok().body(allStats);
    }

    @GetMapping("/stats/{username}")
    public ResponseEntity<?> getCreditsStatsAll(@PathVariable String username){
        long total = etudeAnalysisRepository.countAllByUsername(username);
        long init_all = etudeAnalysisRepository.countAllByStatusAndUsername(Status.INIT_DEMAND, username);
        long progress = etudeAnalysisRepository.countAllByStatusAndUsername(Status.IN_PROGRESS, username);
        long close = etudeAnalysisRepository.countAllByStatusAndUsername(Status.CLOSE_REJECT, username) +
                etudeAnalysisRepository.countAllByStatusAndUsername(Status.CLOSE_SUCCESS, username);

        JSONObject allStats = new JSONObject();
        allStats.put("total", total);
        allStats.put("init_all", init_all);
        allStats.put("progress", progress);
        allStats.put("close", close);

        return ResponseEntity.ok().body(allStats);
    }

    @GetMapping("/names")
    public ResponseEntity<List<?>> getAllNames(){
        List<?> allNamesEtudes = etudeRepository.findAllDossierNamesEtude();
        List<?> allNamesCredits = etudeRepository.findAllDossierNamesCredit();
        List<?> allNames = Stream.concat(allNamesEtudes.stream(), allNamesCredits.stream()).collect(Collectors.toList());

        return ResponseEntity.ok().body(allNames);
    }

    @GetMapping("/etudes/analyst/{username}")
    public ResponseEntity<List<?>> getUserEtudes(@PathVariable String username){

        List<?> allUserEtudes = etudeRepository.findAnalystJoinAnalysis(username);

        return ResponseEntity.ok().body(allUserEtudes);
    }

    @GetMapping("/etudes/manager/{username}")
    public ResponseEntity<List<?>> getManagerEtudes(@PathVariable String username){

        List<?> allUserEtudes = etudeRepository.findAnalystJoinManager(username);

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

    @GetMapping("/monitor/{genericCode}")
    public ResponseEntity<List<?>> getMonitor(@PathVariable String genericCode){
        List<?> allMonitor = etudeRepository.findGenericJoinMonitor(genericCode);

        return ResponseEntity.ok().body(allMonitor);
    }

    @PostMapping("/manager/update/{username}/{action}")
    public ResponseEntity<List<?>> managerUpdate(@RequestBody CommentGet comment, @PathVariable String username, @PathVariable String action){

        log.info("Updating datas action: {}, comment: {}", action, comment);

        EtudeAnalysis etudeAnalysis = etudeAnalysisRepository.findByEtudeGeneric(comment.getEtudeGeneric());

        String myComment = comment.getComment();

        Status status = Status.INIT_DEMAND;
        Status monitorStatus = Status.INIT_DEMAND;
        Monitor monitor = new Monitor();



        switch(action){
            case "valid_init":
                status = Status.IN_PROGRESS;
                monitorStatus = Status.IN_PROGRESS;
                break;
            case "cancel_init":
                status = Status.INIT_REJECT;
                monitorStatus = Status.INIT_REJECT;
                break;
            case "close_success":
                status = Status.CLOSE_SUCCESS;
                monitorStatus = Status.CLOSE_SUCCESS;
                break;
            case "close_failed":
                status = Status.CLOSE_FAILED;
                monitorStatus = Status.CLOSE_FAILED;
                break;
            case "cancel_close":
                status = Status.IN_PROGRESS;
                monitorStatus = Status.CLOSE_REJECT;
                break;
            case "init_suspension":
                status = Status.INIT_SUSPENSION;
                monitorStatus = Status.INIT_SUSPENSION;
                break;
            case "cancel_suspend":
                status = Status.IN_PROGRESS;
                monitorStatus = Status.REJECT_SUSPENSION;
                log.info("Reject: {}", action);
                break;
            case "valid_suspend":
                log.info("Valid: {}", action);
                status = Status.VALID_SUSPENSION;
                monitorStatus = Status.VALID_SUSPENSION;
                break;
            default:
                break;
        }

        // Save new status
        etudeAnalysis.setStatus(status);

        monitor.setStatus(monitorStatus);
        monitor.setDossierId(comment.getEtudeGeneric());
        monitor.setUsername(username);
        monitor.setComment(myComment);

        try{
            monitorRepository.save(monitor);
            etudeAnalysisRepository.save(etudeAnalysis);
        }catch (Exception e){
            log.info("Error saving monitor: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ArrayList<>());
        }

        log.info("Updating datas");

        List<?> allUserEtudes =  !action.equals("init_suspension") ? etudeRepository.findAnalystJoinManager(username): etudeRepository.findAnalystJoinAnalysis(username);

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
        etudeAnalysis.setPerspectives(analysis.getPerspectives());
        etudeAnalysis.setLink(analysis.getLink());

        Status myStatus = Status.UPDATE_ANALYSIS;
        Monitor monitor = new Monitor();
        monitor.setUsername(etudeAnalysis.getUsername());
        monitor.setDossierId(generic_code);
        monitor.setComment(comment);

        if (action.equals("close")){
            myStatus = Status.CLOSE_DEMAND;
            etudeAnalysis.setStatus(myStatus);
        }
        monitor.setStatus(myStatus);

        monitorRepository.save(monitor);
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

        new_etude.setUserManager(userManager);
        new_etude.setOrigin(etude.getOriginEdit());
        new_etude.setProvenance(etude.getProvenanceEdit());
        new_etude.setUserAnalyst(userAnalyst);
        new_etude.setName(etude.getNameEdit());
        new_etude.setRecipients(etude.getTypeEdit());
        new_etude.setDomain(etude.getDomainEdit());
        new_etude.setDeliverables(etude.getDeliverablesEdit());
        new_etude.setStart_date(etude.getStart_dateEdit());
        new_etude.setExpected_end_date(etude.getExpected_end_dateEdit());
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

        log.info("Saving new etude {}");

        // 1- Saving Etude
        Etude new_etude = new Etude();
        String timeStampDate = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        String[] analyst = etude.getAnalyst().split("_");

        List<?> alletudes = etudeRepository.findAnalystJoinAnalysis(analyst[0] +  "_" + analyst[1]);

        String generic_code = analyst[1].charAt(0) + "" + analyst[0].charAt(0) + "-et_pro" +  "-" + (alletudes.size() + 1) + "-" + timeStampDate;

        User userAnalyst = userService.getUser(etude.getAnalyst());
        User userManager = userService.getUser(etude.getUsernameManager());

        new_etude.setGenericCode(generic_code);
        new_etude.setProvenance(etude.getProvenance());
        new_etude.setName(etude.getName());
        new_etude.setOrigin(etude.getOrigin());
        new_etude.setFirst_cote(etude.getFirst_cote());
        new_etude.setRecipients(etude.getRecipients());
        new_etude.setDomain(etude.getDomain());
        new_etude.setDeliverables(etude.getDeliverables());
        new_etude.setStart_date(etude.getStart_date());
        new_etude.setExpected_end_date(etude.getExpected_end_date());
        new_etude.setUserAnalyst(userAnalyst);
        new_etude.setUserManager(userManager);
        new_etude.setType(etude.getType());

        // Settings all suports if exists
        ArrayList<User> supports = new ArrayList();

        ArrayList supportRequest = etude.getSupports();
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
            return ResponseEntity.status(500).body(new ArrayList<>());
        }

        return ResponseEntity.ok().body(etudeRepository.findAnalystJoinAnalysis(username));

    }
}

@Data
class EtudeSaveNew {
    private String origin;
    private String name;
    private String recipients;
    private String analyst;
    private String usernameManager;
    private String type;
    private String provenance;
    private String deliverables;
    private String domain;
    private String first_cote;
    private Date expected_end_date;
    private Date start_date;
    private ArrayList supports;
}

@Data
class EtudeEdit {
    private String genericCodeEdit;
    private String originEdit;
    private String nameEdit;
    private String recipientsEdit;
    private String analystEdit;
    private String usernameManagerEdit;
    private String typeEdit;
    private String provenanceEdit;
    private String deliverablesEdit;
    private String domainEdit;
    private String first_coteEdit;
    private Date expected_end_dateEdit;
    private Date start_dateEdit;
    private ArrayList supportsEdit;
}

@Data
class Analysis {
    private String comment;
    private String etudeGeneric;
    private long amount_given;
    private String link;
    private String perspectives;
    private Date date_comity_dri;
}


@Data
class CommentGet{
    private String etudeGeneric;
    private String comment;
}
