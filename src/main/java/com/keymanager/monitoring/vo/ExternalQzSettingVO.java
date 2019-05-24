package com.keymanager.monitoring.vo;

import java.util.List;

public class ExternalQzSettingVO {

    private Long uuid;

    private String domain;

    private List<String> typeList;

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

    public List<String> getTypeList () {
        return typeList;
    }

    public void setTypeList (List<String> typeList) {
        this.typeList = typeList;
    }
}
