package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.QZSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shunshikj40
 */
@Component
public class CacheCustomerUuidForSyncDataSchedule {

    private static final Logger logger = LoggerFactory.getLogger(CacheCustomerUuidForSyncDataSchedule.class);

    @Autowired
    private QZSettingService qzSettingService;

    public void runTask() {
        logger.info("============= " + " Cache CustomerUuid For Sync Data Task " + "===================");
        try {
            qzSettingService.syncQZCustomerKeyword();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" Cache CustomerUuid For Sync Data Task is error" + e.getMessage());
        }
    }
}
