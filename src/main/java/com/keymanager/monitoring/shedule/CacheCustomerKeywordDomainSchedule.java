package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.UpdateKeywordBearsPawNumberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shunshikj40
 * @date 2019-10-28
 */
@Component
public class CacheCustomerKeywordDomainSchedule {

    private static final Logger logger = LoggerFactory.getLogger(CacheCustomerKeywordDomainSchedule.class);

    @Autowired
    private UpdateKeywordBearsPawNumberService updateKeywordBearsPawNumberService;

    public void runTask() {
        logger.info("============= " + " Cache Customer Keyword Domain Task " + "===================");
        try {
            updateKeywordBearsPawNumberService.cacheCustomerKeywordDomainMap();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Cache Customer Keyword Domain Task is error" + e.getMessage());
        }
    }
}
