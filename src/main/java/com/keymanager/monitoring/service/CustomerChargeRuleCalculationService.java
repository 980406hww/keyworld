package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerChargeRuleCalculationDao;
import com.keymanager.monitoring.dao.CustomerChargeRuleTypeDao;
import com.keymanager.monitoring.entity.CustomerChargeRuleCalculation;
import com.keymanager.monitoring.entity.CustomerChargeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shunshikj20 on 2017/8/21.
 */
@Service
public class CustomerChargeRuleCalculationService extends ServiceImpl<CustomerChargeRuleCalculationDao, CustomerChargeRuleCalculation> {

    @Autowired
    private CustomerChargeRuleCalculationDao customerChargeRuleCalculationDao;

    public List<CustomerChargeRuleCalculation> selectBycustomerChargeRuleTypeUuid(Long uuid){
        return customerChargeRuleCalculationDao.selectBycustomerChargeRuleTypeUuid(uuid);
    }


}
