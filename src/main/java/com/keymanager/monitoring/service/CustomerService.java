package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.enums.CustomerKeywordStatus;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.dao.CustomerDao;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.enums.EntryTypeEnum;
import com.keymanager.monitoring.vo.customerSourceVO;
import com.keymanager.util.Utils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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

	public Customer getCustomerWithKeywordCount(String terminalType, String entryType, long customerUuid, String loginName){
		Customer customer = customerDao.selectById(customerUuid);
		if(customer != null){
			if(!customer.getLoginName().equals(loginName)){
				customer.setEmail(null);
				customer.setQq(null);
				customer.setTelphone(null);
				customer.setSaleRemark(null);
			}
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
			List<Map> customerKeywordCountMap = customerKeywordService.getCustomerKeywordsCount(customerUuids, customerCriteria.getTerminalType(),customerCriteria.getEntryType());
			Map<Integer, Map> customerUuidKeywordCountMap = new HashMap<Integer, Map>();
			for(Map map : customerKeywordCountMap){
				customerUuidKeywordCountMap.put((Integer)map.get("customerUuid"), map);
			}
			for(Customer customer : customers){
				if(!customer.getLoginName().equals(customerCriteria.getLoginName())){
					customer.setSaleRemark(null);
					customer.setTelphone(null);
					customer.setQq(null);
					customer.setEmail(null);
				}
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

	public void updateCustomer(Customer customer, String loginName) {
		Customer oldCustomer = customerDao.selectById(customer.getUuid());
		if (oldCustomer != null) {
			if(oldCustomer.getLoginName().equals(loginName)) {
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
			customerDao.updateById(oldCustomer);
		}
	}

	public void saveCustomer(Customer customer, String loginName) {
		//修改
		if (null != customer.getUuid()) {
			updateCustomer(customer, loginName);
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

	public List<Customer> getActiveCustomerSimpleInfo(CustomerCriteria customerCriteria){
		return customerDao.getActiveCustomerSimpleInfo(customerCriteria);
	}

    public Customer getCustomer(int customerUuid) {
		return customerDao.selectById(customerUuid);
    }

	public List<Customer> searchCustomersWithKeyword(List<String> groupNames,String terminalType)
	{
		return customerDao.searchCustomerWithKeyword(groupNames,terminalType);
	}

	public List<Map> searchCustomerTypes(CustomerCriteria customerCriteria) {
		return customerDao.searchCustomerTypes(customerCriteria);
	}

	public void updateCustomerType(Long customerUuid, String customerType) {
		Customer customer = customerDao.selectById(customerUuid);
		customer.setType(customerType);
		updateById(customer);
	}

	public void updateCustomerExternalAccount(Long customerUuid, String externalAccount) {
		Customer customer = customerDao.selectById(customerUuid);
		customer.setExternalAccount(externalAccount);
		updateById(customer);
	}

	public void updateCustomerSearchEngine(Long customerUuid, String searchEngine) {
		Customer customer = customerDao.selectById(customerUuid);
		customer.setSearchEngine(searchEngine);
		updateById(customer);
	}

	public void setCustomerKeywordStatusSwitchTime(List<String> uuids, String activeHour, String inActiveHour) {
		customerDao.setCustomerKeywordStatusSwitchTime(uuids, activeHour, inActiveHour);
	}

	public void autoSwitchCustomerKeywordStatus() {
		boolean openFlag = false;
		String day = "" + Utils.getDayOfMonth();
		Integer hour = Utils.getCurrentHour();
		List<Long> activeCustomerUuids = new ArrayList<Long>();
		List<Long> inActiveCustomerUuids = new ArrayList<Long>();
		List<Customer> customers = customerDao.searchNeedSwitchCustomer();
		for(Customer customer : customers) {
			openFlag = false;
			// 按日期启停
			if(StringUtils.isNotBlank(customer.getUpdateInterval()) && StringUtils.isBlank(customer.getActiveHour())) {
				String[] updateIntervals = customer.getUpdateInterval().split(",");
				openFlag = ArrayUtils.contains(updateIntervals, day);
				if(openFlag) {
					activeCustomerUuids.add(customer.getUuid());
				}
			}
			// 按小时启停
			if(StringUtils.isNotBlank(customer.getActiveHour())) {
				boolean existFlag = true;
				if(StringUtils.isNotBlank(customer.getUpdateInterval())) {
					String[] updateIntervals = customer.getUpdateInterval().split(",");
					existFlag = ArrayUtils.contains(updateIntervals, day);
				}
				if(existFlag) {
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

	public Customer findCustomerByExternalAccountInfo(String externalAccount, String searchEngine) {
		return customerDao.findCustomerByExternalAccountInfo(externalAccount, searchEngine);
	}

	public List<Customer> searchTargetCustomers(String entryType,String loginName){
		List<Customer> customers = customerDao.searchTargetCustomers(entryType,loginName);
		return customers;
	}

	public void setCustomerUpdateInterval(List<String> uuids, String updateInterval) {
		customerDao.setCustomerUpdateInterval(uuids, updateInterval);
	}

	public List<customerSourceVO> findCustomerKeywordSource () {
		return customerDao.findCustomerKeywordSource();
	}

	public void updateCustomerUserID(List<String> uuids, String userID) {
		customerDao.updateCustomerUserID(uuids, userID);
	}

	public void changeCustomerDailyReportIdentify (long uuid, boolean identify) {
        Customer customer = customerDao.selectById(uuid);
        customer.setDailyReportIdentify(identify);
        customer.setUpdateTime(new Date());
        customerDao.updateById(customer);
    }

    public List<Long> getActiveDailyReportIdentifyCustomerUuids () {
	    return customerDao.getActiveDailyReportIdentifyCustomerUuids();
    }

	public void updateCustomerDailyReportIdentify (String uuidstr) {
        String[] uuids = uuidstr.split(",");
        customerDao.updateCustomerDailyReportIdentify(uuids);
	}
}
