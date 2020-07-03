package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.PtCustomerKeywordService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author shunshikj40
 */
@Component
public class CheckPtCustomerKeywordStatusSchedule {

    private static final Logger logger = LoggerFactory.getLogger(CheckPtCustomerKeywordStatusSchedule.class);

    @Resource(name = "ptCustomerKeywordService2")
    private PtCustomerKeywordService ptCustomerKeywordService;

    public void runTask() {
        logger.info("============= " + " Check Pt Customer Keyword Status Schedule Task Start " + "===================");
        try {
            ptCustomerKeywordService.updatePtCustomerKeywordStatus();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" Check Pt Customer Keyword Status Schedule Task is error" + e.getMessage());
        }
        logger.info("============= " + " Check Pt Customer Keyword Status Schedule Task Finish" + "===================");
    }
}
