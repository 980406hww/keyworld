package com.keymanager.monitoring.criteria;

import java.util.Date;

public class CustomerKeywordCrilteria extends BaseCriteria{
    private String customerUuid;
    private String url;
    private String keyword;
    private String creationFromTime;//添加时间
    private String creationToTime;
    private String status;
    private String optimizeGroupName;//优化组名
    private String orderElement;
    private String invalidRefreshCount;//无效点击数
    private String position;//显示前几条
    private String entryType;//
    private String terminalType;//8088 PC  8089  phone


    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCreationFromTime() {
        return creationFromTime;
    }

    public void setCreationFromTime(String creationFromTime) {
        this.creationFromTime = creationFromTime;
    }

    public String getCreationToTime() {
        return creationToTime;
    }

    public void setCreationToTime(String creationToTime) {
        this.creationToTime = creationToTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOptimizeGroupName() {
        return optimizeGroupName;
    }

    public void setOptimizeGroupName(String optimizeGroupName) {
        this.optimizeGroupName = optimizeGroupName;
    }

    public String getOrderElement() {
        return orderElement;
    }

    public void setOrderElement(String orderElement) {
        this.orderElement = orderElement;
    }

    public String getInvalidRefreshCount() {
        return invalidRefreshCount;
    }

    public void setInvalidRefreshCount(String invalidRefreshCount) {
        this.invalidRefreshCount = invalidRefreshCount;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public CustomerKeywordCrilteria(String customerUuid, String url, String keyword, String creationFromTime, String creationToTime, String status, String optimizeGroupName, String orderElement, String invalidRefreshCount, String position, String entryType, String terminalType) {
        this.customerUuid = customerUuid;
        this.url = url;
        this.keyword = keyword;
        this.creationFromTime = creationFromTime;
        this.creationToTime = creationToTime;
        this.status = status;
        this.optimizeGroupName = optimizeGroupName;
        this.orderElement = orderElement;
        this.invalidRefreshCount = invalidRefreshCount;
        this.position = position;
        this.entryType = entryType;
        this.terminalType = terminalType;
    }

    public CustomerKeywordCrilteria() {
    }
}
