package com.keymanager.monitoring.vo;

public class QZKeywordRankForSync {

    private Long uuid;
    private Long qzSettingUuid;
    private String terminalType;
    private String websiteType;
    private Boolean dataProcessingStatus;
    private String topTen;
    private String topTwenty;
    private String topThirty;
    private String topForty;
    private String topFifty;
    private String topHundred;
    private String date;
    private Double increase;
    private Integer todayDifference;
    private Integer oneWeekDifference;
    private String recordDate;
    private String record;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getQzSettingUuid() {
        return qzSettingUuid;
    }

    public void setQzSettingUuid(Long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getWebsiteType() {
        return websiteType;
    }

    public void setWebsiteType(String websiteType) {
        this.websiteType = websiteType;
    }

    public Boolean getDataProcessingStatus() {
        return dataProcessingStatus;
    }

    public void setDataProcessingStatus(Boolean dataProcessingStatus) {
        this.dataProcessingStatus = dataProcessingStatus;
    }

    public String getTopTen() {
        return topTen;
    }

    public void setTopTen(String topTen) {
        this.topTen = topTen;
    }

    public String getTopTwenty() {
        return topTwenty;
    }

    public void setTopTwenty(String topTwenty) {
        this.topTwenty = topTwenty;
    }

    public String getTopThirty() {
        return topThirty;
    }

    public void setTopThirty(String topThirty) {
        this.topThirty = topThirty;
    }

    public String getTopForty() {
        return topForty;
    }

    public void setTopForty(String topForty) {
        this.topForty = topForty;
    }

    public String getTopFifty() {
        return topFifty;
    }

    public void setTopFifty(String topFifty) {
        this.topFifty = topFifty;
    }

    public String getTopHundred() {
        return topHundred;
    }

    public void setTopHundred(String topHundred) {
        this.topHundred = topHundred;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getIncrease() {
        return increase;
    }

    public void setIncrease(Double increase) {
        this.increase = increase;
    }

    public Integer getTodayDifference() {
        return todayDifference;
    }

    public void setTodayDifference(Integer todayDifference) {
        this.todayDifference = todayDifference;
    }

    public Integer getOneWeekDifference() {
        return oneWeekDifference;
    }

    public void setOneWeekDifference(Integer oneWeekDifference) {
        this.oneWeekDifference = oneWeekDifference;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
