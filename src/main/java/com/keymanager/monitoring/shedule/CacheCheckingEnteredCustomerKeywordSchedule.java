package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheCheckingEnteredCustomerKeywordSchedule {

    private static Logger logger = LoggerFactory.getLogger(CacheCheckingEnteredCustomerKeywordSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void runTask() {
        logger.info("============= " + " Cache checking entered Customer Keyword Task " + "===================");
        try {
            customerKeywordService.cacheCheckingEnteredCustomerKeywords();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Cache checking entered Customer Keyword is error" + e.getMessage());
        }
    }
}
