package com.followorkback.followorkback.service;

import com.followorkback.followorkback.entity.Monitor;

public interface MonitorService {
    Monitor getMonitor(String dossierId);
    Monitor saveMonitor (Monitor monitor);
}
