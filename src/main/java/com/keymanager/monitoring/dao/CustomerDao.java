package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.entity.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerDao extends BaseMapper<Customer> {
    List<Customer> searchCustomers(Page<Customer> page, @Param("customerCriteria")CustomerCriteria customerCriteria);

    List<Customer> getActiveCustomerSimpleInfo();

    int selectLastId();

    List<Customer> searchCustomerWithKeyword(@Param("contactPerson") String contactPerson,@Param("terminalType") String terminalType);
}
