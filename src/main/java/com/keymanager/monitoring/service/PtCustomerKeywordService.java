package com.keymanager.monitoring.service;

import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.monitoring.dao.PtCustomerKeywordDao;
import com.keymanager.monitoring.entity.PtCustomerKeyword;
import com.keymanager.util.Constants;
import com.keymanager.util.common.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

@Service("ptCustomerKeywordService2")
public class PtCustomerKeywordService extends ServiceImpl<PtCustomerKeywordDao, PtCustomerKeyword> {

    @Autowired
    private PtCustomerKeywordDao ptCustomerKeywordDao;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @Resource(name = "configService2")
    private ConfigService configService;

    public void updatePtCustomerKeywordStatus() {
        // 同步关键词操作状态的开关 1：开 0：关
        Config syncSwitch = configService.getConfig(Constants.CONFIG_TYPE_SYNC_OPERA_STATUS_SWITCH, Constants.CONFIG_KEY_SYNC_OPERA_STATUS_SWITCH_NAME);
        if (null != syncSwitch && "1".equals(syncSwitch.getValue())) {
            // 读取配置表需要同步pt关键词的客户信息
            Config config = configService.getConfig(Constants.CONFIG_TYPE_SYNC_CUSTOMER_PT_KEYWORD, Constants.CONFIG_KEY_SYNC_CUSTOMER_NAME);
            if (null != config) {
                String customerNameStr = config.getValue();
                if (StringUtil.isNotNullNorEmpty(customerNameStr)) {
                    String[] customerNames = customerNameStr.replaceAll(" ", "").split(",");

                    // 处理有更新状态的关键词 status = 4 keyword, url, title
                    ptCustomerKeywordDao.updateCustomerKeyword();

                    // 处理已删除的关键词 status = 3
                    List<Long> customerKeywordUuids = ptCustomerKeywordDao.selectCustomerDelKeywords();
                    if (CollectionUtils.isNotEmpty(customerKeywordUuids)) {
                        customerKeywordService.deleteBatchIds(customerKeywordUuids);
                    }
                    ptCustomerKeywordDao.deleteSaleDelKeywords();

                    // 处理暂不操作的词 status = 0
                    ptCustomerKeywordDao.updateCustomerKeywordDiffStatus();

                    // 清理不再需要同步的客户数据
                    ptCustomerKeywordDao.cleanNotExistCustomerKeyword(customerNames);

                    // 一次处理 n条数据
                    int rows = 5000;
                    // 读取配置表一次操作的大小
                    Config defaultSubListSize = configService.getConfig(Constants.CONFIG_TYPE_KEYWORD_SUB_LIST_SIZE, Constants.CONFIG_KEY_KEYWORD_SUB_LIST_NAME);
                    if (null != defaultSubListSize) {
                        rows = Integer.parseInt(defaultSubListSize.getValue());
                    }
                    // 默认优化组
                    String optimizeGroupName = "Default";
                    // 读取配置表默认优化组
                    Config defaultOptimizeGroupName = configService.getConfig(Constants.CONFIG_TYPE_DEFAULT_OPTIMIZE_GROUP, Constants.CONFIG_KEY_DEFAULT_OPTIMIZE_GROUP_NAME);
                    if (null != defaultOptimizeGroupName) {
                        optimizeGroupName = defaultOptimizeGroupName.getValue();
                    }
                    // 默认机器分组
                    String machineGroupName = "super";
                    // 读取配置表默认优化组
                    Config defaultMachineGroupName = configService.getConfig(Constants.CONFIG_TYPE_DEFAULT_MACHINE_GROUP, Constants.CONFIG_KEY_DEFAULT_MACHINE_GROUP_NAME);
                    if (null != defaultMachineGroupName) {
                        machineGroupName = defaultMachineGroupName.getValue();
                    }

                    for (String customerName : customerNames) {
                        // 处理新增状态的关键词 status = 2
                        List<PtCustomerKeyword> ptKeywords = ptCustomerKeywordDao.selectNewPtKeyword(customerName);
                        if (CollectionUtils.isNotEmpty(ptKeywords)) {
                            int fromIndex = 0, toIndex = rows;
                            List<PtCustomerKeyword> tempList;
                            do {
                                tempList = ptKeywords.subList(fromIndex, Math.min(toIndex, ptKeywords.size()));

                                Customer customer = customerService.selectByName(customerName);
                                // 类似列表上传关键词，新增关键词并激活
                                customerKeywordService.addCustomerKeywordsFromSeoSystem(tempList, customer.getUuid(), optimizeGroupName, machineGroupName);
                                // 修改新增关键词的状态为激活并关联customerKeywordId
                                this.updateBatchById(tempList);

                                fromIndex += rows;
                                toIndex += rows;
                                tempList.clear();
                            } while (ptKeywords.size() > fromIndex);
                        }
                    }
                }
            }
        }
    }

    public void updatePtKeywordCurrentPosition() {
        ptCustomerKeywordDao.updatePtKeywordCurrentPosition();
    }

    public void updatePtKeywordOperaStatus() {
        ptCustomerKeywordDao.updatePtKeywordOperaStatus();
    }
}
