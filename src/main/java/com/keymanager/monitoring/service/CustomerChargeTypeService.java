package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerChargeTypeDao;
import com.keymanager.monitoring.entity.CustomerChargeType;
import com.keymanager.monitoring.entity.CustomerChargeTypeCalculation;
import com.keymanager.monitoring.entity.CustomerChargeTypeInterval;
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

    public void saveCustomerChargeType(CustomerChargeType customerChargeType) throws Exception {
        if (null != customerChargeType.getUuid()) {
            customerChargeTypeCalculationService.deleteByCustomerChargeTypeUuid(customerChargeType.getUuid().longValue());
            customerChargeTypeIntervalService.deleteByCustomerChargeTypeUuid(customerChargeType.getUuid().longValue());
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
