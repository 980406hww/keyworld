package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
@TableName(value = "t_ts_main_keyword")
public class TSMainKeyword extends BaseEntity {

    @TableField(value = "fKeyword")
    private String keyword;

    @TableField(value = "fGroup")
    private String group;

    @TableField(value = "fComplaintsTime")
    private Date complaintsTime;

    @TableField(exist=false)
    private List<TSNegativeKeyword> tsNegativeKeywords;

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
