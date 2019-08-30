package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.AlgorithmTestResultStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoSaveTestDataStatisticsSchedule {
    private static Logger logger = LoggerFactory.getLogger(AutoSaveTestDataStatisticsSchedule.class);

    @Autowired
    private AlgorithmTestResultStatisticsService algorithmTestResultStatisticsService;

    public void runTask(){
        logger.info("============= "+" Auto Save Test Data Analysis Schedule "+"===================");
        try {
            algorithmTestResultStatisticsService.saveAlgorithmTaskDataDailyTask();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Auto Save Test Data Analysis is error" + e.getMessage());
        }
    }
}
