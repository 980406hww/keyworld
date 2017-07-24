package com.keymanager.value;

import java.sql.Date;
import java.sql.Timestamp;

public class RelatedKeywordVO {
	private int uuid;
	private int customerUuid;
	private String keyword;
	private int keywordIndex;
	private String relatedKeyword;
	private int plannedCount;
	private int completedCount;
	private Date operationDate;
	private Timestamp operationTime;
	private Timestamp captureIndexTime;
	private Timestamp createTime;

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public int getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(int customerUuid) {
		this.customerUuid = customerUuid;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPlannedCount() {
		return plannedCount;
	}

	public void setPlannedCount(int plannedCount) {
		this.plannedCount = plannedCount;
	}

	public int getCompletedCount() {
		return completedCount;
	}

	public void setCompletedCount(int completedCount) {
		this.completedCount = completedCount;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getRelatedKeyword() {
		return relatedKeyword;
	}

	public void setRelatedKeyword(String relatedKeyword) {
		this.relatedKeyword = relatedKeyword;
	}

	public int getKeywordIndex() {
		return keywordIndex;
	}

	public void setKeywordIndex(int keywordIndex) {
		this.keywordIndex = keywordIndex;
	}

	public Timestamp getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Timestamp operationTime) {
		this.operationTime = operationTime;
	}

	public Timestamp getCaptureIndexTime() {
		return captureIndexTime;
	}

	public void setCaptureIndexTime(Timestamp captureIndexTime) {
		this.captureIndexTime = captureIndexTime;
	}
	
	public String toOptimizeString(){
		return String.format("%d__col__%s__col__%s", this.getUuid(), this.getKeyword(), this.getRelatedKeyword());
	}
}
