package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.entity.Customer;

/**
 * <p>
 * 算法测试任务表 服务实现接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
public interface CustomerInterface extends IService<Customer> {


    Page<Customer> searchCustomers(Page<Customer> page,
                                   CustomerCriteria customerCriteria);

    void saveCustomer(Customer customer, String loginName);

    void deleteCustomer(long uuid);
}
