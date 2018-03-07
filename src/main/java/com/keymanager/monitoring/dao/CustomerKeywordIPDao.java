package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerKeywordIP;

public interface CustomerKeywordIPDao extends BaseMapper<CustomerKeywordIP> {
    void deleteCustomerKeywordIPFromAMonthAgo();
}
