package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.CustomerChargeRuleCalculation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerChargeRuleCalculationDao extends BaseMapper<CustomerChargeRuleCalculation> {

    List<CustomerChargeRuleCalculation> selectBycustomerChargeRuleTypeUuid(@Param("uuid") Long uuid );
}
