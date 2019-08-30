package com.keymanager.monitoring.excel.definition;

public enum IndustryDetailInfoDefinition {

    IndustryName(0),
    Url(1),
    QQ(2),
    Telephone(3),
    Weight(4),
    identifyCustomer(5),
    Remark(6);

    private int columnIndex;

    private IndustryDetailInfoDefinition(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
