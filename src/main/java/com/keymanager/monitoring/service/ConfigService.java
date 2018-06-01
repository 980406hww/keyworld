package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ClientStatusDao;
import com.keymanager.monitoring.dao.ConfigDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.util.Constants;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		configDao.updateConfig(optimizationDateConfig);
	}

	public void updateNegativeKeywordsFromConfig(String negativeKeywords) {
		Config config = new Config();
		config.setConfigType(Constants.CONFIG_TYPE_TJ_XG);
		config.setKey(Constants.CONFIG_KEY_NEGATIVE_KEYWORDS);
		config.setValue(negativeKeywords);
		configDao.updateConfig(config);
	}

	public void updateCustomerNegativeKeywords(File targetFile, String searchEngine) {
		List<String> contents = FileUtil.readTxtFile(targetFile, "UTF-8");
		String result = contents.toString();
		result = StringUtils.deleteWhitespace(result);
		result = result.substring(1, result.length() - 1);
		Config config = new Config();
		config.setConfigType(Constants.CONFIG_TYPE_NEGATIVE_KEYWORD);
		config.setKey(searchEngine);
		config.setValue(result);
		configDao.updateConfig(config);
	}

	public void updateCustomerUuidsForDailyReport(String customerUuids, String terminalType) {
		Config config = new Config();
		config.setConfigType(Constants.CONFIG_TYPE_DAILY_REPORT);
		config.setKey(terminalType);
		config.setValue(customerUuids);
		configDao.updateConfig(config);
	}

	public Set<String> getNegativeKeyword(){
		List<Config> configs=configDao.getNegativeKeyword (Constants.CONFIG_TYPE_NEGATIVE_KEYWORD);
		Set<String> keywords=new HashSet<String>();
		for (Config config : configs){
			String[] ky=config.getValue().split(",");
			for(String keyword:ky){
				keywords.add(keyword);
			}
		}
		return keywords;
	}

}
