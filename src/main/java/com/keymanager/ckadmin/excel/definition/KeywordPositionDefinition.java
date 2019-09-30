package com.keymanager.ckadmin.excel.definition;

public enum KeywordPositionDefinition {
    Uuid(0),
    Keyword(1),
    URL(2),
    Domain(3),
    Group(4),
    Level(5),
    ShouLu(6),
    PcPosition(7),
    JisuPosition(8),
    ChupingPosition(9),
    PcUpdateTime(10),
    PhoneUpdateTime(11),
    UpdateTime(12),
    Valid(13);
    private int columnIndex;

    private KeywordPositionDefinition(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
