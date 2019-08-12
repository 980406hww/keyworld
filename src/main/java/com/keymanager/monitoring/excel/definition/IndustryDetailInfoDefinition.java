package com.keymanager.monitoring.excel.definition;

public enum IndustryDetailInfoDefinition {

    IndustryName(0),
    Url(1),
    QQ(2),
    Telephone(3),
    Weight(4),
    Remark(5);

    private int columnIndex;

    private IndustryDetailInfoDefinition(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
