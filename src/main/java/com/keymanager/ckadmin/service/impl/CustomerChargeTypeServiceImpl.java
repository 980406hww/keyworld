package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.CustomerChargeTypeDao;
import com.keymanager.ckadmin.entity.CustomerChargeType;
import com.keymanager.ckadmin.entity.CustomerChargeTypeCalculation;
import com.keymanager.ckadmin.entity.CustomerChargeTypeInterval;
import com.keymanager.ckadmin.entity.CustomerChargeTypePercentage;
import com.keymanager.ckadmin.enums.ChargeTypeEnum;
import com.keymanager.ckadmin.service.CustomerChargeTypeCalculationService;
import com.keymanager.ckadmin.service.CustomerChargeTypeIntervalService;
import com.keymanager.ckadmin.service.CustomerChargeTypePercentageService;
import com.keymanager.ckadmin.service.CustomerChargeTypeService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("customerChargeTypeService2")
public class CustomerChargeTypeServiceImpl extends ServiceImpl<CustomerChargeTypeDao, CustomerChargeType> implements CustomerChargeTypeService {

    @Resource(name = "customerChargeTypeDao2")
    private CustomerChargeTypeDao customerChargeTypeDao;

    @Resource(name = "customerChargeTypeCalculationService2")
    private CustomerChargeTypeCalculationService customerChargeTypeCalculationService;

    @Resource(name = "customerChargeTypeIntervalService2")
    private CustomerChargeTypeIntervalService customerChargeTypeIntervalService;

    @Resource(name = "customerChargeTypePercentageService2")
    private CustomerChargeTypePercentageService customerChargeTypePercentageService;

    @Override
    public void saveCustomerChargeType(CustomerChargeType customerChargeType) {
        if (null != customerChargeType.getUuid()) {
            customerChargeTypeCalculationService.deleteByCustomerChargeTypeUuid(customerChargeType.getUuid());
            customerChargeTypeIntervalService.deleteByCustomerChargeTypeUuid(customerChargeType.getUuid());
            customerChargeTypePercentageService.deleteByCustomerChargeTypeUuid(customerChargeType.getUuid());
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

    @Override
    public CustomerChargeType getCustomerChargeType(Long customerUuid) {
        CustomerChargeType customerChargeType = customerChargeTypeDao.getCustomerChargeType(customerUuid);
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

    private void addCustomerChargeType(CustomerChargeType customerChargeType) {
        customerChargeTypeDao.insert(customerChargeType);
        addCustomerChargeRules(customerChargeType);
    }

    private void addCustomerChargeRules(CustomerChargeType customerChargeType) {
        if (ChargeTypeEnum.Percentage.name().equals(customerChargeType.getChargeType())) {
            List<CustomerChargeTypeCalculation> customerChargeTypeCalculations = customerChargeType.getCustomerChargeTypeCalculations();
            for (CustomerChargeTypeCalculation customerChargeTypeCalculation : customerChargeTypeCalculations) {
                customerChargeTypeCalculation.setCustomerChargeTypeUuid(customerChargeType.getUuid().longValue());
                customerChargeTypeCalculationService.insert(customerChargeTypeCalculation);
            }
        } else {
            List<CustomerChargeTypeInterval> customerChargeTypeIntervals = customerChargeType.getCustomerChargeTypeIntervals();
            for (CustomerChargeTypeInterval customerChargeTypeInterval : customerChargeTypeIntervals) {
                customerChargeTypeInterval.setCustomerChargeTypeUuid(customerChargeType.getUuid().longValue());
                customerChargeTypeIntervalService.insert(customerChargeTypeInterval);
            }
            for (CustomerChargeTypePercentage customerChargeTypePercentage : customerChargeType.getCustomerChargeTypePercentages()) {
                customerChargeTypePercentage.setCustomerChargeTypeUuid(customerChargeType.getUuid().longValue());
                customerChargeTypePercentageService.insert(customerChargeTypePercentage);
            }
        }
    }
}
