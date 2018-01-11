package com.keymanager.monitoring.criteria;

import java.util.List;

public class CustomerKeywordUpdateCriteria {
    private Long customerUuid;
    private String targetGroupName;
    private String targetSearchEngine;
    private List<String> customerKeywordUuids;
    private String terminalType;
    private String entryType;

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getTargetGroupName() {
        return targetGroupName;
    }

    public void setTargetGroupName(String targetGroupName) {
        this.targetGroupName = targetGroupName;
    }

    public String getTargetSearchEngine() {
        return targetSearchEngine;
    }

    public void setTargetSearchEngine(String targetSearchEngine) {
        this.targetSearchEngine = targetSearchEngine;
    }

    public List<String> getCustomerKeywordUuids() {
        return customerKeywordUuids;
    }

    public void setCustomerKeywordUuids(List<String> customerKeywordUuids) {
        this.customerKeywordUuids = customerKeywordUuids;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }
}
