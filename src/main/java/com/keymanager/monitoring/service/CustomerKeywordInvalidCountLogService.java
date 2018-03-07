package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerKeywordInvalidCountLogDao;
import com.keymanager.monitoring.entity.CustomerKeywordInvalidCountLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerKeywordInvalidCountLogService extends ServiceImpl<CustomerKeywordInvalidCountLogDao, CustomerKeywordInvalidCountLog>{
	
	@Autowired
	private CustomerKeywordInvalidCountLogDao customerKeywordInvalidCountLogDao;

	@Autowired
	private CustomerKeywordService customerKeywordService;

	public void addCustomerKeywordInvalidCountLog() {
		customerKeywordInvalidCountLogDao.addCustomerKeywordInvalidCountLog();
	}

	public List<Long> findInvalidCustomerKeyword() {
		return customerKeywordInvalidCountLogDao.findInvalidCustomerKeyword();
	}

	public void deleteInvalidCountLogFromAWeekAgo() {
		customerKeywordInvalidCountLogDao.deleteInvalidCountLogFromAWeekAgo();
	}
}
