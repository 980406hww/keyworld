package com.keymanager.monitoring.criteria;

/**
 * @Author zhoukai
 * @Date 2019/7/5 20:37
 **/
public class IndustryDetailCriteria extends BaseCriteria {

    private long industryID;
    private String website;
    private String title;
    private Integer weight;
    private String remark;
    private String qqs;
    private String phones;
    private int level;

    public long getIndustryID() {
        return industryID;
    }

    public void setIndustryID(long industryID) {
        this.industryID = industryID;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getQqs() {
        return qqs;
    }

    public void setQqs(String qqs) {
        this.qqs = qqs;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
