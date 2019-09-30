package com.keymanager.monitoring.vo;

import java.util.List;

/**
 * @author shunshikj40
 */
public class QZSettingForSync {

    private Long uuid;
    private Long customerUuid;
    private String domain;
    private String searchEngine;
    private String bearPawNumber;
    private Boolean renewalStatus;

    List<QZKeywordRankForSync> qzKeywordRankForSyncs;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getBearPawNumber() {
        return bearPawNumber;
    }

    public void setBearPawNumber(String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
    }

    public Boolean getRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(Boolean renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    public List<QZKeywordRankForSync> getQzKeywordRankForSyncs() {
        return qzKeywordRankForSyncs;
    }

    public void setQzKeywordRankForSyncs(List<QZKeywordRankForSync> qzKeywordRankForSyncs) {
        this.qzKeywordRankForSyncs = qzKeywordRankForSyncs;
    }
}
