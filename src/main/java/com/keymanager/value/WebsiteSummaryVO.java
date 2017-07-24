package com.keymanager.value;

import java.sql.Timestamp;

public class WebsiteSummaryVO {
	private String url;
	private String title;
	private String snapshotDateTime;

	public WebsiteSummaryVO(String url, String title, String snapshotDateTime){
		this.url = url;
		this.title = title;
		this.snapshotDateTime = snapshotDateTime;
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

	public String getSnapshotDateTime() {
		return snapshotDateTime;
	}

	public void setSnapshotDateTime(String snapshotDateTime) {
		this.snapshotDateTime = snapshotDateTime;
	}
}
