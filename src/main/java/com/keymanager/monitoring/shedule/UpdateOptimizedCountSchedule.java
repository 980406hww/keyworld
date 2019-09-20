package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.service.MachineInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateOptimizedCountSchedule {
    private static Logger logger = LoggerFactory.getLogger(UpdateOptimizedCountSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void runTask(){
        logger.info("=============  Update Optimized Count Task ===================");
        try {
            customerKeywordService.updateOptimizedCount();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Update Optimized Count is error" + e.getMessage());
        }
    }
}
