package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.entity.NegativeKeywordNamePositionInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NegativeKeywordNamePositionInfoDao extends BaseMapper<NegativeKeywordNamePositionInfo> {

    List<NegativeKeywordNamePositionInfo> findPositionInfoByUuid(@Param("uuid") Long uuid);
}
