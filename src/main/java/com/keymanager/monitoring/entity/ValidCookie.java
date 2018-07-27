package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_valid_cookie")
public class ValidCookie extends BaseEntity {

	@TableField(value = "fCookieCount")
	private int cookieCount;

	@TableField(value = "fCookieStr")
	private String cookieStr;

	@TableField(value = "fSearchEngine")
	private String searchEngine;

	public int getCookieCount() {
		return cookieCount;
	}

	public void setCookieCount(int cookieCount) {
		this.cookieCount = cookieCount;
	}

	public String getCookieStr() {
		return cookieStr;
	}

	public void setCookieStr(String cookieStr) {
		this.cookieStr = cookieStr;
	}

	public String getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(String searchEngine) {
		this.searchEngine = searchEngine;
	}
}
