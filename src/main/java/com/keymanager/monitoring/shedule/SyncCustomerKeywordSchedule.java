package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.service.PtCustomerKeywordService;
import com.keymanager.monitoring.service.QZSettingService;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shunshikj40
 */
@Component
public class SyncCustomerKeywordSchedule {

    private static final Logger logger = LoggerFactory.getLogger(SyncCustomerKeywordSchedule.class);

    @Autowired
    private QZSettingService qzSettingService;

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @Autowired
    private PtCustomerKeywordService ptCustomerKeywordService;

    @Autowired
    private ConfigService configService;

    public void runTask() {
        logger.info("============= " + " Sync QZ Customer Data Schedule Task " + "===================");
        try {
            // 同步指定的全站曲线和关键词
            qzSettingService.getQZCustomerKeyword();

            // 获取当前时间
            String currentDate = Utils.getCurrentDate();
            // 读取配置表客户pt关键词日期和完成标识
            Config ptFinishedConfig = configService.getConfig(Constants.CONFIG_TYPE_SYNC_CUSTOMER_PT_KEYWORD_SWITCH, currentDate);
            if (null != ptFinishedConfig) {
                if (!currentDate.equals(ptFinishedConfig.getKey())) {
                    ptFinishedConfig.setKey(currentDate);
                    ptFinishedConfig.setValue("1");
                    configService.updateConfig(ptFinishedConfig);
                }

                // 检查操作中的关键词排名是否爬取完成, 关闭开关
                if (ptCustomerKeywordService.checkFinishedCapturePosition() == 0) {
                    if (!"0".equals(ptFinishedConfig.getValue())) {
                        ptFinishedConfig.setValue("0");
                        configService.updateConfig(ptFinishedConfig);
                    }
                }

                if (!"0".equals(ptFinishedConfig.getValue())) {
                    // 同步指定的客户关键词
                    customerKeywordService.getPTCustomerKeyword();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" Sync Customer Keyword Schedule Task is error" + e.getMessage());
        }
    }
}
