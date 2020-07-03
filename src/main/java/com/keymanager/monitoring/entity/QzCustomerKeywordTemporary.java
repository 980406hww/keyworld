package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "t_qz_customer_keyword")
public class QzCustomerKeywordTemporary implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "fUuid")
    private Long uuid;

    @TableField(value = "fCurrentPosition")
    private Integer currentPosition;

    @TableField(value = "fCapturePositionQueryTime")
    private Date capturePositionQueryTime;

    @TableField(value = "fOptimizePlanCount")
    private int optimizePlanCount;

    @TableField(value = "fOptimizedCount")
    private int optimizedCount;

    @TableField(value = "fMark")
    private Integer mark;

    @TableField(value = "fOperaStatus")
    private Integer operaStatus;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Date getCapturePositionQueryTime() {
        return capturePositionQueryTime;
    }

    public void setCapturePositionQueryTime(Date capturePositionQueryTime) {
        this.capturePositionQueryTime = capturePositionQueryTime;
    }

    public int getOptimizePlanCount() {
        return optimizePlanCount;
    }

    public void setOptimizePlanCount(int optimizePlanCount) {
        this.optimizePlanCount = optimizePlanCount;
    }

    public int getOptimizedCount() {
        return optimizedCount;
    }

    public void setOptimizedCount(int optimizedCount) {
        this.optimizedCount = optimizedCount;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Integer getOperaStatus() {
        return operaStatus;
    }

    public void setOperaStatus(Integer operaStatus) {
        this.operaStatus = operaStatus;
    }
}
