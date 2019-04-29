package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.GroupSettingCriteria;
import com.keymanager.monitoring.entity.GroupSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupSettingDao extends BaseMapper<GroupSetting> {

    List<GroupSetting> searchGroupSettings (@Param("groupUuid") long groupUuid, @Param("operationType") String operationType);
}
