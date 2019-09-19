package com.keymanager.ckadmin.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.dao.CustomerDao;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.service.CustomerBusinessService;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.CustomerKeywordService;

import java.math.BigDecimal;
import java.sql.Wrapper;
import java.util.*;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Resource(name = "customerBusinessService2")
    private CustomerBusinessService customerBusinessService;

    @Override
    public Page<Customer> searchCustomers(Page<Customer> page, CustomerCriteria customerCriteria) {
        List<Customer> customerList = customerDao.searchCustomers(page, customerCriteria);

        if (CollectionUtils.isNotEmpty(customerList)) {
            List<Long> customerUuids = new ArrayList<Long>();
            for (Customer customer : customerList) {
                customerUuids.add(customer.getUuid());
            }
            List<Map> customerCustomerBusinessMapList = customerBusinessService.getCustomerBusinessMapList(customerUuids);
            Map<Integer, Map> customerCustomerBusinessMap = new HashMap<>();
            for (Map map : customerCustomerBusinessMapList) {
                customerCustomerBusinessMap.put((Integer) map.get("customerUuid"), map);
            }
            for (Customer customer :customerList){
                Map map = customerCustomerBusinessMap.get(customer.getUuid().intValue());
                if (map != null) {
                    String customerBusinessStr = (String) map.get("customerBusinessStr");
                    if (customerBusinessStr != null) {
                        customer.setCustomerBusinessList(Arrays.asList(customerBusinessStr.split(",")));
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
            customerBusinessService.deleteByCustomerUuid(customer.getUuid());
            if (CollectionUtils.isNotEmpty(customer.getCustomerBusinessList())){
                customerBusinessService.saveCustomerBusiness(customer);
            }
        } else {//添加
            customer.setUpdateTime(new Date());
            customer.setLoginName(loginName);
            customer.setStatus(1);
            customerDao.insert(customer);
            if (CollectionUtils.isNotEmpty(customer.getCustomerBusinessList())){
                customerBusinessService.saveCustomerBusiness(customer);
            }
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
                oldCustomer.setWechat(customer.getWechat());
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
    public void deleteAll(List<String> uuids) {
        for (String uuid : uuids) {
            deleteCustomer(Long.valueOf(uuid));
        }
    }

    @Override
    public void updateCustomerDailyReportIdentify(List<Integer> uuids) {
        customerDao.updateCustomerDailyReportIdentify(uuids);
    }

    @Override
    public void changeCustomerDailyReportIdentify(long uuid, int identify) {
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
            List<String> customerBusinessList = customerBusinessService.getCustomerBusinessStrByCustomerUuid(customerUuid);
            if (CollectionUtils.isNotEmpty(customerBusinessList)){
                customer.setCustomerBusinessList(customerBusinessList);
            }
            /*if (!customer.getLoginName().equals(loginName)) {
                customer.setEmail(null);
                customer.setQq(null);
                customer.setTelphone(null);
                customer.setSaleRemark(null);
            }
            customer.setKeywordCount(customerKeywordService
                .getCustomerKeywordCount(terminalType, entryType, customerUuid));*/
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
