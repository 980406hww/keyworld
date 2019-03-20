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
        CustomerExcludeKeyword customerExcludeKeyword = customerExcludeKeywordDao.searchCustomerExcludeKeyword(qzSettingExcludeCustomerKeywordsCriteria.getQzSettingUuid(),qzSettingExcludeCustomerKeywordsCriteria.getTerminalType());
        if (null == customerExcludeKeyword){
            customerExcludeKeyword = new CustomerExcludeKeyword();
            customerExcludeKeyword.setCustomerUuid(qzSettingExcludeCustomerKeywordsCriteria.getCustomerUuid());
            customerExcludeKeyword.setQzSettingUuid(qzSettingExcludeCustomerKeywordsCriteria.getQzSettingUuid());
            customerExcludeKeyword.setTerminalType(qzSettingExcludeCustomerKeywordsCriteria.getTerminalType());
            for (String keyword: qzSettingExcludeCustomerKeywordsCriteria.getKeywords()) {
                jointKeyword.append(keyword + ",");
            }
            customerExcludeKeyword.setKeyword(jointKeyword.toString().substring(0, jointKeyword.toString().length() - 1));
            customerExcludeKeywordDao.insert(customerExcludeKeyword);
        } else {
            for (String keyword: qzSettingExcludeCustomerKeywordsCriteria.getKeywords()) {
                jointKeyword.append("," + keyword);
            }
            customerExcludeKeyword.setKeyword(customerExcludeKeyword.getKeyword() + jointKeyword);
            customerExcludeKeyword.setUpdateTime(new Date());
            customerExcludeKeywordDao.updateById(customerExcludeKeyword);
        }
    }

    public String getCustomerExcludeKeyword(Long customerUuid, Long qzSettingUuid, String terminalType, String url){
        url = url.replace("www.", "");
        return customerExcludeKeywordDao.getCustomerExcludeKeyword(customerUuid, qzSettingUuid, terminalType, url);
    }
}
