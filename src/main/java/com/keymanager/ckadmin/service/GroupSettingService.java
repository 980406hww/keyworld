package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.GroupSettingCriteria;
import com.keymanager.ckadmin.criteria.UpdateGroupSettingCriteria;
import com.keymanager.ckadmin.entity.GroupSetting;
import com.keymanager.ckadmin.entity.OperationCombine;
import java.util.List;

public interface GroupSettingService extends IService<GroupSetting> {

    public Page<OperationCombine> searchGroupSettings(Page<OperationCombine> page, GroupSettingCriteria groupSettingCriteria);

    public void deleteGroupSetting (long uuid);

    public GroupSetting findGroupSetting (long uuid);

    public void saveGroupSetting (GroupSetting groupSetting);

    public void updateGroupSetting (UpdateGroupSettingCriteria updateGroupSettingCriteria);

    public GroupSetting getGroupSettingViaPercentage(String groupName, String terminalType);

    public GroupSetting getGroupSetting(OperationCombine operationCombine);

    public List<Long> getGroupSettingUuids (long operationCombineUuid);

    public void updateGroupSetting(GroupSetting gs, GroupSetting groupSetting);

    public void deleteGroupSettingByOperationCombineUuid (long operationCombineUuid);
}
