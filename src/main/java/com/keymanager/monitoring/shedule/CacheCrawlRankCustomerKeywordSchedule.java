package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheCrawlRankCustomerKeywordSchedule {

    private static Logger logger = LoggerFactory.getLogger(CacheCrawlRankCustomerKeywordSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void runTask() {
        logger.info("============= " + " Cache crawl rank Customer Keyword Task " + "===================");
        try {
            customerKeywordService.cacheCrawlRankCustomerKeywords();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Cache crawl rank Customer Keyword is error" + e.getMessage());
        }
    }
}
