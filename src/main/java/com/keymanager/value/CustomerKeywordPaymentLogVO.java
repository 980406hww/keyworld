package com.keymanager.value;

import java.sql.Timestamp;

public class CustomerKeywordPaymentLogVO {
	private int uuid;
	private int customerKeywordUuid;
	private Timestamp effectiveFromTime;
	private Timestamp effectiveToTime;
	private double payable;
	private double realPaid;
	private Timestamp paidTime;
	private String remarks;
	private Timestamp createTime;

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public int getCustomerKeywordUuid() {
		return customerKeywordUuid;
	}

	public void setCustomerKeywordUuid(int customerKeywordUuid) {
		this.customerKeywordUuid = customerKeywordUuid;
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

	public Timestamp getPaidTime() {
		return paidTime;
	}

	public void setPaidTime(Timestamp paidTime) {
		this.paidTime = paidTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public double getPayable() {
		return payable;
	}

	public void setPayable(double payable) {
		this.payable = payable;
	}

	public double getRealPaid() {
		return realPaid;
	}

	public void setRealPaid(double realPaid) {
		this.realPaid = realPaid;
	}
}
