package com.keymanager.ckadmin.schedule;

import com.keymanager.ckadmin.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SyncPTCustomerKeywordOperaStatusSchedule {

    private static final Logger logger = LoggerFactory.getLogger(SyncPTCustomerKeywordOperaStatusSchedule.class);

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    public void runTask() {
        logger.info("============= " + " Check CustomerKeyword OperaStatus Task Start" + "===================");
        try {
            customerKeywordService.checkCustomerKeywordOperaStatus();
        } catch (Exception e) {
            logger.error(" Check CustomerKeyword OperaStatus Task Run error " + e.getMessage());
        }
        logger.info("============= " + " Check CustomerKeyword OperaStatus Task Finish" + "===================");
    }
}
