package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.PerformanceDao;
import com.keymanager.ckadmin.entity.Performance;
import com.keymanager.ckadmin.service.PerformanceService;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


@Service("performanceService2")
public class PerformanceServiceImpl extends ServiceImpl<PerformanceDao, Performance> implements PerformanceService {

    @Resource(name = "performanceDao2")
    private PerformanceDao performanceDao;

    @Override
    public void addPerformanceLog(String module, long milleSeconds, String remarks) {
        if ("ResetInfoDailySchedule".equals(module)) {
            Performance performance = new Performance();
            performance.setModule(module);
            performance.setMilleSecond(milleSeconds);
            performance.setCurrentMemory(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
            performance.setRemarks(remarks);
            performance.setCreateTime(new Date());
            performanceDao.insert(performance);
        }
    }
}
