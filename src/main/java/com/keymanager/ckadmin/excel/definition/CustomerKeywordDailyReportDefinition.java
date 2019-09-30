package com.keymanager.ckadmin.excel.definition;

public enum CustomerKeywordDailyReportDefinition {
    Sequence(0, "序号"),
    Keyword(1, "关键词"),
    URL(2, "网址"),
    Index(3, "指数"),
    Price1(4, "前三名价格"),
    Price4(5, "四、五名价格"),
    CurrentPosition(6, "当前排名"),
    TodayPrice(7, "今日收费");

    private int columnIndex;
    private String title;

    private CustomerKeywordDailyReportDefinition(int columnIndex, String title) {
        this.columnIndex = columnIndex;
        this.title = title;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public String getTitle() {
        return this.title;
    }
}
