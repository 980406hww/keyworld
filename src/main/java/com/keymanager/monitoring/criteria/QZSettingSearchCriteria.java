package com.keymanager.monitoring.criteria;

public class QZSettingSearchCriteria{
    private String customerUuid;
    private String customerInfo;
    private String domain;
    private String group;
    private String updateStatus;
    private String dateRangeType;

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

    public String getDateRangeType() {
        return dateRangeType;
    }

    public void setDateRangeType(String dateRangeType) {
        this.dateRangeType = dateRangeType;
    }
}
