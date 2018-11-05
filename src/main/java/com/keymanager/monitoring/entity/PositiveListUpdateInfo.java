package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_positive_list_update_info")
public class PositiveListUpdateInfo extends BaseEntity {

	@TableField(value = "fPid")
	private Long pid;

	@TableField(value = "fOptimizeMethod")
	private String optimizeMethod;

	@TableField(value = "fPosition")
	private Integer position;

	public Long getPid () {
		return pid;
	}

	public void setPid (Long pid) {
		this.pid = pid;
	}

	public String getOptimizeMethod () {
		return optimizeMethod;
	}

	public void setOptimizeMethod (String optimizeMethod) {
		this.optimizeMethod = optimizeMethod;
	}

	public Integer getPosition () {
		return position;
	}

	public void setPosition (Integer position) {
		this.position = position;
	}
}
