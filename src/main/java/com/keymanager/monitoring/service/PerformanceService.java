package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ClientStatusDao;
import com.keymanager.monitoring.dao.ConfigDao;
import com.keymanager.monitoring.dao.PerformanceDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.Performance;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class PerformanceService extends ServiceImpl<ClientStatusDao, ClientStatus>{
	
	@Autowired
	private PerformanceDao performanceDao;

	public void addPerformanceLog(String module, long milleSeconds, String remarks){
		if("ResetInfoDailySchedule".equals(module)) {
			Performance performance = new Performance();
			performance.setModule(module);
			performance.setMilleSecond(milleSeconds);
			performance.setCurrentMemory(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
			performance.setRemarks(remarks);
			performance.setCreateTime(new Date());
			performanceDao.insert(performance);
		}
	}
}
