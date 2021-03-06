package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.monitoring.dao.CustomerExcludeKeywordDao;
import com.keymanager.monitoring.entity.CustomerExcludeKeyword;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomerExcludeKeywordService extends ServiceImpl<CustomerExcludeKeywordDao, CustomerExcludeKeyword> {

    @Autowired
    private CustomerExcludeKeywordDao customerExcludeKeywordDao;

    public void excludeCustomerKeywords(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria){
        CustomerExcludeKeyword customerExcludeKeyword = new CustomerExcludeKeyword();
        customerExcludeKeyword.setCustomerUuid(qzSettingExcludeCustomerKeywordsCriteria.getCustomerUuid());
        customerExcludeKeyword.setQzSettingUuid(qzSettingExcludeCustomerKeywordsCriteria.getQzSettingUuid());
        customerExcludeKeyword.setTerminalType(qzSettingExcludeCustomerKeywordsCriteria.getTerminalType());
        if (CollectionUtils.isNotEmpty(qzSettingExcludeCustomerKeywordsCriteria.getKeywords())) {
            StringBuilder jointKeyword = new StringBuilder();
            for (String keyword : qzSettingExcludeCustomerKeywordsCriteria.getKeywords()) {
                jointKeyword.append(keyword).append(",");
            }
            customerExcludeKeyword.setKeyword(jointKeyword.toString().substring(0, jointKeyword.toString().length() - 1));
            if (null == qzSettingExcludeCustomerKeywordsCriteria.getExcludeKeywordUuid()) {
                customerExcludeKeywordDao.insert(customerExcludeKeyword);
            } else {
                customerExcludeKeyword.setUuid(qzSettingExcludeCustomerKeywordsCriteria.getExcludeKeywordUuid());
                customerExcludeKeyword.setUpdateTime(new Date());
                customerExcludeKeywordDao.updateById(customerExcludeKeyword);
            }
        } else {
            customerExcludeKeywordDao.deleteByQZSettingUuid(qzSettingExcludeCustomerKeywordsCriteria.getQzSettingUuid());
        }
    }

    public String getCustomerExcludeKeyword(Long customerUuid, Long qzSettingUuid, String terminalType, String url){
        url = url.replace("www.","").replace("http://","").replace("https://","").split("/")[0];
        return customerExcludeKeywordDao.getCustomerExcludeKeyword(customerUuid, qzSettingUuid, terminalType, url);
    }

    public CustomerExcludeKeyword echoExcludeKeyword(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria){
        return customerExcludeKeywordDao.searchCustomerExcludeKeyword(qzSettingExcludeCustomerKeywordsCriteria.getQzSettingUuid(), qzSettingExcludeCustomerKeywordsCriteria.getTerminalType());
    }
}
