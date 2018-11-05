package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.PositiveListUpdateInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PositiveListUpdateInfoDao extends BaseMapper<PositiveListUpdateInfo> {

    List<PositiveListUpdateInfo> findMostRecentPositiveListUpdateInfo (@Param("pid") Long pid);
}
