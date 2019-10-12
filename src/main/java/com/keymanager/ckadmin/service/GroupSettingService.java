package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.GroupSettingCriteria;
import com.keymanager.ckadmin.criteria.UpdateGroupSettingCriteria;
import com.keymanager.ckadmin.entity.GroupSetting;
import com.keymanager.ckadmin.entity.OperationCombine;
import java.util.List;

public interface GroupSettingService extends IService<GroupSetting> {

    Page<OperationCombine> searchGroupSettings(Page<OperationCombine> page, GroupSettingCriteria groupSettingCriteria);

    void deleteGroupSetting(long uuid);

    GroupSetting findGroupSetting(long uuid);

    void saveGroupSetting(GroupSetting groupSetting);

    void updateGroupSetting(UpdateGroupSettingCriteria updateGroupSettingCriteria);

    GroupSetting getGroupSettingViaPercentage(String groupName, String terminalType);

    GroupSetting getGroupSetting(OperationCombine operationCombine);

    List<Long> getGroupSettingUuids(long operationCombineUuid);

    void updateGroupSetting(GroupSetting gs, GroupSetting groupSetting);

    void deleteGroupSettingByOperationCombineUuid(long operationCombineUuid);

    GroupSetting getGroupSettingByUuid(Long uuid);
}
