package com.keymanager.monitoring.criteria;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/6/25 15:49
 **/
public class GroupBatchAddCriteria {

    private List<String> groupNames;

    private long operationCombineUuid;

    private String terminalType;

    private int maxInvalidCount;

    private int remainingAccount;

    private String createBy;

    public List<String> getGroupNames () {
        return groupNames;
    }

    public void setGroupNames (List<String> groupNames) {
        this.groupNames = groupNames;
    }

    public long getOperationCombineUuid () {
        return operationCombineUuid;
    }

    public void setOperationCombineUuid (long operationCombineUuid) {
        this.operationCombineUuid = operationCombineUuid;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public int getMaxInvalidCount () {
        return maxInvalidCount;
    }

    public void setMaxInvalidCount (int maxInvalidCount) {
        this.maxInvalidCount = maxInvalidCount;
    }

    public int getRemainingAccount () {
        return remainingAccount;
    }

    public void setRemainingAccount (int remainingAccount) {
        this.remainingAccount = remainingAccount;
    }

    public String getCreateBy () {
        return createBy;
    }

    public void setCreateBy (String createBy) {
        this.createBy = createBy;
    }
}
