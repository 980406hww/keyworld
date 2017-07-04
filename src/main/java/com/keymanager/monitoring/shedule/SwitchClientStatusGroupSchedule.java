package com.keymanager.monitoring.shedule;

import com.keymanager.manager.SwitchClientStatusGroupManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SwitchClientStatusGroupSchedule {
    private static Logger logger = LoggerFactory.getLogger(SwitchClientStatusGroupSchedule.class);

    public void runTask(){
        logger.info("============= "+" Switch Client Status Group Task "+"===================");
        SwitchClientStatusGroupManager manager = new SwitchClientStatusGroupManager();
        try {
            manager.switchGroup();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Switch Client Status Group is error" + e.getMessage());
        }
    }
}
