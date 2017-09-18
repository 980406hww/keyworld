package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.criteria.BaseCriteria;

import java.util.List;

public class SearchEngineResultVO extends BaseCriteria{
	private String group;
	private String keyword;
	private int customerUuid;
	private String entryType;
	private List<SearchEngineResultItemVO> searchEngineResultItemVOs;


	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(int customerUuid) {
		this.customerUuid = customerUuid;
	}

	public List<SearchEngineResultItemVO> getSearchEngineResultItemVOs() {
		return searchEngineResultItemVOs;
	}

	public void setSearchEngineResultItemVOs(List<SearchEngineResultItemVO> searchEngineResultItemVOs) {
		this.searchEngineResultItemVOs = searchEngineResultItemVOs;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
}
