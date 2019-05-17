package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.GroupBatchCriteria;
import com.keymanager.monitoring.criteria.GroupCriteria;
import com.keymanager.monitoring.criteria.GroupSettingCriteria;
import com.keymanager.monitoring.criteria.UpdateGroupSettingCriteria;
import com.keymanager.monitoring.dao.GroupDao;
import com.keymanager.monitoring.entity.Group;
import com.keymanager.monitoring.entity.GroupSetting;
import com.keymanager.monitoring.vo.GroupVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    public void batchAddGroups (GroupBatchCriteria groupBatchCriteria, String userName) {
        for (GroupSettingCriteria groupSettingCriteria : groupBatchCriteria.getOptimizationGroupList()) {
            groupDao.saveGroup(groupSettingCriteria.getOptimizedGroupName(), groupSettingCriteria.getTerminalType(), userName, 0);
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

    public List<String> getAvailableOptimizationGroups (String terminalType) {
        List<String> availableCustomerKeywordOptimizationGroups = customerKeywordService.getAvailableOptimizationGroups(terminalType);
        List<String> availableQZSettingOptimizationGroups = qzSettingService.getAvailableOptimizationGroups(terminalType);
        availableCustomerKeywordOptimizationGroups.addAll(availableQZSettingOptimizationGroups);
        Set<String> middleLinkedHashSet = new LinkedHashSet<>(availableCustomerKeywordOptimizationGroups);
        return new ArrayList<>(middleLinkedHashSet);
    }
}
