package com.keymanager.monitoring.criteria;

/**
 * @author wjianwu 2019/6/13 14:04
 */
public class WebsiteBackGroundInfoCriteria {

    private String backgroundDomain;
    private String backgroundUserName;
    private String backgroundPassword;
    private String websiteType;

    public String getBackgroundDomain() {
        return backgroundDomain;
    }

    public void setBackgroundDomain(String backgroundDomain) {
        this.backgroundDomain = backgroundDomain;
    }

    public String getBackgroundUserName() {
        return backgroundUserName;
    }

    public void setBackgroundUserName(String backgroundUserName) {
        this.backgroundUserName = backgroundUserName;
    }

    public String getBackgroundPassword() {
        return backgroundPassword;
    }

    public void setBackgroundPassword(String backgroundPassword) {
        this.backgroundPassword = backgroundPassword;
    }

    public String getWebsiteType() {
        return websiteType;
    }

    public void setWebsiteType(String websiteType) {
        this.websiteType = websiteType;
    }
}
