package com.keymanager.monitoring.excel.definition;

public enum CustomerKeywordDailyReportSummaryDefinition {
	Date(0, "日期"),
	BaiduPC(1, "百度电脑"),
	BaiduPhone(2, "百度手机"),
	SogouPC(3, "搜狗电脑"),
	SogouPhone(4, "搜狗手机"),
	SoPC(5, "360电脑"),
	SoPhone(6, "360手机"),
	UC(7, "神马"),
	BingCN(8, "必应中国"),
	TodayFee(9, "当日合计");

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
