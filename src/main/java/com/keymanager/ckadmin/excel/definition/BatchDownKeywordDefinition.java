package com.keymanager.ckadmin.excel.definition;

public enum BatchDownKeywordDefinition {
    Keyword(0),
    URL(1),
    SearchEngine(2);
    private int columnIndex;

    private BatchDownKeywordDefinition(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
