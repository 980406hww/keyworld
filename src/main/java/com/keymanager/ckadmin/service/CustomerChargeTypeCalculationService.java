package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.CustomerChargeTypeCalculation;
import java.util.List;

public interface CustomerChargeTypeCalculationService extends IService<CustomerChargeTypeCalculation> {

    void deleteByCustomerChargeTypeUuid(Long customerChargeTypeUuid);

    List<CustomerChargeTypeCalculation> searchCustomerChargeTypeCalculations(Long customerChargeTypeUuid);
}
