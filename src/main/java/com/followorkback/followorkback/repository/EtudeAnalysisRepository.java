package com.followorkback.followorkback.repository;

import com.followorkback.followorkback.entity.EtudeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.UUID;

public interface EtudeAnalysisRepository extends JpaRepository<EtudeAnalysis, Long> {

      EtudeAnalysis findByEtudeGeneric(String etudeGeneric);
      @Transactional
      long deleteAllByEtudeGeneric(String etudeGeneric);
//    EtudeAnalysis saveEtudeAnalysis(EtudeAnalysis etudeanalysis);
//    EtudeAnalysis findBydossier_uuidAndusername_analyst(UUID dossier_uuid, String username_analyst);
//    EtudeAnalysis findBydossier_uuidAndusername_manager(UUID dossier_uuid, String username_manager);
}
