package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.criteria.BaseCriteria;

public class SearchEngineResultItemVO extends BaseCriteria{
	private int uuid;
	private String title;
	private String url;
	private int order;
	private String href;
	private boolean hasOfficialWebsiteIndicator;
	private String desc;
	private int clickCount;
	private String type;
	private String keyword;
	private String bearPawNumber;

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isHasOfficialWebsiteIndicator() {
		return hasOfficialWebsiteIndicator;
	}

	public void setHasOfficialWebsiteIndicator(boolean hasOfficialWebsiteIndicator) {
		this.hasOfficialWebsiteIndicator = hasOfficialWebsiteIndicator;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getBearPawNumber () {
		return bearPawNumber;
	}

	public void setBearPawNumber (String bearPawNumber) {
		this.bearPawNumber = bearPawNumber;
	}
}
