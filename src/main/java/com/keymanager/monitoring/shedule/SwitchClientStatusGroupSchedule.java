package com.keymanager.monitoring.shedule;

import com.keymanager.manager.SwitchClientStatusGroupManager;
import com.keymanager.monitoring.service.ClientStatusService;
import com.keymanager.monitoring.service.DailyReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SwitchClientStatusGroupSchedule {
    private static Logger logger = LoggerFactory.getLogger(SwitchClientStatusGroupSchedule.class);

    @Autowired
    private ClientStatusService clientStatusService;

    public void runTask(){
        logger.info("============= "+" Switch Client Status Group Task "+"===================");
        try {
            clientStatusService.switchGroup();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Switch Client Status Group is error" + e.getMessage());
        }
    }
}
