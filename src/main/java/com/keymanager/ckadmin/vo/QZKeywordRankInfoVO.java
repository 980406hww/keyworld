package com.keymanager.ckadmin.vo;

import java.util.Date;

public class QZKeywordRankInfoVO {

    private long uuid;
    private long qzSettingUuid;
    private String terminalType;
    private String websiteType;
    private boolean dataProcessingStatus;
    private String[] topTen;
    private String[] topTwenty;
    private String[] topThirty;
    private String[] topForty;
    private String[] topFifty;
    private String[] topHundred;
    private String[] date;
    private int createTopTenNum;
    private int createTopFiftyNum;
    private int topTenNum;
    private int topFiftyNum;
    private String[] baiduRecord;
    private String[] baiduRecordFullDate;
    private Date achieveTime;

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public long getQzSettingUuid() {
        return qzSettingUuid;
    }

    public void setQzSettingUuid(long qzSettingUuid) {
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

    public boolean isDataProcessingStatus() {
        return dataProcessingStatus;
    }

    public void setDataProcessingStatus(boolean dataProcessingStatus) {
        this.dataProcessingStatus = dataProcessingStatus;
    }

    public String[] getTopTen() {
        return topTen;
    }

    public void setTopTen(String[] topTen) {
        this.topTen = topTen;
    }

    public String[] getTopTwenty() {
        return topTwenty;
    }

    public void setTopTwenty(String[] topTwenty) {
        this.topTwenty = topTwenty;
    }

    public String[] getTopThirty() {
        return topThirty;
    }

    public void setTopThirty(String[] topThirty) {
        this.topThirty = topThirty;
    }

    public String[] getTopForty() {
        return topForty;
    }

    public void setTopForty(String[] topForty) {
        this.topForty = topForty;
    }

    public String[] getTopFifty() {
        return topFifty;
    }

    public void setTopFifty(String[] topFifty) {
        this.topFifty = topFifty;
    }

    public String[] getTopHundred() {
        return topHundred;
    }

    public void setTopHundred(String[] topHundred) {
        this.topHundred = topHundred;
    }

    public String[] getDate() {
        return date;
    }

    public void setDate(String[] date) {
        this.date = date;
    }

    public int getCreateTopTenNum() {
        return createTopTenNum;
    }

    public void setCreateTopTenNum(int createTopTenNum) {
        this.createTopTenNum = createTopTenNum;
    }

    public int getCreateTopFiftyNum() {
        return createTopFiftyNum;
    }

    public void setCreateTopFiftyNum(int createTopFiftyNum) {
        this.createTopFiftyNum = createTopFiftyNum;
    }

    public int getTopTenNum() {
        return topTenNum;
    }

    public void setTopTenNum(int topTenNum) {
        this.topTenNum = topTenNum;
    }

    public int getTopFiftyNum() {
        return topFiftyNum;
    }

    public void setTopFiftyNum(int topFiftyNum) {
        this.topFiftyNum = topFiftyNum;
    }

    public String[] getBaiduRecord() {
        return baiduRecord;
    }

    public void setBaiduRecord(String[] baiduRecord) {
        this.baiduRecord = baiduRecord;
    }

    public String[] getBaiduRecordFullDate() {
        return baiduRecordFullDate;
    }

    public void setBaiduRecordFullDate(String[] baiduRecordFullDate) {
        this.baiduRecordFullDate = baiduRecordFullDate;
    }

    public Date getAchieveTime() {
        return achieveTime;
    }

    public void setAchieveTime(Date achieveTime) {
        this.achieveTime = achieveTime;
    }
}
