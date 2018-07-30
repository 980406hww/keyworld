package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

@TableName(value = "t_qz_setting")
public class QZSetting extends BaseEntity{
	private static final long serialVersionUID = -1101942701283949852L;

	@TableField(value = "fCustomerUuid")
	private int customerUuid;

	@TableField(exist=false)
	private String contactPerson;

	@TableField(exist=false)
	private String userID;

	@TableField(value = "fDomain")
	private String domain;

	@TableField(value = "fBearPawNumber", strategy = FieldStrategy.IGNORED)
	private String bearPawNumber;

	@TableField(value = "fPCGroup")
	private String pcGroup;

	@TableField(value = "fPhoneGroup")
	private String phoneGroup;

	@TableField(value = "fType")
	private String type;

	@TableField(value = "fIgnoreNoIndex")
	private boolean ignoreNoIndex;

	@TableField(value = "fIgnoreNoOrder")
	private boolean ignoreNoOrder;

	@TableField(value = "fUpdateInterval")
	private int updateInterval;

	@TableField(value = "fUpdateStatus", strategy = FieldStrategy.IGNORED)
	private String updateStatus;

	@TableField(value = "fUpdateStartTime")
	private Date updateStartTime;

	@TableField(value = "fUpdateEndTime")
	private Date updateEndTime;

	@TableField(value = "fCaptureCurrentKeywordCountTime")
	private Date captureCurrentKeywordCountTime;

	@TableField(value = "fCaptureCurrentKeywordStatus", strategy = FieldStrategy.IGNORED)
	private String captureCurrentKeywordStatus;

	@TableField(value = "fStatus")
	private int status;

	@TableField(exist=false)
	private List<QZOperationType> qzOperationTypes;//qzOperationTypes为全站表子类  一对多

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(int customerUuid) {
		this.customerUuid = customerUuid;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getBearPawNumber() {
		return bearPawNumber;
	}

	public void setBearPawNumber(String bearPawNumber) {
		this.bearPawNumber = bearPawNumber;
	}

	public String getPcGroup() {
		return pcGroup;
	}

	public void setPcGroup(String pcGroup) {
		this.pcGroup = pcGroup;
	}

	public String getPhoneGroup() {
		return phoneGroup;
	}

	public void setPhoneGroup(String phoneGroup) {
		this.phoneGroup = phoneGroup;
	}

	public int getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	public String getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}

	public Date getUpdateStartTime() {
		return updateStartTime;
	}

	public void setUpdateStartTime(Date updateStartTime) {
		this.updateStartTime = updateStartTime;
	}

	public Date getUpdateEndTime() {
		return updateEndTime;
	}

	public void setUpdateEndTime(Date updateEndTime) {
		this.updateEndTime = updateEndTime;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isIgnoreNoIndex() {
		return ignoreNoIndex;
	}

	public void setIgnoreNoIndex(boolean ignoreNoIndex) {
		this.ignoreNoIndex = ignoreNoIndex;
	}

	public boolean isIgnoreNoOrder() {
		return ignoreNoOrder;
	}

	public void setIgnoreNoOrder(boolean ignoreNoOrder) {
		this.ignoreNoOrder = ignoreNoOrder;
	}

	public List<QZOperationType> getQzOperationTypes() {
		return qzOperationTypes;
	}

	public void setQzOperationTypes(List<QZOperationType> qzOperationTypes) {
		this.qzOperationTypes = qzOperationTypes;
	}

	public Date getCaptureCurrentKeywordCountTime() {
		return captureCurrentKeywordCountTime;
	}

	public void setCaptureCurrentKeywordCountTime(Date captureCurrentKeywordCountTime) {
		this.captureCurrentKeywordCountTime = captureCurrentKeywordCountTime;
	}

	public String getCaptureCurrentKeywordStatus() {
		return captureCurrentKeywordStatus;
	}

	public void setCaptureCurrentKeywordStatus(String captureCurrentKeywordStatus) {
		this.captureCurrentKeywordStatus = captureCurrentKeywordStatus;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
