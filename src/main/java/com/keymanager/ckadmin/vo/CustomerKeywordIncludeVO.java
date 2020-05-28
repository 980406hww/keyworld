package com.keymanager.ckadmin.vo;

public class CustomerKeywordIncludeVO {
    private Long uuid;
    private String keyword;
    private String url;
    private String title;
    private String bearPawNumber;
    private String terminalType;
    private String searchEngine;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBearPawNumber() {
        return bearPawNumber;
    }

    public void setBearPawNumber(String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
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
}
