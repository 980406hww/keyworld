package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.GroupSettingCriteria;
import com.keymanager.monitoring.criteria.UpdateGroupSettingCriteria;
import com.keymanager.monitoring.dao.GroupSettingDao;
import com.keymanager.monitoring.entity.GroupSetting;
import com.keymanager.monitoring.entity.OperationCombine;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupSettingService extends ServiceImpl<GroupSettingDao, GroupSetting> {

    @Autowired
    private GroupSettingDao groupSettingDao;

    @Autowired
    private OperationCombineService operationCombineService;

    public Page<OperationCombine> searchGroupSettings(Page<OperationCombine> page, GroupSettingCriteria groupSettingCriteria) {
        page.setRecords(operationCombineService.searchOperationCombines(page, groupSettingCriteria));
        for (OperationCombine operationCombine : page.getRecords()) {
            List<GroupSetting> groupSettings = groupSettingDao.searchGroupSettings(operationCombine.getUuid());
            operationCombine.setGroupSettings(groupSettings);
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

    public void saveGroupSetting (GroupSetting groupSetting) {
        groupSettingDao.saveGroupSetting(groupSetting);
        operationCombineService.updateOperationCombineRemainingAccount(groupSetting.getOperationCombineUuid(), groupSetting.getRemainingAccount());
    }

    public void updateGroupSetting (UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        groupSettingDao.updateGroupSetting(updateGroupSettingCriteria.getGs(), updateGroupSettingCriteria.getGroupSetting());
        GroupSetting groupSetting = groupSettingDao.selectById(updateGroupSettingCriteria.getGroupSetting().getUuid());
        operationCombineService.updateOperationCombineUpdateTime(groupSetting.getOperationCombineUuid());
        if (1 == updateGroupSettingCriteria.getGs().getMachineUsedPercent()) {
            operationCombineService.updateOperationCombineRemainingAccount(updateGroupSettingCriteria.getGroupSetting().getOperationCombineUuid(), updateGroupSettingCriteria.getGroupSetting().getRemainingAccount());
        }
        if ("1".equals(updateGroupSettingCriteria.getGs().getOperationCombineName())) {
            operationCombineService.updateOperationCombineName(groupSetting.getOperationCombineUuid(), updateGroupSettingCriteria.getGroupSetting().getOperationCombineName());
        }
    }

    public GroupSetting getGroupSettingViaPercentage(String groupName, String terminalType){
        OperationCombine operationCombine = operationCombineService.getOperationCombine(groupName, terminalType);
        if(operationCombine != null) {
            return getGroupSetting(operationCombine);
        }
        return null;
    }

    public GroupSetting getGroupSetting(OperationCombine operationCombine) {
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
