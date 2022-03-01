package com.followorkback.followorkback.repository;

import com.followorkback.followorkback.entity.EtudeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.UUID;

public interface EtudeAnalysisRepository extends JpaRepository<EtudeAnalysis, Long> {

      EtudeAnalysis findByEtudeGeneric(String etudeGeneric);
      @Transactional
      long deleteAllByEtudeGeneric(String etudeGeneric);
}
