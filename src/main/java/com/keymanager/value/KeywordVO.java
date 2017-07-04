package com.keymanager.value;

import java.sql.Timestamp;

import com.keymanager.enums.KeywordType;

public class KeywordVO {
	private int uuid;
	private String keyword;
	private String searchEngine;
	private String type;
	private Timestamp querySearchEngineTime;
	private Timestamp captureIndexCountTime;
	private int captureStatus;
	private int status;
	private Timestamp createTime;

	public KeywordVO(){
		super();
	}
	
	public KeywordVO(String keyword, String searchEngine, String type){
		this.keyword = keyword;
		this.searchEngine = searchEngine;
		this.type = type;
		this.status = 1;
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

	public String getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(String searchEngine) {
		this.searchEngine = searchEngine;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getQuerySearchEngineTime() {
		return querySearchEngineTime;
	}

	public void setQuerySearchEngineTime(Timestamp querySearchEngineTime) {
		this.querySearchEngineTime = querySearchEngineTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public int getRecoundCount(){
		KeywordType keywordType = KeywordType.findByName(this.getType());
		if (keywordType != null){
			return keywordType.getFetchRecordCount();
		}else{
			return 0;
		}
	}
	
	public String toSummaryString(){
		return String.format("%d__col__%s__col__%s__col__%d", this.getUuid(), this.getKeyword(), this.getSearchEngine(),this.getRecoundCount());
	}

	public int getCaptureStatus() {
		return captureStatus;
	}

	public void setCaptureStatus(int captureStatus) {
		this.captureStatus = captureStatus;
	}

	public Timestamp getCaptureIndexCountTime() {
		return captureIndexCountTime;
	}

	public void setCaptureIndexCountTime(Timestamp captureIndexCountTime) {
		this.captureIndexCountTime = captureIndexCountTime;
	}
}
