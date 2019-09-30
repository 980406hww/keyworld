package com.keymanager.ckadmin.service;

public interface PerformanceService {
    void addPerformanceLog(String module, long milleSeconds, String remarks);
}
