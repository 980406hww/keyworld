package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_daily_report_item")
public class DailyReportItem extends BaseEntity{
	private static final long serialVersionUID = -1101942701283949852L;

	@TableField(value = "fDailyReportUuid")
	private int dailyReportUuid;

	@TableField(value = "fCustomerUuid")
	private int customerUuid;

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

	public int getDailyReportUuid() {
		return dailyReportUuid;
	}

	public void setDailyReportUuid(int dailyReportUuid) {
		this.dailyReportUuid = dailyReportUuid;
	}
}
