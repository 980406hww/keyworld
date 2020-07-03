package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shunshikj40
 */
@Component
public class SyncPtCustomerKeywordSchedule {

    private static final Logger logger = LoggerFactory.getLogger(SyncPtCustomerKeywordSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void runTask() {
        logger.info("============= " + " Sync Pt Customer Keyword Schedule Task Start" + "===================");
        try {
            // 同步指定的客户关键词
            customerKeywordService.getPTCustomerKeyword();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" Sync Pt Customer Keyword Schedule Task is error" + e.getMessage());
        }
        logger.info("============= " + " Sync Pt Customer Keyword Schedule Task Finish" + "===================");
    }
}
