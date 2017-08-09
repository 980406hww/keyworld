package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.entity.TSNegativeKeyword;

import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj08 on 2017/8/3.
 */
public class TSMainKeywordVO {
    private Long uuid;
    private String keyword;
    private String group;
    private Date updateTime;
    private Date createTime;
    private Date complaintsTime;
    private List<TSNegativeKeyword> tsNegativeKeywords;

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<TSNegativeKeyword> getTsNegativeKeywords() {
        return tsNegativeKeywords;
    }

    public void setTsNegativeKeywords(List<TSNegativeKeyword> tsNegativeKeywords) {
        this.tsNegativeKeywords = tsNegativeKeywords;
    }

    public Date getComplaintsTime() {
        return complaintsTime;
    }

    public void setComplaintsTime(Date complaintsTime) {
        this.complaintsTime = complaintsTime;
    }
}
