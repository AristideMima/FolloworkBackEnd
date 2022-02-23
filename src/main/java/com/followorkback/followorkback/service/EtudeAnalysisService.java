package com.followorkback.followorkback.service;
import com.followorkback.followorkback.entity.EtudeAnalysis;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface EtudeAnalysisService {
    EtudeAnalysis saveEtudeAnalysis(EtudeAnalysis etudeAnalysis);
    EtudeAnalysis updateEtudeAnalysis(EtudeAnalysis etudeAnalysis);
    Boolean deleteEtudeAnalysis(UUID uuid);
    EtudeAnalysis getEtudeAnalysis(UUID uuid);
    Collection<EtudeAnalysis> getAllEtudeAnalysis(int limit);
}
