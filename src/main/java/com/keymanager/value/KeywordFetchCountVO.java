package com.keymanager.value;

public class KeywordFetchCountVO {
	private int uuid;
	private String group;
	private int normalKeywordFetchedCount;
	private double bigKeywordPercentage;
	private int bigKeywordFetchedCount;

	public boolean fetchNormalKeyword(){
		return this.normalKeywordFetchedCount < 30;
	}
	
	public boolean fetchBigKeyword(){
//		return ((this.bigKeywordFetchedCount*1.0)/(normalKeywordFetchedCount + bigKeywordFetchedCount)) < bigKeywordPercentage;
		return this.bigKeywordFetchedCount < 20;
	}
	
	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getNormalKeywordFetchedCount() {
		return normalKeywordFetchedCount;
	}

	public void setNormalKeywordFetchedCount(int normalKeywordFetchedCount) {
		this.normalKeywordFetchedCount = normalKeywordFetchedCount;
	}

	public double getBigKeywordPercentage() {
		return bigKeywordPercentage;
	}

	public void setBigKeywordPercentage(double bigKeywordPercentage) {
		this.bigKeywordPercentage = bigKeywordPercentage;
	}

	public int getBigKeywordFetchedCount() {
		return bigKeywordFetchedCount;
	}

	public void setBigKeywordFetchedCount(int bigKeywordFetchedCount) {
		this.bigKeywordFetchedCount = bigKeywordFetchedCount;
	}
}
