package com.keymanager.monitoring.vo;

import java.util.List;

/**
 * @author shunshikj40
 */
public class QZSettingForSync {

    private Long qsId;
    private Long customerId;
    private String domain;
    private String searchEngine;
    private String bearPawNumber;
    private Boolean renewalStatus;
    List<QZKeywordRankForSync> qzKeywordRanks;

    public Long getQsId() {
        return qsId;
    }

    public void setQsId(Long qsId) {
        this.qsId = qsId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public List<QZKeywordRankForSync> getQzKeywordRanks() {
        return qzKeywordRanks;
    }

    public void setQzKeywordRanks(List<QZKeywordRankForSync> qzKeywordRanks) {
        this.qzKeywordRanks = qzKeywordRanks;
    }
}
