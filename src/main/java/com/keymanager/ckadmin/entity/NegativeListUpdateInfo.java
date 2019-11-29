package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;

@TableName(value = "t_negative_list_update_info")
public class NegativeListUpdateInfo extends BaseEntity {

	@TableField(value = "fKeyword")
	private String keyword;

	@TableField(value = "fNegativeListUpdateTime")
	private Date negativeListUpdateTime;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Date getNegativeListUpdateTime() {
		return negativeListUpdateTime;
	}

	public void setNegativeListUpdateTime(Date negativeListUpdateTime) {
		this.negativeListUpdateTime = negativeListUpdateTime;
	}
}
