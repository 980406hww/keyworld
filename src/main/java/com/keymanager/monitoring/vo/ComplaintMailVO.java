package com.keymanager.monitoring.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by shunshikj01 on 2017/8/7.
 */
public class ComplaintMailVO {

  private String uuid;

  private String group;

  private String keyword;

  private Long tsNegativeKeywordUuid;

  private String tsNegativeKeyword;

  private Integer pcOccurTimes;

  private Integer phoneOccurTimes;

  private Timestamp pcComplainTime;

  private Timestamp phoneComplainTime;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public Long getTsNegativeKeywordUuid() {
    return tsNegativeKeywordUuid;
  }

  public void setTsNegativeKeywordUuid(Long tsNegativeKeywordUuid) {
    this.tsNegativeKeywordUuid = tsNegativeKeywordUuid;
  }

  public String getTsNegativeKeyword() {
    return tsNegativeKeyword;
  }

  public void setTsNegativeKeyword(String tsNegativeKeyword) {
    this.tsNegativeKeyword = tsNegativeKeyword;
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
}
