package com.followorkback.followorkback.repository;

import com.followorkback.followorkback.entity.CreditAnalysis;
import com.followorkback.followorkback.entity.EtudeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CreditAnalysisRepository extends JpaRepository<CreditAnalysis, Long> {
//    CreditAnalysisRepository findByDossierAndUsernameAnalyst(UUID dossier_uuid, String username_analyst);
//    CreditAnalysisRepository findBydossier_uuidAndusername_manager(UUID dossier_uuid, String username_manager);
}
