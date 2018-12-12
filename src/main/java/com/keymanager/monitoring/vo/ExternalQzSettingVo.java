package com.keymanager.monitoring.vo;

public class ExternalQzSettingVo{

    private Long uuid;

    private String domain;

    private String pcGroup;

    private String phoneGroup;

    private String createTime;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
