package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ConfigDao;
import com.keymanager.monitoring.dao.CustomerKeywordRefreshStatInfoDao;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.value.CustomerKeywordRefreshStatInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj08 on 2017/9/12.
 */
@Service
public class ConfigService extends ServiceImpl<CustomerKeywordRefreshStatInfoDao, CustomerKeywordRefreshStatInfoVO> {

    private static Logger logger = LoggerFactory.getLogger(ConfigService.class);

    @Autowired
    private ConfigDao configDao;

    public Config findConfigs(String configType, String key) {
        return configDao.getConfig(configType, key);
    }
}
