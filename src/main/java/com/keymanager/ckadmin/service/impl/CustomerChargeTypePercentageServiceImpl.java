package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.CustomerChargeTypePercentageDao;
import com.keymanager.ckadmin.entity.CustomerChargeTypePercentage;
import com.keymanager.ckadmin.service.CustomerChargeTypePercentageService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("customerChargeTypePercentageService2")
public class CustomerChargeTypePercentageServiceImpl extends ServiceImpl<CustomerChargeTypePercentageDao, CustomerChargeTypePercentage> implements
    CustomerChargeTypePercentageService {

    @Resource(name = "customerChargeTypePercentageDao2")
    private CustomerChargeTypePercentageDao customerChargeTypePercentageDao;

    @Override
    public void deleteByCustomerChargeTypeUuid(Long customerChargeTypeUuid) {
        customerChargeTypePercentageDao.deleteByCustomerChargeTypeUuid(customerChargeTypeUuid);
    }

    @Override
    public List<CustomerChargeTypePercentage> searchCustomerChargeTypePercentages(Long customerChargeTypeUuid) {
        return customerChargeTypePercentageDao.searchCustomerChargeTypePercentages(customerChargeTypeUuid);
    }
}
