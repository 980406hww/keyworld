package com.keymanager.monitoring.vo;

public class GroupVO {

    private long Uuid;

    private String groupName;

    public long getUuid() {
        return Uuid;
    }

    public void setUuid(long uuid) {
        Uuid = uuid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
