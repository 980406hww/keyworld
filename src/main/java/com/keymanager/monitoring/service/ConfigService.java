package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ClientStatusDao;
import com.keymanager.monitoring.dao.ConfigDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigService extends ServiceImpl<ClientStatusDao, ClientStatus>{
	
	@Autowired
	private ConfigDao configDao;

	public Config getConfig(String configType, String key){
		return configDao.getConfig(configType, key);
	}

	public boolean optimizationDateChanged(){
		Config optimizationDateConfig = getConfig(Constants.CONFIG_TYPE_OPTIMIZATION_DATE, Constants.CONFIG_TYPE_OPTIMIZATION_DATE);
		String currentDate = Utils.getCurrentDate();
		return !currentDate.equals(optimizationDateConfig.getValue());
	}

	public void updateOptimizationDateAsToday(){
		Config optimizationDateConfig = getConfig(Constants.CONFIG_TYPE_OPTIMIZATION_DATE, Constants.CONFIG_TYPE_OPTIMIZATION_DATE);
		String currentDate = Utils.getCurrentDate();
		optimizationDateConfig.setValue(currentDate);
		configDao.updateById(optimizationDateConfig);
	}
}
