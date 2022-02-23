package com.followorkback.followorkback.service;

import com.followorkback.followorkback.entity.CreditAnalysis;
import com.followorkback.followorkback.entity.User;
import com.followorkback.followorkback.repository.CreditAnalysisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;




@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CreditAnalysisServiceImpl implements CreditAnalysisService {

    private CreditAnalysisRepository creditAnalysisRepository;

    @Override
    public CreditAnalysis saveCreditAnalysis(CreditAnalysis creditAnalysis) {
        return null;
    }

    @Override
    public CreditAnalysis updateCreditAnalysis(CreditAnalysis creditAnalysis) {
        return null;
    }

    @Override
    public Boolean deleteCreditAnalysis(UUID uuid) {
        return null;
    }

    @Override
    public CreditAnalysis getCreditAnalysis(UUID uuid) {
        return null;
    }

    @Override
    public void addSupportToCreditAnalysis(User user) {

    }

    @Override
    public Collection<CreditAnalysis> getAllCreditAnalysis(int limit) {
        return null;
    }
}
