package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.GroupSettingCriteria;
import com.keymanager.monitoring.criteria.UpdateGroupSettingCriteria;
import com.keymanager.monitoring.dao.GroupDao;
import com.keymanager.monitoring.dao.GroupSettingDao;
import com.keymanager.monitoring.entity.GroupSetting;
import com.keymanager.monitoring.vo.GroupVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupSettingService extends ServiceImpl<GroupSettingDao, GroupSetting> {

    private static Logger logger = LoggerFactory.getLogger(GroupSettingService.class);

    @Autowired
    private GroupSettingDao groupSettingDao;

    @Autowired
    private GroupDao groupDao;

    public Page<GroupVO> searchGroupSettings(Page<GroupVO> page, GroupSettingCriteria groupSettingCriteria) {
        page.setRecords(groupDao.searchGroups(page, groupSettingCriteria));
        for (GroupVO groupVo : page.getRecords()) {
            List<GroupSetting> groupSettings = groupSettingDao.searchGroupSettings(groupVo.getUuid(), groupSettingCriteria.getOperationType());
            groupVo.setGroupSettings(groupSettings);
        }
        return page;
    }

    public void deleteGroupSetting (long uuid) {
        groupSettingDao.deleteById(uuid);
    }

    public GroupSetting findGroupSetting (long uuid) {
        return groupSettingDao.selectById(uuid);
    }

    public void saveGroupSetting (GroupSetting groupSetting) {
        groupSettingDao.saveGroupSetting(groupSetting);
    }

    public void updateGroupSetting (UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        groupSettingDao.updateGroupSetting(updateGroupSettingCriteria.getGs(), updateGroupSettingCriteria.getGroupSetting());
    }
}
