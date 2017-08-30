package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerChargeTypeCalculation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerChargeTypeCalculationDao extends BaseMapper<CustomerChargeTypeCalculation> {

    List<CustomerChargeTypeCalculation> searchCustomerChargeTypeCalculations(@Param("uuid") Long uuid );
}
