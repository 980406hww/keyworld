package com.keymanager.monitoring.excel.definition;

public enum CustomerKeywordDailyReportSummaryDefinition {
	Date(0, "日期"),
	BaiduPC(1, "百度电脑"),
	BaiduPhone(2, "百度手机"),
	SogouPC(3, "搜狗电脑"),
	SogouPhone(4, "搜狗手机"),
	So(5, "360"),
	UC(6, "UC"),
	TodayFee(7, "当日合计");

	private int columnIndex;
	private String title;

	private CustomerKeywordDailyReportSummaryDefinition(int columnIndex, String title){
		this.columnIndex = columnIndex;
		this.title = title;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public String getTitle(){
		return this.title;
	}
}
