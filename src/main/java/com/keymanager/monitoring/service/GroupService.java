package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.GroupCriteria;
import com.keymanager.monitoring.criteria.GroupSettingCriteria;
import com.keymanager.monitoring.criteria.UpdateGroupSettingCriteria;
import com.keymanager.monitoring.dao.GroupDao;
import com.keymanager.monitoring.entity.Group;
import com.keymanager.monitoring.vo.GroupVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/4/27 9:42
 **/
@Service
public class GroupService extends ServiceImpl<GroupDao, Group> {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private GroupSettingService groupSettingService;

    public void saveGroup (GroupCriteria groupCriteria) {
        groupDao.saveGroup(groupCriteria.getGroupName(), groupCriteria.getTerminalType(), groupCriteria.getCreateBy(), groupCriteria.getGroupSetting().getRemainingAccount());
        long lastInsertID = groupDao.lastInsertID();
        groupCriteria.getGroupSetting().setGroupUuid(lastInsertID);
        groupSettingService.saveGroupSetting(groupCriteria.getGroupSetting(), false);
    }

    public void deleteGroup (long uuid) {
        groupDao.deleteById(uuid);
        groupSettingService.deleteByGroupUuid(uuid);
    }

    public void updateGroupSettings (long groupUuid, UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        List<Long> groupSettingUuids = groupSettingService.getGroupSettingUuids(groupUuid);
        if (CollectionUtils.isNotEmpty(groupSettingUuids)) {
            for (Long groupSettingUuid : groupSettingUuids) {
                updateGroupSettingCriteria.getGroupSetting().setUuid(groupSettingUuid);
                groupSettingService.updateGroupSetting(updateGroupSettingCriteria.getGs(), updateGroupSettingCriteria.getGroupSetting());
            }
            if (1 == updateGroupSettingCriteria.getGs().getMachineUsedPercent()) {
                this.updateGroupRemainingAccount(updateGroupSettingCriteria.getGroupSetting().getGroupUuid(), updateGroupSettingCriteria.getGroupSetting().getRemainingAccount());
            }
        }
    }

    public List<GroupVO> searchGroups (Page<GroupVO> page, GroupSettingCriteria groupSettingCriteria) {
        return groupDao.searchGroups(page, groupSettingCriteria);
    }
    
    public Group findGroup(String groupName, String terminalType){
        return groupDao.findGroup(groupName, terminalType);
    }
    
    public void updateGroupRemainingAccount (long groupUuid, int remainingAccount) {
        groupDao.updateGroupRemainingAccount(groupUuid, remainingAccount);
    }
}
