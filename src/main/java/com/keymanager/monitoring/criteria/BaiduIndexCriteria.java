package com.keymanager.monitoring.criteria;

public class BaiduIndexCriteria extends BaseCriteria{
    private String keyword;
    private int pcIndex;
    private int phoneIndex;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPcIndex() {
        return pcIndex;
    }

    public void setPcIndex(int pcIndex) {
        this.pcIndex = pcIndex;
    }

    public int getPhoneIndex() {
        return phoneIndex;
    }

    public void setPhoneIndex(int phoneIndex) {
        this.phoneIndex = phoneIndex;
    }
}
