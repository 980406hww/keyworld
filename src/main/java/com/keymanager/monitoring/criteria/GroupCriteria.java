package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.GroupSetting;

/**
 * @Author zhoukai
 * @Date 2019/4/29 18:20
 **/
public class GroupCriteria {

    private String groupName;

    private String terminalType;

    private String createBy;

    private GroupSetting groupSetting;

    public String getGroupName () {
        return groupName;
    }

    public void setGroupName (String groupName) {
        this.groupName = groupName;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public String getCreateBy () {
        return createBy;
    }

    public void setCreateBy (String createBy) {
        this.createBy = createBy;
    }

    public GroupSetting getGroupSetting () {
        return groupSetting;
    }

    public void setGroupSetting (GroupSetting groupSetting) {
        this.groupSetting = groupSetting;
    }
}
