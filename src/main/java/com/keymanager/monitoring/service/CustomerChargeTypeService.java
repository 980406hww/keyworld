package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerChargeTypeDao;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.ChargeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerChargeTypeService extends ServiceImpl<CustomerChargeTypeDao, CustomerChargeType> {

    @Autowired
    private CustomerChargeTypeDao customerChargeTypeDao;

    @Autowired
    private CustomerChargeTypeCalculationService customerChargeTypeCalculationService;

    @Autowired
    private CustomerChargeTypeIntervalService customerChargeTypeIntervalService;

    @Autowired
    private CustomerChargeTypePercentageService customerChargeTypePercentageService;

    public void saveCustomerChargeType(CustomerChargeType customerChargeType) {
        if (null != customerChargeType.getUuid()) {
            customerChargeTypeCalculationService.deleteByCustomerChargeTypeUuid(customerChargeType.getUuid().longValue());
            customerChargeTypeIntervalService.deleteByCustomerChargeTypeUuid(customerChargeType.getUuid().longValue());
            customerChargeTypePercentageService.deleteByCustomerChargeTypeUuid(customerChargeType.getUuid().longValue());
            addCustomerChargeRules(customerChargeType);
            CustomerChargeType oldCustomerChargeType = customerChargeTypeDao.selectById(customerChargeType.getUuid());
            oldCustomerChargeType.setCustomerUuid(customerChargeType.getCustomerUuid());
            oldCustomerChargeType.setChargeType(customerChargeType.getChargeType());
            oldCustomerChargeType.setUpdateTime(new Date());
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
            for (CustomerChargeTypePercentage customerChargeTypePercentage : customerChargeType.getCustomerChargeTypePercentages()) {
                customerChargeTypePercentage.setCustomerChargeTypeUuid(customerChargeType.getUuid().intValue());
                customerChargeTypePercentageService.insert(customerChargeTypePercentage);
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
            List<CustomerChargeTypePercentage> customerChargeTypePercentages = customerChargeTypePercentageService.searchCustomerChargeTypePercentages(customerChargeType.getUuid());
            if (customerChargeTypePercentages.size() > 0) {
                customerChargeType.setCustomerChargeTypePercentages(customerChargeTypePercentages);
            }
        }
        return customerChargeType;
    }
}
