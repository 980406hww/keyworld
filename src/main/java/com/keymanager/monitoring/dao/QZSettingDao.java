package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.QZSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QZSettingDao extends BaseMapper<QZSetting> {
    List<QZSetting> getAvailableQZSettings();

    List<QZSetting> searchQZSettingsByUuids(@Param("uuids") String uuids);

    List<QZSetting> searchQZSettings(@Param("uuid") Long uuid, @Param("customerUuid") Long customerUuid, @Param("domain") String
            domain, @Param("group") String group, @Param("updateStatus") String updateStatus);
}
