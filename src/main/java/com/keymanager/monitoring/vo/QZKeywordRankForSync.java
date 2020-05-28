package com.keymanager.monitoring.vo;

import java.io.Serializable;

public class QZKeywordRankForSync implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long qkId;
    private Long qsId;
    private String terminalType;
    private String websiteType;
    private Boolean processingType;
    private String topTen;
    private String topTwenty;
    private String topThirty;
    private String topForty;
    private String topFifty;
    private String topHundred;
    private String date;
    private Double increase;
    private Integer todayDiffVal;
    private Integer oneWeekDiffVal;
    private String recordDate;
    private String record;
    private Integer initTopTenNum;
    private Integer initTopFiftyNum;

    public Long getQkId() {
        return qkId;
    }

    public void setQkId(Long qkId) {
        this.qkId = qkId;
    }

    public Long getQsId() {
        return qsId;
    }

    public void setQsId(Long qsId) {
        this.qsId = qsId;
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

    public Boolean getProcessingType() {
        return processingType;
    }

    public void setProcessingType(Boolean processingType) {
        this.processingType = processingType;
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

    public Integer getTodayDiffVal() {
        return todayDiffVal;
    }

    public void setTodayDiffVal(Integer todayDiffVal) {
        this.todayDiffVal = todayDiffVal;
    }

    public Integer getOneWeekDiffVal() {
        return oneWeekDiffVal;
    }

    public void setOneWeekDiffVal(Integer oneWeekDiffVal) {
        this.oneWeekDiffVal = oneWeekDiffVal;
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

    public Integer getInitTopTenNum() {
        return initTopTenNum;
    }

    public void setInitTopTenNum(Integer initTopTenNum) {
        this.initTopTenNum = initTopTenNum;
    }

    public Integer getInitTopFiftyNum() {
        return initTopFiftyNum;
    }

    public void setInitTopFiftyNum(Integer initTopFiftyNum) {
        this.initTopFiftyNum = initTopFiftyNum;
    }
}
