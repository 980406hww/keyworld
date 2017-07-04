package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_keyword")
public class Keyword extends BaseEntity{

	private static final long serialVersionUID = 2046259721350440670L;

	@TableField(value = "user_id")
	private Long userId;
	
	@TableField(value = "keyword")
	private String keyword;

	@TableField(value = "status")
	private String status;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
