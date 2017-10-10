package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/26.
 */
@TableName(value = "t_capture_rank_job")
public class CaptureRankJob extends BaseEntity{

    @TableField(value = "fGroupNames",validate= FieldStrategy.IGNORED)
    private String groupNames;

    @TableField(value = "fCustomerUuid")
    private Long customerUuid;

    @TableField(value = "fOperationType")
    private String operationType;

    @TableField(value = "fExectionType")
    private String exectionType;

    @TableField(value = "fExectionTime")
    private java.sql.Time exectionTime;

    @TableField(value = "fExectionStatus")
    private String exectionStatus;

    @TableField(value = "fStartTime")
    private Date startTime;

    @TableField(value = "fEndTime")
    private Date endTime;

    @TableField(value = "fCreateBy")
    private String createBy;

    @TableField(value = "fUpdateBy")
    private String updateBy;

    @TableField(value = "fLastExecutionDate")
    private java.sql.Date lastExecutionDate;

    @TableField(value = "fRowNumber")
    private Integer rowNumber;

    @TableField(value = "fExecutionCycle")
    private Integer executionCycle;

    public Integer getExecutionCycle() {
        return executionCycle;
    }

    public void setExecutionCycle(Integer executionCycle) {
        this.executionCycle = executionCycle;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }


    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }



    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getExectionType() {
        return exectionType;
    }

    public void setExectionType(String exectionType) {
        this.exectionType = exectionType;
    }

    public Time getExectionTime() {
        return exectionTime;
    }

    public void setExectionTime(Time exectionTime) {
        this.exectionTime = exectionTime;
    }

    public String getExectionStatus() {
        return exectionStatus;
    }

    public void setExectionStatus(String exectionStatus) {
        this.exectionStatus = exectionStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public java.sql.Date getLastExecutionDate() {
        return lastExecutionDate;
    }

    public void setLastExecutionDate(java.sql.Date lastExecutionDate) {
        this.lastExecutionDate = lastExecutionDate;
    }
}
