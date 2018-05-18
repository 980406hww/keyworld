package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.KeywordInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KeywordInfoSynchronizeSchedule {
    private static Logger logger = LoggerFactory.getLogger(KeywordInfoSynchronizeSchedule.class);

    @Autowired
    private KeywordInfoService keywordInfoService;


    public void runTask(){
        logger.info("============= "+" KeywordInfo Synchronize Task "+"===================");
        try {

        } catch (Exception e) {
            logger.error(" KeywordInfo Synchronize is error" + e.getMessage());
        }
    }


}
