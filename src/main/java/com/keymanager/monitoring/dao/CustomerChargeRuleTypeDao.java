package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerChargeType;
import org.apache.ibatis.annotations.Param;

public interface CustomerChargeRuleTypeDao extends BaseMapper<CustomerChargeType> {

    int selectLastId();

    CustomerChargeType selectByCustomerUuid(@Param("uuid") Long uuid);

}
