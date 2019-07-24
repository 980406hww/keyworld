package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheCustomerKeywordSchedule {
    private static Logger logger = LoggerFactory.getLogger(CacheCustomerKeywordSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void runTask(){
        logger.info("============= " + " Cache Customer Keyword Task "+"===================");
        try{
            customerKeywordService.cacheCustomerKeywords();
        } catch (Exception e) {
            logger.error("Cache Customer Keyword is error" + e.getMessage());
        }
    }
}
