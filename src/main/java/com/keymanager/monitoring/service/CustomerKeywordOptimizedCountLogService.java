package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerKeywordOptimizedCountLogDao;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.CustomerKeywordOptimizedCountLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerKeywordOptimizedCountLogService extends ServiceImpl<CustomerKeywordOptimizedCountLogDao, CustomerKeywordOptimizedCountLog>{
	
	@Autowired
	private CustomerKeywordOptimizedCountLogDao customerKeywordOptimizedCountLogDao;

	@Autowired
	private CustomerKeywordService customerKeywordService;

	public void addCustomerKeywordOptimizedCountLog(Long customerKeywordUuid, int count) {
		CustomerKeyword customerKeyword = customerKeywordService.selectById(customerKeywordUuid);
		CustomerKeywordOptimizedCountLog customerKeywordOptimizedCountLog = new CustomerKeywordOptimizedCountLog();
		customerKeywordOptimizedCountLog.setCustomerKeywordUuid(customerKeywordUuid);
		customerKeywordOptimizedCountLog.setOptimizedCount(customerKeyword.getOptimizedCount() + count);
		customerKeywordOptimizedCountLog.setOptimizedDate(new Date());
		customerKeywordOptimizedCountLogDao.insert(customerKeywordOptimizedCountLog);
	}

	public List<CustomerKeywordOptimizedCountLog> groupCustomerKeywordOptimizedCountLogs() {
		return customerKeywordOptimizedCountLogDao.groupCustomerKeywordOptimizedCountLogs();
	}

	public CustomerKeywordOptimizedCountLog findCurrentCountLog(Long customerKeywordUuid) {
		return customerKeywordOptimizedCountLogDao.findCurrentCountLog(customerKeywordUuid);
	}

	public CustomerKeywordOptimizedCountLog findThreeDaysAgoCountLog(Long customerKeywordUuid) {
		return customerKeywordOptimizedCountLogDao.findThreeDaysAgoCountLog(customerKeywordUuid);
	}
}
