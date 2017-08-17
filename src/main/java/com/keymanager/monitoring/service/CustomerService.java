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
		return null;
	}
}
