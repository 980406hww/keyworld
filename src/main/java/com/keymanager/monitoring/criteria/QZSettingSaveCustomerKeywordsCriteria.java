package com.keymanager.monitoring.criteria;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/24 11:48
 **/
public class QZSettingSaveCustomerKeywordsCriteria {
    private long qzSettingUuid;
    private long customerUuid;
    private String domain;
    private String optimizeGroupName;
    private String type;
    private String searchEngine;
    private String terminalType;
    private List<String> keywords;

    public long getQzSettingUuid () {
        return qzSettingUuid;
    }

    public void setQzSettingUuid (long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public long getCustomerUuid () {
        return customerUuid;
    }

    public void setCustomerUuid (long customerUuid) {
        this.customerUuid = customerUuid;
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

    public String getSearchEngine () {
        return searchEngine;
    }

    public void setSearchEngine (String searchEngine) {
        this.searchEngine = searchEngine;
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
