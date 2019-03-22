package com.keymanager.monitoring.criteria;

public class CustomerKeywordRefreshStatInfoCriteria extends BaseCriteria {
    private String entryType;

    private String groupName;

    private String customerName;

    private String configValue;

    private String terminalType;

    private boolean fullMatchGroup;

    private int dayNum;

    private String categoryTag;

    public int getDayNum () {
        return dayNum > 0 ? dayNum : 0;
    }

    public void setDayNum (int dayNum) {
        this.dayNum = dayNum;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
}
