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

    @TableField(value = "fPCEmailSentOver3Times")
    private Integer pcEmailSentOver3Times;

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

    @TableField(value = "fPCComplained")
    private boolean pcComplained;

    @TableField(value = "fPhoneComplained")
    private boolean phoneComplained;

    @TableField(value = "fPCSetAppeared")
    private boolean pcSetAppeared;

    @TableField(value = "fPhoneSetAppeared")
    private boolean phoneSetAppeared;

    @TableField(value = "IsDeleted")
    private Integer isDeleted;

    @TableField(exist = false)
    private List<String> pcAppearedKeywordTypes;

    @TableField(exist = false)
    private List<String> phoneAppearedKeywordTypes;

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

    public Integer getPcEmailSentOver3Times() {
        return pcEmailSentOver3Times;
    }

    public void setPcEmailSentOver3Times(Integer pcEmailSentOver3Times) {
        this.pcEmailSentOver3Times = pcEmailSentOver3Times;
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isPcComplained() {
        return pcComplained;
    }

    public void setPcComplained(boolean pcComplained) {
        this.pcComplained = pcComplained;
    }

    public boolean isPcSetAppeared() {
        return pcSetAppeared;
    }

    public void setPcSetAppeared(boolean pcSetAppeared) {
        this.pcSetAppeared = pcSetAppeared;
    }

    public boolean isPhoneComplained() {
        return phoneComplained;
    }

    public void setPhoneComplained(boolean phoneComplained) {
        this.phoneComplained = phoneComplained;
    }

    public boolean isPhoneSetAppeared() {
        return phoneSetAppeared;
    }

    public void setPhoneSetAppeared(boolean phoneSetAppeared) {
        this.phoneSetAppeared = phoneSetAppeared;
    }

    public List<String> getPcAppearedKeywordTypes() {
        return pcAppearedKeywordTypes;
    }

    public void setPcAppearedKeywordTypes(List<String> pcAppearedKeywordTypes) {
        this.pcAppearedKeywordTypes = pcAppearedKeywordTypes;
    }

    public List<String> getPhoneAppearedKeywordTypes() {
        return phoneAppearedKeywordTypes;
    }

    public void setPhoneAppearedKeywordTypes(List<String> phoneAppearedKeywordTypes) {
        this.phoneAppearedKeywordTypes = phoneAppearedKeywordTypes;
    }
}
