package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerChargeRuleInterval;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerChargeRuleIntervalDao extends BaseMapper<CustomerChargeRuleInterval> {

    List<CustomerChargeRuleInterval> selectBycustomerChargeRuleTypeUuid(@Param("uuid") Long uuid);
}
