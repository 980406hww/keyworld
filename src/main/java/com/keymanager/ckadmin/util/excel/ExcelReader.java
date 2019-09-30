package com.keymanager.ckadmin.util.excel;

public interface ExcelReader {

    void setCurrentWorkSheetWithName(String sheetName);

    void close();

    String readStringValueAt(int columnIndex, int rowIndex);
}
