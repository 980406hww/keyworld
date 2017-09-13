package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.value.CustomerKeywordRefreshStatInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj08 on 2017/9/12.
 */
public interface ConfigDao extends BaseMapper<Config> {

    Config getConfig(@Param("configType")String configType, @Param("key")String key);

}
