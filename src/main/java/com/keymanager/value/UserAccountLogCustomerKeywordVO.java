package com.keymanager.value;

import java.sql.Timestamp;

public class UserAccountLogCustomerKeywordVO extends CustomerKeywordCommonPartVO {
	private int uuid;
	private int userAccountLogUuid;
	private int customerKeywordUuid;
	private String userID;
	private String userCollectMethod;

	private Timestamp createTime;

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public int getUserAccountLogUuid() {
		return userAccountLogUuid;
	}

	public void setUserAccountLogUuid(int userAccountLogUuid) {
		this.userAccountLogUuid = userAccountLogUuid;
	}

	public int getCustomerKeywordUuid() {
		return customerKeywordUuid;
	}

	public void setCustomerKeywordUuid(int customerKeywordUuid) {
		this.customerKeywordUuid = customerKeywordUuid;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public double getJisuFee() {
		return this.getJisuFee(this.getJisuCurrentPosition());
	}

	@Override
	public double getPcFee() {
		return this.getPcFee(this.getCurrentPosition());
	}

	@Override
	public double getChupingFee() {
		return this.getChupingFee(this.getChupingCurrentPosition());
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserCollectMethod() {
		return userCollectMethod;
	}

	public void setUserCollectMethod(String userCollectMethod) {
		this.userCollectMethod = userCollectMethod;
	}
}