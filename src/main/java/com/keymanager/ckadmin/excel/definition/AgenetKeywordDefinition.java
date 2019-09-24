package com.keymanager.ckadmin.excel.definition;


public enum AgenetKeywordDefinition {
    Keyword(0),
    URL(1),
    SearchEngine(2),
    StartOptimization(3),
    CollectMethod(4),
    FirstFee(5),
    SecondFee(6),
    ThirdFee(7),
    ForthFee(8),
    FifthFee(9),
    Remarks(10);

    private int columnIndex;

    private AgenetKeywordDefinition(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
