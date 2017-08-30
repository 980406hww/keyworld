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
    private CustomerChargeTypeCalculationService customerChargeTypeCalculationService;

    @Autowired
    private CustomerChargeTypeIntervalService customerChargeTypeIntervalService;

    public void saveCustomerChargeType(CustomerChargeType customerChargeType) throws Exception {
        if (null != customerChargeType.getUuid()) {
            CustomerChargeTypeCalculation customerChargeTypeCalculation = new CustomerChargeTypeCalculation();
            customerChargeTypeCalculation.setCustomerChargeTypeUuid(customerChargeType.getUuid().intValue());
            Wrapper wrapperCalculation = new EntityWrapper(customerChargeTypeCalculation);
            CustomerChargeTypeInterval customerChargeTypeInterval = new CustomerChargeTypeInterval();
            customerChargeTypeInterval.setCustomerChargeTypeUuid(customerChargeType.getUuid().intValue());
            Wrapper wrapperInterval = new EntityWrapper(customerChargeTypeInterval);
            customerChargeTypeCalculationService.delete(wrapperCalculation);
            customerChargeTypeIntervalService.delete(wrapperInterval);
            addCustomerChargeRules(customerChargeType);
            CustomerChargeType oldCustomerChargeType = customerChargeTypeDao.selectById(customerChargeType.getUuid());
            oldCustomerChargeType.setCustomerUuid(customerChargeType.getCustomerUuid());
            oldCustomerChargeType.setChargeType(customerChargeType.getChargeType());
            customerChargeType.setUpdateTime(new Date());
            customerChargeTypeDao.updateById(oldCustomerChargeType);
        } else {
            addCustomerChargeType(customerChargeType);
        }
    }

    private void addCustomerChargeType(CustomerChargeType customerChargeType) {
        customerChargeTypeDao.insert(customerChargeType);
        addCustomerChargeRules(customerChargeType);
    }

    private void addCustomerChargeRules(CustomerChargeType customerChargeType) {
        if (ChargeTypeEnum.Percentage.name().equals(customerChargeType.getChargeType())) {
            List<CustomerChargeTypeCalculation> customerChargeTypeCalculations = customerChargeType.getCustomerChargeTypeCalculations();
            for (CustomerChargeTypeCalculation customerChargeTypeCalculation : customerChargeTypeCalculations) {
                customerChargeTypeCalculation.setCustomerChargeTypeUuid(customerChargeType.getUuid().intValue());
                customerChargeTypeCalculationService.insert(customerChargeTypeCalculation);
            }
        } else {
            List<CustomerChargeTypeInterval> customerChargeTypeIntervals = customerChargeType.getCustomerChargeTypeIntervals();
            for (CustomerChargeTypeInterval customerChargeTypeInterval : customerChargeTypeIntervals) {
                customerChargeTypeInterval.setCustomerChargeTypeUuid(customerChargeType.getUuid().intValue());
                customerChargeTypeIntervalService.insert(customerChargeTypeInterval);
            }
        }
    }

    public CustomerChargeType getCustomerChargeType(Long customerUuid) {
        CustomerChargeType customerChargeType = customerChargeTypeDao.getCustomerChargeType(customerUuid.intValue());
        if (null != customerChargeType) {
            List<CustomerChargeTypeCalculation> customerChargeTypeCalculations = customerChargeTypeCalculationService.searchCustomerChargeTypeCalculations(customerChargeType.getUuid());
            if (customerChargeTypeCalculations.size() > 0) {
                customerChargeType.setCustomerChargeTypeCalculations(customerChargeTypeCalculations);
            }
            List<CustomerChargeTypeInterval> customerChargeTypeIntervals = customerChargeTypeIntervalService.searchCustomerChargeTypeIntervals(customerChargeType.getUuid());
            if (customerChargeTypeIntervals.size() > 0) {
                customerChargeType.setCustomerChargeTypeIntervals(customerChargeTypeIntervals);
            }
        }
        return customerChargeType;
    }
}
