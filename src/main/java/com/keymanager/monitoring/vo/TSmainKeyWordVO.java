package com.keymanager.monitoring.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by shunshikj01 on 2017/8/7.
 */
@TableName(value = "t_ts_main_keywordvo")
public class TSmainKeyWordVO {

  @TableField(value = "uuid")
  private String uuid;

  @TableField(value = "group")
  private String group;

  @TableField(value = "keyword")
  private String keyword;

  @TableField(value = "tsNegativeKeywordUuid")
  private String tsNegativeKeywordUuid;

  @TableField(value = "tsNegativeKeyword")
  private String tsNegativeKeyword;

  @TableField(value = "pcEmailSentOver2Weeks")
  private Integer pcEmailSentOver2Weeks;

  @TableField(value = "phoneEmailSentOver2Weeks")
  private Integer phoneEmailSentOver2Weeks;

  @TableField(value = "pcEmailSentOver3Times")
  private Integer pcEmailSentOver3Times;

  @TableField(value = "phoneEmailSentOver3Times")
  private Integer phoneEmailSentOver3Times;

  @TableField(value = "pcAppeared")
  private Integer pcAppeared;

  @TableField(value = "phoneAppeared")
  private Integer phoneAppeared;

  @TableField(value = "pcOccurTimes")
  private Integer pcOccurTimes;

  @TableField(value = "phoneOccurTimes")
  private Integer phoneOccurTimes;

  @TableField(value = "pcComplainTime")
  private Timestamp pcComplainTime;

  @TableField(value = "phoneComplainTime")
  private Timestamp phoneComplainTime;

  @TableField(value = "pcComplained")
  private boolean pcComplained;

  @TableField(value = "phoneComplained")
  private boolean phoneComplained;

  @TableField(value = "pcSetAppeared")
  private boolean pcSetAppeared;

  @TableField(value = "phoneSetAppeared")
  private boolean phoneSetAppeared;

  @TableField(value = "isDeleted")
  private Integer isDeleted;

  @TableField(value = "UpdateTime")
  private Date updateTime;

  @TableField(value = "createTime")
  private Date createTime;
  @TableField(value = "complaintsTime")
  private Date complaintsTime;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
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

  public String getTsNegativeKeywordUuid() {
    return tsNegativeKeywordUuid;
  }

  public void setTsNegativeKeywordUuid(String tsNegativeKeywordUuid) {
    this.tsNegativeKeywordUuid = tsNegativeKeywordUuid;
  }

  public String getTsNegativeKeyword() {
    return tsNegativeKeyword;
  }

  public void setTsNegativeKeyword(String tsNegativeKeyword) {
    this.tsNegativeKeyword = tsNegativeKeyword;
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

  public boolean isPcComplained() {
    return pcComplained;
  }

  public void setPcComplained(boolean pcComplained) {
    this.pcComplained = pcComplained;
  }

  public boolean isPhoneComplained() {
    return phoneComplained;
  }

  public void setPhoneComplained(boolean phoneComplained) {
    this.phoneComplained = phoneComplained;
  }

  public boolean isPcSetAppeared() {
    return pcSetAppeared;
  }

  public void setPcSetAppeared(boolean pcSetAppeared) {
    this.pcSetAppeared = pcSetAppeared;
  }

  public boolean isPhoneSetAppeared() {
    return phoneSetAppeared;
  }

  public void setPhoneSetAppeared(boolean phoneSetAppeared) {
    this.phoneSetAppeared = phoneSetAppeared;
  }

  public Integer getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Integer isDeleted) {
    this.isDeleted = isDeleted;
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

  public Date getComplaintsTime() {
    return complaintsTime;
  }

  public void setComplaintsTime(Date complaintsTime) {
    this.complaintsTime = complaintsTime;
  }
}
