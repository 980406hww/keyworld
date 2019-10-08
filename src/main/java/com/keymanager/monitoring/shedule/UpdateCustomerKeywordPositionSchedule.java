package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shunshikj40 2019-09-25 16:14
 */
@Component
public class UpdateCustomerKeywordPositionSchedule {

    private static final Logger logger = LoggerFactory.getLogger(UpdateCustomerKeywordPositionSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void runTask(){
        logger.info("=============  Update Customer Keyword Position Task ===================");
        try {
            customerKeywordService.autoUpdateCustomerKeywordPosition();
        } catch (Exception e) {
            logger.error("Update Customer Keyword Position is error" + e.getMessage());
        }
    }
}
