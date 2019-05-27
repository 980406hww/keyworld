package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.monitoring.service.DailyReportService;
import com.keymanager.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateDailyReportSchedule {
    private static Logger logger = LoggerFactory.getLogger(GenerateDailyReportSchedule.class);

    @Autowired
    private DailyReportService dailyReportService;

    @Autowired
    private ConfigService configService;

    public void runTask(){
        logger.info("============= "+" Generate Report Task "+"===================");
        try {
            System.setProperty("sun.jnu.encoding", "utf-8");
            Config config = configService.getConfig(Constants.CONFIG_TYPE_DAILY_REPORT, Constants.CONFIG_TYPE_DAILY_REPORT_AUTO_TRIGGER);
            if("1".equals(config.getValue())) {
                dailyReportService.generateReport();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" Generate Report is error" + e.getMessage());
        }
    }


}
