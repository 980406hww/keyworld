package com.keymanager.monitoring.criteria;

public class QZSettingSearchCriteria{
    private String loginName;
    private String customerUuid;
    private String customerInfo;
    private String domain;
    private String group;
    private String updateStatus;
    private Integer dateRangeType;
    private Integer status;
    private Integer downNum;
    private Integer upNum;
    private double upperValue;
    private double lowerValue;
    private Boolean increaseType;
    private String terminalType;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public Integer getDateRangeType() {
        return dateRangeType;
    }

    public void setDateRangeType(Integer dateRangeType) {
        this.dateRangeType = dateRangeType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDownNum() {
        return downNum;
    }

    public void setUpperValue (double upperValue) {
        this.upperValue = upperValue;
    }

    public Integer getUpNum() {
        return upNum;
    }

    public void setLowerValue (double lowerValue) {
        this.lowerValue = lowerValue;
    }

    public Boolean getIncreaseType () {
        return increaseType;
    }

    public void setIncreaseType (Boolean increaseType) {
        this.increaseType = increaseType;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public void setDownNum(Integer downNum) {
        this.downNum = downNum;
    }

    public void setUpNum(Integer upNum) {
        this.upNum = upNum;
    }

    public double getUpperValue() {
        return upperValue;
    }

    public double getLowerValue() {
        return lowerValue;
    }
}
