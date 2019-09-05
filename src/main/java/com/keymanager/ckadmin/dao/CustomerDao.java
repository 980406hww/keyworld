package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.entity.Customer;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 算法测试任务表 Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
@Repository("customerDao2")
public interface CustomerDao extends BaseMapper<Customer> {

    List<Customer> searchCustomers(Page<Customer> page,
        @Param("customerCriteria") CustomerCriteria customerCriteria);

    void updateCustomerDailyReportIdentify(@Param("uuids") List<Integer> uuids);
}
