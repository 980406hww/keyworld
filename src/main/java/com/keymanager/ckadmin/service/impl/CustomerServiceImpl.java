package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.criteria.CustomerTypeCriteria;
import com.keymanager.ckadmin.dao.CustomerDao;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.service.CustomerBusinessService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.vo.CustomerTypeVO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
@Service("customerService2")
public class CustomerServiceImpl extends ServiceImpl<CustomerDao, Customer> implements CustomerService {

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
            List<Long> customerUuids = new ArrayList<>();
            for (Customer customer : customerList) {
                if (!customer.getLoginName().equals(customerCriteria.getLoginName())) {
                    customer.setSaleRemark("");
                    customer.setTelphone("");
                    customer.setQq("");
                    customer.setEmail("");
                }
                customerUuids.add(customer.getUuid());
            }
            List<Map> customerCustomerBusinessMapList = customerBusinessService.getCustomerBusinessMapList(customerUuids);
            Map<Integer, Map> customerCustomerBusinessMap = new HashMap<>(customerUuids.size());
            for (Map map : customerCustomerBusinessMapList) {
                customerCustomerBusinessMap.put((Integer) map.get("customerUuid"), map);
            }
            for (Customer customer : customerList) {
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
            if (CollectionUtils.isNotEmpty(customer.getCustomerBusinessList())) {
                customerBusinessService.saveCustomerBusiness(customer);
            }
        } else {//添加
            customer.setUpdateTime(new Date());
            customer.setLoginName(loginName);
            customer.setStatus(1);
            customerDao.insert(customer);
            if (CollectionUtils.isNotEmpty(customer.getCustomerBusinessList())) {
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
                oldCustomer.setEmail(customer.getEmail());
                oldCustomer.setTelphone(customer.getTelphone());
                oldCustomer.setSaleRemark(customer.getSaleRemark());
            }
            oldCustomer.setContactPerson(customer.getContactPerson());
            oldCustomer.setAlipay(customer.getAlipay());
            oldCustomer.setPaidFee(customer.getPaidFee());
            oldCustomer.setRemark(customer.getRemark());
            oldCustomer.setType(customer.getType());
            oldCustomer.setDailyReportIdentify(customer.getDailyReportIdentify());
            oldCustomer.setLoginName(customer.getLoginName());
            oldCustomer.setExternalAccount(customer.getExternalAccount());
            oldCustomer.setSearchEngine(customer.getSearchEngine());
            oldCustomer.setUpdateTime(new Date());
            customerDao.updateById(oldCustomer);
        }
    }

    @Override
    public void deleteAll(List<String> uuids) {
        for (String uuid : uuids) {
            deleteCustomer(Long.parseLong(uuid));
        }
    }

    @Override
    public void updateCustomerDailyReportIdentify(List<Integer> uuids) {
        customerDao.updateCustomerDailyReportIdentify(uuids);
    }

    @Override
    public void changeCustomerStatus(long uuid, int status) {
        Customer customer = customerDao.selectById(uuid);
        customer.setStatus(status);
        customer.setUpdateTime(new Date());
        customerDao.updateById(customer);
    }

    @Override
    public List<Customer> getCustomerListByUser(String username, String type) {
        return customerDao.getCustomerListByUser(username, type);
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
            if (CollectionUtils.isNotEmpty(customerBusinessList)) {
                customer.setCustomerBusinessList(customerBusinessList);
            }

            if (!customer.getLoginName().equals(loginName)) {
                customer.setEmail(null);
                customer.setQq(null);
                customer.setTelphone(null);
                customer.setSaleRemark(null);
            }
            /*
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
    public Customer getCustomer(Long customerUuid) {
        return customerDao.selectById(customerUuid);
    }

    @Override
    public Customer getCustomerByCustomerUuid(String terminalType, String businessType, Long customerUuid) {
        Customer customer = customerDao.getCustomerByCustomerUuid(customerUuid);
//        customer.setKeywordCount(customerKeywordService.getCustomerKeywordCount(terminalType, businessType, customerUuid));
        return customer;
    }

    @Override
    public List<Long> getActiveDailyReportIdentifyCustomerUuids(String userID) {
        return customerDao.getActiveDailyReportIdentifyCustomerUuids(userID);
    }

    @Override
    public List<String> getActiveDailyReportIdentifyUserIDs() {
        return customerDao.getActiveDailyReportIdentifyUserIDs();
    }

    @Override
    public void changeSaleRemark(Long uuid, String saleRemark) {
        customerDao.changeSaleRemark(uuid, saleRemark);
    }

    @Override
    public void changeRemark(Long uuid, String remark) {
        customerDao.changeRemark(uuid, remark);
    }

    @Override
    public void changeExternalAccount(Long uuid, String externalAccount) {
        customerDao.changeExternalAccount(uuid, externalAccount);
    }

    @Override
    public void changeSearchEngine(Long uuid, String searchEngine) {
        customerDao.changeSearchEngine(uuid, searchEngine);
    }

    @Override
    public List<CustomerTypeVO> searchCustomerTypeCount(CustomerTypeCriteria customerTypeCriteria) {
        return customerDao.searchCustomerTypes(customerTypeCriteria);
    }

    @Override
    public List<Customer> searchCustomersWithKeyword(List<String> groupNames, String terminalType) {
        return customerDao.searchCustomerWithKeyword(groupNames, terminalType);
    }

    @Override
    public void updateCustomerUserID(List<Integer> uuids, String userID) {
        customerDao.updateCustomerUserID(uuids, userID);
    }

    @Override
    public List<Customer> searchTargetCustomers(String entryType, String accountName) {
        List<Customer> customers = customerDao.searchTargetCustomers(entryType, accountName);
        return customers;
    }

    @Override
    public Customer selectByName(String name) {
        return customerDao.selectByName(name);
    }
}
