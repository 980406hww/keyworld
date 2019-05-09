package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.entity.GroupSetting;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/18 15:09
 **/
public class GroupVO {

    private long uuid;

    private String userName;

    private String groupName;

    private List<GroupSetting> groupSettings;

    public long getUuid () {
        return uuid;
    }

    public void setUuid (long uuid) {
        this.uuid = uuid;
    }

    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    public String getGroupName () {
        return groupName;
    }

    public void setGroupName (String groupName) {
        this.groupName = groupName;
    }

    public List<GroupSetting> getGroupSettings () {
        return groupSettings;
    }

    public void setGroupSettings (List<GroupSetting> groupSettings) {
        this.groupSettings = groupSettings;
    }
}
