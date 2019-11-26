package com.keymanager.monitoring.shedule;

import com.keymanager.ckadmin.service.CustomerKeywordService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CacheCrawlRankCustomerQZKeywordSchedule {

    private static Logger logger = LoggerFactory.getLogger(CacheCrawlRankCustomerQZKeywordSchedule.class);

    @Resource(name = "customerKeywordService2")
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
