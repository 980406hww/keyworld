package com.keymanager.value;

import java.sql.Date;


public class CustomerKeywordPositionSummaryVO{
	private int customerKeywordUuid;
	private int pcPosition;
	private int jisuPosition;
	private int chupingPosition;
	private Date createDate;

	public CustomerKeywordPositionSummaryVO(){
		super();
	}

	public int getCustomerKeywordUuid() {
		return customerKeywordUuid;
	}

	public void setCustomerKeywordUuid(int customerKeywordUuid) {
		this.customerKeywordUuid = customerKeywordUuid;
	}

	public int getPcPosition() {
		return pcPosition;
	}

	public void setPcPosition(int pcPosition) {
		this.pcPosition = pcPosition;
	}

	public int getJisuPosition() {
		return jisuPosition;
	}

	public void setJisuPosition(int jisuPosition) {
		this.jisuPosition = jisuPosition;
	}

	public int getChupingPosition() {
		return chupingPosition;
	}

	public void setChupingPosition(int chupingPosition) {
		this.chupingPosition = chupingPosition;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
