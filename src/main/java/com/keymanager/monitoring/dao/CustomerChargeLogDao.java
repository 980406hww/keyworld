package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerChargeRuleCriteria;
import com.keymanager.monitoring.entity.CustomerChargeLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerChargeLogDao extends BaseMapper<CustomerChargeLog> {

    List<CustomerChargeLog> findCustomerChargeLog(@Param("customerUuid") Integer customerUuid);

    void deleteCustomerChargeLogs(@Param("customerUuid")Integer customerUuid);
}
