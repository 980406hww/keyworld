package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

/**
 * @ClassName QZSettingCriteria
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/6 18:24
 * @Version 1.0
 */
public class QZSettingCriteria extends BaseCriteria {
    private String loginName;
    private String customerUuid;
    private String terminalType;
    private String searchEngine;
    private String group;
    private String domain;
    private String userInfoID;

    public String getUserInfoID() {
        return userInfoID;
    }

    public void setUserInfoID(String userInfoID) {
        this.userInfoID = userInfoID;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
