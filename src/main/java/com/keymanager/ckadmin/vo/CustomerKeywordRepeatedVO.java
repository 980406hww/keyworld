package com.keymanager.ckadmin.vo;

public class CustomerKeywordRepeatedVO {
    private String keyword;
    private String terminalType;
    private String searchEngine;
    private String keywordUuids;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public String getKeywordUuids() {
        return keywordUuids;
    }

    public void setKeywordUuids(String positions) {
        this.keywordUuids = positions;
    }
}
