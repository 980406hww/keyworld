package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;

import java.util.Date;
/**
 * Created by shunshikj08 on 2017/12/14.
 */
@TableName("t_website")
public class Website extends BaseEntity {

    @TableField(value = "fWebsiteName")
    private String websiteName;

    @TableField(value = "fDomain")
    private String domain;

    @TableField(value = "fIndustry", strategy = FieldStrategy.IGNORED)
    private String industry;

    @TableField(value = "fAccessFailCount")
    private Integer accessFailCount;

    @TableField(value = "fAccessFailTime", strategy = FieldStrategy.IGNORED)
    private Date accessFailTime;

    @TableField(value = "fLastAccessTime")
    private Date lastAccessTime;

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Integer getAccessFailCount() {
        return accessFailCount;
    }

    public void setAccessFailCount(Integer accessFailCount) {
        this.accessFailCount = accessFailCount;
    }

    public Date getAccessFailTime() {
        return accessFailTime;
    }

    public void setAccessFailTime(Date accessFailTime) {
        this.accessFailTime = accessFailTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}
