package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author lhc
 * @since 2019-10-22
 */
@TableName("t_qz_rate_statistics")
public class QZRateStatistics {

    @TableId(value = "fUuid", type = IdType.AUTO)
    private Long uuid;

    /**
     * 全站id
     */
    @TableField("fQZSettingUuid")
    private Long qzSettingUuid;

    /**
     * 客户id
     */
    @TableField("fCustomerUuid")
    private Long customerUuid;

    /**
     * 终端类型
     */
    @TableField("fTerminalType")
    private String terminalType;

    /**
     * 涨幅
     */
    @TableField("fRate")
    private Integer rate;

    /**
     * 记录日期
     */
    @TableField("fRateDate")
    private String rateDate;

    /**
     * 带年份记录日期
     */
    @TableField("fRateFullDate")
    private String rateFullDate;

    @TableField("fCreateTime")
    private Date createTime;

    @TableField("fUpdateTime")
    private Date updateTime;

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

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getRateDate() {
        return rateDate;
    }

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public String getRateFullDate() {
        return rateFullDate;
    }

    public void setRateFullDate(String rateFullDate) {
        this.rateFullDate = rateFullDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
