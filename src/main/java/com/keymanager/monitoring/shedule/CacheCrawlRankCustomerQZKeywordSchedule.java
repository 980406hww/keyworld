package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheCrawlRankCustomerQZKeywordSchedule {

    private static Logger logger = LoggerFactory.getLogger(CacheCrawlRankCustomerQZKeywordSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void runTask() {
        logger.info("============= " + " Cache crawl rank Customer QZ Keyword Task " + "===================");
        try {
            customerKeywordService.cacheCrawlRankCustomerQZKeywords();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Cache crawl rank Customer QZ Keyword is error" + e.getMessage());
        }
    }
}
