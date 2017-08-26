package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerChargeRuleTypeDao;
import com.keymanager.monitoring.dao.CustomerKeywordDao;
import com.keymanager.monitoring.entity.CustomerChargeRuleCalculation;
import com.keymanager.monitoring.entity.CustomerChargeRuleInterval;
import com.keymanager.monitoring.entity.CustomerChargeType;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.enums.ChargeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shunshikj20 on 2017/8/21.
 */
@Service
public class CustomerChargeRuleTypeService extends ServiceImpl<CustomerChargeRuleTypeDao, CustomerChargeType> {

    @Autowired
    private CustomerChargeRuleTypeDao customerChargeRuleTypeDao;

    @Autowired
    private CustomerChargeRuleCalculationService customerChargeRuleCalculationService;

    @Autowired
    private CustomerChargeRuleIntervalService customerChargeRuleIntervalService;

    public void saveAndUpdateCustomerChargeRule(CustomerChargeType customerChargeType) throws Exception{
        Long customerChargeTypeUuid = customerChargeType.getUuid();
        if(null == customerChargeTypeUuid) {
            CustomerChargeType existingCustomerChargeType = customerChargeRuleTypeDao.selectByCustomerUuid(customerChargeType.getCustomerUuid());
            if(existingCustomerChargeType != null) {
                customerChargeTypeUuid = existingCustomerChargeType.getUuid();
            }
        }

        if(customerChargeTypeUuid != null){
            CustomerChargeRuleCalculation customerChargeRuleCalculation = new CustomerChargeRuleCalculation();
            customerChargeRuleCalculation.setCustomerChargeTypeUuid(customerChargeTypeUuid.intValue());
            Wrapper wrapperCalculation = new EntityWrapper(customerChargeRuleCalculation);
            CustomerChargeRuleInterval customerChargeRuleInterval  = new CustomerChargeRuleInterval();
            customerChargeRuleInterval.setCustomerChargeTypeUuid(customerChargeTypeUuid.intValue());
            Wrapper wrapperInterval = new EntityWrapper(customerChargeRuleInterval);
            customerChargeRuleCalculationService.delete(wrapperCalculation);
            customerChargeRuleIntervalService.delete(wrapperInterval);
            customerChargeRuleTypeDao.deleteById(customerChargeTypeUuid);
        }
        addCustomerChargeRule(customerChargeType);
    }

    private Boolean addCustomerChargeRule(CustomerChargeType customerChargeType){
        CustomerChargeType customerChargeTypeTmp = new CustomerChargeType();
        customerChargeTypeTmp.setCustomerUuid(customerChargeType.getCustomerUuid());
        customerChargeTypeTmp.setChargeType(customerChargeType.getChargeType());
        customerChargeRuleTypeDao.insert(customerChargeTypeTmp);
        int customerChargeRuleTypeUuid = customerChargeRuleTypeDao.selectLastId();
        if(ChargeTypeEnum.FixedPrice.name().equals(customerChargeType.getChargeType()) || ChargeTypeEnum.Percentage.name().equals(customerChargeType.getChargeType())){
            List<CustomerChargeRuleCalculation> customerChargeRuleCalculations = customerChargeType.getCustomerChargeRuleCalculations();
            for(CustomerChargeRuleCalculation customerChargeRuleCalculation:customerChargeRuleCalculations){
                customerChargeRuleCalculation.setCustomerChargeTypeUuid(customerChargeRuleTypeUuid);
                customerChargeRuleCalculationService.insert(customerChargeRuleCalculation);
            }
        }else{
            List<CustomerChargeRuleInterval> customerChargeRuleIntervals = customerChargeType.getCustomerChargeRuleIntervals();
            for(CustomerChargeRuleInterval customerChargeRuleInterval:customerChargeRuleIntervals){
                customerChargeRuleInterval.setCustomerChargeTypeUuid(customerChargeRuleTypeUuid);
                customerChargeRuleIntervalService.insert(customerChargeRuleInterval);
            }
        }
        return true;
    }


    public CustomerChargeType getCustomerChargeRule(Long customerUuid){
        CustomerChargeType customerChargeType = customerChargeRuleTypeDao.selectByCustomerUuid(customerUuid.intValue());
        if(null!=customerChargeType){
            List<CustomerChargeRuleCalculation> customerChargeRuleCalculations = customerChargeRuleCalculationService.selectBycustomerChargeRuleTypeUuid(customerChargeType.getUuid());
            if(customerChargeRuleCalculations.size()>0){
                customerChargeType.setCustomerChargeRuleCalculations(customerChargeRuleCalculations);
            }
            List<CustomerChargeRuleInterval> customerChargeRuleIntervals = customerChargeRuleIntervalService.selectBycustomerChargeRuleTypeUuid(customerChargeType.getUuid());
            if(customerChargeRuleIntervals.size()>0){
                customerChargeType.setCustomerChargeRuleIntervals(customerChargeRuleIntervals);
            }
        }
        return customerChargeType;
    }

}
