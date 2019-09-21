package com.keymanager.monitoring.vo;

import java.util.List;

public class OptimizationMachineVO {
    private int clearCookie;
    private int disableStatistics;
    private int disableVisitWebsite;

    private int entryPageMinCount;
    private int entryPageMaxCount;
    private int pageRemainMinTime;
    private int pageRemainMaxTime;
    private int inputDelayMinTime;
    private int inputDelayMaxTime;
    private int slideDelayMinTime;
    private int slideDelayMaxTime;
    private int titleRemainMinTime;
    private int titleRemainMaxTime;
    private int optimizeKeywordCountPerIP;
    private int randomlyClickNoResult;
    private int maxUserCount;
    private int page;
    private int pageSize;
    private String operationType;

    private List<OptimizationKeywordVO> keywordVOList;

    public List<OptimizationKeywordVO> getKeywordVOList() {
        return keywordVOList;
    }

    public void setKeywordVOList(List<OptimizationKeywordVO> keywordVOList) {
        this.keywordVOList = keywordVOList;
    }

    public int getClearCookie() {
        return clearCookie;
    }

    public void setClearCookie(int clearCookie) {
        this.clearCookie = clearCookie;
    }

    public int getDisableStatistics() {
        return disableStatistics;
    }

    public void setDisableStatistics(int disableStatistics) {
        this.disableStatistics = disableStatistics;
    }

    public int getDisableVisitWebsite() {
        return disableVisitWebsite;
    }

    public void setDisableVisitWebsite(int disableVisitWebsite) {
        this.disableVisitWebsite = disableVisitWebsite;
    }

    public int getEntryPageMinCount() {
        return entryPageMinCount;
    }

    public void setEntryPageMinCount(int entryPageMinCount) {
        this.entryPageMinCount = entryPageMinCount;
    }

    public int getEntryPageMaxCount() {
        return entryPageMaxCount;
    }

    public void setEntryPageMaxCount(int entryPageMaxCount) {
        this.entryPageMaxCount = entryPageMaxCount;
    }

    public int getPageRemainMinTime() {
        return pageRemainMinTime;
    }

    public void setPageRemainMinTime(int pageRemainMinTime) {
        this.pageRemainMinTime = pageRemainMinTime;
    }

    public int getPageRemainMaxTime() {
        return pageRemainMaxTime;
    }

    public void setPageRemainMaxTime(int pageRemainMaxTime) {
        this.pageRemainMaxTime = pageRemainMaxTime;
    }

    public int getInputDelayMinTime() {
        return inputDelayMinTime;
    }

    public void setInputDelayMinTime(int inputDelayMinTime) {
        this.inputDelayMinTime = inputDelayMinTime;
    }

    public int getInputDelayMaxTime() {
        return inputDelayMaxTime;
    }

    public void setInputDelayMaxTime(int inputDelayMaxTime) {
        this.inputDelayMaxTime = inputDelayMaxTime;
    }

    public int getSlideDelayMinTime() {
        return slideDelayMinTime;
    }

    public void setSlideDelayMinTime(int slideDelayMinTime) {
        this.slideDelayMinTime = slideDelayMinTime;
    }

    public int getSlideDelayMaxTime() {
        return slideDelayMaxTime;
    }

    public void setSlideDelayMaxTime(int slideDelayMaxTime) {
        this.slideDelayMaxTime = slideDelayMaxTime;
    }

    public int getTitleRemainMinTime() {
        return titleRemainMinTime;
    }

    public void setTitleRemainMinTime(int titleRemainMinTime) {
        this.titleRemainMinTime = titleRemainMinTime;
    }

    public int getTitleRemainMaxTime() {
        return titleRemainMaxTime;
    }

    public void setTitleRemainMaxTime(int titleRemainMaxTime) {
        this.titleRemainMaxTime = titleRemainMaxTime;
    }

    public int getOptimizeKeywordCountPerIP() {
        return optimizeKeywordCountPerIP;
    }

    public void setOptimizeKeywordCountPerIP(int optimizeKeywordCountPerIP) {
        this.optimizeKeywordCountPerIP = optimizeKeywordCountPerIP;
    }

    public int getRandomlyClickNoResult() {
        return randomlyClickNoResult;
    }

    public void setRandomlyClickNoResult(int randomlyClickNoResult) {
        this.randomlyClickNoResult = randomlyClickNoResult;
    }

    public int getMaxUserCount() {
        return maxUserCount;
    }

    public void setMaxUserCount(int maxUserCount) {
        this.maxUserCount = maxUserCount;
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

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OptimizationMachineVO that = (OptimizationMachineVO) o;

        if (clearCookie != that.clearCookie) return false;
        if (disableStatistics != that.disableStatistics) return false;
        if (disableVisitWebsite != that.disableVisitWebsite) return false;
        if (entryPageMinCount != that.entryPageMinCount) return false;
        if (entryPageMaxCount != that.entryPageMaxCount) return false;
        if (pageRemainMinTime != that.pageRemainMinTime) return false;
        if (pageRemainMaxTime != that.pageRemainMaxTime) return false;
        if (inputDelayMinTime != that.inputDelayMinTime) return false;
        if (inputDelayMaxTime != that.inputDelayMaxTime) return false;
        if (slideDelayMinTime != that.slideDelayMinTime) return false;
        if (slideDelayMaxTime != that.slideDelayMaxTime) return false;
        if (titleRemainMinTime != that.titleRemainMinTime) return false;
        if (titleRemainMaxTime != that.titleRemainMaxTime) return false;
        if (optimizeKeywordCountPerIP != that.optimizeKeywordCountPerIP) return false;
        if (randomlyClickNoResult != that.randomlyClickNoResult) return false;
        if (maxUserCount != that.maxUserCount) return false;
        if (page != that.page) return false;
        if (pageSize != that.pageSize) return false;
        return operationType != null ? operationType.equals(that.operationType) : that.operationType == null;
    }

    @Override
    public int hashCode() {
        int result = clearCookie;
        result = 31 * result + disableStatistics;
        result = 31 * result + disableVisitWebsite;
        result = 31 * result + entryPageMinCount;
        result = 31 * result + entryPageMaxCount;
        result = 31 * result + pageRemainMinTime;
        result = 31 * result + pageRemainMaxTime;
        result = 31 * result + inputDelayMinTime;
        result = 31 * result + inputDelayMaxTime;
        result = 31 * result + slideDelayMinTime;
        result = 31 * result + slideDelayMaxTime;
        result = 31 * result + titleRemainMinTime;
        result = 31 * result + titleRemainMaxTime;
        result = 31 * result + optimizeKeywordCountPerIP;
        result = 31 * result + randomlyClickNoResult;
        result = 31 * result + maxUserCount;
        result = 31 * result + page;
        result = 31 * result + pageSize;
        result = 31 * result + (operationType != null ? operationType.hashCode() : 0);
        return result;
    }
}