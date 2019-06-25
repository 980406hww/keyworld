package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.dao.GroupDao;
import com.keymanager.monitoring.entity.Group;
import com.keymanager.monitoring.entity.GroupSetting;
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

    public void saveGroup (GroupCriteria groupCriteria) {
        groupDao.saveGroup(groupCriteria.getGroupName(), groupCriteria.getTerminalType(), groupCriteria.getCreateBy(), groupCriteria.getGroupSetting().getRemainingAccount(), groupCriteria.getMaxInvalidCount());
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

    public void batchAddGroups (GroupBatchCriteria groupBatchCriteria, String userName, int maxInvalidCount) {
        for (GroupSettingCriteria groupSettingCriteria : groupBatchCriteria.getOptimizationGroupList()) {
            groupDao.saveGroup(groupSettingCriteria.getOptimizedGroupName(), groupSettingCriteria.getTerminalType(), userName, 0, maxInvalidCount);
            long lastInsertID = groupDao.lastInsertID();
            GroupSetting groupSetting = new GroupSetting();
            groupSetting.setGroupUuid(lastInsertID);
            groupSetting.setOperationType(groupSettingCriteria.getOperationType());
            groupSetting.setDisableStatistics(0);
            if(groupSettingCriteria.getTerminalType().equals("PC")) {
                groupSetting.setPage(5);
            } else {
                groupSetting.setPage(3);
            }
            groupSetting.setPageSize(0);
            groupSetting.setZhanneiPercent(0);
            groupSetting.setZhanwaiPercent(0);
            groupSetting.setKuaizhaoPercent(0);
            groupSetting.setBaiduSemPercent(0);
            groupSetting.setDragPercent(0);
            groupSetting.setSpecialCharPercent(0);
            groupSetting.setMultiBrowser(1);
            groupSetting.setClearCookie(0);
            groupSetting.setEntryPageMinCount(0);
            groupSetting.setEntryPageMaxCount(0);
            groupSetting.setPageRemainMinTime(3000);
            groupSetting.setPageRemainMaxTime(5000);
            groupSetting.setInputDelayMinTime(50);
            groupSetting.setInputDelayMaxTime(80);
            groupSetting.setSlideDelayMinTime(700);
            groupSetting.setSlideDelayMaxTime(1500);
            groupSetting.setTitleRemainMinTime(1000);
            groupSetting.setTitleRemainMaxTime(3000);
            groupSetting.setOptimizeKeywordCountPerIP(1);
            groupSetting.setMaxUserCount(300);
            groupSetting.setWaitTimeAfterOpenBaidu(1000);
            groupSetting.setWaitTimeBeforeClick(1000);
            groupSetting.setWaitTimeAfterClick(5000);
            groupSetting.setJustVisitSelfPage(1);
            groupSetting.setSleepPer2Words(1);
            groupSetting.setSupportPaste(1);
            groupSetting.setMoveRandomly(1);
            groupSetting.setClearLocalStorage(1);
            groupSetting.setOptimizeRelatedKeyword(0);
            groupSetting.setMachineUsedPercent(100);
            groupSettingService.saveGroupSetting(groupSetting, false);
        }
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

    public void saveGroupsBelowOperationCombine (GroupBatchAddCriteria groupBatchAddCriteria) {
        List<Long> groupUuids = new ArrayList<>();
        List<String> notExistsGroupNames = new ArrayList<>();
        for (String groupName : groupBatchAddCriteria.getGroupNames()) {
            Long existingGroupUuid = groupDao.searchExistingGroupUuid(groupBatchAddCriteria.getTerminalType(), groupName);
            if (null != existingGroupUuid) {
                groupUuids.add(existingGroupUuid);
            } else {
                notExistsGroupNames.add(groupName);
            }
        }

        if (CollectionUtils.isNotEmpty(groupUuids)) {
            this.updateGroupsBelowOperationCombine(groupUuids, groupBatchAddCriteria.getOperationCombineUuid());
        }

        if (CollectionUtils.isNotEmpty(notExistsGroupNames)) {
            groupBatchAddCriteria.setGroupNames(notExistsGroupNames);
            groupDao.insertBatchGroups(groupBatchAddCriteria);
        }
    }

    public void updateGroupsBelowOperationCombine (List<Long> groupUuids, Long operationCombineUuid) {
        groupDao.updateGroupOperationUuid(groupUuids, operationCombineUuid);
    }
}
