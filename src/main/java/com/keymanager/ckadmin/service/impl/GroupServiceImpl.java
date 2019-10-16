package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.keymanager.ckadmin.criteria.GroupSettingCriteria;
import com.keymanager.ckadmin.criteria.OperationCombineCriteria;
import com.keymanager.ckadmin.dao.GroupDao;
import com.keymanager.ckadmin.entity.Group;
import com.keymanager.ckadmin.entity.OperationCombine;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.GroupService;
import com.keymanager.ckadmin.service.OperationCombineService;
import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.ckadmin.vo.GroupVO;
import com.keymanager.ckadmin.vo.OperationCombineVO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("groupService2")
public class GroupServiceImpl extends ServiceImpl<GroupDao, Group> implements GroupService {

    @Resource(name = "groupDao2")
    private GroupDao groupDao;

    @Resource(name = "operationCombineService2")
    private OperationCombineService operationCombineService;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    @Override
    public void updateGroupOperationCombineUuid(Long operationCombineUuid, List<String> groupNames,
        String loginName) {
        for (String groupName : groupNames) {
            Group group = groupDao.findGroupByGroupName(groupName);
            if (null == group) {
                group = new Group();
                group.setGroupName(groupName);
                group.setOperationCombineUuid(operationCombineUuid);
                group.setCreateBy(loginName);
                groupDao.insert(group);
            } else {
                group.setOperationCombineUuid(operationCombineUuid);
                group.setUpdateTime(new Date());
                groupDao.updateById(group);
            }
        }
    }

    @Override
    public void deleteByGroupName(String groupName) {
        groupDao.deleteByGroupName(groupName);
    }

    @Override
    public List<String> getGroupNames (long operationCombineUuid) {
        return groupDao.getGroupNames(operationCombineUuid);
    }

    @Override
    public List<OperationCombineVO> searchGroupsBelowOperationCombine (Long operationCombineUuid, String groupName) {
        return groupDao.searchGroupsBelowOperationCombine(operationCombineUuid, groupName);
    }

    @Override
    public void updateGroupsBelowOperationCombine (List<Long> groupUuids, Long operationCombineUuid) {
        groupDao.updateGroupOperationUuid(groupUuids, operationCombineUuid);
    }

    @Override
    public void saveGroupsBelowOperationCombine (OperationCombineCriteria operationCombineCriteria) {
        List<String> existingGroupNames = operationCombineService.getGroupNames(operationCombineCriteria.getOperationCombineUuid());
        Map<String, String> existingGroupNameMap = new HashMap<>();
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
            groupDao.deleteGroupByGroupName(operationCombineCriteria.getOperationCombineUuid(), new ArrayList<String>(existingGroupNameMap.values()));
        }
    }

    @Override
    public List<GroupVO> getGroupsByOperationCombineUuid(Long operationCombineUuid, String groupName) {
        return groupDao.getGroupsByOperationCombineUuid(operationCombineUuid, groupName);
    }

    @Override
    public void deleteGroupsBelowOperationCombine(List<Long> groupUuids) {
        groupDao.deleteBatchIds(groupUuids);
    }

    @Override
    public List<GroupVO> getAvailableOptimizationGroups (GroupSettingCriteria groupSettingCriteria) {
        List<GroupVO> availableOptimizationGroups;
        if (groupSettingCriteria.getOptimizedGroupNameSearchSource()){
            availableOptimizationGroups = customerKeywordService.getAvailableOptimizationGroups(groupSettingCriteria);
            List<String> stopArray = Arrays.asList("stop", "zanting", "ZANTING", "sotp");
            Iterator<GroupVO> iterator = availableOptimizationGroups.iterator();
            while (iterator.hasNext()) {
                GroupVO s = iterator.next();
                if (stopArray.contains(s.getGroupName())||s.getGroupName().startsWith("no_")) {
                    iterator.remove();//使用迭代器的删除方法删除
                }
            }

        }else {
            availableOptimizationGroups = qzSettingService.getAvailableOptimizationGroups(groupSettingCriteria);
        }
        List<GroupVO> optimizationGroups = groupDao.getOptimizationGroups(groupSettingCriteria.getTerminalType());
        for (GroupVO g : optimizationGroups){
            Iterator<GroupVO> iterator = availableOptimizationGroups.iterator();
            while (iterator.hasNext()) {
                GroupVO s = iterator.next();
                if (g.getGroupName().equals(s.getGroupName())) {
                    iterator.remove();//使用迭代器的删除方法删除
                }
            }
        }
        return availableOptimizationGroups;
    }

    @Override
    public void batchAddGroups (OperationCombineCriteria operationCombineCriteria) {
        OperationCombine operationCombine = operationCombineService.selectById(operationCombineCriteria.getOperationCombineUuid());
        operationCombineCriteria.setTerminalType(operationCombine.getTerminalType());
        operationCombineCriteria.setMaxInvalidCount(operationCombine.getMaxInvalidCount());
        this.saveGroupsBelowOperationCombine(operationCombineCriteria);
    }

    @Override
    public List<String> getAllGroupNames() {
        return groupDao.getAllGroupNames();
    }
}
