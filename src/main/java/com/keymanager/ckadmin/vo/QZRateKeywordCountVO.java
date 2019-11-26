package com.keymanager.ckadmin.vo;

public class QZRateKeywordCountVO implements Cloneable{
    private Long customerUuid;
    private String customerName;
    private String terminalType;
    private Long qzUuid;
    private Integer totalKeywordCount;
    private Integer topTenKeywordCount;
    private Integer rate;
    private Integer hasPC;
    private Integer hasPhone;
    private String qzDomain;

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getQzUuid() {
        return qzUuid;
    }

    public void setQzUuid(Long qzUuid) {
        this.qzUuid = qzUuid;
    }

    public Integer getTotalKeywordCount() {
        return totalKeywordCount;
    }

    public void setTotalKeywordCount(Integer totalKeywordCount) {
        this.totalKeywordCount = totalKeywordCount;
    }

    public Integer getTopTenKeywordCount() {
        return topTenKeywordCount;
    }

    public void setTopTenKeywordCount(Integer topTenKeywordCount) {
        this.topTenKeywordCount = topTenKeywordCount;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getHasPC() {
        return hasPC;
    }

    public void setHasPC(Integer hasPC) {
        this.hasPC = hasPC;
    }

    public Integer getHasPhone() {
        return hasPhone;
    }

    public void setHasPhone(Integer hasPhone) {
        this.hasPhone = hasPhone;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getQzDomain() {
        return qzDomain;
    }

    public void setQzDomain(String qzDomain) {
        this.qzDomain = qzDomain;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Object object = super.clone();
        return object;
    }
}
