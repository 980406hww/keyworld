package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.Config;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ConfigDao extends BaseMapper<Config> {
    Config getConfig(@Param("configType")String configType, @Param("key")String key);

    void updateConfig(@Param("config") Config config);

    List<Config> getNegativeKeyword(@Param("configType")String configType);

    List<Map> getSameCustomerKeywordCount(@Param("defaultOptimizeGroupNameConfigType")String defaultOptimizeGroupNameConfigType, @Param("sameCustomerKeywordCountConfigType")String sameCustomerKeywordCountConfigType);
}
