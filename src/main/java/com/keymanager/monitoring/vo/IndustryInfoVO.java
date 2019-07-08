package com.keymanager.monitoring.vo;

public class IndustryInfoVO {

    private long uuid;
    private String searchEngine;
    private String terminalType;
    private String industryName;
    private String targetUrl;
    private int pageNum;
    private int pagePerNum;
    private String telReg;
    private String qqReg;

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPagePerNum() {
        return pagePerNum;
    }

    public void setPagePerNum(int pagePerNum) {
        this.pagePerNum = pagePerNum;
    }

    public String getTelReg() {
        return telReg;
    }

    public void setTelReg(String telReg) {
        this.telReg = telReg;
    }

    public String getQqReg() {
        return qqReg;
    }

    public void setQqReg(String qqReg) {
        this.qqReg = qqReg;
    }
}
