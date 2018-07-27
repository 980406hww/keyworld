package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.NegativeListUpdateInfo;
import org.apache.ibatis.annotations.Param;

public interface NegativeListUpdateInfoDao extends BaseMapper<NegativeListUpdateInfo> {

    NegativeListUpdateInfo getNegativeListUpdateInfo(@Param("keyword") String keyword);
}
