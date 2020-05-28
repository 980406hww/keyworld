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
public class SyncQzCustomerKeywordSchedule {

    private static final Logger logger = LoggerFactory.getLogger(SyncQzCustomerKeywordSchedule.class);

    @Autowired
    private QZSettingService qzSettingService;

    public void runTask() {
        logger.info("============= " + " Sync QZ Customer Data Schedule Task " + "===================");
        try {
            // 同步客户QZ关键词和曲线信息
            qzSettingService.getQZCustomerKeyword();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" Sync Customer Keyword Schedule Task is error" + e.getMessage());
        }
    }
}
