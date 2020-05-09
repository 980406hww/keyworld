package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

public class UserRefreshStatisticInfo implements Serializable {
    private static final long serialVersionUID = 3922222059082125030L;

    @TableId(value = "fUuid", type = IdType.AUTO)
    private Long uuid;

    @TableField(value = "fUserName")
    protected String userName;

    @TableField(value = "fType")
    protected String type;

    @TableField(value = "fTerminalType")
    protected String terminalType;

    @TableField(value = "fTotalKeywordCount")
    protected int totalKeywordCount;

    @TableField(value = "fNeedOptimizeKeywordCount")
    protected int needOptimizeKeywordCount;

    @TableField(value = "fZeroOptimizedCount")
    protected int zeroOptimizedCount;

    @TableField(value = "fReachStandardKeywordCount")
    protected int reachStandardKeywordCount;

    @TableField(value = "fTodaySubTotal")
    protected double todaySubTotal;

    @TableField(value = "fTotalOptimizeCount")
    protected int totalOptimizeCount;

    @TableField(value = "fTotalOptimizedCount")
    protected int totalOptimizedCount;

    @TableField(value = "fNeedOptimizeCount")
    protected int needOptimizeCount;

    @TableField(value = "fQueryCount")
    protected int queryCount;

    @TableField(value = "fTotalMachineCount")
    protected int totalMachineCount;

    @TableField(value = "fUnworkMachineCount")
    protected int unworkMachineCount;

    @TableField(value = "fMaxInvalidCount")
    protected int maxInvalidCount;

    @TableField(value = "fIdleTotalMinutes")
    protected long idleTotalMinutes;

    @TableField(value = "fCreateDate")
    private Date createDate;

    @TableField(exist = false)
    private Double reachStandardPercentage;

    @TableField(exist = false)
    private Double avgOptimizedCount;

    @TableField(exist = false)
    private Double idlePercentage;

    @TableField(exist = false)
    private Double invalidOptimizePercentage;

    public void setInvalidOptimizePercentage() {
        this.invalidOptimizePercentage = (this.queryCount > 0) ? (((this.queryCount - this.totalOptimizedCount) * 1.0) / this.queryCount) * 100 : 0;
    }

    public Double getInvalidOptimizePercentage() {
        return invalidOptimizePercentage;
    }

    public void setReachStandardPercentage() {
        this.reachStandardPercentage = (this.totalKeywordCount > 0) ? (this.reachStandardKeywordCount * 1.0) / this.totalKeywordCount * 100 : 0;
    }

    public void setAvgOptimizedCount() {
        this.avgOptimizedCount = (this.totalKeywordCount > 0) ? this.totalOptimizedCount * 1.0 / this.totalKeywordCount : 0;
    }

    public void setIdlePercentage() {
        this.idlePercentage = (this.totalMachineCount > 0) ? this.idleTotalMinutes / (this.totalMachineCount * 24 * 60.0) * 100 : 0;
    }

    public Double getAvgOptimizedCount() {
        return avgOptimizedCount;
    }

    public Double getReachStandardPercentage() {
        return reachStandardPercentage;
    }

    public Double getIdlePercentage() {
        return idlePercentage;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
