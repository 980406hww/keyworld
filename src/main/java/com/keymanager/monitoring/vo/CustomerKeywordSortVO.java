package com.keymanager.monitoring.vo;

/**
 * Created by shunshikj08 on 2018/7/6.
 */
public class CustomerKeywordSortVO {
    private String keyword;
    private String searchEngine;
    private String terminalType;
    private String uuids;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public String getUuids() {
        return uuids;
    }

    public void setUuids(String uuids) {
        this.uuids = uuids;
    }
}
