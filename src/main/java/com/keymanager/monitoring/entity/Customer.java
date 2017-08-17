package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_customer")
public class Customer extends BaseEntity{
	protected static final long serialVersionUID = -1101942701283949852L;

	@TableField(value = "fUserID")
	private String userID;

	@TableField(value = "fEntryType")
	private String entryType;

	@TableField(value = "fContactPerson")
	private String contactPerson;

	@TableField(value = "fQQ")
	private String qq;

	@TableField(value = "fEmail")
	private String email;

	@TableField(value = "fTelphone")
	private String telphone;

	@TableField(value = "fAlipay")
	private String alipay;

	@TableField(value = "fPaidFee")
	private int paidFee;

	@TableField(value = "fRemark")
	private String remark;

	@TableField(value = "fType")
	private String type;

	@TableField(exist=false)
	private int keywordCount;

	@TableField(value = "fStatus")
	private int status;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
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

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public int getPaidFee() {
		return paidFee;
	}

	public void setPaidFee(int paidFee) {
		this.paidFee = paidFee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getKeywordCount() {
		return keywordCount;
	}

	public void setKeywordCount(int keywordCount) {
		this.keywordCount = keywordCount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
