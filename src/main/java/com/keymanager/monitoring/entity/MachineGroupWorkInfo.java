package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author zhoukai
 * @Date 2018/11/28 17:32
 **/
@TableName(value = "t_machine_group_work_info")
public class MachineGroupWorkInfo implements Serializable{

    private static final long serialVersionUID = 3922222059082125030L;

    @TableId(value = "fUuid", type= IdType.AUTO)
    private Long uuid;

    @TableField(value = "fMachineGroup")
    protected String machineGroup;

    @TableField(value = "fType")
    protected String type;

    @TableField(value = "fTerminalType")
    protected String terminalType;

    @TableField(value = "fTotalKeywordCount")
    protected int totalKeywordCount;

    @TableField(value = "fNeedOptimizeKeywordCount")
    protected int needOptimizeKeywordCount;

    @TableField(value = "fInvalidKeywordCount")
    protected int invalidKeywordCount;

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

    public Long getUuid () {
        return uuid;
    }

    public void setUuid (Long uuid) {
        this.uuid = uuid;
    }

    public double getInvalidKeywordPercentage(){
        return (this.getTotalKeywordCount() > 0) ? ((this.getInvalidKeywordCount() * 1.0) / this.getTotalKeywordCount()) * 100 : 0;
    }

    public double getInvalidOptimizePercentage(){
        return (this.getQueryCount() > 0) ? (((this.getQueryCount() - this.getTotalOptimizedCount()) * 1.0) / this.getQueryCount()) * 100 : 0;
    }

    public double getReachStandardPercentage() {
        return (this.getReachStandardKeywordCount() * 1.0) / this.getTotalKeywordCount() * 100;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
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

    public Date getCreateDate () {
        return createDate;
    }

    public void setCreateDate (Date createDate) {
        this.createDate = createDate;
    }

    public void setIdleTotalMinutes(long idleTotalMinutes) {
        this.idleTotalMinutes = idleTotalMinutes;
    }

    public double getIdlePercentage(){
        if(this.getIdleTotalMinutes() > 0) {
            BigDecimal b = new BigDecimal(this.getIdleTotalMinutes() / (this.getTotalMachineCount() * 24 * 60.0));
            return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100;
        }else{
            return 0;
        }
    }

    public String getMachineGroup() {
        return machineGroup;
    }

    public void setMachineGroup(String machineGroup) {
        this.machineGroup = machineGroup;
    }
}
