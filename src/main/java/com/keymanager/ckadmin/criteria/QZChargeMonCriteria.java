package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

public class QZChargeMonCriteria extends BaseCriteria {

    private String terminal;
    private String searchEngine;
    private String dateStart;
    private String dateEnd;
    private Integer operationType;

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
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
