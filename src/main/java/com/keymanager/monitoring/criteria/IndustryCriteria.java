package com.keymanager.monitoring.criteria;

/**
 * @Author zhoukai
 * @Date 2019/7/5 11:42
 **/
public class IndustryCriteria {

    private String loginName;
    private String searchEngine;
    private String industryName;
    private String terminalType;
    private Integer status;

    public String getLoginName () {
        return loginName;
    }

    public void setLoginName (String loginName) {
        this.loginName = loginName;
    }

    public String getSearchEngine () {
        return searchEngine;
    }

    public void setSearchEngine (String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getIndustryName () {
        return industryName;
    }

    public void setIndustryName (String industryName) {
        this.industryName = industryName;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public Integer getStatus () {
        return status;
    }

    public void setStatus (Integer status) {
        this.status = status;
    }
}
