package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerChargeIntervalDao;
import com.keymanager.monitoring.entity.CustomerChargeTypeInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shunshikj20 on 2017/8/21.
 */
@Service
public class CustomerChargeRuleIntervalService extends ServiceImpl<CustomerChargeIntervalDao, CustomerChargeTypeInterval> {

    @Autowired
    private CustomerChargeIntervalDao customerChargeIntervalDao;

    public List<CustomerChargeTypeInterval> selectBycustomerChargeRuleTypeUuid(Long uuid){
        return customerChargeIntervalDao.selectBycustomerChargeTypeUuid(uuid);
    }

}
