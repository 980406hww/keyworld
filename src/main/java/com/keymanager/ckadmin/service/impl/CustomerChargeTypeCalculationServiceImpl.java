package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.CustomerChargeTypeCalculationDao;
import com.keymanager.ckadmin.entity.CustomerChargeTypeCalculation;
import com.keymanager.ckadmin.service.CustomerChargeTypeCalculationService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("customerChargeTypeCalculationService2")
public class CustomerChargeTypeCalculationServiceImpl extends ServiceImpl<CustomerChargeTypeCalculationDao, CustomerChargeTypeCalculation> implements
    CustomerChargeTypeCalculationService {

    @Resource(name = "customerChargeTypeCalculationDao2")
    private CustomerChargeTypeCalculationDao customerChargeTypeCalculationDao;

    @Override
    public void deleteByCustomerChargeTypeUuid(Long customerChargeTypeUuid) {
        customerChargeTypeCalculationDao.deleteByCustomerChargeTypeUuid(customerChargeTypeUuid);
    }

    @Override
    public List<CustomerChargeTypeCalculation> searchCustomerChargeTypeCalculations(Long customerChargeTypeUuid) {
        return customerChargeTypeCalculationDao.searchCustomerChargeTypeCalculations(customerChargeTypeUuid);
    }
}
