package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.NegativeStandardSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NegativeStandardSettingCountSchedule {
    private static Logger logger = LoggerFactory.getLogger(NegativeStandardSettingCountSchedule.class);

    @Autowired
    private NegativeStandardSettingService negativeStandardSettingService;

    public void runTask(){
        logger.info("============= "+" Negative  Standard Setting Count Task "+"===================");
        try {
            negativeStandardSettingService.negativeStandardSettingCountSchedule();
        } catch (Exception e) {
            logger.error(" Negative Standard Setting Count is error" + e.getMessage());
        }
    }


}
