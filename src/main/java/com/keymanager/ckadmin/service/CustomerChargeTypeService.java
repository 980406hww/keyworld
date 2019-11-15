package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.CustomerChargeType;

public interface CustomerChargeTypeService extends IService<CustomerChargeType> {

    void saveCustomerChargeType(CustomerChargeType customerChargeType);

    CustomerChargeType getCustomerChargeType(Long customerUuid);
}
