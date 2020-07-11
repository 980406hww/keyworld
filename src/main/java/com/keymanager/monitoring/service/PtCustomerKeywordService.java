package com.keymanager.monitoring.service;

import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.monitoring.dao.PtCustomerKeywordDao;
import com.keymanager.monitoring.entity.CmsSyncManage;
import com.keymanager.monitoring.entity.PtCustomerKeyword;
import com.keymanager.util.Constants;
import com.keymanager.util.common.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Resource(name = "cmsSyncManageService2")
    private CmsSyncManageService syncManageService;

    @Resource(name = "ptCustomerKeywordTemporaryService2")
    private PtCustomerKeywordTemporaryService ptCustomerKeywordTemporaryService;

    public void updatePtCustomerKeywordStatus() {
        // 读取配置表需要同步pt关键词的客户信息
        Config config = configService.getConfig(Constants.CONFIG_TYPE_SYNC_CUSTOMER_PT_KEYWORD, Constants.CONFIG_KEY_SYNC_CUSTOMER_NAME);
        if (null != config) {
            String customerNameStr = config.getValue();
            if (StringUtil.isNotNullNorEmpty(customerNameStr)) {
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

                HashMap<Long, HashMap<String, CmsSyncManage>> syncMap = syncManageService.searchSyncManageMap(customerNameStr, "pt");
                if (!syncMap.isEmpty()) {
                    for (Map.Entry<Long, HashMap<String, CmsSyncManage>> entry : syncMap.entrySet()) {
                        long userId = entry.getKey();
                        // 处理有更新的关键词 status = 4
                        // 清空临时表数据 delete
                        ptCustomerKeywordTemporaryService.cleanPtCustomerKeyword();
                        // 临时存放需要更新状态的关键词
                        ptCustomerKeywordTemporaryService.temporarilyStoreData(userId);
                        do {
                            // 修改标识为更新中，行数 rows set fMark = 2
                            ptCustomerKeywordTemporaryService.updatePtKeywordMarks(rows, 2, 0);
                            // 处理有更新状态的关键词 keyword, url
                            ptCustomerKeywordTemporaryService.updateCustomerKeywordStatus();
                            // 修改标识为已更新，行数 rows set fMark = 1
                            ptCustomerKeywordTemporaryService.updatePtKeywordMarks(rows, 1, 2);
                            // 更新 cms_keyword status 4 => 1 limit rows
                            ptCustomerKeywordDao.updatePtKeywordStatus(userId, rows);
                        } while (ptCustomerKeywordTemporaryService.searchPtKeywordTemporaryCount() > 0);

                        // 处理已下架的关键词 status = 3
                        List<Long> customerKeywordUuids = ptCustomerKeywordDao.selectCustomerDelKeywords(userId);
                        if (CollectionUtils.isNotEmpty(customerKeywordUuids)) {
                            customerKeywordService.updateSyncKeywordStatus(customerKeywordUuids, rows);
                        }

                        for (CmsSyncManage cmsSyncManage : entry.getValue().values()) {
                            Customer customer = customerService.selectByName(cmsSyncManage.getCompanyCode());
                            if (null != customer) {
                                // 处理新增状态的关键词 status = 2
                                List<PtCustomerKeyword> ptKeywords = ptCustomerKeywordDao.selectNewPtKeyword(userId);
                                if (CollectionUtils.isNotEmpty(ptKeywords)) {
                                    int fromIndex = 0, toIndex = rows;
                                    List<PtCustomerKeyword> tempList;
                                    do {
                                        tempList = ptKeywords.subList(fromIndex, Math.min(toIndex, ptKeywords.size()));
                                        // 类似列表上传关键词，新增关键词并激活
                                        customerKeywordService.addCustomerKeywordsFromSeoSystem(tempList, customer.getUuid(), optimizeGroupName, machineGroupName);
                                        // 修改新增关键词的状态为激活并关联customerKeywordId
                                        this.updateBatchById(tempList);
                                        tempList.clear();

                                        fromIndex += rows;
                                        toIndex += rows;
                                    } while (ptKeywords.size() > fromIndex);
                                    // 当前时间
                                    String currentTime = Utils.formatDatetime(Utils.getCurrentTimestamp(), "yyyy-MM-dd HH:mm");

                                    // 记录最近同步状态的时间
                                    cmsSyncManage.setSyncStatusTime(currentTime);
                                    syncManageService.updateById(cmsSyncManage);
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    public void updatePtKeywordCurrentPosition(long userId) {
        ptCustomerKeywordDao.updatePtKeywordCurrentPosition(userId);
    }

    public void updatePtKeywordOperaStatus(long userId) {
        ptCustomerKeywordDao.updatePtKeywordOperaStatus(userId);
    }
}
