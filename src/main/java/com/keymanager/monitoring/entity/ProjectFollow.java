package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_project_follow")
public class ProjectFollow extends BaseEntity {

	@TableField(value = "fCustomerUuid")
	private int customerUuid;

	@TableField(value = "fFollowContent")
	private String followContent;

	@TableField(value = "fFollowPerson")
	private String followPerson;

	public int getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(int customerUuid) {
		this.customerUuid = customerUuid;
	}

	public String getFollowContent() {
		return followContent;
	}

	public void setFollowContent(String followContent) {
		this.followContent = followContent;
	}

	public String getFollowPerson() {
		return followPerson;
	}

	public void setFollowPerson(String followPerson) {
		this.followPerson = followPerson;
	}
}
