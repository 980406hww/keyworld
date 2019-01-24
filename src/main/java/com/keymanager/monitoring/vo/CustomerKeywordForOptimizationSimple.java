package com.keymanager.monitoring.vo;

import java.util.Date;
import java.util.List;

public class CustomerKeywordForOptimizationSimple {
    private String terminalType;
    private String searchEngine;
    private int page;
    private int pageSize;
    private String keyword;
    private long uuid;
    private String url;

    private String bearPawNumber;
    private String group;
    private String entryType;
    private String operationType;
    private String relatedKeyword;
    private int currentPosition;
    private String originalUrl;
    private String title;
    private String baiduAdUrl;
    private List<String> negativeKeywords;
    private List<String> excludeKeywords;
    private List<String> recommendedKeywords;
    private Boolean operateSelectKeyword;
    private Boolean operateRelatedKeyword;
    private Boolean operateRecommendKeyword;
    private Boolean operateSearchAfterSelectKeyword;
    private String clickUrl;
    private String showPage;
    private Integer relatedKeywordPercentage;

    private List<String> excludeTitles;
    private List<KeywordSimpleVO> relatedQZKeywords;

    private String remarks;
    private String ct;
    private String fromSource;
    private Date negativeListUpdateTime;

    private String disableVisitUrl;

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

    public String getBearPawNumber() {
        return bearPawNumber;
    }

    public void setBearPawNumber(String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getRelatedKeyword() {
        return relatedKeyword;
    }

    public void setRelatedKeyword(String relatedKeyword) {
        this.relatedKeyword = relatedKeyword;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBaiduAdUrl() {
        return baiduAdUrl;
    }

    public void setBaiduAdUrl(String baiduAdUrl) {
        this.baiduAdUrl = baiduAdUrl;
    }

    public List<String> getExcludeTitles() {
        return excludeTitles;
    }

    public void setExcludeTitles(List<String> excludeTitles) {
        this.excludeTitles = excludeTitles;
    }

    public List<String> getNegativeKeywords() {
        return negativeKeywords;
    }

    public void setNegativeKeywords(List<String> negativeKeywords) {
        this.negativeKeywords = negativeKeywords;
    }

    public List<String> getExcludeKeywords() {
        return excludeKeywords;
    }

    public void setExcludeKeywords(List<String> excludeKeywords) {
        this.excludeKeywords = excludeKeywords;
    }

    public List<String> getRecommendedKeywords() {
        return recommendedKeywords;
    }

    public void setRecommendedKeywords(List<String> recommendedKeywords) {
        this.recommendedKeywords = recommendedKeywords;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getShowPage() {
        return showPage;
    }

    public void setShowPage(String showPage) {
        this.showPage = showPage;
    }

    public Integer getRelatedKeywordPercentage() {
        return relatedKeywordPercentage;
    }

    public void setRelatedKeywordPercentage(Integer relatedKeywordPercentage) {
        this.relatedKeywordPercentage = relatedKeywordPercentage;
    }

    public Boolean getOperateSelectKeyword() {
        return operateSelectKeyword;
    }

    public void setOperateSelectKeyword(Boolean operateSelectKeyword) {
        this.operateSelectKeyword = operateSelectKeyword;
    }

    public Boolean getOperateRelatedKeyword() {
        return operateRelatedKeyword;
    }

    public void setOperateRelatedKeyword(Boolean operateRelatedKeyword) {
        this.operateRelatedKeyword = operateRelatedKeyword;
    }

    public Boolean getOperateRecommendKeyword() {
        return operateRecommendKeyword;
    }

    public void setOperateRecommendKeyword(Boolean operateRecommendKeyword) {
        this.operateRecommendKeyword = operateRecommendKeyword;
    }

    public Boolean getOperateSearchAfterSelectKeyword() {
        return operateSearchAfterSelectKeyword;
    }

    public void setOperateSearchAfterSelectKeyword(Boolean operateSearchAfterSelectKeyword) {
        this.operateSearchAfterSelectKeyword = operateSearchAfterSelectKeyword;
    }

    public List<KeywordSimpleVO> getRelatedQZKeywords() {
        return relatedQZKeywords;
    }

    public void setRelatedQZKeywords(List<KeywordSimpleVO> relatedQZKeywords) {
        this.relatedQZKeywords = relatedQZKeywords;
    }

    public Date getNegativeListUpdateTime() {
        return negativeListUpdateTime;
    }

    public void setNegativeListUpdateTime(Date negativeListUpdateTime) {
        this.negativeListUpdateTime = negativeListUpdateTime;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getFromSource() {
        return fromSource;
    }

    public void setFromSource(String fromSource) {
        this.fromSource = fromSource;
    }

    public String getDisableVisitUrl() {
        return disableVisitUrl;
    }

    public void setDisableVisitUrl(String disableVisitUrl) {
        this.disableVisitUrl = disableVisitUrl;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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
}