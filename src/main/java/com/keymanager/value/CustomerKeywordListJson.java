package com.keymanager.value;

import java.util.List;

public class CustomerKeywordListJson {
	private String group;
	private String keyword;
	private int customerUuid;
	private List<CustomerKeywordJson> capturedResults;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<CustomerKeywordJson> getCapturedResults() {
		return capturedResults;
	}

	public void setCapturedResults(List<CustomerKeywordJson> capturedResults) {
		this.capturedResults = capturedResults;
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
}
