package com.keymanager.monitoring.vo;

import java.io.Serializable;

public class QZKeywordRankForSync implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long qkId;
    private Long qsId;
    private String terminalType;
    private String websiteType;
    private String topTen;
    private String topTwenty;
    private String topThirty;
    private String topForty;
    private String topFifty;
    private String date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
