package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerChargeTypePercentage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerChargeTypePercentageDao extends BaseMapper<CustomerChargeTypePercentage> {

    List<CustomerChargeTypePercentage> searchCustomerChargeTypePercentages(@Param("customerChargeTypeUuid") Long customerChargeTypeUuid);

    void deleteByCustomerChargeTypeUuid(@Param("customerChargeTypeUuid") Long customerChargeTypeUuid);
}
