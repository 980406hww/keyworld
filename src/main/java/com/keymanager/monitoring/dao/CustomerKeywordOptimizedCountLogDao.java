package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerKeywordOptimizedCountLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerKeywordOptimizedCountLogDao extends BaseMapper<CustomerKeywordOptimizedCountLog> {

    List<CustomerKeywordOptimizedCountLog> groupCustomerKeywordOptimizedCountLogs();

    CustomerKeywordOptimizedCountLog findCurrentCountLog(@Param("customerKeywordUuid") Long customerKeywordUuid);

    CustomerKeywordOptimizedCountLog findThreeDaysAgoCountLog(@Param("customerKeywordUuid") Long customerKeywordUuid);
}
