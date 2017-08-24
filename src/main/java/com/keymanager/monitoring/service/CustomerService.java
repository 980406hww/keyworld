package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.dao.CustomerDao;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerKeyword;
import org.apache.commons.collections.CollectionUtils;
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

	public Boolean updateCustomer(Customer customer) {
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
			return true;
		}
		return false;
	}

	public Boolean addCustomer(Customer customer){
		//修改
		if(null!=customer.getUuid()){
			Boolean updateflag = updateCustomer(customer);
			return	updateflag;
		}else{//添加
			customer.setUpdateTime(new Date());
			customerDao.insert(customer);
			return true;
		}
	}

	public boolean deleteCustomer(long uuid){
		try {
			customerDao.deleteById(uuid);
			customerKeywordService.deleteCustomerKeywords(uuid);
			return true;
		}catch (Exception e){
			return false;
		}
	}

	public boolean deleteAll( List<String> uuids){
		try {
		for(String uuid :uuids){
			deleteCustomer(Long.valueOf(uuid));
		}
			return true;
		}catch (Exception e){
			return false;
		}
	}

	public List<Customer> getActiveCustomerSimpleInfo(){
		return customerDao.getActiveCustomerSimpleInfo();
	}
}