package com.keymanager.monitoring.criteria;

import java.util.List;

public class CustomerKeywordCleanCriteria {
    private String cleanType;
    private String customerUuid;
    private List<String> customerKeywordUuids;
    private String terminalType;
    private String entryType;

    public String getCleanType() {
        return cleanType;
    }

    public void setCleanType(String cleanType) {
        this.cleanType = cleanType;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
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
