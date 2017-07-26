package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by shunshikj01 on 2017/7/17.
 */
@TableName("t_qz_charge_log")//收费流水表
public class QZChargeLog extends BaseEntity{
    @TableField("fQZOperationTypeUuid")
    private  Long qzOperationTypeUuid;//整站操作类型信息ID
    @TableField("fPlanChargeDate")
    private Date planChargeDate;//计划收费日期
    @TableField("fAcutalChargeDate")
    private Date acutalChargeDate;//实际收费日期
    @TableField ("fReceivableAmount")
    private BigDecimal receivableAmount;//应收金额
    @TableField("fActualAmount")
    private BigDecimal actualAmount;// 实收金额
    @TableField("fUserID")
    private String userID;// 收费人ID
    @TableField(exist = false)
    private String operationtype;//操作类型()
    @TableField(exist = false)
    private String userName;//收费人姓名

    public QZChargeLog() {
    }

    public QZChargeLog(Long qzOperationTypeUuid, Date planChargeDate, Date acutalChargeDate, BigDecimal receivableAmount, BigDecimal actualAmount, String userID) {
        this.qzOperationTypeUuid = qzOperationTypeUuid;
        this.planChargeDate = planChargeDate;
        this.acutalChargeDate = acutalChargeDate;
        this.receivableAmount = receivableAmount;
        this.actualAmount = actualAmount;
        this.userID = userID;
    }

    public Long getQzOperationTypeUuid() {
        return qzOperationTypeUuid;
    }

    public void setQzOperationTypeUuid(Long qzOperationTypeUuid) {
        this.qzOperationTypeUuid = qzOperationTypeUuid;
    }

    public Date getPlanChargeDate() {
        return planChargeDate;
    }

    public void setPlanChargeDate(Date planChargeDate) {
        this.planChargeDate = planChargeDate;
    }

    public Date getAcutalChargeDate() {
        return acutalChargeDate;
    }

    public void setAcutalChargeDate(Date acutalChargeDate) {
        this.acutalChargeDate = acutalChargeDate;
    }

    public BigDecimal getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(BigDecimal receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOperationtype() {
        return operationtype;
    }

    public void setOperationtype(String operationtype) {
        this.operationtype = operationtype;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
