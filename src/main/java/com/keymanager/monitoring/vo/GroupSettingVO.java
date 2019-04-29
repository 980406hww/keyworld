package com.keymanager.monitoring.vo;


import com.keymanager.monitoring.entity.GroupSetting;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/18 15:09
 **/
public class GroupSettingVO {

    private List<GroupResultVO> groupResultVos;

    private GroupSetting groupSetting;

    public List<GroupResultVO> getGroupResultVos () {
        return groupResultVos;
    }

    public void setGroupResultVos (List<GroupResultVO> groupResultVos) {
        this.groupResultVos = groupResultVos;
    }

    public GroupSetting getGroupSetting () {
        return groupSetting;
    }

    public void setGroupSetting (GroupSetting groupSetting) {
        this.groupSetting = groupSetting;
    }
}
