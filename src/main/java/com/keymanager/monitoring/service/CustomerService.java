package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.dao.CustomerDao;
import com.keymanager.monitoring.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerService extends ServiceImpl<CustomerDao, Customer> {
	private static Logger logger = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private CustomerKeywordService customerKeywordService;

	public void updateStatus(long uuid, int status){
		Customer customer = customerDao.selectById(uuid);
		if(customer != null){
			customer.setStatus(status);
			customer.setUpdateTime(new Date());
			customerDao.updateById(customer);
		}
	}

	public Customer getCustomerWithKeywordCount(long customerUuid){
		Customer customer = customerDao.selectById(customerUuid);
		if(customer != null){
			customer.setKeywordCount(customerKeywordService.getCustomerKeywordCount(customerUuid));
		}
		return customer;
	}

	public Page<Customer> searchCustomers(Page<Customer> page, CustomerCriteria customerCriteria){
		page.setRecords(customerDao.searchCustomers(page, customerCriteria));
		return page;
	}

	public void updateCustomer(Customer customer) {
		Customer oldCustomer = customerDao.selectById(customer.getUuid());
		if (oldCustomer != null) {
			oldCustomer.setContactPerson(customer.getContactPerson());
			oldCustomer.setQq(customer.getQq());
			oldCustomer.setEmail(customer.getEmail());
			oldCustomer.setTelphone(customer.getTelphone());
			oldCustomer.setAlipay(customer.getAlipay());
			oldCustomer.setPaidFee(customer.getPaidFee());
			oldCustomer.setRemark(customer.getRemark());
			oldCustomer.setType(customer.getType());
			oldCustomer.setStatus(customer.getStatus());
			oldCustomer.setUserID(customer.getUserID());
			oldCustomer.setEntryType(customer.getEntryType());
			oldCustomer.setUpdateTime(new Date());
			customerDao.updateById(oldCustomer);
		}
	}

	public void saveCustomer(Customer customer) {
		//修改
		if (null != customer.getUuid()) {
			updateCustomer(customer);
		} else {//添加
			customer.setUpdateTime(new Date());
			customerDao.insert(customer);
		}
	}

	public void deleteCustomer(long uuid) {
		customerDao.deleteById(uuid);
		customerKeywordService.deleteCustomerKeywords(uuid);
	}

	public void deleteAll(List<String> uuids) {
		for (String uuid : uuids) {
			deleteCustomer(Long.valueOf(uuid));
		}
	}

	public List<Customer> getActiveCustomerSimpleInfo(){
		return customerDao.getActiveCustomerSimpleInfo();
	}
}
