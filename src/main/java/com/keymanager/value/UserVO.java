package com.keymanager.value;

import java.sql.Timestamp;

public class UserVO {
	private String userID;
	private String userName;
	private String password;
	private String gender;
	private String qq;
	private String phone;
	private int percentage;
	private double accountAmount;
	private boolean autoPay;
	private String collectMethod;
	private int userLevel;
	private boolean vipType;
	private String clientIp;
	private int status;
	private Timestamp updateTime;
	private Timestamp createTime;

	public String getUserLevelName(){
		if (this.isVipType()){
			return "VIP用户";
		}else{
			switch (this.getUserLevel()){
			case 1:
				return "一级用户";
			case 2:
				return "二级用户";
			case 3:
				return "三级用户";
				default:
					return "非法用户";
			}
		}
	}
	
	public String getStatusName() {
		switch (this.getStatus()) {
		case 0:
			return "暂停";
		case 1:
			return "激活";
		default:
			return "非法状态";
		}
	}
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public boolean isVipType() {
		return vipType;
	}

	public void setVipType(boolean vipType) {
		this.vipType = vipType;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public double getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(double accountAmount) {
		this.accountAmount = accountAmount;
	}

	public boolean isAutoPay() {
		return autoPay;
	}

	public void setAutoPay(boolean autoPay) {
		this.autoPay = autoPay;
	}

	public String getCollectMethod() {
		return collectMethod;
	}

	public void setCollectMethod(String collectMethod) {
		this.collectMethod = collectMethod;
	}
}