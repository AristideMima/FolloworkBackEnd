package com.followorkback.followorkback.service;


import com.followorkback.followorkback.entity.Etude;
import com.followorkback.followorkback.entity.User;
import com.followorkback.followorkback.repository.EtudeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class EtudeServiceImpl implements EtudeService {

    private final EtudeRepository etudeRepository;

    @Override
    public Etude saveEtude(Etude etude) {
        return etudeRepository.save(etude);
    }

    @Override
    public Etude updateEtude(Etude etude)
    {
        log.info("Updating new etude:  {}", etude);
        return null;
    }

    @Override
    public Boolean deleteEtude(String genericCode) {
        log.info("Deleting new etude:  {}", genericCode);
        return null;
    }

    @Override
    public Etude getEtude(String genericCode) {
        log.info("Geting new etude:  {}", genericCode);
        etudeRepository.deleteById(genericCode);
        return null;
    }

    @Override
    public void addSupportToEtude(String username) {

    }

    @Override
    public Collection<Etude> getAllEtudes(int limit) {
        Collection<Etude> datas = etudeRepository.findAll(PageRequest.of(0, limit)).toList();
        return datas;
    }
}
