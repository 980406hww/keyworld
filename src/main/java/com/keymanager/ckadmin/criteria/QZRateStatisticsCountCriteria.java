package com.keymanager.ckadmin.criteria;

public class QZRateStatisticsCountCriteria {

    private Integer qzRateRange;
    private String ltRateFullDate;
    private String gtRateFullDate;
    private String terminalType;
    private String searchEngine;
    private String userID;

    public Integer getQzRateRange() {
        return qzRateRange;
    }

    public void setQzRateRange(Integer qzRateRange) {
        this.qzRateRange = qzRateRange;
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
