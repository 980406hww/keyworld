package com.keymanager.monitoring.vo;

public class OptimizationKeywordVO {
    private String terminalType;
    private String searchEngine;
    private String keyword;
    private long uuid;
    private String url;
    private String optimizeGroup;
    private String entryType;
    private String bearPawNumber;


    private String title;
    private int optimizedCount;
    private int queryInterval;
    private int optimizePlanCount;

    public int getOptimizePlanCount() {
        return optimizePlanCount;
    }

    public void setOptimizePlanCount(int optimizePlanCount) {
        this.optimizePlanCount = optimizePlanCount;
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

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOptimizeGroup() {
        return optimizeGroup;
    }

    public void setOptimizeGroup(String optimizeGroup) {
        this.optimizeGroup = optimizeGroup;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getBearPawNumber() {
        return bearPawNumber;
    }

    public void setBearPawNumber(String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOptimizedCount() {
        return optimizedCount;
    }

    public void setOptimizedCount(int optimizedCount) {
        this.optimizedCount = optimizedCount;
    }

    public int getQueryInterval() {
        return queryInterval;
    }

    public void setQueryInterval(int queryInterval) {
        this.queryInterval = queryInterval;
    }
}