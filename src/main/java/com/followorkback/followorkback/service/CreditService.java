package com.followorkback.followorkback.service;

import com.followorkback.followorkback.entity.Credit;
import com.followorkback.followorkback.entity.Etude;
import com.followorkback.followorkback.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CreditService {
    Credit saveCredit(Credit credit);
    Credit updateCredit(Etude credit);
    Boolean deleteCredit(UUID uuid);
    Credit getCredit(UUID uuid);
    void addSupportToCredit(User user);
    Collection<Credit> getAllCredits(int limit);
}
