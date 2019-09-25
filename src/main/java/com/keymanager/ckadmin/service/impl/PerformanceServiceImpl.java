package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.service.PerformanceService;
import com.keymanager.monitoring.dao.MachineInfoDao;
import com.keymanager.monitoring.dao.PerformanceDao;
import com.keymanager.monitoring.entity.MachineInfo;
import com.keymanager.monitoring.entity.Performance;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("performanceService2")
public class PerformanceServiceImpl extends ServiceImpl<MachineInfoDao, MachineInfo> implements PerformanceService {
	
	@Autowired
	private PerformanceDao performanceDao;

	@Override
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
