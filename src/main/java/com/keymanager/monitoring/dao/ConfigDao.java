package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.Config;
import org.apache.ibatis.annotations.Param;

public interface ConfigDao extends BaseMapper<Config> {
    Config getConfig(@Param("configType")String configType, @Param("key")String key);
}
