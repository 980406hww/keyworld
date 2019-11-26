package com.keymanager.monitoring.shedule;

import com.keymanager.ckadmin.service.CustomerKeywordService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CacheCrawlRankCustomerPTKeywordSchedule {

    private static Logger logger = LoggerFactory.getLogger(CacheCrawlRankCustomerPTKeywordSchedule.class);

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    public void runTask() {
        logger.info("============= " + " Cache crawl rank Customer PT Keyword Task " + "===================");
        try {
            customerKeywordService.cacheCrawlRankCustomerPTKeywords();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Cache crawl rank Customer PT Keyword is error" + e.getMessage());
        }
    }
}
