package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerChargeRuleCriteria;
import com.keymanager.monitoring.entity.CustomerChargeLog;
import com.keymanager.monitoring.vo.CustomerChargeStatVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerChargeLogDao extends BaseMapper<CustomerChargeLog> {

    List<CustomerChargeLog> findCustomerChargeLog(@Param("customerUuid") Integer customerUuid);

    void deleteCustomerChargeLogs(@Param("customerUuid")Integer customerUuid);

    CustomerChargeStatVO addUpCustomerChargeLogs(@Param("loginName")String loginName, @Param("beginDate")String beginDate, @Param("endDate")String endDate);
}
