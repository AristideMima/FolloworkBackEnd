package com.followorkback.followorkback.repository;

import com.followorkback.followorkback.entity.Credit;
import com.followorkback.followorkback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CreditRepository extends JpaRepository<Credit, String> {
//    Credit findByUsernameAnalyst(String usernameAnalyst);
}
