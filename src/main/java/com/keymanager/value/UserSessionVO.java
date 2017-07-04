package com.keymanager.value;

import java.sql.Timestamp;

public class UserSessionVO {
	private int ID;
	private String computerID;
	private String IP;
	private int index;
	private int count;
	private Timestamp updateTime;
	private Timestamp createTime;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getComputerID() {
		return computerID;
	}

	public void setComputerID(String computerID) {
		this.computerID = computerID;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}