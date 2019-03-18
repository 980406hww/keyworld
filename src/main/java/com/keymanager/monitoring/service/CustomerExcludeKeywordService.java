package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.monitoring.dao.CustomerExcludeKeywordDao;
import com.keymanager.monitoring.entity.CustomerExcludeKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomerExcludeKeywordService extends ServiceImpl<CustomerExcludeKeywordDao, CustomerExcludeKeyword> {

    @Autowired
    private CustomerExcludeKeywordDao customerExcludeKeywordDao;

    public void excludeCustomerKeywords(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria){
        StringBuffer jointKeyword = new StringBuffer();
        for (String keyword: qzSettingExcludeCustomerKeywordsCriteria.getKeywords()) {
            jointKeyword.append(keyword+",");
        }
        CustomerExcludeKeyword customerExcludeKeyword = customerExcludeKeywordDao.searchCustomerExcludeKeyword(qzSettingExcludeCustomerKeywordsCriteria.getQzSettingUuid(),qzSettingExcludeCustomerKeywordsCriteria.getTerminalType());
        if (null  == customerExcludeKeyword){
            customerExcludeKeyword = new CustomerExcludeKeyword();
            customerExcludeKeyword.setCustomerUuid(qzSettingExcludeCustomerKeywordsCriteria.getCustomerUuid());
            customerExcludeKeyword.setQzSettingUuid(qzSettingExcludeCustomerKeywordsCriteria.getQzSettingUuid());
            customerExcludeKeyword.setTerminalType(qzSettingExcludeCustomerKeywordsCriteria.getTerminalType());
            customerExcludeKeyword.setKeyword(jointKeyword.toString());
            customerExcludeKeywordDao.insert(customerExcludeKeyword);
        }else {
            customerExcludeKeyword.setKeyword(customerExcludeKeyword.getKeyword()+jointKeyword);
            customerExcludeKeyword.setUpdateTime(new Date());
            customerExcludeKeywordDao.updateById(customerExcludeKeyword);
        }
    }

    public int checkCustomerExcludeKeyword(Long customerUuid, Long qzSettingUuid, String terminalType, String keyword){
        return customerExcludeKeywordDao.checkCustomerExcludeKeyword(customerUuid, qzSettingUuid, terminalType, keyword);
    }
}
