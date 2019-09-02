package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.CustomerKeywordDao2;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.service.CustomerKeywordInterface;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("customerkeywordService2")
public class CustomerKeywordService2 extends ServiceImpl<CustomerKeywordDao2, CustomerKeyword> implements
        CustomerKeywordInterface {
    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordService2.class);

    @Resource(name = "customerKeywordDao2")
    private CustomerKeywordDao2 customerKeywordDao2;

    @Override
    public List<Map> getCustomerKeywordsCount(List<Long> customerUuids, String terminalType, String entryType) {
        return customerKeywordDao2.getCustomerKeywordsCount(customerUuids, terminalType, entryType);
    }


}


