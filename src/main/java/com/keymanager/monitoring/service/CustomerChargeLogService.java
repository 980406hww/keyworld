package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerChargeLogDao;
import com.keymanager.monitoring.entity.CustomerChargeLog;
import com.keymanager.monitoring.vo.CustomerChargeStatVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerChargeLogService extends ServiceImpl<CustomerChargeLogDao, CustomerChargeLog> {
	private static Logger logger = LoggerFactory.getLogger(CustomerChargeLogService.class);

	@Autowired
	private CustomerChargeLogDao customerChargeLogDao;

	@Autowired
	private CustomerChargeRuleService customerChargeRuleService;

	public void addCustomerChargeLog(List<String> customerUuids, Integer planChargeAmount, Integer actualChargeAmount, String cashier, String nextChargeDate) {
		for (String customerUuid : customerUuids) {
			CustomerChargeLog customerChargeLog = new CustomerChargeLog();
			customerChargeLog.setCustomerUuid(Integer.parseInt(customerUuid));
			customerChargeLog.setPlanChargeAmount(planChargeAmount);
			customerChargeLog.setActualChargeAmount(actualChargeAmount);
			customerChargeLog.setCashier(cashier);
			customerChargeLog.setCreateTime(new Date());
			customerChargeLogDao.insert(customerChargeLog);
		}
		customerChargeRuleService.updateNextChargeDate(customerUuids, nextChargeDate);
	}

	public List<CustomerChargeLog> findCustomerChargeLogs(Integer customerUuid) {
		return customerChargeLogDao.findCustomerChargeLog(customerUuid);
	}

	public void deleteCustomerChargeLogs(Integer customerUuid) {
		customerChargeLogDao.deleteCustomerChargeLogs(customerUuid);
	}

	public CustomerChargeStatVO addUpCustomerChargeLogs(String loginName, String beginDate, String endDate) {
		return customerChargeLogDao.addUpCustomerChargeLogs(loginName, beginDate, endDate);
	}
}
