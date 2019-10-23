package com.keymanager.ckadmin.criteria;

import java.util.List;

/**
 * @ClassName QZRateStatisticsCountCriteria
 * @Description TODO
 * @Author lhc
 * @Date 2019/10/23 11:04
 * @Version 1.0
 */
public class QZRateStatisticsCountCriteria {

    private Integer qzRange = -7;
    private String ltRateFullDate;
    private String gtRateFullDate;
    private String terminalType;
    private String searchEngine;
    private String userID;
    private List<Long> qzUuids;

    public Integer getQzRange() {
        return qzRange;
    }

    public void setQzRange(Integer qzRange) {
        this.qzRange = qzRange;
    }

    public String getLtRateFullDate() {
        return ltRateFullDate;
    }

    public void setLtRateFullDate(String ltRateFullDate) {
        this.ltRateFullDate = ltRateFullDate;
    }

    public String getGtRateFullDate() {
        return gtRateFullDate;
    }

    public void setGtRateFullDate(String gtRateFullDate) {
        this.gtRateFullDate = gtRateFullDate;
    }

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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }
}
