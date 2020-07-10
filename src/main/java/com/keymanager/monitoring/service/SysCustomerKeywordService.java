package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.monitoring.dao.SysCustomerKeywordDao;
import com.keymanager.monitoring.entity.CmsSyncManage;
import com.keymanager.monitoring.entity.SysCustomerKeyword;
import com.keymanager.monitoring.vo.QZSettingForSync;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sysCustomerKeywordService2")
public class SysCustomerKeywordService extends ServiceImpl<SysCustomerKeywordDao, SysCustomerKeyword> {

    @Autowired
    private SysCustomerKeywordDao sysCustomerKeywordDao;

    @Resource(name = "configService2")
    private ConfigService configService;

    @Resource(name = "cmsSyncManageService2")
    private CmsSyncManageService syncManageService;

    @Autowired
    private QZSettingService qzSettingService;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @Resource(name = "qzCustomerKeywordTemporaryService2")
    private QzCustomerKeywordTemporaryService qzCustomerKeywordTemporaryService;

    public void updateQzCustomerKeywordStatus() {
        // 读取配置表需要同步的客户网站标签
        Config config = configService.getConfig(Constants.CONFIG_TYPE_SYNC_QZ_CUSTOMER_KEYWORD, Constants.CONFIG_KEY_SYNC_QZ_CUSTOMER_TAG);
        if (null != config) {
            String syncQzCustomerTagStr = config.getValue();
            if (StringUtil.isNotNullNorEmpty(syncQzCustomerTagStr)) {
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

                HashMap<Long, HashMap<String, CmsSyncManage>> syncMap = syncManageService.searchSyncManageMap(syncQzCustomerTagStr, "qz");
                if (!syncMap.isEmpty()) {
                    for (Map.Entry<Long, HashMap<String, CmsSyncManage>> entry : syncMap.entrySet()) {
                        for (CmsSyncManage cmsSyncManage : entry.getValue().values()) {
                            String customerName = cmsSyncManage.getCompanyCode();
                            Customer customer = customerService.selectByName(customerName);
                            if (null != customer) {
                                List<QZSettingForSync> qzSettingForSyncs = qzSettingService.getAvailableQZSettingsByTagName(customerName);
                                if (CollectionUtils.isNotEmpty(qzSettingForSyncs)) {
                                    for (QZSettingForSync settingForSync : qzSettingForSyncs) {
                                        long qsId = settingForSync.getQsId();
                                        // 清空临时表数据 delete
                                        qzCustomerKeywordTemporaryService.cleanQzCustomerKeyword();
                                        // 临时存放需要更新状态的关键词
                                        qzCustomerKeywordTemporaryService.temporarilyStoreData(qsId);
                                        do {
                                            // 修改标识为更新中，行数 rows set fMark = 2
                                            qzCustomerKeywordTemporaryService.updateQzKeywordMarks(rows, 2, 0);
                                            // 处理有更新状态的关键词 keyword, url
                                            qzCustomerKeywordTemporaryService.updateCustomerKeywordStatusByQsID(qsId);
                                            // 修改标识为已更新，行数 rows set fMark = 1
                                            qzCustomerKeywordTemporaryService.updateQzKeywordMarks(rows, 1, 2);
                                        } while (qzCustomerKeywordTemporaryService.searchQzKeywordTemporaryCount() > 0);

                                        // 处理已删除的关键词 status = 3
                                        List<Long> customerKeywordUuids = sysCustomerKeywordDao.selectCustomerDelKeywords(qsId);
                                        if (CollectionUtils.isNotEmpty(customerKeywordUuids)) {
                                            customerKeywordService.deleteBatchIds(customerKeywordUuids);
                                        }
                                        sysCustomerKeywordDao.delBeDeletedKeyword(qsId);

                                        // 处理新增状态的关键词 status = 2
                                        List<SysCustomerKeyword> ptKeywords = sysCustomerKeywordDao.selectNewQzKeyword(qsId);
                                        if (CollectionUtils.isNotEmpty(ptKeywords)) {
                                            int fromIndex = 0, toIndex = rows;
                                            List<SysCustomerKeyword> tempList;
                                            do {
                                                tempList = ptKeywords.subList(fromIndex, Math.min(toIndex, ptKeywords.size()));
                                                // 类似列表上传关键词，新增关键词并激活
                                                customerKeywordService.addQzCustomerKeywordsFromSeoSystem(tempList, customer.getUuid(), qsId, optimizeGroupName, machineGroupName);
                                                // 修改新增关键词的状态为激活并关联customerKeywordId
                                                this.updateBatchById(tempList);
                                                tempList.clear();

                                                fromIndex += rows;
                                                toIndex += rows;
                                            } while (ptKeywords.size() > fromIndex);
                                        }
                                    }
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
}
