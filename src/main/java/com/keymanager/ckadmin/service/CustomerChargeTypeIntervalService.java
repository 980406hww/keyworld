package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.CustomerChargeTypeInterval;
import java.util.List;

public interface CustomerChargeTypeIntervalService extends IService<CustomerChargeTypeInterval> {

    void deleteByCustomerChargeTypeUuid(Long customerChargeTypeUuid);

    List<CustomerChargeTypeInterval> searchCustomerChargeTypeIntervals(Long customerChargeTypeUuid);
}
