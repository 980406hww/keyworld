package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.dao.CustomerDao;
import com.keymanager.monitoring.entity.Customer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

	public Customer getCustomerWithKeywordCount(String terminalType, String entryType, long customerUuid){
		Customer customer = customerDao.selectById(customerUuid);
		if(customer != null){
			customer.setKeywordCount(customerKeywordService.getCustomerKeywordCount(terminalType, entryType, customerUuid));
		}
		return customer;
	}

	public Page<Customer> searchCustomers(Page<Customer> page, CustomerCriteria customerCriteria){
		List<Customer> customers = customerDao.searchCustomers(page, customerCriteria);
		if(CollectionUtils.isNotEmpty(customers)){
			List<Long> customerUuids = new ArrayList<Long>();
			for(Customer customer : customers){
				customerUuids.add(customer.getUuid());
			}
			List<Map> customerKeywordCountMap = customerKeywordService.getCustomerKeywordsCount(customerUuids, customerCriteria.getTerminalType(),
					customerCriteria.getEntryType());
			Map<Integer, Long> customerUuidKeywordCountMap = new HashMap<Integer, Long>();
			for(Map map : customerKeywordCountMap){
				customerUuidKeywordCountMap.put((Integer)map.get("fCustomerUuid"), (Long)map.get("subCount"));
			}
			for(Customer customer : customers){
				Long count = customerUuidKeywordCountMap.get(customer.getUuid().intValue());
				if(count != null) {
					customer.setKeywordCount(count.intValue());
				}
			}
		}
		page.setRecords(customers);
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
			oldCustomer.setLoginName(customer.getLoginName());
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

    public Customer getCustomer(int customerUuid) {
		return customerDao.selectById(customerUuid);
    }

	public List<Customer> searchCustomersWithKeyword(List<String> groupNames,String terminalType)
	{
		return customerDao.searchCustomerWithKeyword(groupNames,terminalType);
	}

	public List<Customer> findNegativeCustomer() {
		return customerDao.findNegativeCustomer();
	}
}
