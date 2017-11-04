package com.keymanager.monitoring.vo;

import java.util.List;

public class NegativeSupportingDataVO {
	private List<String> negativeGroups;
	private List<String> negativeKeywords;
	private List<String> excludeKeywords;
	private List<String> contactKeywords;

	public List<String> getNegativeGroups() {
		return negativeGroups;
	}

	public void setNegativeGroups(List<String> negativeGroups) {
		this.negativeGroups = negativeGroups;
	}

	public List<String> getNegativeKeywords() {
		return negativeKeywords;
	}

	public void setNegativeKeywords(List<String> negativeKeywords) {
		this.negativeKeywords = negativeKeywords;
	}

	public List<String> getExcludeKeywords() {
		return excludeKeywords;
	}

	public void setExcludeKeywords(List<String> excludeKeywords) {
		this.excludeKeywords = excludeKeywords;
	}

	public List<String> getContactKeywords() {
		return contactKeywords;
	}

	public void setContactKeywords(List<String> contactKeywords) {
		this.contactKeywords = contactKeywords;
	}
}
