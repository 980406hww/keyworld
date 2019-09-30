package com.keymanager.ckadmin.util.excel.mapping;

import java.io.Serializable;

public class ExcelMappingElement implements Serializable {

    private static final long serialVersionUID = 3806572263938858059L;

    private String fieldName;

    private int rowIndex;

    private int columnIndex;

    public ExcelMappingElement(String fieldName, String columnIndex, int rowIndex) {
        this.fieldName = fieldName;
        this.columnIndex = getIndex(columnIndex);
        this.rowIndex = rowIndex - 1;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getIndex(String num) {
        byte[] bs = num.getBytes();
        int n = 0;
        for (int i = bs.length - 1, p = 0; i >= 0; i--, p++) {
            n += (int) (Math.pow(26, p) * (bs[i] - 'A'));
        }
        return n;
    }
}
