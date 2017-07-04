package com.keymanager.value;

import java.sql.Timestamp;

import com.keymanager.util.IndexAndPositionHelper;
import com.keymanager.util.Utils;

public class KeywordPositionUrlVO{
	private int uuid;
	private String keyword;
	private String searchEngine;
	private String url;
	private int position;
	private String snapshotDateTime;
	private Timestamp updateTime;

	public KeywordPositionUrlVO(){
		super();
	}
	
	public KeywordPositionUrlVO(String keyword, String searchEngine, String url, int position, String snapshotDateTime, Timestamp updateTime){
		this.keyword = keyword;
		this.searchEngine = searchEngine;
		this.url = url;
		this.position = position;
		this.snapshotDateTime = snapshotDateTime;
		this.updateTime = updateTime;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String toUrlAndPositionString(){
		IndexAndPositionHelper helper = IndexAndPositionHelper.getInstance(this.searchEngine);		
		return String.format("%s__col__%s__col__%d", this.getUrl(), this.getSnapshotDateTime(), this.getPosition());
	}

	public String getSnapshotDateTime() {
		return snapshotDateTime;
	}

	public void setSnapshotDateTime(String snapshotDateTime) {
		this.snapshotDateTime = snapshotDateTime;
	}
}
