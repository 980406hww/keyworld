package com.keymanager.monitoring.criteria;

import java.util.List;

public class QZSettingExcludeCustomerKeywordsCriteria {
    private long excludeKeywordUuid;
    private long customerUuid;
    private long qzSettingUuid;
    private String domain;
    private String optimizeGroupName;
    private String type;
    private String terminalType;
    private List<String> keywords;

    public long getExcludeKeywordUuid() {
        return excludeKeywordUuid;
    }

    public void setExcludeKeywordUuid(long excludeKeywordUuid) {
        this.excludeKeywordUuid = excludeKeywordUuid;
    }

    public long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public long getQzSettingUuid() {
        return qzSettingUuid;
    }

    public void setQzSettingUuid(long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public String getDomain () {
        return domain;
    }

    public void setDomain (String domain) {
        this.domain = domain;
    }

    public String getOptimizeGroupName () {
        return optimizeGroupName;
    }

    public void setOptimizeGroupName (String optimizeGroupName) {
        this.optimizeGroupName = optimizeGroupName;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public List<String> getKeywords () {
        return keywords;
    }

    public void setKeywords (List<String> keywords) {
        this.keywords = keywords;
    }
}
