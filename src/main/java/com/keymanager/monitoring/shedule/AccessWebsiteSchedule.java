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
public class AccessWebsiteSchedule {
    private static Logger logger = LoggerFactory.getLogger(AccessWebsiteSchedule.class);

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private AccessWebsiteFailMailService accessWebsiteFailMailService;

    public void runTask(){
        logger.info("============= "+" Access URL Task "+"===================");
        try {
//            List<Website> websites = websiteService.accessURL();
            Map<String, Object> websitesMap = websiteService.accessURL();
            List<Website> websitesFails = (List<Website>) websitesMap.get("accessFailWebsites");
            if (websitesFails.size() > 0) {
                accessWebsiteFailMailService.sendAccessWebsiteFailMail(websitesFails);
            }
            List<Website> websiteSuccess = (List<Website>) websitesMap.get("accessSuccessWebsites");
            if (websiteSuccess.size() > 0) {
                accessWebsiteFailMailService.sendAccessWebsiteSuccessMail(websiteSuccess);
            }
        } catch (Exception e) {
            logger.error(" Access URL is error" + e.getMessage());
        }
    }


}
