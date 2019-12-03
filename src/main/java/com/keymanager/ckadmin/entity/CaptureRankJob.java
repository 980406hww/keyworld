package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;

import java.sql.Time;
import java.util.Date;

/**
 * Created by shunshikj24 on 2017/9/26.
 */
@TableName(value = "t_capture_rank_job")
public class CaptureRankJob extends BaseEntity {

    @TableField(value = "fGroupNames", strategy = FieldStrategy.IGNORED)
    private String groupNames;

    @TableField(value = "fCustomerUuid", strategy = FieldStrategy.IGNORED)
    private Long customerUuid;

    @TableField(value = "fQZSettingUuid")
    private Long qzSettingUuid;

    @TableField(exist = false)
    private String contactPerson;

    @TableField(value = "fOperationType")
    private String operationType;

    @TableField(value = "fCaptureDaysInterval")
    private Integer captureDaysInterval;

    @TableField(value = "fExectionType")
    private String exectionType;

    @TableField(value = "fRankJobType")
    private String rankJobType;

    @TableField(value = "fRankJobArea", strategy = FieldStrategy.IGNORED)
    private String rankJobArea;

    @TableField(value = "fRankJobCity", strategy = FieldStrategy.IGNORED)
    private String rankJobCity;

    @TableField(value = "fExectionTime")
    private Time exectionTime;

    @TableField(value = "fExectionStatus")
    private String exectionStatus;

    @TableField(value = "fCaptureRankJobStatus")
    private Boolean captureRankJobStatus;

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

    @TableField(value = "fCaptureInterval")
    private Integer captureInterval;

    @TableField(value = "fPageSize")
    private Integer pageSize;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCaptureInterval() {
        return captureInterval;
    }

    public void setCaptureInterval(Integer captureInterval) {
        this.captureInterval = captureInterval;
    }

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

    public Long getQzSettingUuid() {
        return qzSettingUuid;
    }

    public void setQzSettingUuid(Long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public Integer getCaptureDaysInterval() {
        return captureDaysInterval;
    }

    public void setCaptureDaysInterval(Integer captureDaysInterval) {
        this.captureDaysInterval = captureDaysInterval;
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

    public String getRankJobType() {
        return rankJobType;
    }

    public void setRankJobType(String rankJobType) {
        this.rankJobType = rankJobType;
    }

    public String getRankJobArea() {
        return rankJobArea;
    }

    public void setRankJobArea(String rankJobArea) {
        this.rankJobArea = rankJobArea;
    }

    public Boolean getCaptureRankJobStatus() {
        return captureRankJobStatus;
    }

    public void setCaptureRankJobStatus(Boolean captureRankJobStatus) {
        this.captureRankJobStatus = captureRankJobStatus;
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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getRankJobCity() {
        return rankJobCity;
    }

    public void setRankJobCity(String rankJobCity) {
        this.rankJobCity = rankJobCity;
    }

    @Override
    public String toString() {
        return "CaptureRankJob{" +
            "groupNames='" + groupNames + '\'' +
            ", customerUuid=" + customerUuid +
            ", qzSettingUuid=" + qzSettingUuid +
            ", contactPerson='" + contactPerson + '\'' +
            ", operationType='" + operationType + '\'' +
            ", captureDaysInterval=" + captureDaysInterval +
            ", exectionType='" + exectionType + '\'' +
            ", rankJobType='" + rankJobType + '\'' +
            ", rankJobArea='" + rankJobArea + '\'' +
            ", rankJobCity='" + rankJobCity + '\'' +
            ", exectionTime=" + exectionTime +
            ", exectionStatus='" + exectionStatus + '\'' +
            ", captureRankJobStatus=" + captureRankJobStatus +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", createBy='" + createBy + '\'' +
            ", updateBy='" + updateBy + '\'' +
            ", lastExecutionDate=" + lastExecutionDate +
            ", rowNumber=" + rowNumber +
            ", executionCycle=" + executionCycle +
            ", captureInterval=" + captureInterval +
            ", pageSize=" + pageSize +
            '}';
    }
}
