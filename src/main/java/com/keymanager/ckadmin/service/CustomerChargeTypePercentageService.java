package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.CustomerChargeTypePercentage;
import java.util.List;

public interface CustomerChargeTypePercentageService extends IService<CustomerChargeTypePercentage> {

    void deleteByCustomerChargeTypeUuid(Long customerChargeTypeUuid);

    List<CustomerChargeTypePercentage> searchCustomerChargeTypePercentages(Long customerChargeTypeUuid);
}
