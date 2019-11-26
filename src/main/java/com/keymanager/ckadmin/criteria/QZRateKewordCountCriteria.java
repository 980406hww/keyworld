package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;
import java.util.List;

public class QZRateKewordCountCriteria extends BaseCriteria {

    private String terminalType;
    private List<Long> qzUuids;
    private String searchEngine;
    private Integer qzrateRange;
    private String todayDate;
    private String userID;
    private Integer customerUuid;
    private String domain;

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public List<Long> getQzUuids() {
        return qzUuids;
    }

    public void setQzUuids(List<Long> qzUuids) {
        this.qzUuids = qzUuids;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public Integer getQzrateRange() {
        return qzrateRange;
    }

    public void setQzrateRange(Integer qzrateRange) {
        this.qzrateRange = qzrateRange;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Integer getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Integer customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
