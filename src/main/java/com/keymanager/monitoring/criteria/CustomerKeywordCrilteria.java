package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.criteria.BaseCriteria;

import java.util.Date;

public class CustomerKeywordCrilteria {
    private Long customerUuid;
    private String url;
    private String keyword;
    private Date creationFromTime;//添加时间
    private Date creationToTime;
    private int status;
    private String optimizeGroupName;//优化组名
    private String conditionalSorting;
    private int invalidRefreshCount;//无效点击数
    private int position;//显示前几条

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

    public Date getCreationFromTime() {
        return creationFromTime;
    }

    public void setCreationFromTime(Date creationFromTime) {
        this.creationFromTime = creationFromTime;
    }

    public Date getCreationToTime() {
        return creationToTime;
    }

    public void setCreationToTime(Date creationToTime) {
        this.creationToTime = creationToTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOptimizeGroupName() {
        return optimizeGroupName;
    }

    public void setOptimizeGroupName(String optimizeGroupName) {
        this.optimizeGroupName = optimizeGroupName;
    }

    public String getConditionalSorting() {
        return conditionalSorting;
    }

    public void setConditionalSorting(String conditionalSorting) {
        this.conditionalSorting = conditionalSorting;
    }

    public int getInvalidRefreshCount() {
        return invalidRefreshCount;
    }

    public void setInvalidRefreshCount(int invalidRefreshCount) {
        this.invalidRefreshCount = invalidRefreshCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
