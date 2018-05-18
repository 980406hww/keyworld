package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.enums.CustomerKeywordStatus;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.dao.CustomerDao;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.enums.EntryTypeEnum;
import com.keymanager.util.Utils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
			Map<Integer, Map> customerUuidKeywordCountMap = new HashMap<Integer, Map>();
			for(Map map : customerKeywordCountMap){
				customerUuidKeywordCountMap.put((Integer)map.get("customerUuid"), map);
			}
			for(Customer customer : customers){
				Map map = customerUuidKeywordCountMap.get(customer.getUuid().intValue());
				if(map != null) {
					Long totalCount = (Long)map.get("totalCount");
					if(totalCount != null) {
						customer.setKeywordCount(totalCount.intValue());
					}

					BigDecimal activeCount = (BigDecimal)map.get("activeCount");
					if(activeCount != null) {
						customer.setActiveKeywordCount(activeCount.intValue());
					}
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

	public List<Map> searchCustomerTypes(CustomerCriteria customerCriteria) {
		return customerDao.searchCustomerTypes(customerCriteria);
	}

	public void updateCustomerType(Long customerUuid, String customerType) {
		Customer customer = customerDao.selectById(customerUuid);
		customer.setType(customerType);
		updateById(customer);
	}

	public void updateCustomerUserName(Long customerUuid, String userName) {
		Customer customer = customerDao.selectById(customerUuid);
		customer.setUserName(userName);
		updateById(customer);
	}

	public void setCustomerKeywordStatusSwitchTime(List<String> uuids, String activeHour, String inActiveHour) {
		customerDao.setCustomerKeywordStatusSwitchTime(uuids, activeHour, inActiveHour);
	}

	public void autoSwitchCustomerKeywordStatus() {
		boolean openFlag = false;
		Integer hour = Utils.getCurrentHour();
		List<Long> activeCustomerUuids = new ArrayList<Long>();
		List<Long> inActiveCustomerUuids = new ArrayList<Long>();
		List<Customer> customers = customerDao.searchNeedSwitchCustomer();
		for(Customer customer : customers) {
			openFlag = false;
			String[] activeHours = customer.getActiveHour().split(",");
			String[] inActiveHours = customer.getInActiveHour().split(",");
			for(int i = 0; i < activeHours.length; i++) {
				if(Integer.parseInt(activeHours[i]) < Integer.parseInt(inActiveHours[i])) {
					if(hour >= Integer.parseInt(activeHours[i]) && hour < Integer.parseInt(inActiveHours[i])) {
						activeCustomerUuids.add(customer.getUuid());
						openFlag = true;
						break;
					}
				} else {
					if(hour >= Integer.parseInt(activeHours[i]) || hour < Integer.parseInt(inActiveHours[i])) {
						activeCustomerUuids.add(customer.getUuid());
						openFlag = true;
						break;
					}
				}
			}
			if(!openFlag) {
				inActiveCustomerUuids.add(customer.getUuid());
			}
		}
		if(CollectionUtils.isNotEmpty(activeCustomerUuids)) {
			customerKeywordService.batchChangeCustomerKeywordStatus(EntryTypeEnum.fm.name(), activeCustomerUuids, CustomerKeywordStatus.Active.getCode());
		}
		if(CollectionUtils.isNotEmpty(inActiveCustomerUuids)) {
			customerKeywordService.batchChangeCustomerKeywordStatus(EntryTypeEnum.fm.name(), inActiveCustomerUuids, CustomerKeywordStatus.Inactive.getCode());
		}
	}

	public List<String> searchContactPersonList(String customerUuids) {
		String[] uuids = customerUuids.split(",");
		return customerDao.searchContactPersonList(uuids);
	}

	public Customer findCustomerByUserName(String userName) {
		return customerDao.findCustomerByUserName(userName);
	}
}
