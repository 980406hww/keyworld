package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

public class RefreshStatisticsCriteria extends BaseCriteria {

    private String entryType;

    private String historyDate;

    private String groupName;

    private String machineGroup;

    private String customerName;

    private String configValue;

    private String terminalType;

    private boolean fullMatchGroup;

    private String categoryTag;

    private String userName;

    private String password;

    private Integer gtInvalidDays; //无效天数起始
    private Integer ltInvalidDays; //无效天数指数结束

    private Integer optimizeStatus; //操作状态

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getMachineGroup() {
        return machineGroup;
    }

    public void setMachineGroup(String machineGroup) {
        this.machineGroup = machineGroup;
    }

    public String getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(String historyDate) {
        this.historyDate = historyDate;
    }

    public Integer getGtInvalidDays() {
        return gtInvalidDays;
    }

    public void setGtInvalidDays(Integer gtInvalidDays) {
        this.gtInvalidDays = gtInvalidDays;
    }

    public Integer getLtInvalidDays() {
        return ltInvalidDays;
    }

    public void setLtInvalidDays(Integer ltInvalidDays) {
        this.ltInvalidDays = ltInvalidDays;
    }

    public Integer getOptimizeStatus() {
        return optimizeStatus;
    }

    public void setOptimizeStatus(Integer optimizeStatus) {
        this.optimizeStatus = optimizeStatus;
    }
}
