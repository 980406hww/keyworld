package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.Performance;

public interface PerformanceService extends IService<Performance> {

    void addPerformanceLog(String module, long milleSeconds, String remarks);
}
