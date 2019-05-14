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

    @Autowired
    private QZSettingService qzSettingService;

    @Autowired
    private QZSettingDao qzSettingDao;

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public Page<GroupVO> searchGroupSettings(Page<GroupVO> page, GroupSettingCriteria groupSettingCriteria) {
        page.setRecords(groupService.searchGroups(page, groupSettingCriteria));
        for (GroupVO groupVo : page.getRecords()) {
            List<GroupSetting> groupSettings = groupSettingDao.searchGroupSettings(groupVo.getUuid(), groupSettingCriteria.getOperationType());
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

    public void saveGroupSetting (GroupSetting groupSetting) {
        groupSettingDao.saveGroupSetting(groupSetting);
        groupService.updateGroupRemainingAccount(groupSetting.getGroupUuid(), groupSetting.getRemainingAccount());
    }

    public void updateGroupSetting (UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        groupSettingDao.updateGroupSetting(updateGroupSettingCriteria.getGs(), updateGroupSettingCriteria.getGroupSetting());
        if (1 == updateGroupSettingCriteria.getGs().getMachineUsedPercent()) {
            groupService.updateGroupRemainingAccount(updateGroupSettingCriteria.getGroupSetting().getGroupUuid(), updateGroupSettingCriteria.getGroupSetting().getRemainingAccount());
        }
    }

    public Map<String, Date> getAvailableOptimizationGroups (String terminalType) {
        List<Group> availableCustomerKeywordOptimizationGroups = qzSettingService.getAvailableOptimizationGroups(terminalType);
        Map<String, Date> groupMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(availableCustomerKeywordOptimizationGroups)) {
            for (Group group : availableCustomerKeywordOptimizationGroups) {
                groupMap.put(group.getGroupName(), group.getCreateTime());
            }
        }
        List<Group> availableQZSettingOptimizationGroups = customerKeywordService.getAvailableOptimizationGroups(terminalType);
        if (CollectionUtils.isNotEmpty(availableQZSettingOptimizationGroups)) {
            for (Group group : availableQZSettingOptimizationGroups) {
                if (null != group.getGroupName() && !groupMap.containsKey(group.getGroupName())){
                    groupMap.put(group.getGroupName(), group.getCreateTime());
                }
            }
        }
        return groupMap;
    }

    public GroupSetting getGroupSettingViaPercentage(String groupName, String terminalType){
        Group group = groupService.findGroup(groupName, terminalType);
        if(group != null){
            List<GroupSetting> groupSettings = groupSettingDao.searchGroupSettingsSortingPercentage(group.getUuid());
            if(CollectionUtils.isNotEmpty(groupSettings)){
                Random ra = new Random();
                int randomValue = ra.nextInt(100 - group.getRemainingAccount()) + 1;
                int totalPercentage = 0;
                for(GroupSetting groupSetting : groupSettings){
                    totalPercentage = totalPercentage + groupSetting.getMachineUsedPercent();
                    if(randomValue <= totalPercentage){
                        return groupSetting;
                    }
                }
            }
        }
        return null;
    }
}
