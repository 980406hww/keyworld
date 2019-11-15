package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.CustomerChargeTypeIntervalDao;
import com.keymanager.ckadmin.entity.CustomerChargeTypeInterval;
import com.keymanager.ckadmin.service.CustomerChargeTypeIntervalService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("customerChargeTypeIntervalService2")
public class CustomerChargeTypeIntervalServiceImpl extends ServiceImpl<CustomerChargeTypeIntervalDao, CustomerChargeTypeInterval> implements
    CustomerChargeTypeIntervalService {

    @Resource(name = "customerChargeTypeIntervalDao2")
    private CustomerChargeTypeIntervalDao customerChargeTypeIntervalDao;

    @Override
    public void deleteByCustomerChargeTypeUuid(Long customerChargeTypeUuid) {
        customerChargeTypeIntervalDao.deleteByCustomerChargeTypeUuid(customerChargeTypeUuid);
    }

    @Override
    public List<CustomerChargeTypeInterval> searchCustomerChargeTypeIntervals(Long customerChargeTypeUuid) {
        return customerChargeTypeIntervalDao.searchCustomerChargeTypeIntervals(customerChargeTypeUuid);
    }
}
