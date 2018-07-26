package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_client_cookie")
public class ClientCookie extends BaseEntity {

	@TableField(value = "fClientID")
	private String clientID;

	@TableField(value = "fCookieUuid")
	private int cookieUuid;

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public int getCookieUuid() {
		return cookieUuid;
	}

	public void setCookieUuid(int cookieUuid) {
		this.cookieUuid = cookieUuid;
	}
 }
