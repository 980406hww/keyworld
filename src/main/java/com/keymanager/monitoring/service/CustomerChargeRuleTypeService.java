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

    public Boolean saveAndUpdateCustomerChargeRule(CustomerChargeType customerChargeType){
        try{
            if(null!=customerChargeType.getUuid()){//修改--->直接删除在添加
                CustomerChargeRuleCalculation customerChargeRuleCalculation = new CustomerChargeRuleCalculation();
                customerChargeRuleCalculation.setCustomerChargeTypeUuid(customerChargeType.getUuid().intValue());
                Wrapper wrapperCalculation = new EntityWrapper(customerChargeRuleCalculation);
                CustomerChargeRuleInterval customerChargeRuleInterval  = new CustomerChargeRuleInterval();
                customerChargeRuleInterval.setCustomerChargeTypeUuid(customerChargeType.getUuid().intValue());
                Wrapper wrapperInterval = new EntityWrapper(customerChargeRuleInterval);
                wrapperCalculation.or("1=1");
                wrapperInterval.or("1=1");
                customerChargeRuleCalculationService.delete(wrapperCalculation);
                customerChargeRuleIntervalService.delete(wrapperInterval);
                customerChargeRuleTypeDao.deleteById(customerChargeType.getUuid());
            }
            addCustomerChargeRule(customerChargeType);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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
        CustomerChargeType customerChargeType = customerChargeRuleTypeDao.selectByCustomerUuid(customerUuid);
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