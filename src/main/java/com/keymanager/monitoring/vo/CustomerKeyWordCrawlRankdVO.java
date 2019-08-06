package com.keymanager.monitoring.vo;

public class CustomerKeyWordCrawlRankdVO {
    private long uuid;
    private String terminalType;
    private String searchEngine;
    private String keyword;
    private String url;
    private String bearPawNumber;
    private String title;
    private String rankStatus;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getRankStatus() {
        return rankStatus;
    }

    public void setRankStatus(String rankStatus) {
        this.rankStatus = rankStatus;
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

    public String getBearPawNumber() {
        return bearPawNumber;
    }

    public void setBearPawNumber(String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
    }
}
