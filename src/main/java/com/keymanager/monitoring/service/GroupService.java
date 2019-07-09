package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.dao.GroupDao;
import com.keymanager.monitoring.entity.Group;
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
    private CustomerKeywordService customerKeywordService;

    @Autowired
    private QZSettingService qzSettingService;

    @Autowired
    private OperationCombineService operationCombineService;

    public List<GroupVO> searchGroups (Page<GroupVO> page, GroupSettingCriteria groupSettingCriteria) {
        return groupDao.searchGroups(page, groupSettingCriteria);
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

    public List<String> getGroupNames (long operationCombineUuid) {
        return groupDao.getGroupNames(operationCombineUuid);
    }

    public List<OperationCombineVO> searchGroupsBelowOperationCombine (Long operationCombineUuid, String groupName) {
        return groupDao.searchGroupsBelowOperationCombine(operationCombineUuid, groupName);
    }

    public void saveGroupsBelowOperationCombine (OperationCombineCriteria operationCombineCriteria) {
        List<String> existingGroupNames = operationCombineService.getGroupNames(operationCombineCriteria.getOperationCombineUuid());
        Map<String, String> existingGroupNameMap = new HashMap<>(1000);
        for (String groupName : existingGroupNames) {
            existingGroupNameMap.put(groupName, groupName);
        }

        List<Long> groupUuids = new ArrayList<>();
        List<String> notExistsGroupNames = new ArrayList<>();
        for (String groupName : operationCombineCriteria.getGroupNames()) {
            String existingGroupName = existingGroupNameMap.get(groupName);
            if (null == existingGroupName) {
                Long existingGroupUuid = groupDao.searchExistingGroupUuid(operationCombineCriteria.getTerminalType(), groupName);
                if (null != existingGroupUuid) {
                    groupUuids.add(existingGroupUuid);
                } else {
                    notExistsGroupNames.add(groupName);
                }
            } else {
                existingGroupNameMap.remove(groupName);
            }
        }

        if (CollectionUtils.isNotEmpty(groupUuids)) {
            this.updateGroupsBelowOperationCombine(groupUuids, operationCombineCriteria.getOperationCombineUuid());
        }

        if (CollectionUtils.isNotEmpty(notExistsGroupNames)) {
            operationCombineCriteria.setGroupNames(notExistsGroupNames);
            groupDao.insertBatchGroups(operationCombineCriteria);
        }

        if (!operationCombineCriteria.isOnlySaveStatus() && !existingGroupNameMap.isEmpty()) {
            Collection<String> groupNames = existingGroupNameMap.values();
            groupDao.updateOperationCombineUuidByGroupName(new ArrayList<String>(groupNames), operationCombineCriteria.getOperationCombineUuid());
        }
    }

    public void updateGroupsBelowOperationCombine (List<Long> groupUuids, Long operationCombineUuid) {
        groupDao.updateGroupOperationUuid(groupUuids, operationCombineUuid);
    }

    public void updateGroupOperationCombineUuid (long operationCombineUuid) {
        groupDao.updateGroupOperationCombineUuid(operationCombineUuid);
    }

    public void updateQZSettingGroupOperationCombineUuid (Long operationCombineUuid, String groupName) {
        groupDao.updateQZSettingGroupOperationCombineUuid(operationCombineUuid, groupName);
    }

    public void determineAddingGroupInfo (String tmpOptimizeGroupName) {
        Group existingGroup = groupDao.findExistingGroup(tmpOptimizeGroupName);
        if (null == existingGroup) {
            Group group = groupDao.findExistingGroup(tmpOptimizeGroupName.substring(0, tmpOptimizeGroupName.lastIndexOf("_")));
            group.setGroupName(tmpOptimizeGroupName);
            groupDao.insert(group);
        }
    }
}
