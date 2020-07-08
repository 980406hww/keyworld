package com.keymanager.ckadmin.schedule;

import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.QZSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SyncCustomerKeywordOperaStatusSchedule {

    private static final Logger logger = LoggerFactory.getLogger(SyncCustomerKeywordOperaStatusSchedule.class);

    @Resource(name = "configService2")
    private ConfigService configService;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    public void runTask() {
        logger.info("============= " + " Check CustomerKeyword OperaStatus Task Start" + "===================");
        try {
            // 同步关键词操作状态的开关 1：开 0：关
            Config syncSwitch = configService.getConfig(com.keymanager.util.Constants.CONFIG_TYPE_SYNC_OPERA_STATUS_SWITCH, com.keymanager.util.Constants.CONFIG_KEY_SYNC_OPERA_STATUS_SWITCH_NAME);
            if (null != syncSwitch && "1".equals(syncSwitch.getValue())) {
                logger.info("============= " + " Check PT CustomerKeyword OperaStatus Task Start" + "===================");
                customerKeywordService.checkPtCustomerKeywordOperaStatus();
                logger.info("============= " + " Check PT CustomerKeyword OperaStatus Task Finish" + "===================");

                logger.info("============= " + " Check QZ CustomerKeyword OperaStatus Task Start" + "===================");
                qzSettingService.checkQzCustomerKeywordOperaStatus();
                logger.info("============= " + " Check QZ CustomerKeyword OperaStatus Task Finish" + "===================");
            }
        } catch (Exception e) {
            logger.error(" Check CustomerKeyword OperaStatus Task Run error " + e.getMessage());
        }
        logger.info("============= " + " Check CustomerKeyword OperaStatus Task Finish" + "===================");
    }
}
