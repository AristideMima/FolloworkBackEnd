package com.followorkback.followorkback.controller;

import com.followorkback.followorkback.entity.*;
import com.followorkback.followorkback.repository.CreditAnalysisRepository;
import com.followorkback.followorkback.repository.CreditRepository;
import com.followorkback.followorkback.repository.MonitorRepository;
import com.followorkback.followorkback.service.CreditServiceImpl;
import com.followorkback.followorkback.service.MonitorServiceImpl;
import com.followorkback.followorkback.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/credit")
@RequiredArgsConstructor
@Slf4j
public class CreditController {
    private final CreditServiceImpl creditService;
    private final UserService userService;
    private final CreditAnalysisRepository creditAnalysisRepository;
    private final CreditRepository creditRepository;
    private final MonitorRepository monitorRepository;
    private final MonitorServiceImpl monitorService;

    @GetMapping("/credits")
    public ResponseEntity<List<?>> getCredits(){
        log.info("Fetching all credits ");
        return ResponseEntity.ok().body(creditRepository.findAllJoinAnalysis());
    }

    @GetMapping("/credits/analyst/{username}")
    public ResponseEntity<List<?>> getUserCredits(@PathVariable String username){

        List<?> allUserEtudes = creditRepository.findAnalystJoinAnalysis(username);

        return ResponseEntity.ok().body(allUserEtudes);
    }

    @GetMapping("/credits/manager/{username}")
    public ResponseEntity<List<?>> getManagerEtudes(@PathVariable String username){

        List<?> allUserEtudes = creditRepository.findAnalystJoinManager(username);

        return ResponseEntity.ok().body(allUserEtudes);
    }

    @DeleteMapping(value= "/delete/{genericCode}/{username}")
    public ResponseEntity<List<?>> deleteEtude(@PathVariable String genericCode, @PathVariable String username){

        // Find corresponding
        try {
            creditRepository.deleteAllByGenericCode(genericCode);
            creditAnalysisRepository.deleteAllByCreditGeneric(genericCode);
            monitorRepository.deleteAllByDossierId(genericCode);
        }catch (Exception e){
            log.info("Error deleting. Error:  {}", e.getMessage());
            return ResponseEntity.status(500).body(new ArrayList<>());
        }

        List<?> allUserEtudes = creditRepository.findAnalystJoinAnalysis(username);

        return ResponseEntity.ok().body(allUserEtudes);
    }

    @GetMapping("/monitor/{genericCode}")
    public ResponseEntity<List<?>> getMonitor(@PathVariable String genericCode){
        List<?> allMonitor = creditRepository.findGenericJoinMonitor(genericCode);

        return ResponseEntity.ok().body(allMonitor);
    }

