package com.keymanager.ckadmin.excel.definition;

public enum NegativeKeywordNameDefinition {
    Name(0),
    OfficialUrl(1),
    Email(2);

    private int columnIndex;

    private NegativeKeywordNameDefinition(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
