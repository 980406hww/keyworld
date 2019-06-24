package com.keymanager.monitoring.vo;

public class ExternalQzKeywordRankInfoVO {
    private String terminalType;
    private String websiteType;
    private boolean dataProcessingStatus;
    private String topTen;
    private String topTwenty;
    private String topThirty;
    private String topForty;
    private String topFifty;
    private String topHundred;
    private String date;
    private String fullDate;
    private Integer createTopTenNum;
    private Integer createTopFiftyNum;
    private String ipRoute;
    private String baiduRecord;
    private String baiduRecordFullDate;

    public String getBaiduRecordFullDate() {
        return baiduRecordFullDate;
    }

    public void setBaiduRecordFullDate(String baiduRecordFullDate) {
        this.baiduRecordFullDate = baiduRecordFullDate;
    }

    public String getIpRoute() {
        return ipRoute;
    }

    public void setIpRoute(String ipRoute) {
        this.ipRoute = ipRoute;
    }

    public String getBaiduRecord() {
        return baiduRecord;
    }

    public void setBaiduRecord(String baiduRecord) {
        this.baiduRecord = baiduRecord;
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

    public boolean getDataProcessingStatus () {
        return dataProcessingStatus;
    }

    public void setDataProcessingStatus (boolean dataProcessingStatus) {
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

    public String getTopHundred () {
        return topHundred;
    }

    public void setTopHundred (String topHundred) {
        this.topHundred = topHundred;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFullDate() {
        return fullDate;
    }

    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
    }

    public Integer getCreateTopTenNum () {
        return createTopTenNum;
    }

    public void setCreateTopTenNum (Integer createTopTenNum) {
        this.createTopTenNum = createTopTenNum;
    }

    public Integer getCreateTopFiftyNum () {
        return createTopFiftyNum;
    }

    public void setCreateTopFiftyNum (Integer createTopFiftyNum) {
        this.createTopFiftyNum = createTopFiftyNum;
    }
}
