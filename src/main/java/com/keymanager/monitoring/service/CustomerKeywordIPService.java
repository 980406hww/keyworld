package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerKeywordIPDao;
import com.keymanager.monitoring.entity.CustomerKeywordIP;
import com.keymanager.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerKeywordIPService extends ServiceImpl<CustomerKeywordIPDao, CustomerKeywordIP>{
	
	@Autowired
	private CustomerKeywordIPDao customerKeywordIPDao;

	public void addCustomerKeywordIP(Long customerKeywordUuid, String city, String ip){
		CustomerKeywordIP customerKeywordIP = new CustomerKeywordIP();
		customerKeywordIP.setCustomerKeywordUuid(customerKeywordUuid);
		customerKeywordIP.setCity(city);
		customerKeywordIP.setIp(ip);
		customerKeywordIP.setCreateTime(Utils.getCurrentTimestamp());
		customerKeywordIPDao.insert(customerKeywordIP);
	}
}