    @PostMapping("/manager/update/{username}/{action}")
    public ResponseEntity<List<?>> managerUpdate(@RequestBody CommentGetCredit comment, @PathVariable String username, @PathVariable String action){

        log.info("Updating datas action: {}, comment: {}", action, comment);

        CreditAnalysis creditAnalysis = creditAnalysisRepository.findByCreditGeneric(comment.getCreditGeneric());

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
        creditAnalysis.setStatus(status);

        monitor.setStatus(monitorStatus);
        monitor.setDossierId(comment.getCreditGeneric());
        monitor.setUsername(username);
        monitor.setComment(myComment);

        try{
            monitorRepository.save(monitor);
            creditAnalysisRepository.save(creditAnalysis);
        }catch (Exception e){
            log.info("Error saving monitor: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ArrayList<>());
        }

        log.info("Updating datas");

        List<?> allUserCredits =  !action.equals("init_suspension") ? creditRepository.findAnalystJoinManager(username): creditRepository.findAnalystJoinAnalysis(username);

        return ResponseEntity.ok().body(allUserCredits);
    }

    @PostMapping("/analysis/update/{username}/{action}")
    public ResponseEntity<List<?>> updateAnalysis(@RequestBody AnalysisCredit analysis, @PathVariable String username, @PathVariable String action){

        log.info("Updating analysis");

        String generic_code = analysis.getCreditGeneric();
        CreditAnalysis creditAnalysis = creditAnalysisRepository.findByCreditGeneric(generic_code);

        String comment = analysis.getComment();

        creditAnalysis.setAmount_given(analysis.getAmount_given());
        creditAnalysis.setDate_comity_dri(analysis.getDate_comity_dri());
        creditAnalysis.setPerspectives(analysis.getPerspectives());
        creditAnalysis.setLink(analysis.getLink());
        creditAnalysis.setDate_comity_great(analysis.getDate_comity_great());
        creditAnalysis.setDate_transmit(analysis.getDate_transmit());
        creditAnalysis.setDate_establishment(analysis.getDate_establishment());

        Status myStatus = Status.UPDATE_ANALYSIS;
        Monitor monitor = new Monitor();
        monitor.setUsername(creditAnalysis.getUsername());
        monitor.setDossierId(generic_code);
        monitor.setComment(comment);

        if (action.equals("close")){
            myStatus = Status.CLOSE_DEMAND;
            creditAnalysis.setStatus(myStatus);
        }
        monitor.setStatus(myStatus);

        monitorRepository.save(monitor);
        creditAnalysisRepository.save(creditAnalysis);


        return ResponseEntity.ok().body(creditRepository.findAnalystJoinAnalysis(username));
    }

    @PostMapping("/update/{username}")
    public ResponseEntity<List<?>> updateEtude(@RequestBody CreditSaveEdit creditSaveEdit, @PathVariable String username){

        User userAnalyst = userService.getUser(creditSaveEdit.getAnalystEdit());
        User userManager = userService.getUser(creditSaveEdit.getUsernameManagerEdit());
        String generic_code = creditSaveEdit.getGenericCodeEdit();
        Credit credit = creditRepository.findByGenericCode(generic_code);

        credit.setGenericCode(generic_code);
        credit.setProvenance(creditSaveEdit.getProvenanceEdit());
        credit.setName(creditSaveEdit.getNameEdit());
        credit.setOrigin(creditSaveEdit.getOriginEdit());
        credit.setFirst_cote(creditSaveEdit.getFirst_coteEdit());
        credit.setRecipients(creditSaveEdit.getRecipientsEdit());
        credit.setDomain(creditSaveEdit.getDomainEdit());
        credit.setDeliverables(creditSaveEdit.getDeliverablesEdit());
        credit.setStart_date(creditSaveEdit.getStart_dateEdit());
        credit.setExpected_end_date(creditSaveEdit.getExpected_end_dateEdit());
        credit.setUserAnalyst(userAnalyst);
        credit.setUserManager(userManager);
        credit.setAck_date(creditSaveEdit.getAck_dateEdit());
        credit.setNeeded_reason(creditSaveEdit.getNeeded_reasonEdit());
        credit.setNeededAmount(creditSaveEdit.getNeededAmountEdit());

        ArrayList<User> supports = new ArrayList();

        ArrayList supportRequest = creditSaveEdit.getSupportsEdit();

        if (supportRequest != null)
        {
            creditSaveEdit.getSupportsEdit().forEach((support) -> {

                // Adding potential supports
                try{
                    supports.add(userService.getUser((String)support));
                }catch (Exception e){
                    log.info("Erreur get support : {]", e.getMessage());
                }
            });

            credit.setSupports(supports);
        }

        try{
            creditService.saveCredit(credit);
        }catch (Exception e){
            log.info("error saving {}", e.getMessage());
            return ResponseEntity.status(500).body(new ArrayList<>());
        }

        return ResponseEntity.ok().body(creditRepository.findAnalystJoinAnalysis(username));
    }

    @PostMapping("/save/{username}")
    public ResponseEntity<List<?>> saveEtude(@RequestBody CreditSave creditSave, @PathVariable String username){

        // 1- Saving new Credit
        Credit credit = new Credit();
        String timeStampDate = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        String[] analyst = creditSave.getAnalyst().split("_");

        List<?> alletudes = creditRepository.findAnalystJoinAnalysis(creditSave.getAnalyst());

        String generic_code = analyst[1].charAt(0) + "" + analyst[0].charAt(0) + "-cred" +  "-" + (alletudes.size() + 1) + "-" + timeStampDate;

        User userAnalyst = userService.getUser(creditSave.getAnalyst());
        User userManager = userService.getUser(creditSave.getUsernameManager());

        credit.setGenericCode(generic_code);
        credit.setProvenance(creditSave.getProvenance());
        credit.setName(creditSave.getName());
        credit.setOrigin(creditSave.getOrigin());
        credit.setFirst_cote(creditSave.getFirst_cote());
        credit.setRecipients(creditSave.getRecipients());
        credit.setDomain(creditSave.getDomain());
        credit.setDeliverables(creditSave.getDeliverables());
        credit.setStart_date(creditSave.getStart_date());
        credit.setExpected_end_date(creditSave.getExpected_end_date());
        credit.setUserAnalyst(userAnalyst);
        credit.setUserManager(userManager);
        credit.setAck_date(creditSave.getAck_date());
        credit.setNeeded_reason(creditSave.getNeeded_reason());
        credit.setNeededAmount(creditSave.getNeededAmount());

        // Settings all suports if exists
        ArrayList<User> supports = new ArrayList();

        ArrayList supportRequest = creditSave.getSupports();

        if (supportRequest != null)
        {
            creditSave.getSupports().forEach((support) -> {

                // Adding potential supports
                try{
                    supports.add(userService.getUser((String)support));
                }catch (Exception e){
                    log.info("Erreur get support : {]", e.getMessage());
                }
            });

            credit.setSupports(supports);
        }

        try{
            creditService.saveCredit(credit);
        }catch (Exception e){
            log.info("error saving {}", e.getMessage());
            return ResponseEntity.status(500).body(new ArrayList<>());
        }

        try {

            // Register new analysis
            Credit creditSaved = creditRepository.findByGenericCode(generic_code);
            CreditAnalysis creditAnalysis = new CreditAnalysis();
            creditAnalysis.setCreditGeneric(creditSaved.getGenericCode());
            creditAnalysis.setUsername(username);
            creditAnalysis.setStatus(Status.INIT_DEMAND);
            creditAnalysisRepository.save(creditAnalysis);
            // Registering new monitor
            Monitor monitor =  new Monitor();

            monitor.setDossierId(creditSaved.getGenericCode());
            monitor.setStatus(Status.INIT_DEMAND);
            monitor.setUsername(userAnalyst.getUsername());
            monitorService.saveMonitor(monitor);

        }catch (Exception e){
            log.info("Exception e {}", e.getMessage());
            return ResponseEntity.status(500).body(new ArrayList<>());
        }

        return ResponseEntity.ok().body(creditRepository.findAnalystJoinAnalysis(username));
    }
}

@Data
class CreditSave{
    private String origin;
    private String name;
    private String recipients;
    private String analyst;
    private String usernameManager;
    private String provenance;
    private String deliverables;
    private String domain;
    private String first_cote;
    private Date expected_end_date;
    private Date start_date;
    private Date ack_date;
    private String needed_reason;
    private long neededAmount;
    private ArrayList supports;
}

@Data
class CreditSaveEdit{
    private String genericCodeEdit;
    private String originEdit;
    private String nameEdit;
    private String recipientsEdit;
    private String analystEdit;
    private String usernameManagerEdit;
    private String provenanceEdit;
    private String deliverablesEdit;
    private String domainEdit;
    private String first_coteEdit;
    private Date expected_end_dateEdit;
    private Date start_dateEdit;
    private Date ack_dateEdit;
    private String needed_reasonEdit;
    private long neededAmountEdit;
    private ArrayList supportsEdit;
}

@Data
class AnalysisCredit {
    private String comment;
    private String creditGeneric;
    private long amount_given;
    private String link;
    private String perspectives;
    private Date date_comity_dri;
    private Date date_comity_great;
    private Date date_transmit;
    private Date date_establishment;
}


@Data
class CommentGetCredit{
    private String creditGeneric;
    private String comment;
}


