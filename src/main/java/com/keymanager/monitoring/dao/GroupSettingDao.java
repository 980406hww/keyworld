package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.GroupSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupSettingDao extends BaseMapper<GroupSetting> {

    List<GroupSetting> searchGroupSettings (@Param("operationCombineUuid") long operationCombineUuid);

    List<GroupSetting> searchGroupSettingsSortingPercentage (@Param("operationCombineUuid") long operationCombineUuid);

    void saveGroupSetting (@Param("groupSetting") GroupSetting groupSetting);

    void updateGroupSetting (@Param("gs") GroupSetting gs, @Param("groupSetting") GroupSetting groupSetting);

    List<Long> getGroupSettingUuids (@Param("operationCombineUuid") long operationCombineUuid);

    void deleteGroupSettingByOperationCombineUuid (@Param("operationCombineUuid") long operationCombineUuid);

    Integer getSumMachineUsedPercent (@Param("operationCombineUuid") long operationCombineUuid);
}
