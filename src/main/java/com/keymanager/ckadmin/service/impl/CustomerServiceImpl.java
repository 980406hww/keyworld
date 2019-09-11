package com.keymanager.ckadmin.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.dao.CustomerDao;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.CustomerKeywordService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class CustomerServiceImpl extends ServiceImpl<CustomerDao, Customer> implements
    CustomerService {

    @Resource(name = "customerDao2")
    private CustomerDao customerDao;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

/*    @Override
    public Page<AlgorithmTestPlan> searchAlgorithmTestPlans(Page<AlgorithmTestPlan> page, AlgorithmTestCriteria algorithmTestCriteria) {
        List<AlgorithmTestPlan> algorithmTestPlanList = algorithmTestPlanDao.searchAlgorithmTestPlans(page,algorithmTestCriteria);
        page.setRecords(algorithmTestPlanList);
        return page;
    }*/

    @Override
    public Page<Customer> searchCustomers(Page<Customer> page, CustomerCriteria customerCriteria) {
        List<Customer> customerList = customerDao.searchCustomers(page, customerCriteria);
        if (CollectionUtils.isNotEmpty(customerList)) {
            List<Long> customerUuids = new ArrayList<Long>();
            for (Customer customer : customerList) {
                customerUuids.add(customer.getUuid());
            }
            List<Map> customerKeywordCountMap = customerKeywordService
                .getCustomerKeywordsCount(customerUuids, customerCriteria.getTerminalType(),
                    customerCriteria.getEntryType());
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
            customerDao.insert(customer);
        }
    }

    @Override
    public void deleteCustomer(long uuid) {
        customerDao.deleteById(uuid);
        customerKeywordService.deleteCustomerKeywords(uuid);
    }

    private void updateCustomer(Customer customer, String loginName) {
        Customer oldCustomer = customerDao.selectById(customer.getUuid());
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
//            oldCustomer.setType(customer.getType());
            oldCustomer.setStatus(customer.getStatus());
            oldCustomer.setDailyReportIdentify(customer.getDailyReportIdentify());
            oldCustomer.setLoginName(customer.getLoginName());
//            oldCustomer.setEntryType(customer.getEntryType());
            oldCustomer.setUpdateTime(new Date());
            customerDao.updateById(oldCustomer);
        }
    }

    @Override
    public void deleteAll(List<Integer> uuids) {
        for (Integer uuid : uuids) {
            deleteCustomer(Long.valueOf(uuid));
        }
    }

    @Override
    public void updateCustomerDailyReportIdentify(List<Integer> uuids) {
        customerDao.updateCustomerDailyReportIdentify(uuids);
    }

    @Override
    public void changeCustomerDailyReportIdentify(long uuid, boolean identify) {
        Customer customer = customerDao.selectById(uuid);
        customer.setDailyReportIdentify(identify);
        customer.setUpdateTime(new Date());
        customerDao.updateById(customer);
    }

    @Override
    public Customer getCustomerWithKeywordCount(String terminalType, String entryType,
        long customerUuid, String loginName) {
        Customer customer = customerDao.selectById(customerUuid);
        if (customer != null) {
            if (!customer.getLoginName().equals(loginName)) {
                customer.setEmail(null);
                customer.setQq(null);
                customer.setTelphone(null);
                customer.setSaleRemark(null);
            }
            customer.setKeywordCount(customerKeywordService
                .getCustomerKeywordCount(terminalType, entryType, customerUuid));
        }
        return customer;
    }

    @Override
    public List<Customer> getActiveCustomerSimpleInfo(CustomerCriteria customerCriteria) {
        return customerDao.getActiveCustomerSimpleInfo(customerCriteria);
    }

    @Override
    public Customer getCustomer(int customerUuid) {
        return customerDao.selectById(customerUuid);
    }
}
