package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.QZCaptureTitleLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QZCaptureTitleLogDao extends BaseMapper<QZCaptureTitleLog> {
    List<QZCaptureTitleLog> getAvailableQZCaptureTitleLog(@Param("status") String status, @Param("terminalType") String terminalType);
}
