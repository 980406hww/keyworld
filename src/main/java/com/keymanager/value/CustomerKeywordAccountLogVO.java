package com.keymanager.value;

import java.sql.Timestamp;

import com.keymanager.util.Constants;

public class CustomerKeywordAccountLogVO {
	private int uuid;
	private String type;
	private int customerKeywordUuid;
	private String month;
	private Timestamp effectiveFromTime;
	private Timestamp effectiveToTime;
	private double receivable;
	private double firstRealCollection;
	private Timestamp firstReceivedTime;
	private double secondRealCollection;
	private Timestamp secondReceivedTime;
	private String remarks;
	private String status;
	private Timestamp updateTime;
	private Timestamp createTime;

	public boolean isUnPaid(){
		return Constants.ACCOUNT_LOG_STATUS_UN_PAID.equals(this.getStatus());
	}
	
	public String statusName(){
		return Constants.ACCOUNT_LOG_STATUS_MAP.get(this.getStatus());
	}
	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Timestamp getEffectiveFromTime() {
		return effectiveFromTime;
	}

	public void setEffectiveFromTime(Timestamp effectiveFromTime) {
		this.effectiveFromTime = effectiveFromTime;
	}

	public Timestamp getEffectiveToTime() {
		return effectiveToTime;
	}

	public void setEffectiveToTime(Timestamp effectiveToTime) {
		this.effectiveToTime = effectiveToTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getCustomerKeywordUuid() {
		return customerKeywordUuid;
	}

	public void setCustomerKeywordUuid(int customerKeywordUuid) {
		this.customerKeywordUuid = customerKeywordUuid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Timestamp getFirstReceivedTime() {
		return firstReceivedTime;
	}

	public void setFirstReceivedTime(Timestamp firstReceivedTime) {
		this.firstReceivedTime = firstReceivedTime;
	}

	public Timestamp getSecondReceivedTime() {
		return secondReceivedTime;
	}

	public void setSecondReceivedTime(Timestamp secondReceivedTime) {
		this.secondReceivedTime = secondReceivedTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getReceivable() {
		return receivable;
	}

	public void setReceivable(double receivable) {
		this.receivable = receivable;
	}

	public double getFirstRealCollection() {
		return firstRealCollection;
	}

	public void setFirstRealCollection(double firstRealCollection) {
		this.firstRealCollection = firstRealCollection;
	}

	public double getSecondRealCollection() {
		return secondRealCollection;
	}

	public void setSecondRealCollection(double secondRealCollection) {
		this.secondRealCollection = secondRealCollection;
	}
}
