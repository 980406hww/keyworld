package com.keymanager.monitoring.vo;

/**
 * @ClassName ExternalCustomerKeywordVO
 * @Description 外部爬百度指数参数类
 * @Author lhc
 * @Date 2019/8/28 14:08
 * @Version 1.0
 */
public class ExternalCustomerKeywordVO {

    private Long uuid;
    private String keyword;
    private String terminalType;
    private Integer pcIndex;
    private Integer phoneIndex;
    private Integer allIndex;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public Integer getPcIndex() {
        return pcIndex;
    }

    public void setPcIndex(Integer pcIndex) {
        this.pcIndex = pcIndex;
    }

    public Integer getPhoneIndex() {
        return phoneIndex;
    }

    public void setPhoneIndex(Integer phoneIndex) {
        this.phoneIndex = phoneIndex;
    }

    public Integer getAllIndex() {
        return allIndex;
    }

    public void setAllIndex(Integer allIndex) {
        this.allIndex = allIndex;
    }
}
