package com.keymanager.monitoring.vo;

import java.util.List;

public class ExternalQzSettingVO {

    private Long uuid;

    private String domain;

    private Long customerUuid;

    private String type;

    private String bearPawNumber;

    private String keywordType;

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

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBearPawNumber() {
        return bearPawNumber;
    }

    public void setBearPawNumber(String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
    }

    public String getKeywordType() {
        return keywordType;
    }

    public void setKeywordType(String keywordType) {
        this.keywordType = keywordType;
    }
}
