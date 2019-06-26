package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.dao.GroupDao;
import com.keymanager.monitoring.entity.Group;
import com.keymanager.monitoring.entity.GroupSetting;
import com.keymanager.monitoring.entity.OperationCombine;
import com.keymanager.monitoring.vo.GroupVO;
import com.keymanager.monitoring.vo.OperationCombineVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @Autowired
    private QZSettingService qzSettingService;

    @Autowired
    private OperationCombineService operationCombineService;

//    public void saveGroup (GroupCriteria groupCriteria) {
//        groupDao.saveGroup(groupCriteria.getGroupName(), groupCriteria.getTerminalType(), groupCriteria.getCreateBy(), groupCriteria.getGroupSetting().getRemainingAccount(), groupCriteria.getMaxInvalidCount());
//        long lastInsertID = groupDao.lastInsertID();
//        groupCriteria.getGroupSetting().setGroupUuid(lastInsertID);
//        groupSettingService.saveGroupSetting(groupCriteria.getGroupSetting(), false);
//    }

    public void updateGroupSettings (long groupUuid, UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        List<Long> groupSettingUuids = groupSettingService.getGroupSettingUuids(groupUuid);
        if (CollectionUtils.isNotEmpty(groupSettingUuids)) {
            for (Long groupSettingUuid : groupSettingUuids) {
                updateGroupSettingCriteria.getGroupSetting().setUuid(groupSettingUuid);
                groupSettingService.updateGroupSetting(updateGroupSettingCriteria.getGs(), updateGroupSettingCriteria.getGroupSetting());
                GroupSetting groupSetting = groupSettingService.selectById(updateGroupSettingCriteria.getGroupSetting().getUuid());
                this.updateGroupUpdateTime(groupSetting.getGroupUuid());
            }
            if (1 == updateGroupSettingCriteria.getGs().getMachineUsedPercent()) {
                this.updateGroupRemainingAccount(updateGroupSettingCriteria.getGroupSetting().getGroupUuid(), updateGroupSettingCriteria.getGroupSetting().getRemainingAccount());
            }
            if (1 == updateGroupSettingCriteria.getGs().getMaxInvalidCount()) {
                this.updateMaxInvalidCount(updateGroupSettingCriteria.getGroupSetting().getGroupUuid(), updateGroupSettingCriteria.getGroupSetting().getMaxInvalidCount());
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

    public void batchAddGroups (OperationCombineCriteria operationCombineCriteria) {
        OperationCombine operationCombine = operationCombineService.selectById(operationCombineCriteria.getOperationCombineUuid());
        operationCombineCriteria.setTerminalType(operationCombine.getTerminalType());
        operationCombineCriteria.setMaxInvalidCount(operationCombine.getMaxInvalidCount());
        this.saveGroupsBelowOperationCombine(operationCombineCriteria);
    }

    public List<String> getAvailableOptimizationGroups (GroupSettingCriteria groupSettingCriteria) {
        List<String> availableOptimizationGroups = new ArrayList<String>();
        if (groupSettingCriteria.getOptimizedGroupNameSearchSource()){
            availableOptimizationGroups = customerKeywordService.getAvailableOptimizationGroups(groupSettingCriteria);
            availableOptimizationGroups.remove("stop");
            availableOptimizationGroups.remove("zanting");
            availableOptimizationGroups.remove("ZANTING");
            availableOptimizationGroups.remove("sotp");
            List<String> needToRemoves = new ArrayList<String>();
            for (int i = 0; i < availableOptimizationGroups.size(); i++) {
                if (availableOptimizationGroups.get(i).startsWith("no_")){
                    needToRemoves.add(availableOptimizationGroups.get(i));
                }
            }
            availableOptimizationGroups.removeAll(needToRemoves);
        }else {
            availableOptimizationGroups = qzSettingService.getAvailableOptimizationGroups(groupSettingCriteria);
        }
        List<String> optimizationGroups = groupDao.getOptimizationGroups(groupSettingCriteria.getTerminalType());
        availableOptimizationGroups.removeAll(optimizationGroups);
        return new ArrayList<>(availableOptimizationGroups);
    }
    
    public void updateMaxInvalidCount(long uuid, int maxInvalidCount){
        groupDao.updateMaxInvalidCount(uuid, maxInvalidCount);
    }

    public void updateGroupUpdateTime (long groupUuid) {
        Group group = groupDao.selectById(groupUuid);
        group.setUpdateTime(new Date());
        groupDao.updateById(group);
    }

    public List<String> getGroupNames (Long operationCombineUuid) {
        return groupDao.getGroupNames(operationCombineUuid);
    }

    public List<OperationCombineVO> searchGroupsBelowOperationCombine (Long operationCombineUuid, String groupName) {
        return groupDao.searchGroupsBelowOperationCombine(operationCombineUuid, groupName);
    }

    public void saveGroupsBelowOperationCombine (OperationCombineCriteria operationCombineCriteria) {
        List<Long> groupUuids = new ArrayList<>();
        List<String> notExistsGroupNames = new ArrayList<>();
        for (String groupName : operationCombineCriteria.getGroupNames()) {
            Long existingGroupUuid = groupDao.searchExistingGroupUuid(operationCombineCriteria.getTerminalType(), groupName);
            if (null != existingGroupUuid) {
                groupUuids.add(existingGroupUuid);
            } else {
                notExistsGroupNames.add(groupName);
            }
        }

        if (CollectionUtils.isNotEmpty(groupUuids)) {
            this.updateGroupsBelowOperationCombine(groupUuids, operationCombineCriteria.getOperationCombineUuid());
        }

        if (CollectionUtils.isNotEmpty(notExistsGroupNames)) {
            operationCombineCriteria.setGroupNames(notExistsGroupNames);
            Integer remainingAccount = groupSettingService.getSumMachineUsedPercent(operationCombineCriteria.getOperationCombineUuid());
            operationCombineCriteria.setRemainingAccount(remainingAccount);
            groupDao.insertBatchGroups(operationCombineCriteria);
        }
    }

    public void updateGroupsBelowOperationCombine (List<Long> groupUuids, Long operationCombineUuid) {
        groupDao.updateGroupOperationUuid(groupUuids, operationCombineUuid);
    }

    public void updateGroupOperationCombineUuid (Long operationCombineUuid) {
        groupDao.updateGroupOperationCombineUuid(operationCombineUuid);
    }
}
