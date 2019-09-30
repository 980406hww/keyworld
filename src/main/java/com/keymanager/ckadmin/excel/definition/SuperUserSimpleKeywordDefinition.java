package com.keymanager.ckadmin.excel.definition;

public enum SuperUserSimpleKeywordDefinition {
    Keyword(0),
    SearchEngine(1),
    Sequnce(2),
    IndexCount(3),
    OptimizePlanCount(4),
    OriginalURL(5),
    CollectMethod(6),
    Fee(7),
    URL(8),
    MachineGroupName(9),
    OptimizeGroupName(10),
    BearPawNumber(11),
    Title(12),
    RunImmediate(13),
    OrderNumber(14),
    Remarks(15),
    KeywordEffect(16);
    private int columnIndex;

    private SuperUserSimpleKeywordDefinition(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
