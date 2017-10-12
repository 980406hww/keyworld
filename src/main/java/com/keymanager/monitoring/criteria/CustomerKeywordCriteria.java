package com.keymanager.monitoring.criteria;

import java.util.Date;

public class CustomerKeywordCriteria extends BaseCriteria{
    private Long customerUuid;
    private String url;
    private String keyword;
    private String creationFromTime;//添加时间
    private String creationToTime;
    private String status;
    private String optimizeGroupName;//优化组名
    private String orderElement;
    private String invalidRefreshCount;//无效点击数
    private String position;//显示前几条
    private String noPosition;//显示没有排名
    private String entryType;//
    private String terminalType;//8088 PC  8089  phone
    private Integer optimizedCount;//已刷
    private Integer currentIndexCount;


    private String qq;
    private String userName;
    private String pushPay;//催缴
    private String displayStop;//显示下架
    private String orderNumber;//订单号
    private String remarks;//备注

    public Integer getCurrentIndexCount() {
        return currentIndexCount;
    }

    public void setCurrentIndexCount(Integer currentIndexCount) {
        this.currentIndexCount = currentIndexCount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getOptimizedCount() {
        return optimizedCount;
    }

    public void setOptimizedCount(Integer optimizedCount) {
        this.optimizedCount = optimizedCount;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
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

    public String getNoPosition() {
        return noPosition;
    }

    public void setNoPosition(String noPosition) {
        this.noPosition = noPosition;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPushPay() {
        return pushPay;
    }

    public void setPushPay(String pushPay) {
        this.pushPay = pushPay;
    }

    public String getDisplayStop() {
        return displayStop;
    }

    public void setDisplayStop(String displayStop) {
        this.displayStop = displayStop;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
