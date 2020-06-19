package com.keymanager.ckadmin.schedule;

import com.keymanager.ckadmin.service.UserRefreshStatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserRefreshStatisticSchedule {

    private static Logger logger = LoggerFactory.getLogger(UserRefreshStatisticSchedule.class);

    @Resource(name="userRefreshStatisticService")
    private UserRefreshStatisticService userRefreshStatisticService;

    public void runTask(){
        logger.info("============= " + " userRefreshInfo Statistics Task " + "===================");
        try {
           userRefreshStatisticService.updateUserRefreshStatisticInfo();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" userRefreshInfo Statistics is error" + e.getMessage());
        }
    }

}
