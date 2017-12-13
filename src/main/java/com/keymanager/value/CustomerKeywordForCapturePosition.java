package com.keymanager.value;

public class CustomerKeywordForCapturePosition {
	private Long uuid;
	private Boolean captureRankJobStatus;
	private String keyword;
	private String url;
	private String title;
	private String searchEngine;
	private String terminalType;
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	public Boolean getCaptureRankJobStatus() {
		return captureRankJobStatus;
	}

	public void setCaptureRankJobStatus(Boolean captureRankJobStatus) {
		this.captureRankJobStatus = captureRankJobStatus;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(String searchEngine) {
		this.searchEngine = searchEngine;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
}
