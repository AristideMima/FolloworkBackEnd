package com.followorkback.followorkback.service;

import com.followorkback.followorkback.entity.Credit;
import com.followorkback.followorkback.entity.Etude;
import com.followorkback.followorkback.entity.User;
import com.followorkback.followorkback.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    @Override
    public Credit saveCredit(Credit credit) {
        return creditRepository.save(credit);
    }

    @Override
    public Collection<Credit> getAllCredits(int limit)
    {
        Collection<Credit> datas = creditRepository.findAll(PageRequest.of(0, limit)).toList();
        return datas;
    }
}
