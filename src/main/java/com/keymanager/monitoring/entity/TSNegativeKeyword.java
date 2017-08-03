package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
@TableName(value = "t_ts_negative_keyword")
public class TSNegativeKeyword extends BaseEntity {

    @TableField(value = "fTSMainKeywordUuid")
    private Long tsMainKeywordUuid;

    @TableField(value = "fKeyword")
    private String keyword;

    @TableField(value = "fPCEmailSentOver2Weeks")
    private Integer pcEmailSentOver2Weeks;

    @TableField(value = "fPhoneEmailSentOver2Weeks")
    private Integer phoneEmailSentOver2Weeks;

    @TableField(value = "fPCEmailSentOver3Timess")
    private Integer pcEmailSentOver3Timess;

    @TableField(value = "fPhoneEmailSentOver3Times")
    private Integer phoneEmailSentOver3Times;

    @TableField(value = "fPCAppeared")
    private Integer pcAppeared;

    @TableField(value = "fPhoneAppeared")
    private Integer phoneAppeared;

    @TableField(value = "fPCOccurTimes")
    private Integer pcOccurTimes;

    @TableField(value = "fPhoneOccurTimes")
    private Integer phoneOccurTimes;

    @TableField(value = "fPCComplainTime")
    private Timestamp pcComplainTime;

    @TableField(value = "fPhoneComplainTime")
    private Timestamp phoneComplainTime;

    @TableField(value = "IsDeleted")
    private Integer isDeleted;

    @TableField(exist = false)
    private List<TSComplainLog> tsComplainLogs;

    public Long getTsMainKeywordUuid() {
        return tsMainKeywordUuid;
    }

    public void setTsMainKeywordUuid(Long tsMainKeywordUuid) {
        this.tsMainKeywordUuid = tsMainKeywordUuid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPcEmailSentOver2Weeks() {
        return pcEmailSentOver2Weeks;
    }

    public void setPcEmailSentOver2Weeks(Integer pcEmailSentOver2Weeks) {
        this.pcEmailSentOver2Weeks = pcEmailSentOver2Weeks;
    }

    public Integer getPhoneEmailSentOver2Weeks() {
        return phoneEmailSentOver2Weeks;
    }

    public void setPhoneEmailSentOver2Weeks(Integer phoneEmailSentOver2Weeks) {
        this.phoneEmailSentOver2Weeks = phoneEmailSentOver2Weeks;
    }

    public Integer getPcEmailSentOver3Timess() {
        return pcEmailSentOver3Timess;
    }

    public void setPcEmailSentOver3Timess(Integer pcEmailSentOver3Timess) {
        this.pcEmailSentOver3Timess = pcEmailSentOver3Timess;
    }

    public Integer getPhoneEmailSentOver3Times() {
        return phoneEmailSentOver3Times;
    }

    public void setPhoneEmailSentOver3Times(Integer phoneEmailSentOver3Times) {
        this.phoneEmailSentOver3Times = phoneEmailSentOver3Times;
    }

    public Integer getPcAppeared() {
        return pcAppeared;
    }

    public void setPcAppeared(Integer pcAppeared) {
        this.pcAppeared = pcAppeared;
    }

    public Integer getPhoneAppeared() {
        return phoneAppeared;
    }

    public void setPhoneAppeared(Integer phoneAppeared) {
        this.phoneAppeared = phoneAppeared;
    }

    public Integer getPcOccurTimes() {
        return pcOccurTimes;
    }

    public void setPcOccurTimes(Integer pcOccurTimes) {
        this.pcOccurTimes = pcOccurTimes;
    }

    public Integer getPhoneOccurTimes() {
        return phoneOccurTimes;
    }

    public void setPhoneOccurTimes(Integer phoneOccurTimes) {
        this.phoneOccurTimes = phoneOccurTimes;
    }

    public Timestamp getPcComplainTime() {
        return pcComplainTime;
    }

    public void setPcComplainTime(Timestamp pcComplainTime) {
        this.pcComplainTime = pcComplainTime;
    }

    public Timestamp getPhoneComplainTime() {
        return phoneComplainTime;
    }

    public void setPhoneComplainTime(Timestamp phoneComplainTime) {
        this.phoneComplainTime = phoneComplainTime;
    }

    public List<TSComplainLog> getTsComplainLogs() {
        return tsComplainLogs;
    }

    public void setTsComplainLogs(List<TSComplainLog> tsComplainLogs) {
        this.tsComplainLogs = tsComplainLogs;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
