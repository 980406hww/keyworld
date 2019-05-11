package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.GroupCriteria;
import com.keymanager.monitoring.dao.GroupDao;
import com.keymanager.monitoring.dao.GroupSettingDao;
import com.keymanager.monitoring.entity.Group;
import com.keymanager.monitoring.entity.GroupSetting;
import com.keymanager.monitoring.vo.GroupResultVO;
import com.keymanager.monitoring.vo.GroupSettingVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/4/27 9:42
 **/
@Service
public class GroupService extends ServiceImpl<GroupDao, Group> {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private GroupSettingDao groupSettingDao;

    public void saveGroup (GroupCriteria groupCriteria) {
        groupDao.saveGroup(groupCriteria.getGroupName(), groupCriteria.getTerminalType(), groupCriteria.getCreateBy(), groupCriteria.getGroupSetting().getRemainingAccount());
        long lastInsertID = groupDao.lastInsertID();
        groupCriteria.getGroupSetting().setGroupUuid(lastInsertID);
        groupSettingDao.saveGroupSetting(groupCriteria.getGroupSetting());
    }

    public void deleteGroup (long uuid) {
        groupDao.deleteById(uuid);
        groupSettingDao.deleteByGroupUuid(uuid);
    }

    public GroupSettingVO findGroupSettings (long groupUuid) {
        List<GroupSetting> groupSettings = groupSettingDao.searchGroupSettings(groupUuid, null);
        GroupSettingVO groupSettingVo = new GroupSettingVO();
        if (CollectionUtils.isNotEmpty(groupSettings)) {
            List<GroupResultVO> groupResultVos = new ArrayList<>();
           for (GroupSetting groupSetting : groupSettings) {
               GroupResultVO groupResultVo = new GroupResultVO();
               groupResultVo.setUuid(groupSetting.getUuid());
               groupResultVo.setOperationType(groupSetting.getOperationType());
               groupResultVos.add(groupResultVo);
           }
           groupSettingVo.setGroupResultVos(groupResultVos);
           groupSettingVo.setGroupSetting(groupSettings.get(0));
        }
        return groupSettingVo;
    }

}
