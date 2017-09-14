package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.dao.CustomerKeywordRefreshStatInfoDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.util.Constants;
import com.keymanager.value.CustomerKeywordRefreshStatInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj08 on 2017/9/12.
 */
@Service
public class CustomerKeywordRefreshStatInfoService extends ServiceImpl<CustomerKeywordRefreshStatInfoDao, CustomerKeywordRefreshStatInfoVO> {

    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordRefreshStatInfoService.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    private ClientStatusService clientStatusService;

    @Autowired
    private CustomerKeywordRefreshStatInfoDao customerKeywordRefreshStatInfoDao;

    public List<CustomerKeywordRefreshStatInfoVO> generateCustomerKeywordStatInfo(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        List<CustomerKeywordRefreshStatInfoVO> customerKeywordRefreshStatInfoVOs = getCustomerKeywordStatInfoVOList(customerKeywordRefreshStatInfoCriteria);
        Map<String, CustomerKeywordRefreshStatInfoVO> customerKeywordRefreshStatInfoVOMap = new HashMap<String, CustomerKeywordRefreshStatInfoVO>();
        for (CustomerKeywordRefreshStatInfoVO customerKeywordRefreshStatInfoVO : customerKeywordRefreshStatInfoVOs) {
            customerKeywordRefreshStatInfoVOMap.put(customerKeywordRefreshStatInfoVO.getGroup(), customerKeywordRefreshStatInfoVO);
        }

        List<ClientStatus> clientStatuseList = clientStatusService.getClientStatusList(customerKeywordRefreshStatInfoCriteria);
        for (ClientStatus clientStatus : clientStatuseList) {
            CustomerKeywordRefreshStatInfoVO customerKeywordRefreshStatInfoVO = customerKeywordRefreshStatInfoVOMap.get(clientStatus.getGroup());
            if (customerKeywordRefreshStatInfoVO != null) {
                customerKeywordRefreshStatInfoVO.setTotalMachineCount(customerKeywordRefreshStatInfoVO.getTotalMachineCount() + 1);
                if (clientStatus.getRed()) {
                    customerKeywordRefreshStatInfoVO.setUnworkMachineCount(customerKeywordRefreshStatInfoVO.getUnworkMachineCount() + 1);
                }
            }
        }
        CustomerKeywordRefreshStatInfoVO total = new CustomerKeywordRefreshStatInfoVO();
        total.setGroup("总计");
        for (CustomerKeywordRefreshStatInfoVO customerKeywordRefreshStatInfoVO : customerKeywordRefreshStatInfoVOs) {
            total.setInvalidKeywordCount(total.getInvalidKeywordCount() + customerKeywordRefreshStatInfoVO.getInvalidKeywordCount());
            total.setNeedOptimizeCount(total.getNeedOptimizeCount() + customerKeywordRefreshStatInfoVO.getNeedOptimizeCount());
            total.setNeedOptimizeKeywordCount(total.getNeedOptimizeKeywordCount() + customerKeywordRefreshStatInfoVO.getNeedOptimizeKeywordCount());
            total.setQueryCount(total.getQueryCount() + customerKeywordRefreshStatInfoVO.getQueryCount());
            total.setTotalKeywordCount(total.getTotalKeywordCount() + customerKeywordRefreshStatInfoVO.getTotalKeywordCount());
            total.setTotalMachineCount(total.getTotalMachineCount() + customerKeywordRefreshStatInfoVO.getTotalMachineCount());
            total.setTotalOptimizeCount(total.getTotalOptimizeCount() + customerKeywordRefreshStatInfoVO.getTotalOptimizeCount());
            total.setTotalOptimizedCount(total.getTotalOptimizedCount() + customerKeywordRefreshStatInfoVO.getTotalOptimizedCount());
            total.setUnworkMachineCount(total.getUnworkMachineCount() + customerKeywordRefreshStatInfoVO.getUnworkMachineCount());
            total.setMaxInvalidCount(customerKeywordRefreshStatInfoVO.getMaxInvalidCount());
        }
        customerKeywordRefreshStatInfoVOs.add(0, total);
        return customerKeywordRefreshStatInfoVOs;
    }

    private List<CustomerKeywordRefreshStatInfoVO> getCustomerKeywordStatInfoVOList(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        Config config = configService.getConfig(Constants.CONFIG_KEY_MAX_INVALID_COUNT, customerKeywordRefreshStatInfoCriteria.getType());
        customerKeywordRefreshStatInfoCriteria.setConfigValue(config.getValue());
        List<CustomerKeywordRefreshStatInfoVO> customerKeywordRefreshStatInfoVOs = customerKeywordRefreshStatInfoDao.searchCustomerKeywordStatInfoVOs(customerKeywordRefreshStatInfoCriteria);
        List<CustomerKeywordRefreshStatInfoVO> customerKeywordRefreshStatInfoVOList = new ArrayList<CustomerKeywordRefreshStatInfoVO>();
        for(CustomerKeywordRefreshStatInfoVO customerKeywordRefreshStatInfoVO : customerKeywordRefreshStatInfoVOs) {
            CustomerKeywordRefreshStatInfoVO refreshStatInfoVO = new CustomerKeywordRefreshStatInfoVO();
            refreshStatInfoVO.setGroup(customerKeywordRefreshStatInfoVO.getGroup());
            refreshStatInfoVO.setTotalKeywordCount(customerKeywordRefreshStatInfoVO.getTotalKeywordCount());
            refreshStatInfoVO.setNeedOptimizeKeywordCount(customerKeywordRefreshStatInfoVO.getNeedOptimizeKeywordCount());
            refreshStatInfoVO.setInvalidKeywordCount(customerKeywordRefreshStatInfoVO.getInvalidKeywordCount());
            refreshStatInfoVO.setTotalOptimizeCount(customerKeywordRefreshStatInfoVO.getTotalOptimizeCount());
            refreshStatInfoVO.setTotalOptimizedCount(customerKeywordRefreshStatInfoVO.getTotalOptimizedCount());
            refreshStatInfoVO.setNeedOptimizeCount(customerKeywordRefreshStatInfoVO.getNeedOptimizeCount());
            refreshStatInfoVO.setQueryCount(customerKeywordRefreshStatInfoVO.getQueryCount());
            refreshStatInfoVO.setMaxInvalidCount(Integer.parseInt(config.getValue()));
            customerKeywordRefreshStatInfoVOList.add(refreshStatInfoVO);
        }
        return customerKeywordRefreshStatInfoVOList;
    }
}
