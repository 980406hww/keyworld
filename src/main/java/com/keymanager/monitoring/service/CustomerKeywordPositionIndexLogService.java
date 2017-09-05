package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerKeywordPositionIndexLogDao;
import com.keymanager.monitoring.entity.CustomerKeywordPositionIndexLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomerKeywordPositionIndexLogService extends ServiceImpl<CustomerKeywordPositionIndexLogDao, CustomerKeywordPositionIndexLog> {
	private static Logger logger = LoggerFactory.getLogger(CustomerKeywordPositionIndexLogService.class);

	@Autowired
	private CustomerKeywordPositionIndexLogDao customerKeywordPositionIndexLogDao;

	public Page<CustomerKeywordPositionIndexLog> searchCustomerKeywordPositionIndexLogs(Page<CustomerKeywordPositionIndexLog> page, Map<String,Object> condition){
		page.setRecords(customerKeywordPositionIndexLogDao.searchCustomerKeywordPositionIndexLogs(page,condition));
		return page;
	}
}
