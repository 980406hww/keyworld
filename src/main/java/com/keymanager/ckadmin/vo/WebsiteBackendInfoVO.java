package com.keymanager.ckadmin.vo;

/**
 * @author wjianwu 2019/6/14 11:29
 */
public class WebsiteBackendInfoVO {

    private Long uuid;
    private String backendDomain;
    private String backendUserName;
    private String backendPassword;
    private String websiteType;
    private int dnsAnalysisStatus;
    private String domain;
    private String serverIP;

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

    public int getDnsAnalysisStatus() {
        return dnsAnalysisStatus;
    }

    public void setDnsAnalysisStatus(int dnsAnalysisStatus) {
        this.dnsAnalysisStatus = dnsAnalysisStatus;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }
}
