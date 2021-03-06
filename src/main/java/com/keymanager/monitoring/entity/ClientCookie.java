package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_client_cookie")
public class ClientCookie extends BaseEntity {

	@TableField(value = "fClientID")
	private String clientID;

	@TableField(value = "fCookieCount")
	private int cookieCount;

	@TableField(value = "fCookieStr")
	private String cookieStr;

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

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
}