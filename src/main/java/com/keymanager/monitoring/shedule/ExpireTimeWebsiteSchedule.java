package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.common.email.AccessWebsiteFailMailService;
import com.keymanager.monitoring.entity.Website;
import com.keymanager.monitoring.service.WebsiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ExpireTimeWebsiteSchedule {
    private static Logger logger = LoggerFactory.getLogger(ExpireTimeWebsiteSchedule.class);

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private AccessWebsiteFailMailService accessWebsiteFailMailService;

    public void runTask(){
        logger.info("============= "+" Expire Time Task "+"===================");
        try {
            List<Website> websites = websiteService.accessExpireTimeURL();
            if (websites.size() > 0) {
                accessWebsiteFailMailService.sendExpireTimeWebsiteFailMail(websites);
            }
        } catch (Exception e) {
            logger.error(" Access URL is error" + e.getMessage());
        }
    }


}
