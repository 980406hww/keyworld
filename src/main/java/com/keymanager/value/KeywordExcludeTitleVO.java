package com.keymanager.value;

import java.sql.Timestamp;

public class KeywordExcludeTitleVO {
	private int uuid;
	private String keyword;
	private String excludeTitle;
	private Timestamp createTime;

	public KeywordExcludeTitleVO(){
		super();
	}

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getExcludeTitle() {
		return excludeTitle;
	}

	public void setExcludeTitle(String excludeTitle) {
		this.excludeTitle = excludeTitle;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
