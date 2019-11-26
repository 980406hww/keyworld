package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.dao.CustomerExcludeKeywordDao;
import com.keymanager.ckadmin.entity.CustomerExcludeKeyword;
import com.keymanager.ckadmin.service.CustomerExcludeKeywordService;
import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service("customerExcludeKeywordService2")
public class CustomerExcludeKeywordServiceImpl extends ServiceImpl<CustomerExcludeKeywordDao, CustomerExcludeKeyword> implements
    CustomerExcludeKeywordService {

    @Resource(name="customerExcludeKeywordDao2")
    private CustomerExcludeKeywordDao customerExcludeKeywordDao;

    @Override
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

    @Override
    public String getCustomerExcludeKeyword(Long customerUuid, Long qzSettingUuid, String terminalType, String url){
        url = url.replace("www.","").replace("http://","").replace("https://","").split("/")[0];
        return customerExcludeKeywordDao.getCustomerExcludeKeyword(customerUuid, qzSettingUuid, terminalType, url);
    }

    @Override
    public CustomerExcludeKeyword echoExcludeKeyword(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria){
        return customerExcludeKeywordDao.searchCustomerExcludeKeyword(qzSettingExcludeCustomerKeywordsCriteria.getQzSettingUuid(), qzSettingExcludeCustomerKeywordsCriteria.getTerminalType());
    }
}
