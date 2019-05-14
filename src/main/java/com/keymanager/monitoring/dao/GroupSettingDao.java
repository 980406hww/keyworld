package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.GroupSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupSettingDao extends BaseMapper<GroupSetting> {

    List<GroupSetting> searchGroupSettings (@Param("groupUuid") long groupUuid, @Param("operationType") String operationType);

    List<GroupSetting> searchGroupSettingsSortingPercentage (@Param("groupUuid") long groupUuid);

    void saveGroupSetting (@Param("groupSetting") GroupSetting groupSetting);

    void updateGroupSetting (@Param("gs") GroupSetting gs, @Param("groupSetting") GroupSetting groupSetting);

    void deleteByGroupUuid (@Param("groupUuid") long groupUuid);
}
