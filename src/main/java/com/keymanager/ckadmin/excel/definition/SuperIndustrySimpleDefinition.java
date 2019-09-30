package com.keymanager.ckadmin.excel.definition;

public enum SuperIndustrySimpleDefinition {
    IndustryName(0),
    SearchEngine(1),
    TargetUrl(2),
    PageNum(3),
    PagePerNum(4);
    private int columnIndex;

    SuperIndustrySimpleDefinition(int columnIndex) {
        this.columnIndex = columnIndex;
    }


    public int getColumnIndex() {
        return columnIndex;
    }
}
