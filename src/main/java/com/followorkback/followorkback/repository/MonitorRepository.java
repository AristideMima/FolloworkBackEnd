package com.followorkback.followorkback.repository;

import com.followorkback.followorkback.entity.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface MonitorRepository extends JpaRepository<Monitor, Long> {
    Monitor findByDossierId(String dossierId);
    @Transactional
    long deleteAllByDossierId(String dossierId);
}
