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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.*;

@Service
public class ConfigService extends ServiceImpl<ClientStatusDao, ClientStatus>{

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private ConfigCacheService configCacheService;

    @Cacheable(value = "configList", key = "#configType + #key")
    public Config getConfig(String configType, String key){
        return configDao.getConfig(configType, key);
    }

    public void updateConfig(Config config) {
        configDao.updateConfig(config);
        configCacheService.configCacheEvict(config);
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
        updateConfig(optimizationDateConfig);
    }

    public void updateNegativeKeywordsFromConfig(String negativeKeywords) {
        Config config = new Config();
        config.setConfigType(Constants.CONFIG_TYPE_TJ_XG);
        config.setKey(Constants.CONFIG_KEY_NEGATIVE_KEYWORDS);
        config.setValue(negativeKeywords);
        updateConfig(config);
    }

    public void updateCustomerNegativeKeywords(File targetFile, String searchEngine) throws Exception {
        List<String> contents = FileUtil.readTxtFile(targetFile, "UTF-8");
        String result = "";
        for (String content : contents) {
            result += "," + content;
        }
        result = result.substring(1, result.length());
        Config config = new Config();
        config.setConfigType(Constants.CONFIG_TYPE_NEGATIVE_KEYWORD);
        config.setKey(searchEngine);
        config.setValue(result);
        updateConfig(config);
    }

    public void updateCustomerUuidsForDailyReport(String customerUuids, String terminalType) {
        Config config = new Config();
        config.setConfigType(Constants.CONFIG_TYPE_DAILY_REPORT);
        config.setKey(terminalType);
        config.setValue(customerUuids);
        updateConfig(config);
    }

    public Set<String> getNegativeKeyword(){
        List<Config> configs = findConfigs(Constants.CONFIG_TYPE_NEGATIVE_KEYWORD);
        Set<String> keywords=new HashSet<String>();
        for (Config config : configs){
            String[] ky=config.getValue().split(",");
            for(String keyword:ky){
                keywords.add(keyword);
            }
        }
        return keywords;
    }

    public void refreshCustomerNegativeKeywords(String searchEngine,String negativeKeywords){
        Config config = new Config();
        config.setConfigType(Constants.CONFIG_TYPE_NEGATIVE_KEYWORD);
        config.setKey(searchEngine);
        config.setValue(negativeKeywords);
        updateConfig(config);
    }

    public void refreshWebsiteWhiteList(String websiteWhiteList){
        Config config = new Config();
        config.setConfigType(Constants.CONFIG_TYPE_WEBSITE_WHITE_LIST);
        config.setKey(Constants.CONFIG_KEY_URL);
        config.setValue(websiteWhiteList);
        updateConfig(config);
    }

    public void updateWebsiteWhiteList(File targetFile) throws Exception {
        List<String> contents = FileUtil.readTxtFile(targetFile, "UTF-8");
        String result = "";
        for (String content : contents) {
            result += "," + content;
        }
        result = result.substring(1, result.length());
        Config config = new Config();
        config.setConfigType(Constants.CONFIG_TYPE_WEBSITE_WHITE_LIST);
        config.setKey(Constants.CONFIG_KEY_URL);
        config.setValue(result);
        updateConfig(config);
    }
    public List<Config> findConfigs(String configType) {
        return configDao.findConfigs(configType);
    }

    public List<String> getMonitorOptimizeGroupName(String configType) {
        List<String> configList = new ArrayList<String>();
        List<Config> configs = findConfigs(configType);
        for (Config config : configs) {
            String[] temp = config.getValue().split(",");
            configList.addAll(Arrays.asList(temp));
        }
        return configList;
    }

    public Map<String, Integer> getSameCustomerKeywordCount() {
        Map<String, Integer> sameCustomerKeywordCountMap = new HashMap<String, Integer>();
        List<Config> configs = findConfigs(Constants.CONFIG_TYPE_SAME_CUSTOMER_KEYWORD_COUNT);
        for (Config config : configs) {
            sameCustomerKeywordCountMap.put(config.getKey(), Integer.parseInt(config.getValue()));
        }
        return sameCustomerKeywordCountMap;
    }
}