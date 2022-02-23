package com.followorkback.followorkback.service;

import com.followorkback.followorkback.entity.Comment;
import com.followorkback.followorkback.entity.CreditAnalysis;
import com.followorkback.followorkback.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CreditAnalysisService {
    CreditAnalysis saveCreditAnalysis(CreditAnalysis creditAnalysis);
    CreditAnalysis updateCreditAnalysis(CreditAnalysis creditAnalysis);
    Boolean deleteCreditAnalysis(UUID uuid);
    CreditAnalysis getCreditAnalysis(UUID uuid);
    void addSupportToCreditAnalysis(User user);
    Collection<CreditAnalysis> getAllCreditAnalysis(int limit);
}
