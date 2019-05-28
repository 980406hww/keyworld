package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * Created by shunshikj01 on 2017/7/7.
 * 收费规则
 */
@TableName(value = "t_qz_charge_rule")
public class QZChargeRule extends  BaseEntity {
    @TableField(value = "fQZOperationTypeUuid")
    private Long qzOperationTypeUuid;// 整站操作类型信息ID

    @TableField(value = "fStandardSpecies")
    private String standardSpecies; // 达标种类

    @TableField(value = "fStartKeywordCount")
    private Integer startKeywordCount;//起始词数()

    @TableField(value = "fEndKeywordCount")
    private Integer endKeywordCount;// 终止词数()

    @TableField(value = "fAmount")
    private Integer amount;//金额(fAmount)

    @TableField(exist = false)
    private Integer achieveLevel;

    @TableField(exist = false)
    private Double differenceValue;

    public Long getQzOperationTypeUuid() {
        return qzOperationTypeUuid;
    }

    public void setQzOperationTypeUuid(Long qzOperationTypeUuid) {
        this.qzOperationTypeUuid = qzOperationTypeUuid;
    }

    public String getStandardSpecies () {
        return standardSpecies;
    }

    public void setStandardSpecies (String standardSpecies) {
        this.standardSpecies = standardSpecies;
    }

    public Integer getStartKeywordCount() {
        return startKeywordCount;
    }

    public void setStartKeywordCount(Integer startKeywordCount) {
        this.startKeywordCount = startKeywordCount;
    }

    public Integer getEndKeywordCount() {
        return endKeywordCount;
    }

    public void setEndKeywordCount(Integer endKeywordCount) {
        this.endKeywordCount = endKeywordCount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAchieveLevel () {
        return achieveLevel;
    }

    public void setAchieveLevel (Integer achieveLevel) {
        this.achieveLevel = achieveLevel;
    }

    public Double getDifferenceValue () {
        return differenceValue;
    }

    public void setDifferenceValue (Double differenceValue) {
        this.differenceValue = differenceValue;
    }

    public QZChargeRule(Long qzOperationTypeUuid, Integer startKeywordCount, Integer endKeywordCount, Integer amount, String standardSpecies) {
        this.qzOperationTypeUuid = qzOperationTypeUuid;
        this.startKeywordCount = startKeywordCount;
        this.endKeywordCount = endKeywordCount;
        this.amount = amount;
        this.standardSpecies = standardSpecies;
    }

    public QZChargeRule() {
    }
}
