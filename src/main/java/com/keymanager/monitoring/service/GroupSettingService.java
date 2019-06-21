package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.GroupSettingCriteria;
import com.keymanager.monitoring.criteria.UpdateGroupSettingCriteria;
import com.keymanager.monitoring.dao.GroupSettingDao;
import com.keymanager.monitoring.entity.Group;
import com.keymanager.monitoring.entity.GroupSetting;
import com.keymanager.monitoring.vo.GroupVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupSettingService extends ServiceImpl<GroupSettingDao, GroupSetting> {

    private static Logger logger = LoggerFactory.getLogger(GroupSettingService.class);

    @Autowired
    private GroupSettingDao groupSettingDao;

    @Autowired
    private GroupService groupService;

    public Page<GroupVO> searchGroupSettings(Page<GroupVO> page, GroupSettingCriteria groupSettingCriteria) {
        page.setRecords(groupService.searchGroups(page, groupSettingCriteria));
        for (GroupVO groupVo : page.getRecords()) {
            List<GroupSetting> groupSettings = groupSettingDao.searchGroupSettings(groupVo.getUuid());
            groupVo.setGroupSettings(groupSettings);
        }
        return page;
    }

    public void deleteGroupSetting (long uuid) {
        GroupSetting groupSetting = groupSettingDao.selectById(uuid);
        Group group = groupService.selectById(groupSetting.getGroupUuid());
        group.setRemainingAccount(group.getRemainingAccount() + groupSetting.getMachineUsedPercent());
        groupService.updateById(group);
        groupSettingDao.deleteById(uuid);
    }

    public GroupSetting findGroupSetting (long uuid) {
        return groupSettingDao.selectById(uuid);
    }

    public void saveGroupSetting (GroupSetting groupSetting, Boolean needUpdateRemainingAccount) {
        groupSettingDao.saveGroupSetting(groupSetting);
        if (needUpdateRemainingAccount) {
            groupService.updateGroupRemainingAccount(groupSetting.getGroupUuid(), groupSetting.getRemainingAccount());
        }
    }

    public void updateGroupSetting (UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        groupSettingDao.updateGroupSetting(updateGroupSettingCriteria.getGs(), updateGroupSettingCriteria.getGroupSetting());
        GroupSetting groupSetting = groupSettingDao.selectById(updateGroupSettingCriteria.getGroupSetting().getUuid());
        groupService.updateGroupUpdateTime(groupSetting.getGroupUuid());
        if (1 == updateGroupSettingCriteria.getGs().getMachineUsedPercent()) {
            groupService.updateGroupRemainingAccount(updateGroupSettingCriteria.getGroupSetting().getGroupUuid(), updateGroupSettingCriteria.getGroupSetting().getRemainingAccount());
        }
    }

    public GroupSetting getGroupSettingViaPercentage(String groupName, String terminalType){
        Group group = groupService.findGroup(groupName, terminalType);
        if(group != null){
            List<GroupSetting> groupSettings = groupSettingDao.searchGroupSettingsSortingPercentage(group.getUuid());
            if(CollectionUtils.isNotEmpty(groupSettings)){
                Random ra = new Random();
                if(group.getRemainingAccount() < 100) {
                    int randomValue = ra.nextInt(100 - group.getRemainingAccount()) + 1;
                    int totalPercentage = 0;
                    for (GroupSetting groupSetting : groupSettings) {
                        totalPercentage = totalPercentage + groupSetting.getMachineUsedPercent();
                        if (randomValue <= totalPercentage) {
                            return groupSetting;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void deleteByGroupUuid (long uuid) {
        groupSettingDao.deleteByGroupUuid(uuid);
    }

    public List<Long> getGroupSettingUuids (long groupUuid) {
        return groupSettingDao.getGroupSettingUuids(groupUuid);
    }

    public void updateGroupSetting(GroupSetting gs, GroupSetting groupSetting){
        groupSettingDao.updateGroupSetting(gs, groupSetting);
    }
}
