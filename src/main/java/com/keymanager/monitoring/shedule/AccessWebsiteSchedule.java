package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.WebsiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessWebsiteSchedule {
    private static Logger logger = LoggerFactory.getLogger(AccessWebsiteSchedule.class);

    @Autowired
    private WebsiteService websiteService;

    public void runTask(){
        logger.info("============= "+" Access URL Task "+"===================");
        try {
            websiteService.accessURL();
        } catch (Exception e) {
            logger.error(" Access URL is error" + e.getMessage());
        }
    }


}
