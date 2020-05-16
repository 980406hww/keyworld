package com.keymanager.ckadmin.schedule;

import com.keymanager.ckadmin.service.CustomerKeywordService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CacheCustomerKeywordSchedule {
    private static Logger logger = LoggerFactory.getLogger(CacheCustomerKeywordSchedule.class);

    @Resource(name = "customerKeywordService2")
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
