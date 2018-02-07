package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.entity.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomerDao extends BaseMapper<Customer> {
    List<Customer> searchCustomers(Page<Customer> page, @Param("customerCriteria")CustomerCriteria customerCriteria);

    List<Customer> getActiveCustomerSimpleInfo();

    int selectLastId();

    List<Customer> searchCustomerWithKeyword(@Param("groupNames") List<String> groupNames,@Param("terminalType") String terminalType);

    List<Customer> findNegativeCustomer();

    List<Map> searchCustomerTypes(@Param("customerCriteria")CustomerCriteria customerCriteria);

    void setCustomerKeywordStatusSwitchTime(@Param("uuids")List<String> uuids, @Param("activeHour")Integer activeHour, @Param("inActiveHour")Integer inActiveHour);

    List<Customer> searchNeedSwitchCustomer(@Param("hour")Integer hour);
}
