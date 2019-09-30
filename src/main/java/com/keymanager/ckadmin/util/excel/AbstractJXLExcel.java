package com.keymanager.ckadmin.util.excel;

import jxl.Cell;
import jxl.Sheet;

abstract public class AbstractJXLExcel {

    abstract protected Sheet getCurrentSheet();

    abstract public void setCurrentWorkSheetWithName(String sheetName);

    public String readStringValueAt(int columnIndex, int rowIndex) {
        return getCell(columnIndex, rowIndex).getContents();
    }

    public Cell getCell(int columnIndex, int rowIndex) {
        return getCurrentSheet().getCell(columnIndex, rowIndex);
    }
}
