package com.keymanager.monitoring.shedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UpdateOptimizedCountSchedule {
    private static Logger logger = LoggerFactory.getLogger(UpdateOptimizedCountSchedule.class);

    @Resource(name = "customerKeywordService2")
    private com.keymanager.ckadmin.service.CustomerKeywordService customerKeywordService;

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
