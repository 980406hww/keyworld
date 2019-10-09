package com.keymanager.ckadmin.criteria;

public class KeywordStandardCriteria {
    private Integer status;
    private Integer noReachStandardDays;//未达标天数
    private String type;
    private String terminalType;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNoReachStandardDays() {
        return noReachStandardDays;
    }

    public void setNoReachStandardDays(Integer noReachStandardDays) {
        this.noReachStandardDays = noReachStandardDays;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }
}
