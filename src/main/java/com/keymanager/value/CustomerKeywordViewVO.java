package com.keymanager.value;

import java.sql.Timestamp;


public class CustomerKeywordViewVO extends CustomerKeywordVO{
	private String contactPerson;
	private String qq;
	private String email;
	private String telphone;
	private int paidFee;
	private String type;
	private int customerStatus;
	private String month;
	private Timestamp accountLogEffectiveFromTime;
	private Timestamp accountEffectiveToTime;
	private int receivable;
	private int firstRealCollection;
	private Timestamp firstReceivedTime;
	private int secondRealCollection;
	private Timestamp secondReceivedTime;
	private String accountStatus;

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public int getPaidFee() {
		return paidFee;
	}

	public void setPaidFee(int paidFee) {
		this.paidFee = paidFee;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(int customerStatus) {
		this.customerStatus = customerStatus;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Timestamp getAccountLogEffectiveFromTime() {
		return accountLogEffectiveFromTime;
	}

	public void setAccountLogEffectiveFromTime(Timestamp accountLogEffectiveFromTime) {
		this.accountLogEffectiveFromTime = accountLogEffectiveFromTime;
	}

	public Timestamp getAccountEffectiveToTime() {
		return accountEffectiveToTime;
	}

	public void setAccountEffectiveToTime(Timestamp accountEffectiveToTime) {
		this.accountEffectiveToTime = accountEffectiveToTime;
	}

	public int getReceivable() {
		return receivable;
	}

	public void setReceivable(int receivable) {
		this.receivable = receivable;
	}

	public int getFirstRealCollection() {
		return firstRealCollection;
	}

	public void setFirstRealCollection(int firstRealCollection) {
		this.firstRealCollection = firstRealCollection;
	}

	public Timestamp getFirstReceivedTime() {
		return firstReceivedTime;
	}

	public void setFirstReceivedTime(Timestamp firstReceivedTime) {
		this.firstReceivedTime = firstReceivedTime;
	}

	public int getSecondRealCollection() {
		return secondRealCollection;
	}

	public void setSecondRealCollection(int secondRealCollection) {
		this.secondRealCollection = secondRealCollection;
	}

	public Timestamp getSecondReceivedTime() {
		return secondReceivedTime;
	}

	public void setSecondReceivedTime(Timestamp secondReceivedTime) {
		this.secondReceivedTime = secondReceivedTime;
	}
}