package com.keymanager.monitoring.vo;

import java.sql.Timestamp;

public class ClassificationWebSiteInfoVO {
	private Integer uuid;
	private Integer classificationUuid;
	private String href;
	private String emailAddress;

	public Integer getUuid() {
		return uuid;
	}

	public void setUuid(Integer uuid) {
		this.uuid = uuid;
	}

	public Integer getClassificationUuid() {
		return classificationUuid;
	}

	public void setClassificationUuid(Integer classificationUuid) {
		this.classificationUuid = classificationUuid;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
