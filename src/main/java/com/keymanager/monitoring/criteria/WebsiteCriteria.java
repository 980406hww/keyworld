package com.keymanager.monitoring.criteria;

/**
 * Created by shunshikj08 on 2017/12/14.
 */
public class WebsiteCriteria {
    private String websiteName;
    private String domain;
    private Integer accessFailCount;

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getAccessFailCount() {
        return accessFailCount;
    }

    public void setAccessFailCount(Integer accessFailCount) {
        this.accessFailCount = accessFailCount;
    }
}
