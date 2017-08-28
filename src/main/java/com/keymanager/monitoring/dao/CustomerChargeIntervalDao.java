package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerChargeTypeInterval;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerChargeIntervalDao extends BaseMapper<CustomerChargeTypeInterval> {

    List<CustomerChargeTypeInterval> selectBycustomerChargeTypeUuid(@Param("uuid") Long uuid);
}
