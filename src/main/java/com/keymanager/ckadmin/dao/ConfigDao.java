package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.Config;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("configDao2")
public interface ConfigDao extends BaseMapper<Config> {
    Config getConfig(@Param("configType") String configType, @Param("key") String key);

    void updateConfig(@Param("config") Config config);

    List<Config> findConfigs(@Param("configType") String configType);
}