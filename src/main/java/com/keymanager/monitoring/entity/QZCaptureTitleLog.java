package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;

@TableName(value = "t_qz_capture_title_log")
public class QZCaptureTitleLog extends BaseEntity{
	private static final long serialVersionUID = -1101942701283949852L;

	@TableField(value = "fQZOperationTypeUuid")
	private long qzOperationTypeUuid;

	@TableField(exist=false)
	private Integer customerUuid;

	@TableField(exist=false)
	private String group;

	@TableField(value = "fStatus")
	private String status;

	@TableField(exist=false)
	private String type;

	@TableField(value = "fTerminalType")
	private String terminalType;

	@TableField(value = "fStartTime")
	private Date startTime;

	@TableField(value = "fEndTime")
	private Date endTime;

	public long getQzOperationTypeUuid() {
		return qzOperationTypeUuid;
	}

	public void setQzOperationTypeUuid(long qzOperationTypeUuid) {
		this.qzOperationTypeUuid = qzOperationTypeUuid;
	}

	public Integer getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(Integer customerUuid) {
		this.customerUuid = customerUuid;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
}
