package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.entity.GroupSetting;
import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/4/29 18:20
 **/
public class OperationCombineCriteria {

    private long operationCombineUuid;

    private String operationCombineName;

    private List<String> groupNames;

    private String terminalType;

    private String creator;

    private int maxInvalidCount;

    private int remainingAccount;

    private boolean onlySaveStatus;

    private String searchEngine;



    private GroupSetting groupSetting;

    public long getOperationCombineUuid () {
        return operationCombineUuid;
    }

    public void setOperationCombineUuid (long operationCombineUuid) {
        this.operationCombineUuid = operationCombineUuid;
    }

    public String getOperationCombineName () {
        return operationCombineName;
    }

    public void setOperationCombineName (String operationCombineName) {
        this.operationCombineName = operationCombineName;
    }

    public List<String> getGroupNames () {
        return groupNames;
    }

    public void setGroupNames (List<String> groupNames) {
        this.groupNames = groupNames;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public String getCreator () {
        return creator;
    }

    public void setCreator (String creator) {
        this.creator = creator;
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

    public boolean isOnlySaveStatus () {
        return onlySaveStatus;
    }

    public void setOnlySaveStatus (boolean onlySaveStatus) {
        this.onlySaveStatus = onlySaveStatus;
    }

    public GroupSetting getGroupSetting () {
        return groupSetting;
    }

    public void setGroupSetting (GroupSetting groupSetting) {
        this.groupSetting = groupSetting;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }
}
