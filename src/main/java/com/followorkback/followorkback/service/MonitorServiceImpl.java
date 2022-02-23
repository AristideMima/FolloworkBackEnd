package com.followorkback.followorkback.service;

import com.followorkback.followorkback.entity.Monitor;
import com.followorkback.followorkback.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MonitorServiceImpl implements MonitorService {

    private final MonitorRepository monitorRepository;

    @Override
    public Monitor getMonitor(String dossierId) {
        return monitorRepository.findByDossierId(dossierId);
    }

    @Override
    public Monitor saveMonitor(Monitor monitor) {
        return monitorRepository.save(monitor);
    }


}
