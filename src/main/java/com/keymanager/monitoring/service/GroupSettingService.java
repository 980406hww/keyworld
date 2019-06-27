package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.GroupSettingCriteria;
import com.keymanager.monitoring.criteria.UpdateGroupSettingCriteria;
import com.keymanager.monitoring.dao.GroupSettingDao;
import com.keymanager.monitoring.entity.GroupSetting;
import com.keymanager.monitoring.entity.OperationCombine;
import com.keymanager.monitoring.vo.GroupVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupSettingService extends ServiceImpl<GroupSettingDao, GroupSetting> {

    @Autowired
    private GroupSettingDao groupSettingDao;

    @Autowired
    private GroupService groupService;

    @Autowired
    private OperationCombineService operationCombineService;

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
        OperationCombine operationCombine = operationCombineService.selectById(groupSetting.getOperationCombineUuid());
        operationCombine.setRemainingAccount(operationCombine.getRemainingAccount() + groupSetting.getMachineUsedPercent());
        operationCombineService.updateById(operationCombine);
        groupSettingDao.deleteById(uuid);
    }

    public GroupSetting findGroupSetting (long uuid) {
        return groupSettingDao.selectById(uuid);
    }

    public void saveGroupSetting (GroupSetting groupSetting, Boolean needUpdateRemainingAccount) {
        groupSettingDao.saveGroupSetting(groupSetting);
        if (needUpdateRemainingAccount) {
            operationCombineService.updateOperationCombineRemainingAccount(groupSetting.getOperationCombineUuid(), groupSetting.getRemainingAccount());
        }
    }

    public void updateGroupSetting (UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        groupSettingDao.updateGroupSetting(updateGroupSettingCriteria.getGs(), updateGroupSettingCriteria.getGroupSetting());
        GroupSetting groupSetting = groupSettingDao.selectById(updateGroupSettingCriteria.getGroupSetting().getUuid());
        operationCombineService.updateOperationCombineUpdateTime(groupSetting.getOperationCombineUuid());
        if (1 == updateGroupSettingCriteria.getGs().getMachineUsedPercent()) {
            operationCombineService.updateOperationCombineRemainingAccount(updateGroupSettingCriteria.getGroupSetting().getOperationCombineUuid(), updateGroupSettingCriteria.getGroupSetting().getRemainingAccount());
        }
    }

    public GroupSetting getGroupSettingViaPercentage(String groupName, String terminalType){
        OperationCombine operationCombine = operationCombineService.getOperationCombine(groupName, terminalType);
        if(operationCombine != null){
            List<GroupSetting> groupSettings = groupSettingDao.searchGroupSettingsSortingPercentage(operationCombine.getUuid());
            if(CollectionUtils.isNotEmpty(groupSettings)){
                Random ra = new Random();
                if(operationCombine.getRemainingAccount() < 100) {
                    int randomValue = ra.nextInt(100 - operationCombine.getRemainingAccount()) + 1;
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

    public List<Long> getGroupSettingUuids (long operationCombineUuid) {
        return groupSettingDao.getGroupSettingUuids(operationCombineUuid);
    }

    public void updateGroupSetting(GroupSetting gs, GroupSetting groupSetting){
        groupSettingDao.updateGroupSetting(gs, groupSetting);
    }

    public void deleteGroupSettingByOperationCombineUuid (long operationCombineUuid) {
        groupSettingDao.deleteGroupSettingByOperationCombineUuid(operationCombineUuid);
    }
}
