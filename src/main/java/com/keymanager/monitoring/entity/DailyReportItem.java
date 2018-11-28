package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_daily_report_item")
public class DailyReportItem extends BaseEntity{
	private static final long serialVersionUID = -1101942701283949852L;

	@TableField(value = "fDailyReportUuid")
	private long dailyReportUuid;

	@TableField(value = "fCustomerUuid")
	private int customerUuid;

	@TableField(value = "fTerminalType")
	private String terminalType;

	@TableField(value = "fStatus")
	private String status;

	public int getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(int customerUuid) {
		this.customerUuid = customerUuid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getDailyReportUuid() {
		return dailyReportUuid;
	}

	public void setDailyReportUuid(long dailyReportUuid) {
		this.dailyReportUuid = dailyReportUuid;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
}
