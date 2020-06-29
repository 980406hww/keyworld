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
import java.util.ArrayList;
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

                for (String customerName : customerNames) {
                    // 处理新增状态的关键词 status = 2
                    List<PtCustomerKeyword> ptKeywords = ptCustomerKeywordDao.selectNewPtKeyword(customerName);
                    if (CollectionUtils.isNotEmpty(ptKeywords)) {
                        // 考虑到数据量太大，可能出现sql超时问题
                        // 一次处理5000条数据
                        int fromIndex = 0, toIndex = 5000;
                        ArrayList<PtCustomerKeyword> tempList;
                        do {
                            tempList = (ArrayList<PtCustomerKeyword>) ptKeywords.subList(fromIndex, Math.min(toIndex, ptKeywords.size()));

                            Customer customer = customerService.selectByName(customerName);
                            // 类似列表上传关键词，新增关键词并激活
                            customerKeywordService.addCustomerKeywordsFromSeoSystem(tempList, customer.getUuid());
                            // 修改新增关键词的状态为激活并关联customerKeywordId
                            this.updateBatchById(tempList);

                            fromIndex += 5000;
                            toIndex += 5000;
                            tempList.clear();
                        } while (ptKeywords.size() > fromIndex);
                    }
                }
            }
        }
    }

    public void updatePtKeywordCurrentPosition() {
        ptCustomerKeywordDao.updatePtKeywordCurrentPosition();
    }
}
