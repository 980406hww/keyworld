package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.Config;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigDao2 extends BaseMapper<Config> {
    Config getConfig(@Param("configType") String configType, @Param("key") String key);

    void updateConfig(@Param("config") Config config);

    List<Config> findConfigs(@Param("configType") String configType);
}