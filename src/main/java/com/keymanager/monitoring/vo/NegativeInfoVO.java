package com.keymanager.monitoring.vo;

import java.util.List;

public class NegativeInfoVO {
	private int uuid;
	private String userName;
	private String password;
	private String terminalType;
	private String suggestionNegativeKeyword;
	private String relativeNegativeKeyword;
	private String officialWebsiteUrl;
	private String emailAddress;
	private List<NegativeDetailInfoVO> negativeInfos;

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getSuggestionNegativeKeyword() {
		return suggestionNegativeKeyword;
	}

	public void setSuggestionNegativeKeyword(String suggestionNegativeKeyword) {
		this.suggestionNegativeKeyword = suggestionNegativeKeyword;
	}

	public String getRelativeNegativeKeyword() {
		return relativeNegativeKeyword;
	}

	public void setRelativeNegativeKeyword(String relativeNegativeKeyword) {
		this.relativeNegativeKeyword = relativeNegativeKeyword;
	}

	public String getOfficialWebsiteUrl() {
		return officialWebsiteUrl;
	}

	public void setOfficialWebsiteUrl(String officialWebsiteUrl) {
		this.officialWebsiteUrl = officialWebsiteUrl;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public List<NegativeDetailInfoVO> getNegativeInfos() {
		return negativeInfos;
	}

	public void setNegativeInfos(List<NegativeDetailInfoVO> negativeInfos) {
		this.negativeInfos = negativeInfos;
	}
}
