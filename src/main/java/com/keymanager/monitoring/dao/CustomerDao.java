package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.Customer;

public interface CustomerDao extends BaseMapper<Customer> {
    int selectLastId();
}
