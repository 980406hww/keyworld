package com.keymanager.ckadmin.shedule;

import com.keymanager.ckadmin.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "cacheCustomerKeywordSchedule2")
public class CacheCustomerKeywordSchedule2 {
    private static Logger logger = LoggerFactory.getLogger(CacheCustomerKeywordSchedule2.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void runTask(){
        logger.info("============= " + " Cache Customer Keyword Task "+"===================");
        try{
            customerKeywordService.cacheCustomerKeywords();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Cache Customer Keyword is error" + e.getMessage());
        }
    }
}
