package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj24 on 2017/9/26.
 */
@TableName(value = "t_capture_current_rank_job")
public class CaptureCurrentRankJob {
    @TableId(value = "fUuid", type= IdType.AUTO)
    private Long uuid;

    @TableField(value = "fGroupNames")
    private String groupNames;

    @TableField(value = "fCustomerIds")
    private String customerIds;

    @TableField(value = "fOperationType")
    private String operationType;

    @TableField(value = "fExectionTime")
    private String exectionTime;

    @TableField(value = "fExectionStatus")
    private String exectionStatus;

    @TableField(value = "fStartTime")
    private Date startTime;

    @TableField(value = "fEndTime")
    private Date endTime;

    @TableField(value = "fCreateBy")
    private Long createBy;

    @TableField(value = "fCreateTime")
    private Date createTime;

    @TableField(value = "fUpdateBy")
    private Long updateBy;

    @TableField(value = "fUpdateTime")
    private Date updateTime;

    @TableField(value = "fLastExecutionDate")
    private Date lastExecutionDate;

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLastExecutionDate() {
        return lastExecutionDate;
    }

    public void setLastExecutionDate(Date lastExecutionDate) {
        this.lastExecutionDate = lastExecutionDate;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    public String getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(String customerIds) {
        this.customerIds = customerIds;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getExectionTime() {
        return exectionTime;
    }

    public void setExectionTime(String exectionTime) {
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
}
