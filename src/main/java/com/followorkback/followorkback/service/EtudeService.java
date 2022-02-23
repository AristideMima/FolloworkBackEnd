package com.followorkback.followorkback.service;

import com.followorkback.followorkback.entity.Etude;
import com.followorkback.followorkback.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface EtudeService {
    Etude saveEtude(Etude etude);
    Etude updateEtude(Etude etude);
    Boolean deleteEtude(String genericCOde);
    Etude getEtude(String genericCode);
    void addSupportToEtude(String username);
    Collection<Etude> getAllEtudes(int limit);
}
