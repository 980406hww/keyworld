package com.keymanager.monitoring.shedule;

import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.monitoring.service.PtCustomerKeywordService;

import com.keymanager.monitoring.service.SysCustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author shunshikj40
 */
@Component
public class CheckCustomerKeywordStatusSchedule {

    private static final Logger logger = LoggerFactory.getLogger(CheckCustomerKeywordStatusSchedule.class);

    @Resource(name = "ptCustomerKeywordService2")
    private PtCustomerKeywordService ptCustomerKeywordService;

    @Resource(name = "sysCustomerKeywordService2")
    private SysCustomerKeywordService sysCustomerKeywordService;

    public void runTask() {
        logger.info("============= " + " Check Customer Keyword Status Schedule Task Start " + "===================");
        try {
            logger.info("============= " + " Check PT Customer Keyword Status Schedule Task Start " + "===================");
            ptCustomerKeywordService.updatePtCustomerKeywordStatus();
            logger.info("============= " + " Check PT Customer Keyword Status Schedule Task Finish" + "===================");

            logger.info("============= " + " Check QZ Customer Keyword Status Schedule Task Start " + "===================");
            sysCustomerKeywordService.updateQzCustomerKeywordStatus();
            logger.info("============= " + " Check QZ Customer Keyword Status Schedule Task Finish" + "===================");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" Check Customer Keyword Status Schedule Task is error" + e.getMessage());
        }
        logger.info("============= " + " Check Pt Customer Keyword Status Schedule Task Finish" + "===================");
    }
}
