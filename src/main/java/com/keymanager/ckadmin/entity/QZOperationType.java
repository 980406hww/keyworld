package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import java.util.Date;
import java.util.List;

/**
 * 操作类型表
 */
@TableName(value = "t_qz_operation_type")
public class QZOperationType extends BaseEntity {

    /**
     * 整站基本信息id
     */
    @TableField(value = "fQZSettingUuid")
    private Long qzSettingUuid;
    /**
     * 操作类型
     */
    @TableField(value = "fOperationtype")
    private String operationType;
    /**
     * 达标优化类型
     */
    @TableField(value = "fOptimizationType")
    private int optimizationType;
    /**
     * 起始首页词数
     */
    @TableField(value = "fInitialKeywordCount")
    private Long initialKeywordCount;
    /**
     * 当前首页词数
     */
    @TableField(value = "fCurrentKeywordCount", strategy = FieldStrategy.IGNORED)
    private Long currentKeywordCount;
    /**
     * 分组
     */
    @TableField(value = "fGroup")
    private String group;
    /**
     * 二级域名
     */
    @TableField(value = "fSubDomainName", strategy = FieldStrategy.IGNORED)
    private String subDomainName;
    /**
     * 达标备注
     */
    @TableField(value = "fMonitorRemark")
    private String monitorRemark;
    /**
     * 达标日期
     */
    @TableField(value = "fReachTargetDate")
    private Date reachTargetDate;
    /**
     * 下次收费日期
     */
    @TableField(value = "fNextChargeDate")
    private Date nextChargeDate;
    /**
     * 状态
     */
    @TableField(value = "fIsDeleted")
    private Integer isDeleted;
    /**
     * 最大限制词数
     */
    @TableField(value = "fMaxKeywordCount")
    private Integer maxKeywordCount;
    /**
     * 达标类型
     */
    @TableField(value = "fStandardType")
    private String standardType;
    /**
     * 达标时间
     */
    @TableField(value = "fStandardTime")
    private Date standardTime;
    /**
     * 一个操作类型对应多个规则
     */
    @TableField(exist = false)
    private List<QZChargeRule> qzChargeRules;

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

    public Integer getMaxKeywordCount() {
        return maxKeywordCount;
    }

    public void setMaxKeywordCount(Integer maxKeywordCount) {
        this.maxKeywordCount = maxKeywordCount;
    }

    public String getStandardType() {
        return standardType;
    }

    public void setStandardType(String standardType) {
        this.standardType = standardType;
    }

    public Date getStandardTime() {
        return standardTime;
    }

    public void setStandardTime(Date standardTime) {
        this.standardTime = standardTime;
    }

    public int getOptimizationType() {
        return optimizationType;
    }

    public void setOptimizationType(int optimizationType) {
        this.optimizationType = optimizationType;
    }

    public String getMonitorRemark() {
        return monitorRemark;
    }

    public void setMonitorRemark(String monitorRemark) {
        this.monitorRemark = monitorRemark;
    }
}
