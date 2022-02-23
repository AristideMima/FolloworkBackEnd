package com.followorkback.followorkback.service;

import com.followorkback.followorkback.entity.EtudeAnalysis;
import com.followorkback.followorkback.repository.EtudeAnalysisRepository;
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
public class EtudeAnalysisServiceImpl implements EtudeAnalysisService {

    private final EtudeAnalysisRepository etudeAnalysisRepository;

    @Override
    public EtudeAnalysis saveEtudeAnalysis(EtudeAnalysis etudeAnalysis) {
        log.info("etude analysis {} ", etudeAnalysis);

        return etudeAnalysisRepository.save(etudeAnalysis);
    }

    @Override
    public EtudeAnalysis updateEtudeAnalysis(EtudeAnalysis etudeAnalysis) {
        return null;
    }

    @Override
    public Boolean deleteEtudeAnalysis(UUID uuid) {
        return null;
    }

    @Override
    public EtudeAnalysis getEtudeAnalysis(UUID uuid) {
        return null;
    }

    @Override
    public Collection<EtudeAnalysis> getAllEtudeAnalysis(int limit) {
        return null;
    }
}
