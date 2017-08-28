package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerChargeTypeCalculationDao;
import com.keymanager.monitoring.entity.CustomerChargeTypeCalculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shunshikj20 on 2017/8/21.
 */
@Service
public class CustomerChargeRuleCalculationService extends ServiceImpl<CustomerChargeTypeCalculationDao, CustomerChargeTypeCalculation> {

    @Autowired
    private CustomerChargeTypeCalculationDao customerChargeTypeCalculationDao;

    public List<CustomerChargeTypeCalculation> selectBycustomerChargeRuleTypeUuid(Long uuid){
        return customerChargeTypeCalculationDao.selectBycustomerChargeTypeUuid(uuid);
    }


}
