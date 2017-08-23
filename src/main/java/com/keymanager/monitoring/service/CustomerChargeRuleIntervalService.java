package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerChargeRuleIntervalDao;
import com.keymanager.monitoring.dao.CustomerChargeRuleTypeDao;
import com.keymanager.monitoring.entity.CustomerChargeRuleCalculation;
import com.keymanager.monitoring.entity.CustomerChargeRuleInterval;
import com.keymanager.monitoring.entity.CustomerChargeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.List;

/**
 * Created by shunshikj20 on 2017/8/21.
 */
@Service
public class CustomerChargeRuleIntervalService extends ServiceImpl<CustomerChargeRuleIntervalDao, CustomerChargeRuleInterval> {

    @Autowired
    private CustomerChargeRuleIntervalDao customerChargeRuleIntervalDao;

    public List<CustomerChargeRuleInterval> selectBycustomerChargeRuleTypeUuid(Long uuid){
        return customerChargeRuleIntervalDao.selectBycustomerChargeRuleTypeUuid(uuid);
    }

}
