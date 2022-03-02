package com.followorkback.followorkback.repository;

import com.followorkback.followorkback.entity.EtudeAnalysis;
import com.followorkback.followorkback.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.UUID;

public interface EtudeAnalysisRepository extends JpaRepository<EtudeAnalysis, Long> {

      EtudeAnalysis findByEtudeGeneric(String etudeGeneric);
      @Transactional
      long deleteAllByEtudeGeneric(String etudeGeneric);

      long countAllByStatus(Status status);
      long countAllByStatusAndUsername(Status status, String username);
      long countAllByUsername(String username);
}
