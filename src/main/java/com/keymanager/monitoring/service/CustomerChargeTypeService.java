package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerChargeTypeDao;
import com.keymanager.monitoring.entity.CustomerChargeTypeCalculation;
import com.keymanager.monitoring.entity.CustomerChargeTypeInterval;
import com.keymanager.monitoring.entity.CustomerChargeType;
import com.keymanager.monitoring.enums.ChargeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj20 on 2017/8/21.
 */
@Service
public class CustomerChargeTypeService extends ServiceImpl<CustomerChargeTypeDao, CustomerChargeType> {

    @Autowired
    private CustomerChargeTypeDao customerChargeTypeDao;

    @Autowired
    private CustomerChargeRuleCalculationService customerChargeRuleCalculationService;

    @Autowired
    private CustomerChargeRuleIntervalService customerChargeRuleIntervalService;

    public void saveCustomerChargeType(CustomerChargeType customerChargeType) throws Exception{
            if(null!=customerChargeType.getUuid()){
                CustomerChargeTypeCalculation customerChargeTypeCalculation = new CustomerChargeTypeCalculation();
                customerChargeTypeCalculation.setCustomerChargeTypeUuid(customerChargeType.getUuid().intValue());
                Wrapper wrapperCalculation = new EntityWrapper(customerChargeTypeCalculation);
                CustomerChargeTypeInterval customerChargeTypeInterval = new CustomerChargeTypeInterval();
                customerChargeTypeInterval.setCustomerChargeTypeUuid(customerChargeType.getUuid().intValue());
                Wrapper wrapperInterval = new EntityWrapper(customerChargeTypeInterval);
                customerChargeRuleCalculationService.delete(wrapperCalculation);
                customerChargeRuleIntervalService.delete(wrapperInterval);
                updateCustomerChargeRule(customerChargeType);
                CustomerChargeType OldCustomerChargeType = customerChargeTypeDao.selectById(customerChargeType.getUuid());
                OldCustomerChargeType.setCustomerUuid(customerChargeType.getCustomerUuid());
                OldCustomerChargeType.setChargeType(customerChargeType.getChargeType());
                customerChargeType.setUpdateTime(new Date());
                customerChargeTypeDao.updateById(OldCustomerChargeType);
            }else {
                addCustomerChargeRule(customerChargeType);
            }
    }

    private void addCustomerChargeRule(CustomerChargeType customerChargeType){
        CustomerChargeType customerChargeTypeTmp = new CustomerChargeType();
        customerChargeTypeTmp.setCustomerUuid(customerChargeType.getCustomerUuid());
        customerChargeTypeTmp.setChargeType(customerChargeType.getChargeType());
        customerChargeTypeDao.insert(customerChargeTypeTmp);
        int customerChargeRuleTypeUuid = customerChargeTypeDao.selectLastId();
        if(ChargeTypeEnum.Percentage.name().equals(customerChargeType.getChargeType())){
            List<CustomerChargeTypeCalculation> customerChargeTypeCalculations = customerChargeType.getCustomerChargeTypeCalculations();
            for(CustomerChargeTypeCalculation customerChargeTypeCalculation : customerChargeTypeCalculations){
                customerChargeTypeCalculation.setCustomerChargeTypeUuid(customerChargeRuleTypeUuid);
                customerChargeRuleCalculationService.insert(customerChargeTypeCalculation);
            }
        }else{
            List<CustomerChargeTypeInterval> customerChargeTypeIntervals = customerChargeType.getCustomerChargeTypeIntervals();
            for(CustomerChargeTypeInterval customerChargeTypeInterval : customerChargeTypeIntervals){
                customerChargeTypeInterval.setCustomerChargeTypeUuid(customerChargeRuleTypeUuid);
                customerChargeRuleIntervalService.insert(customerChargeTypeInterval);
            }
        }
    }

    private void updateCustomerChargeRule(CustomerChargeType customerChargeType){
        if(ChargeTypeEnum.Percentage.name().equals(customerChargeType.getChargeType())){
            List<CustomerChargeTypeCalculation> customerChargeTypeCalculations = customerChargeType.getCustomerChargeTypeCalculations();
            for(CustomerChargeTypeCalculation customerChargeTypeCalculation : customerChargeTypeCalculations){
                customerChargeTypeCalculation.setCustomerChargeTypeUuid(customerChargeType.getUuid().intValue());
                customerChargeRuleCalculationService.insert(customerChargeTypeCalculation);
            }
        }else{
            List<CustomerChargeTypeInterval> customerChargeTypeIntervals = customerChargeType.getCustomerChargeTypeIntervals();
            for(CustomerChargeTypeInterval customerChargeTypeInterval : customerChargeTypeIntervals){
                customerChargeTypeInterval.setCustomerChargeTypeUuid(customerChargeType.getUuid().intValue());
                customerChargeRuleIntervalService.insert(customerChargeTypeInterval);
            }
        }
    }


    public CustomerChargeType getCustomerChargeType(Long customerUuid){
        CustomerChargeType customerChargeType = customerChargeTypeDao.selectByCustomerUuid(customerUuid.intValue());
        if(null!=customerChargeType){
            List<CustomerChargeTypeCalculation> customerChargeTypeCalculations = customerChargeRuleCalculationService.selectBycustomerChargeRuleTypeUuid(customerChargeType.getUuid());
            if(customerChargeTypeCalculations.size()>0){
                customerChargeType.setCustomerChargeTypeCalculations(customerChargeTypeCalculations);
            }
            List<CustomerChargeTypeInterval> customerChargeTypeIntervals = customerChargeRuleIntervalService.selectBycustomerChargeRuleTypeUuid(customerChargeType.getUuid());
            if(customerChargeTypeIntervals.size()>0){
                customerChargeType.setCustomerChargeTypeIntervals(customerChargeTypeIntervals);
            }
        }
        return customerChargeType;
    }

}
