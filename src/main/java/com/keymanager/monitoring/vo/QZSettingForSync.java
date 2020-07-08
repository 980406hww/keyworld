package com.keymanager.monitoring.vo;

/**
 * @author shunshikj40
 */
public class QZSettingForSync {

    private Long qsId;
    private String domain;
    private String searchEngine;
    private String pcGroup;
    private String phoneGroup;

    public Long getQsId() {
        return qsId;
    }

    public void setQsId(Long qsId) {
        this.qsId = qsId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getPcGroup() {
        return pcGroup;
    }

    public void setPcGroup(String pcGroup) {
        this.pcGroup = pcGroup;
    }

    public String getPhoneGroup() {
        return phoneGroup;
    }

    public void setPhoneGroup(String phoneGroup) {
        this.phoneGroup = phoneGroup;
    }
}
