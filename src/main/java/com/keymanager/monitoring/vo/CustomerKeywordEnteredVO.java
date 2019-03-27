package com.keymanager.monitoring.vo;

import java.util.Date;

public class CustomerKeywordEnteredVO {

    private long uuid;
    private String terminalType;
    private String optimizeGroupName;
    private String searchEngine;
    private String keyword;
    private String url;
    private Date captureTitleQueryTime;
    private String enteredKeywordRemarks;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getOptimizeGroupName() {
        return optimizeGroupName;
    }

    public void setOptimizeGroupName(String optimizeGroupName) {
        this.optimizeGroupName = optimizeGroupName;
    }

    public String getEnteredKeywordRemarks() {
        return enteredKeywordRemarks;
    }

    public void setEnteredKeywordRemarks(String enteredKeywordRemarks) {
        this.enteredKeywordRemarks = enteredKeywordRemarks;
    }

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getCaptureTitleQueryTime() {
        return captureTitleQueryTime;
    }

    public void setCaptureTitleQueryTime(Date captureTitleQueryTime) {
        this.captureTitleQueryTime = captureTitleQueryTime;
    }
}
