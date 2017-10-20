package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerKeywordInvalidCountLog;

import java.util.List;

public interface CustomerKeywordInvalidCountLogDao extends BaseMapper<CustomerKeywordInvalidCountLog> {

    List<Long> findInvalidCustomerKeyword();

    void addCustomerKeywordInvalidCountLog();
}
