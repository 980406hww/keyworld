package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;

@TableName(value = "t_daily_report")
public class DailyReport extends BaseEntity{
	private static final long serialVersionUID = -1101942701283949852L;

	@TableField(value = "fUserID")
	private String userID;

	@TableField(value = "fTerminalType")
	private String terminalType;

	@TableField(value = "fTriggerTime")
	private Date triggerTime;

	@TableField(value = "fCompleteTime")
	private Date completeTime;

	@TableField(value = "fReportPath")
	private String reportPath;

	@TableField(value = "fTriggerMode")
	private String triggerMode;

	@TableField(value = "fStatus")
	private String status;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public Date getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(Date triggerTime) {
		this.triggerTime = triggerTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getTriggerMode() {
		return triggerMode;
	}

	public void setTriggerMode(String triggerMode) {
		this.triggerMode = triggerMode;
	}
}
