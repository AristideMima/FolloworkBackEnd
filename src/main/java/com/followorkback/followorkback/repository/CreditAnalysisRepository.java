package com.followorkback.followorkback.repository;

import com.followorkback.followorkback.entity.Credit;
import com.followorkback.followorkback.entity.CreditAnalysis;
import com.followorkback.followorkback.entity.EtudeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface CreditAnalysisRepository extends JpaRepository<CreditAnalysis, String> {
    CreditAnalysis findByCreditGeneric(String etudeGeneric);
    @Transactional
    long deleteAllByCreditGeneric(String etudeGeneric);
}
