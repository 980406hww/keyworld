package com.keymanager.ckadmin.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.dao.CustomerDao2;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.service.CustomerInterface;
import com.keymanager.ckadmin.service.CustomerKeywordInterface;

import java.math.BigDecimal;
import java.util.*;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
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

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordInterface customerKeywordService2;

   /* @Override
    public Page<AlgorithmTestPlan> searchAlgorithmTestPlans(Page<AlgorithmTestPlan> page, AlgorithmTestCriteria algorithmTestCriteria) {
        List<AlgorithmTestPlan> algorithmTestPlanList = algorithmTestPlanDao2.searchAlgorithmTestPlans(page,algorithmTestCriteria);
        page.setRecords(algorithmTestPlanList);
        return page;
    }*/

    @Override
    public Page<Customer> searchCustomers(Page<Customer> page, CustomerCriteria customerCriteria) {
        List<Customer> customerList = customerDao2.searchCustomers(page, customerCriteria);
        if (CollectionUtils.isNotEmpty(customerList)) {
            List<Long> customerUuids = new ArrayList<Long>();
            for (Customer customer : customerList) {
                customerUuids.add(customer.getUuid());
            }
            List<Map> customerKeywordCountMap = customerKeywordService2.getCustomerKeywordsCount(customerUuids, customerCriteria.getTerminalType(),customerCriteria.getEntryType());
            Map<Integer, Map> customerUuidKeywordCountMap = new HashMap<Integer, Map>();
            for (Map map : customerKeywordCountMap) {
                customerUuidKeywordCountMap.put((Integer) map.get("customerUuid"), map);
            }
            for (Customer customer : customerList) {
                if (!customer.getLoginName().equals(customerCriteria.getLoginName())) {
                    customer.setSaleRemark(null);
                    customer.setTelphone(null);
                    customer.setQq(null);
                    customer.setEmail(null);
                }
                Map map = customerUuidKeywordCountMap.get(customer.getUuid().intValue());
                if (map != null) {
                    Long totalCount = (Long) map.get("totalCount");
                    if (totalCount != null) {
                        customer.setKeywordCount(totalCount.intValue());
                    }

                    BigDecimal activeCount = (BigDecimal) map.get("activeCount");
                    if (activeCount != null) {
                        customer.setActiveKeywordCount(activeCount.intValue());
                    }
                }
            }
        }
        page.setRecords(customerList);
        return page;
    }

    @Override
    public void saveCustomer(Customer customer, String loginName) {
        //修改
        if (null != customer.getUuid()) {
            updateCustomer(customer, loginName);
        } else {//添加
            customer.setUpdateTime(new Date());
            customer.setLoginName(loginName);
            customerDao2.insert(customer);
        }
    }

    @Override
    public void deleteCustomer(long uuid) {
        customerDao2.deleteById(uuid);
        customerKeywordService2.deleteCustomerKeywords(uuid);
    }

    private void updateCustomer(Customer customer, String loginName) {
        Customer oldCustomer = customerDao2.selectById(customer.getUuid());
        if (oldCustomer != null) {
            if (oldCustomer.getLoginName().equals(loginName)) {
                oldCustomer.setQq(customer.getQq());
                oldCustomer.setEmail(customer.getEmail());
                oldCustomer.setTelphone(customer.getTelphone());
                oldCustomer.setSaleRemark(customer.getSaleRemark());
            }
            oldCustomer.setContactPerson(customer.getContactPerson());
            oldCustomer.setAlipay(customer.getAlipay());
            oldCustomer.setPaidFee(customer.getPaidFee());
            oldCustomer.setRemark(customer.getRemark());
            oldCustomer.setType(customer.getType());
            oldCustomer.setStatus(customer.getStatus());
            oldCustomer.setDailyReportIdentify(customer.getDailyReportIdentify());
            oldCustomer.setLoginName(customer.getLoginName());
            oldCustomer.setEntryType(customer.getEntryType());
            oldCustomer.setUpdateTime(new Date());
            customerDao2.updateById(oldCustomer);
        }
    }
}
