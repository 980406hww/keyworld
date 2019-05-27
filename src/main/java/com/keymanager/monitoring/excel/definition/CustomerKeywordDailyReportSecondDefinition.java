package com.keymanager.monitoring.excel.definition;

public enum CustomerKeywordDailyReportSecondDefinition {
	Keyword(0, "关键词"),
	URL(1, "网址"),
	Price1(2, "前三单价"),
	Price4(3, "首页单价"),
	CurrentPosition(4, "当天排名"),
	TodayPrice(5, "当天收费");

	private int columnIndex;
	private String title;

	private CustomerKeywordDailyReportSecondDefinition(int columnIndex, String title){
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
