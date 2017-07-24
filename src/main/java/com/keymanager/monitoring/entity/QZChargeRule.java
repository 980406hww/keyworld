package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by shunshikj01 on 2017/7/7.
 * 收费规则
 */
@TableName(value = "t_qz_charge_rule")
public class QZChargeRule extends  BaseEntity {
    @TableField(value = "fQZOperationTypeUuid")
    private Long qzOperationTypeUuid;// 整站操作类型信息ID

    @TableField(value = "fStartKeywordCount")
    private Long   startKeywordCount;//起始词数()

    @TableField(value = "fEndKeywordCount")
    private Long   endKeywordCount;// 终止词数()

    @TableField(value = "fAmount")
    private BigDecimal amount;//金额(fAmount)

    public Long getQzOperationTypeUuid() {
        return qzOperationTypeUuid;
    }

    public void setQzOperationTypeUuid(Long qzOperationTypeUuid) {
        this.qzOperationTypeUuid = qzOperationTypeUuid;
    }

    public Long getStartKeywordCount() {
        return startKeywordCount;
    }

    public void setStartKeywordCount(Long startKeywordCount) {
        this.startKeywordCount = startKeywordCount;
    }

    public Long getEndKeywordCount() {
        return endKeywordCount;
    }

    public void setEndKeywordCount(Long endKeywordCount) {
        this.endKeywordCount = endKeywordCount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public QZChargeRule(Long qzOperationTypeUuid, Long startKeywordCount, Long endKeywordCount, BigDecimal amount) {
        this.qzOperationTypeUuid = qzOperationTypeUuid;
        this.startKeywordCount = startKeywordCount;
        this.endKeywordCount = endKeywordCount;
        this.amount = amount;
    }

    public QZChargeRule() {
    }
}
