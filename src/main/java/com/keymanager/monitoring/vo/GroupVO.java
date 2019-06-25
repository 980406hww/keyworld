package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.entity.GroupSetting;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/18 15:09
 **/
public class GroupVO {

    private long uuid; // 操作组合ID

    private String userName; // 分组创建者

    private String operationCombineName; // 操作组合名

    private int remainingAccount; // 剩余百分比

    private int maxInvalidCount; // 最大点击次数

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

    public String getOperationCombineName () {
        return operationCombineName;
    }

    public void setOperationCombineName (String operationCombineName) {
        this.operationCombineName = operationCombineName;
    }

    public int getRemainingAccount () {
        return remainingAccount;
    }

    public void setRemainingAccount (int remainingAccount) {
        this.remainingAccount = remainingAccount;
    }

    public List<GroupSetting> getGroupSettings () {
        return groupSettings;
    }

    public void setGroupSettings (List<GroupSetting> groupSettings) {
        this.groupSettings = groupSettings;
    }

    public int getMaxInvalidCount() {
        return maxInvalidCount;
    }

    public void setMaxInvalidCount(int maxInvalidCount) {
        this.maxInvalidCount = maxInvalidCount;
    }
}
