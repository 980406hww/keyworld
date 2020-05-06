package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

public class UserRefreshStatisticCriteria extends BaseCriteria {

    private String entryType;

    private String groupName;

    private String customerName;

    private String configValue;

    private String terminalType;

    private boolean fullMatchGroup;

   private String historyDate;

    private String categoryTag;

    private String userName;

    private String password;

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public boolean isFullMatchGroup() {
        return fullMatchGroup;
    }

    public void setFullMatchGroup(boolean fullMatchGroup) {
        this.fullMatchGroup = fullMatchGroup;
    }

    public String getCategoryTag() {
        return categoryTag;
    }

    public void setCategoryTag(String categoryTag) {
        this.categoryTag = categoryTag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(String historyDate) {
        this.historyDate = historyDate;
    }
}
