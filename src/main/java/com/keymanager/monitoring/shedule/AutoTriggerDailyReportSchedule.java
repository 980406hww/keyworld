package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.DailyReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoTriggerDailyReportSchedule {
    private static Logger logger = LoggerFactory.getLogger(AutoTriggerDailyReportSchedule.class);

    @Autowired
    private DailyReportService dailyReportService;

    public void runTask(){
        logger.info("============= "+" Auto Trigger Daily Report Task "+"===================");
        try {
            dailyReportService.autoTriggerDailyReport();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Auto Trigger Daily Report is error" + e.getMessage());
        }
    }
}
