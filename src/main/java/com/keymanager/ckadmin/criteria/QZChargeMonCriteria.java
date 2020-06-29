package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

public class QZChargeMonCriteria extends BaseCriteria {

    private String qzDomain;
    private String customerName;
    private String qzTerminal;
    private String searchEngine;
    private String dateStart;
    private String dateEnd;
    private Integer operationType;
    private String loginName;

    public String getQzDomain() {
        return qzDomain;
    }

    public void setQzDomain(String qzDomain) {
        this.qzDomain = qzDomain;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getQzTerminal() {
        return qzTerminal;
    }

    public void setQzTerminal(String qzTerminal) {
        this.qzTerminal = qzTerminal;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }
}
