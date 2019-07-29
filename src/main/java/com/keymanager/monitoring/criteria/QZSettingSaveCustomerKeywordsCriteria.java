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
    private String machineGroupName;
    private String type;
    private String searchEngine;
    private String bearPawNumber;
    private String keywordEffect;
    private List<String> terminalTypes;
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

    public String getMachineGroupName() {
        return machineGroupName;
    }

    public void setMachineGroupName(String machineGroupName) {
        this.machineGroupName = machineGroupName;
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

    public String getBearPawNumber () {
        return bearPawNumber;
    }

    public void setBearPawNumber (String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
    }

    public String getKeywordEffect() {
        return keywordEffect;
    }

    public void setKeywordEffect(String keywordEffect) {
        this.keywordEffect = keywordEffect;
    }

    public List<String> getTerminalTypes () {
        return terminalTypes;
    }

    public void setTerminalTypes (List<String> terminalTypes) {
        this.terminalTypes = terminalTypes;
    }

    public List<String> getKeywords () {
        return keywords;
    }

    public void setKeywords (List<String> keywords) {
        this.keywords = keywords;
    }
}
