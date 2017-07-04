package com.keymanager.value;

import java.sql.Date;
import java.sql.Timestamp;

public class RelatedKeywordUrlVO {
	private int uuid;
	private String keyword;
	private String relatedKeyword;
	private String relatedKeywordUrl;
	private int plannedCount;
	private int refreshedCount;
	private Timestamp updateTime;
	private Timestamp createTime;

	public String toOptimizeString() {
		return String.format("%d__col__%s__col__%s__col__%s", this.getUuid(),
				this.getKeyword(), this.getRelatedKeyword(),
				this.getRelatedKeywordUrl());
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

	public String getRelatedKeyword() {
		return relatedKeyword;
	}

	public void setRelatedKeyword(String relatedKeyword) {
		this.relatedKeyword = relatedKeyword;
	}

	public String getRelatedKeywordUrl() {
		return relatedKeywordUrl;
	}

	public void setRelatedKeywordUrl(String relatedKeywordUrl) {
		this.relatedKeywordUrl = relatedKeywordUrl;
	}

	public int getPlannedCount() {
		return plannedCount;
	}

	public void setPlannedCount(int plannedCount) {
		this.plannedCount = plannedCount;
	}

	public int getRefreshedCount() {
		return refreshedCount;
	}

	public void setRefreshedCount(int refreshedCount) {
		this.refreshedCount = refreshedCount;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
