package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.PtCustomerKeywordService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shunshikj40
 */
@Component
public class CheckPtCustomerKeywordStatusSchedule {

    private static final Logger logger = LoggerFactory.getLogger(CheckPtCustomerKeywordStatusSchedule.class);

    @Autowired
    private PtCustomerKeywordService ptCustomerKeywordService;

    public void runTask() {
        logger.info("============= " + " Check Pt Customer Keyword Status Schedule Task " + "===================");
        try {
            ptCustomerKeywordService.updatePtCustomerKeywordStatus();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" Check Pt Customer Keyword Status Schedule Task is error" + e.getMessage());
        }
    }
}
