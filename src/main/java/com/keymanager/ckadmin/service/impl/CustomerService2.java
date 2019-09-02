package com.keymanager.ckadmin.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.dao.CustomerDao2;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.service.CustomerInterface;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 算法测试任务表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
@Service("customerService2")
public class CustomerService2 extends ServiceImpl<CustomerDao2, Customer> implements
        CustomerInterface {

    @Resource(name = "customerDao2")
    private CustomerDao2 customerDao2;

   /* @Override
    public Page<AlgorithmTestPlan> searchAlgorithmTestPlans(Page<AlgorithmTestPlan> page, AlgorithmTestCriteria algorithmTestCriteria) {
        List<AlgorithmTestPlan> algorithmTestPlanList = algorithmTestPlanDao2.searchAlgorithmTestPlans(page,algorithmTestCriteria);
        page.setRecords(algorithmTestPlanList);
        return page;
    }*/

    @Override
    public Page<Customer> searchCustomers(Page<Customer> page, CustomerCriteria customerCriteria) {
        List<Customer> customerList = customerDao2.searchCustomers(page,customerCriteria);
        page.setRecords(customerList);
        return page;
    }
}
