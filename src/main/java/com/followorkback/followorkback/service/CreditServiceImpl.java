package com.followorkback.followorkback.service;

import com.followorkback.followorkback.entity.Credit;
import com.followorkback.followorkback.entity.Etude;
import com.followorkback.followorkback.entity.User;
import com.followorkback.followorkback.repository.CreditRepository;
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
public class CreditServiceImpl implements CreditService {

    private CreditRepository creditRepository;

    @Override
    public Credit saveCredit(Credit credit) {
        return null;
    }

    @Override
    public Credit updateCredit(Etude credit) {
        return null;
    }

    @Override
    public Boolean deleteCredit(UUID uuid) {
        return null;
    }

    @Override
    public Credit getCredit(UUID uuid) {
        return null;
    }

    @Override
    public void addSupportToCredit(User user) {

    }

    @Override
    public Collection<Credit> getAllCredits(int limit) {
        return null;
    }
}
