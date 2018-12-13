package com.keymanager.monitoring.criteria;

import com.keymanager.util.Constants;

public class QZSettingSearchCriteria{
    private String loginName;
    private String customerUuid;
    private String customerInfo;
    private String domain;
    private String group;
    private String updateStatus;
    private Integer dateRangeType;
    private Integer status;
    private double upperValue = Constants.QZSETTING_KEYWORD_RANK_UPPER_VALUE;
    private double lowerValue = Constants.QZSETTING_KEYWORD_RANK_LOWER_VALUE;
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

    public double getUpperValue () {
        return upperValue;
    }

    public void setUpperValue (double upperValue) {
        this.upperValue = upperValue;
    }

    public double getLowerValue () {
        return lowerValue;
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
}
