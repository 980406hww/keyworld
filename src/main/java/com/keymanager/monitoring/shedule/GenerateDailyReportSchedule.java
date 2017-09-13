package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.DailyReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateDailyReportSchedule {
    private static Logger logger = LoggerFactory.getLogger(GenerateDailyReportSchedule.class);

    @Autowired
    private DailyReportService dailyReportService;

    public void runTask(){
//        logger.info("============= "+" Generate Report Task "+"===================");
        try {
//            dailyReportService.generateReport();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" Generate Report is error" + e.getMessage());
        }
    }


}
