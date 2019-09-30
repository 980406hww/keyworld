package com.keymanager.ckadmin.excel.definition;

public enum SuperUserFullKeywordDefinition {
    Keyword(0),
    SearchEngine(1),
    Sequnce(2),
    IndexCount(3),
    OptimizePlanCount(4),
    CollectMethod(5),
    OriginalURL(6),
    URL(7),
    PositionFirstFee(8),
    PositionSecondFee(9),
    PositionThirdFee(10),
    PositionForthFee(11),
    PositionFifthFee(12),
    PositionFirstPageFee(13),
    OptimizeGroupName(14),
    BearPawNumber(15),
    Title(16),
    RunImmediate(17),
    OrderNumber(18),
    Remarks(19);
    private int columnIndex;

    private SuperUserFullKeywordDefinition(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
