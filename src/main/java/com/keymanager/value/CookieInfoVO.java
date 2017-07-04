package com.keymanager.value;

import java.sql.Timestamp;

import com.keymanager.util.Constants;

public class CookieInfoVO {
	private String ip;
	private String domain;
	private String key;
	private String value;
	private Timestamp updateTime;
	private Timestamp createTime;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String toStr(){
		return String.format("%s" + Constants.COLUMN_SPLITTOR + "%s" + Constants.COLUMN_SPLITTOR + "%s" + Constants.COLUMN_SPLITTOR + "%s", this.getIp(), this.getDomain(), this.getKey(), this.getValue());
	}
	
	public String toKey(){
		return String.format("%s" + Constants.COLUMN_SPLITTOR + "%s" + Constants.COLUMN_SPLITTOR + "%s", this.getIp(), this.getDomain(), this.getKey());
	}
}
