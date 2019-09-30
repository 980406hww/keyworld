package com.keymanager.ckadmin.excel.definition;

public enum KeywordPositionHistoryLogDefinition {
    Uuid(0),
    ContactPerson(1),
    Keyword(2),
    OriginalURL(3),
    OriginalPhoneURL(4),
    Type(5),
    PositionNumber(6),
    IP(7),
    CreateTime(8);
    private int columnIndex;

    private KeywordPositionHistoryLogDefinition(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
