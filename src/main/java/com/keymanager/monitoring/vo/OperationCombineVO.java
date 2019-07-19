package com.keymanager.monitoring.vo;

/**
 * @Author zhoukai
 * @Date 2019/6/25 10:22
 **/
public class OperationCombineVO {

    private long groupUuid;

    private String groupName;

    public long getGroupUuid () {
        return groupUuid;
    }

    public void setGroupUuid (long groupUuid) {
        this.groupUuid = groupUuid;
    }

    public String getGroupName () {
        return groupName;
    }

    public void setGroupName (String groupName) {
        this.groupName = groupName;
    }
}
