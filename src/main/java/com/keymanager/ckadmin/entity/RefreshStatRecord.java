package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName(value = "t_ck_refresh_stat_record")
public class RefreshStatRecord implements Serializable {

    private static final long serialVersionUID = 3922222059082125030L;

    @TableId(value = "fUuid", type = IdType.AUTO)
    private Long uuid;

    @TableField(value = "fGroup")
    private String group;

    @TableField(value = "fType")
    private String type;

    @TableField(value = "fTerminalType")
    private String terminalType;

    @TableField(value = "fTotalKeywordCount")
    private int totalKeywordCount;

    @TableField(value = "fNeedOptimizeKeywordCount")
    private int needOptimizeKeywordCount;

    @TableField(value = "fInvalidKeywordCount")
    private int invalidKeywordCount;

    @TableField(value = "fFailedKeywordCount")
    private int failedKeywordCount;

    @TableField(value = "fZeroOptimizedCount")
    private int zeroOptimizedCount;

    @TableField(value = "fReachStandardKeywordCount")
    private int reachStandardKeywordCount;

    @TableField(value = "fTodaySubTotal")
    private double todaySubTotal;

    @TableField(value = "fTotalOptimizeCount")
    private int totalOptimizeCount;

    @TableField(value = "fTotalOptimizedCount")
    private int totalOptimizedCount;

    @TableField(value = "fNeedOptimizeCount")
    private int needOptimizeCount;

    @TableField(value = "fQueryCount")
    private int queryCount;

    @TableField(value = "fTotalMachineCount")
    private int totalMachineCount;

    @TableField(value = "fUnworkMachineCount")
    private int unworkMachineCount;

    @TableField(value = "fMaxInvalidCount")
    private int maxInvalidCount;

    @TableField(value = "fIdleTotalMinutes")
    private long idleTotalMinutes;

    @TableField(value = "fCreateDate")
    private Date createDate;

    @TableField(exist = false)
    private Double reachStandardPercentage;

    @TableField(exist = false)
    private Double invalidKeywordPercentage;

    @TableField(exist = false)
    private Double avgOptimizedCount;

    @TableField(exist = false)
    private Double invalidOptimizePercentage;

    public Double getReachStandardPercentage() {
        return reachStandardPercentage;
    }

    public void setReachStandardPercentage() {
        this.reachStandardPercentage = (this.totalKeywordCount > 0) ? (this.reachStandardKeywordCount * 1.0) / this.totalKeywordCount * 100 : 0;
    }

    public Double getInvalidKeywordPercentage() {
        return invalidKeywordPercentage;
    }

    public void setInvalidKeywordPercentage() {
        this.invalidKeywordPercentage = (this.totalKeywordCount > 0) ? ((this.invalidKeywordCount * 1.0) / this.totalKeywordCount) * 100 : 0;
    }

    public Double getAvgOptimizedCount() {
        return avgOptimizedCount;
    }

    public void setAvgOptimizedCount() {
        this.avgOptimizedCount = (this.totalKeywordCount > 0) ? this.totalOptimizedCount * 1.0 / this.totalKeywordCount : 0;
    }

    public Double getInvalidOptimizePercentage() {
        return invalidOptimizePercentage;
    }

    public void setInvalidOptimizePercentage() {
        this.invalidOptimizePercentage = (this.queryCount > 0) ? (((this.queryCount - this.totalOptimizedCount) * 1.0) / this.queryCount) * 100 : 0;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public int getTotalKeywordCount() {
        return totalKeywordCount;
    }

    public void setTotalKeywordCount(int totalKeywordCount) {
        this.totalKeywordCount = totalKeywordCount;
    }

    public int getNeedOptimizeKeywordCount() {
        return needOptimizeKeywordCount;
    }

    public void setNeedOptimizeKeywordCount(int needOptimizeKeywordCount) {
        this.needOptimizeKeywordCount = needOptimizeKeywordCount;
    }

    public int getInvalidKeywordCount() {
        return invalidKeywordCount;
    }

    public void setInvalidKeywordCount(int invalidKeywordCount) {
        this.invalidKeywordCount = invalidKeywordCount;
    }

    public int getTotalOptimizeCount() {
        return totalOptimizeCount;
    }

    public void setTotalOptimizeCount(int totalOptimizeCount) {
        this.totalOptimizeCount = totalOptimizeCount;
    }

    public int getTotalOptimizedCount() {
        return totalOptimizedCount;
    }

    public void setTotalOptimizedCount(int totalOptimizedCount) {
        this.totalOptimizedCount = totalOptimizedCount;
    }

    public int getNeedOptimizeCount() {
        return needOptimizeCount;
    }

    public void setNeedOptimizeCount(int needOptimizeCount) {
        this.needOptimizeCount = needOptimizeCount;
    }

    public int getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(int queryCount) {
        this.queryCount = queryCount;
    }

    public int getTotalMachineCount() {
        return totalMachineCount;
    }

    public void setTotalMachineCount(int totalMachineCount) {
        this.totalMachineCount = totalMachineCount;
    }

    public int getUnworkMachineCount() {
        return unworkMachineCount;
    }

    public void setUnworkMachineCount(int unworkMachineCount) {
        this.unworkMachineCount = unworkMachineCount;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getMaxInvalidCount() {
        return maxInvalidCount;
    }

    public void setMaxInvalidCount(int maxInvalidCount) {
        this.maxInvalidCount = maxInvalidCount;
    }

    public int getZeroOptimizedCount() {
        return zeroOptimizedCount;
    }

    public void setZeroOptimizedCount(int zeroOptimizedCount) {
        this.zeroOptimizedCount = zeroOptimizedCount;
    }

    public int getReachStandardKeywordCount() {
        return reachStandardKeywordCount;
    }

    public void setReachStandardKeywordCount(int reachStandardKeywordCount) {
        this.reachStandardKeywordCount = reachStandardKeywordCount;
    }

    public double getTodaySubTotal() {
        return todaySubTotal;
    }

    public void setTodaySubTotal(double todaySubTotal) {
        this.todaySubTotal = todaySubTotal;
    }

    public long getIdleTotalMinutes() {
        return idleTotalMinutes;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setIdleTotalMinutes(long idleTotalMinutes) {
        this.idleTotalMinutes = idleTotalMinutes;
    }

    public int getFailedKeywordCount() {
        return failedKeywordCount;
    }

    public void setFailedKeywordCount(int failedKeywordCount) {
        this.failedKeywordCount = failedKeywordCount;
    }

    public double getIdlePercentage() {
        if (this.getIdleTotalMinutes() > 0) {
            BigDecimal b = new BigDecimal(this.getIdleTotalMinutes() / (this.getTotalMachineCount() * 24 * 60.0));
            return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
        } else {
            return 0;
        }
    }
}
