package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.entity.CustomerKeyword;

public class KeywordStatusBatchUpdateVO {
    private CustomerKeyword keywordChecks;
    private CustomerKeyword keywordStatus;
    private String customerUuids;

    public CustomerKeyword getKeywordChecks() {
        return keywordChecks;
    }
    public void setKeywordChecks(CustomerKeyword keywordChecks) {
        this.keywordChecks = keywordChecks;
    }
    public CustomerKeyword getKeywordStatus() {
        return keywordStatus;
    }
    public void setKeywordStatus(CustomerKeyword keywordStatus) {
        this.keywordStatus = keywordStatus;
    }
    public String getCustomerUuids() {
        return customerUuids;
    }
    public void setCustomerUuids(String customerUuids) {
        this.customerUuids = customerUuids;
    }
    }
