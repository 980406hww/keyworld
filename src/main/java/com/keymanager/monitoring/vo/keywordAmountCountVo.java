package com.keymanager.monitoring.vo;


public class keywordAmountCountVo {
    private String terminalType;
    private String keyword;
    private String searchEngine;
    private Integer keywordCount;
    private Integer customerCount;
    private Integer userCount;
    private Integer topThree;
    private Integer topTen;

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

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

    public Integer getKeywordCount() {
        return keywordCount;
    }

    public void setKeywordCount(Integer keywordCount) {
        this.keywordCount = keywordCount;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getTopThree() {
        return topThree;
    }

    public void setTopThree(Integer topThree) {
        this.topThree = topThree;
    }

    public Integer getTopTen() {
        return topTen;
    }

    public void setTopTen(Integer topTen) {
        this.topTen = topTen;
    }
}
