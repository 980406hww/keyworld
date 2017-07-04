package com.keymanager.value;

import java.sql.Timestamp;

public class CustomerKeywordRefreshSummaryInfoVO {
	private int uuid;
	private int customerUuid;
	private String contactPerson;
	private String keyword;
	private String url;
	private String originalUrl;
	private int currentIndexCount;
	private int optimizePlanCount;
	private Timestamp optimizeDate;
	private int optimizedCount;
	private int currentPosition;
	private int queryCount;
	private int invalidRefreshCount;
	
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
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOriginalUrl() {
		return originalUrl;
	}
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	public int getCurrentIndexCount() {
		return currentIndexCount;
	}
	public void setCurrentIndexCount(int currentIndexCount) {
		this.currentIndexCount = currentIndexCount;
	}
	public int getOptimizePlanCount() {
		return optimizePlanCount;
	}
	public void setOptimizePlanCount(int optimizePlanCount) {
		this.optimizePlanCount = optimizePlanCount;
	}
	public Timestamp getOptimizeDate() {
		return optimizeDate;
	}
	public void setOptimizeDate(Timestamp optimizeDate) {
		this.optimizeDate = optimizeDate;
	}
	public int getOptimizedCount() {
		return optimizedCount;
	}
	public void setOptimizedCount(int optimizedCount) {
		this.optimizedCount = optimizedCount;
	}
	public int getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}
	public int getQueryCount() {
		return queryCount;
	}
	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}
	public int getInvalidRefreshCount() {
		return invalidRefreshCount;
	}
	public void setInvalidRefreshCount(int invalidRefreshCount) {
		this.invalidRefreshCount = invalidRefreshCount;
	}
}