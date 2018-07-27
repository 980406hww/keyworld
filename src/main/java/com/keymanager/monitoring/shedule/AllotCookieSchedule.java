package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CookieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AllotCookieSchedule {
    private static Logger logger = LoggerFactory.getLogger(AllotCookieSchedule.class);

    @Autowired
    private CookieService cookieService;

    public void runTask(){
        logger.info("============= "+" Allot Cookie Task "+"===================");
        try {
            cookieService.allotCookieForClient();
        } catch (Exception e) {
            logger.error(" Allot Cookie is error" + e.getMessage());
        }
    }


}
