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

    @Resource(name = "positionHistoryService2")
    private PtKeywordPositionHistoryService positionHistoryService;

    /**
     * 检查操作中的关键词排名是否爬取完成, 关闭开关
     */
    public int checkFinishedCapturePosition() {
        return ptCustomerKeywordDao.checkFinishedCapturePosition();
    }

    public void updatePtCustomerKeywordStatus() {
        // 读取配置表需要同步pt关键词的客户信息
        Config config = configService.getConfig(Constants.CONFIG_TYPE_SYNC_CUSTOMER_PT_KEYWORD, Constants.CONFIG_KEY_SYNC_CUSTOMER_PT_KEYWORD);
        if (null != config) {
            String customerNameStr = config.getValue();
            if (StringUtil.isNotNullNorEmpty(customerNameStr)) {
                String[] customerNames = customerNameStr.replaceAll(" ", "").split(",");
                for (String customerName : customerNames) {
                    // 处理新增状态的关键词
                    List<PtCustomerKeyword> ptKeywords = ptCustomerKeywordDao.selectNewPtKeyword(customerName);
                    if (CollectionUtils.isNotEmpty(ptKeywords)) {
                        Customer customer = customerService.selectByName(customerName);
                        // 类似列表上传关键词，新增关键词并激活
                        customerKeywordService.addCustomerKeywordsFromSeoSystem(ptKeywords, customer.getUuid());
                        // 修改新增关键词的状态为激活并关联customerKeywordId
                        this.updateBatchById(ptKeywords);
                    }
                }

                // 处理已删除的关键词
                List<Long> customerKeywordUuids = ptCustomerKeywordDao.selectCustomerDelKeywords();
                if (CollectionUtils.isNotEmpty(customerKeywordUuids)) {
                    customerKeywordService.deleteBatchIds(customerKeywordUuids);
                }
                ptCustomerKeywordDao.deleteSaleDelKeywords();

                // 处理状态不同的关键词
                ptCustomerKeywordDao.updateCustomerKeywordDiffStatus();

                // 清理不再需要同步的客户数据
                ptCustomerKeywordDao.cleanNotExistCustomerKeyword(customerNames);

                // 统一清理历史排名数据
                positionHistoryService.cleanNotExistKeywordPositionHistory();
            }
        }
    }

    public void updatePtKeywordCurrentPosition(Long cusId, String type) {
        ptCustomerKeywordDao.updatePtKeywordCurrentPosition(cusId, type);
    }
}
