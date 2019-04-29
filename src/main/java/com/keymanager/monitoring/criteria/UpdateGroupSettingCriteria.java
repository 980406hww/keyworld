package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.GroupSetting;

/**
 * @Author zhoukai
 * @Date 2019/4/29 15:01
 **/
public class UpdateGroupSettingCriteria {

    private GroupSetting gs;

    private GroupSetting groupSetting;

    public GroupSetting getGs () {
        return gs;
    }

    public void setGs (GroupSetting gs) {
        this.gs = gs;
    }

    public GroupSetting getGroupSetting () {
        return groupSetting;
    }

    public void setGroupSetting (GroupSetting groupSetting) {
        this.groupSetting = groupSetting;
    }
}
