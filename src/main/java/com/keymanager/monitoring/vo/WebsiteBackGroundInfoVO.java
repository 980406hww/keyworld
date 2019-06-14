package com.keymanager.monitoring.vo;

/**
 * @author wjianwu 2019/6/14 11:29
 */
public class WebsiteBackGroundInfoVO {

    private Long uuid;
    private String backgroundDomain;
    private String backgroundUserName;
    private String backgroundPassword;
    private String websiteType;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

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
