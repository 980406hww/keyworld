package com.keymanager.ckadmin.schedule;

import com.keymanager.ckadmin.service.QZRateStatisticsService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GenerateQZRateStatisticsSchedule {

    private static Logger logger = LoggerFactory.getLogger(GenerateQZRateStatisticsSchedule.class);

    @Resource(name = "qzRateStatisticsService2")
    private QZRateStatisticsService qzRateStatisticsService;

    public void runTask() {
        logger.info("============= " + " QZ Rate Statistics Task " + "===================");
        try {
            qzRateStatisticsService.generateQZRateStatistics();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" QZ Rate Statistics is error" + e.getMessage());
        }
    }

}
