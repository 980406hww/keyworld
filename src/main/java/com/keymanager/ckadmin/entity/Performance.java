package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;

@TableName(value = "t_performance")
public class Performance {
	@TableField(value = "fModule")
	private String module;

	@TableField(value = "fMilleSecond")
	private double milleSecond;

	@TableField(value = "fCurrentMemory")
	private double currentMemory;

	@TableField(value = "fRemarks")
	private String remarks;

	@TableField(value = "fCreateTime")
	private Date createTime;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public double getMilleSecond() {
		return milleSecond;
	}

	public void setMilleSecond(double milleSecond) {
		this.milleSecond = milleSecond;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public double getCurrentMemory() {
		return currentMemory;
	}

	public void setCurrentMemory(double currentMemory) {
		this.currentMemory = currentMemory;
	}
}
