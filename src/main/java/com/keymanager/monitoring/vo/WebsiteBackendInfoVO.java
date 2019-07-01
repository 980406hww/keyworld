package com.keymanager.monitoring.vo;

/**
 * @author wjianwu 2019/6/14 11:29
 */
public class WebsiteBackendInfoVO {

    private Long uuid;
    private String backendDomain;
    private String backendUserName;
    private String backendPassword;
    private String websiteType;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getBackendDomain() {
        return backendDomain;
    }

    public void setBackendDomain(String backendDomain) {
        this.backendDomain = backendDomain;
    }

    public String getBackendUserName() {
        return backendUserName;
    }

    public void setBackendUserName(String backendUserName) {
        this.backendUserName = backendUserName;
    }

    public String getBackendPassword() {
        return backendPassword;
    }

    public void setBackendPassword(String backendPassword) {
        this.backendPassword = backendPassword;
    }

    public String getWebsiteType() {
        return websiteType;
    }

    public void setWebsiteType(String websiteType) {
        this.websiteType = websiteType;
    }
}
