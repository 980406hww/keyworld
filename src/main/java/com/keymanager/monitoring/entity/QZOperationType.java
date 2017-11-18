package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import com.baomidou.mybatisplus.enums.FieldStrategy;
import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj01 on 2017/7/7.
 * 操作类型表
 */
@TableName(value = "t_qz_operation_type")
public class QZOperationType extends BaseEntity {

  @TableField(value = "fQZSettingUuid")
  private Long qzSettingUuid;// 整站基本信息id

  @TableField(value = "fOperationtype")
  private String operationType;//操作类型()

  @TableField(value = "fInitialKeywordCount")
  private Long initialKeywordCount;//起始首页词数()

  @TableField(value = "fCurrentKeywordCount", validate = FieldStrategy.IGNORED)
  private Long currentKeywordCount;//当前首页词数()

  @TableField(value = "fGroup")
  private String group;//分组()

  @TableField(value = "fSubDomainName",validate= FieldStrategy.IGNORED)
  private String subDomainName;//二级域名()

  @TableField(value = "fReachTargetDate")
  private Date reachTargetDate;// 达标日期()

  @TableField(value = "fNextChargeDate")
  private Date nextChargeDate;// 下次收费日期()

  @TableField(value = "fIsDeleted")
  private Integer isDeleted;//状态

  @TableField(exist = false)
  protected List<QZChargeRule> qzChargeRules;//一个操作类型对应多个规则

  public String getSubDomainName() {
    return subDomainName;
  }

  public void setSubDomainName(String subDomainName) {
    this.subDomainName = subDomainName;
  }

  public Long getQzSettingUuid() {
    return qzSettingUuid;
  }

  public void setQzSettingUuid(Long qzSettingUuid) {
    this.qzSettingUuid = qzSettingUuid;
  }

  public String getOperationType() {
    return operationType;
  }

  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  public Long getInitialKeywordCount() {
    return initialKeywordCount;
  }

  public void setInitialKeywordCount(Long initialKeywordCount) {
    this.initialKeywordCount = initialKeywordCount;
  }

  public Long getCurrentKeywordCount() {
    return currentKeywordCount;
  }

  public void setCurrentKeywordCount(Long currentKeywordCount) {
    this.currentKeywordCount = currentKeywordCount;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public Date getReachTargetDate() {
    return reachTargetDate;
  }

  public void setReachTargetDate(Date reachTargetDate) {
    this.reachTargetDate = reachTargetDate;
  }

  public Date getNextChargeDate() {
    return nextChargeDate;
  }

  public void setNextChargeDate(Date nextChargeDate) {
    this.nextChargeDate = nextChargeDate;
  }

  public List<QZChargeRule> getQzChargeRules() {
    return qzChargeRules;
  }

  public void setQzChargeRules(List<QZChargeRule> qzChargeRules) {
    this.qzChargeRules = qzChargeRules;
  }

  public Integer getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Integer isDeleted) {
    this.isDeleted = isDeleted;
  }
}
