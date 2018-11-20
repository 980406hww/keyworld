package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.QZSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QZKeywordExceedMaxCountSchedule {
    private static Logger logger = LoggerFactory.getLogger(QZKeywordExceedMaxCountSchedule.class);

    @Autowired
    private QZSettingService qzSettingService;

    public void runTask(){
        logger.info("============= " + " QZ Keyword Exceed Max Count Task "+"===================");
        try {
            qzSettingService.detectExceedMaxCountFlag();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("QZ Keyword Exceed Max Count is error" + e.getMessage());
        }
    }
}
