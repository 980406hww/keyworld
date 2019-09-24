package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 收费状态表
 *
 * @author lhc
 * @since 2019-09-21
 */
@TableName("t_qz_charge_status")
public class QZChargeStatus {

    /**
     * 主键ID
     */
    @TableId(value = "fUuid", type = IdType.AUTO)
    private Long uuid;

    /**
     * 全站对应表
     */
    @TableField("fQZSettingUuid")
    private Long qzSettingUuid;

    /**
     * 收费状态 1：续费中 0：暂停续费 2：首次收费 3：下架 4：其他
     */
    @TableField("fChargeStatus")
    private Integer chargeStatus;

    /**
     * 收费金额
     */
    @TableField("fChargeMoney")
    private BigDecimal chargeMoney;

    /**
     * 客户满意度 5：特别满意 4：满意 3：中等合适 2：不满意 1：特别不满意
     */
    @TableField("fCustomerSatisfaction")
    private Integer customerSatisfaction;

    /**
     * 状态备注
     */
    @TableField("fChargeStatusMsg")
    private String chargeStatusMsg;

    /**
     * 创建时间
     */
    @TableField("fCreateTime")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField("fLoginName")
    private String loginName;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getQzSettingUuid() {
        return qzSettingUuid;
    }

    public void setQzSettingUuid(Long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public Integer getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Integer chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public Integer getCustomerSatisfaction() {
        return customerSatisfaction;
    }

    public void setCustomerSatisfaction(Integer customerSatisfaction) {
        this.customerSatisfaction = customerSatisfaction;
    }

    public String getChargeStatusMsg() {
        return chargeStatusMsg;
    }

    public void setChargeStatusMsg(String chargeStatusMsg) {
        this.chargeStatusMsg = chargeStatusMsg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String toString() {
        return "QzChargeStatus{" +
            "Uuid=" + uuid +
            ", QZSettingUuid=" + qzSettingUuid +
            ", ChargeStatus=" + chargeStatus +
            ", ChargeMoney=" + chargeMoney +
            ", CustomerSatisfaction=" + customerSatisfaction +
            ", ChargeStatusMsg=" + chargeStatusMsg +
            ", CreateTime=" + createTime +
            ", LoginName=" + loginName +
            "}";
    }
}
