package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerChargeTypePercentageDao;
import com.keymanager.monitoring.entity.CustomerChargeTypePercentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerChargeTypePercentageService extends ServiceImpl<CustomerChargeTypePercentageDao, CustomerChargeTypePercentage> {

    @Autowired
    private CustomerChargeTypePercentageDao customerChargeTypePercentageDao;

    public List<CustomerChargeTypePercentage> searchCustomerChargeTypePercentages(Long customerChargeTypeUuid) {
        return customerChargeTypePercentageDao.searchCustomerChargeTypePercentages(customerChargeTypeUuid);
    }

    public void deleteByCustomerChargeTypeUuid(Long customerChargeTypeUuid){
        customerChargeTypePercentageDao.deleteByCustomerChargeTypeUuid(customerChargeTypeUuid);
    }
}
