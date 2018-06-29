package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerChargeRuleCriteria;
import com.keymanager.monitoring.dao.CustomerChargeRuleDao;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerChargeRule;
import com.keymanager.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerChargeRuleService extends ServiceImpl<CustomerChargeRuleDao, CustomerChargeRule> {
	private static Logger logger = LoggerFactory.getLogger(CustomerChargeRuleService.class);

	@Autowired
	private CustomerChargeRuleDao customerChargeRuleDao;

	public Page<CustomerChargeRule> searchCustomerChargeRules(Page<CustomerChargeRule> page, CustomerChargeRuleCriteria customerChargeRuleCriteria) {
		page.setRecords(customerChargeRuleDao.searchCustomerChargeRules(page, customerChargeRuleCriteria));
		return page;
	}

	public void saveCustomerChargeRule(CustomerChargeRule customerChargeRule) {
		if(customerChargeRule.getUuid() == null) {
			CustomerChargeRule oldCustomerChargeRule = customerChargeRuleDao.findCustomerChargeRule(customerChargeRule.getCustomerUuid());
			if(oldCustomerChargeRule == null) {
				customerChargeRule.setCreateTime(new Date());
				customerChargeRuleDao.insert(customerChargeRule);
			}
		} else {
			customerChargeRuleDao.updateById(customerChargeRule);
		}
	}

	public void deleteCustomerChargeRule(Long uuid) {
		customerChargeRuleDao.deleteById(uuid);
	}

	public void deleteCustomerChargeRules(List<String> uuids) {
		for (String uuid : uuids) {
			customerChargeRuleDao.deleteById(Long.parseLong(uuid));
		}
	}

	public void getChargeRemindCustomer(CustomerChargeRuleCriteria customerChargeRuleCriteria) {
		int expiredChargeCount  = 0;
		int nowChargeCount = 0;
		int threeDayChargeCount = 0;
		int sevenDayChargeCount = 0;
		List<CustomerChargeRule> customerChargeRules = customerChargeRuleDao.getChargeRemindCustomer();
		for (CustomerChargeRule customerChargeRule : customerChargeRules) {
			int intervalDays = Utils.getIntervalDays(new Date(),customerChargeRule.getNextChargeDate());
			if(intervalDays < 0) {
				expiredChargeCount++;
			} else if(intervalDays == 0) {
				nowChargeCount++;
			} else if(intervalDays <= 3) {
				threeDayChargeCount++;
			} else if(intervalDays <= 7) {
				sevenDayChargeCount++;
			}
		}
		customerChargeRuleCriteria.setExpiredChargeCount(expiredChargeCount);
		customerChargeRuleCriteria.setNowChargeCount(nowChargeCount);
		customerChargeRuleCriteria.setThreeDayChargeCount(threeDayChargeCount);
		customerChargeRuleCriteria.setSevenChargeCount(sevenDayChargeCount);
	}

	public void updateNextChargeDate(List<String> customerUuids, String nextChargeDate) {
		customerChargeRuleDao.updateNextChargeDate(customerUuids, nextChargeDate);
	}
}
