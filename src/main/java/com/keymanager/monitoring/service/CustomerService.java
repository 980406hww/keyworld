package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.dao.CustomerDao;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerKeyword;
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

	public Customer getCustomerWithKeywordCount(long uuid){
		Customer customer = customerDao.selectById(uuid);
		if(customer != null){
			customer.setKeywordCount(customerKeywordService.getCustomerKeywordCount(uuid));
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

	public int addCustomer(Customer customer){
		customerDao.insert(customer);
		return customerDao.selectLastId();
	}

	public void deleteCustomer(long uuid){
		customerDao.deleteById(uuid);
		CustomerKeyword customerKeyword = new CustomerKeyword();
		customerKeyword.setCustomerUuid(uuid);
		Wrapper wrapper = new EntityWrapper(customerKeyword);
		customerKeywordService.delete(wrapper);
	}

	public List<Customer> getActiveCustomerSimpleInfo(){
		return customerDao.getActiveCustomerSimpleInfo();
	}
}
