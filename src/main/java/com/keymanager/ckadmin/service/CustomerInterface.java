package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.entity.Customer;
import java.util.List;

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

    void deleteCustomer(long uuid);

    void deleteAll(List<Integer> uuids);


    void updateCustomerDailyReportIdentify(List<Integer> uuids);

    void changeCustomerDailyReportIdentify(long uuid, boolean identify);
}
