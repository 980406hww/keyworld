package com.keymanager.util.excel;

import java.util.Date;

import jxl.Cell;
import jxl.write.WritableCell;

public interface ExcelWriter {
  public void close() throws ExcelWriteException;

  public int getRowCount();

  public int getColumnCount();

  public void saveAs(String filePath) throws Exception;

  public byte[] getExcelContentBytes() throws Exception;

  public void setCurrentWorkSheetWithName(String sheetName);

  public void createSheetWithNameAndSetAsCurrentSheet(String sheetName,
      int index);

  public void addLabelCell(int columnIndex, int rowIndex,
      double subscriptionRecipitionNumber) throws ExcelWriteException;

  public void addDateCell(int columnIndex, int rowIndex,
      ExcelDateFormat dateFormat, Date date) throws ExcelWriteException;

  public void addDateCell(int columnIndex, int rowIndex,
      ExcelDateFormat dateFormat, java.sql.Date date)
      throws ExcelWriteException;

  public void addLabelCell(int columnIndex, int rowIndex, String labelString)
      throws ExcelWriteException;

  public void setCellDateFormat(WritableCell cell, ExcelDateFormat dateFormat);

  public void setCellFontColor(int columnIndex, int rowIndex, int pointSize,
      ExcelFontName fontName, ExcelFontBoldStyle boldStyle,
      ExcelUnderlineStyle underlineStyle, ExcelColor color)
      throws ExcelWriteException;

  public void setCellFontColor(WritableCell cell, int pointSize,
      ExcelFontName fontName, ExcelFontBoldStyle boldStyle,
      ExcelUnderlineStyle underlineStyle, ExcelColor color);

  public void setCellFontColor(WritableCell cell, ExcelColor color)
      throws ExcelWriteException;

  public void setBackground(int columnIndex, int rowIndex, ExcelColor color)
      throws ExcelWriteException;

  public void setBackground(WritableCell cell, ExcelColor color)
      throws ExcelWriteException;

  public WritableCell getWritableCell(int columnIndex, int rowIndex)
      throws ExcelWriteException;

  public String readStringValueAt(int columnIndex, int rowIndex);

  public Cell getCell(int columnIndex, int rowIndex);

  public void addBlankCell(int columnIndex, int rowIndex)
      throws ExcelWriteException;

  public void addCell(WritableCell cell) throws ExcelWriteException;

  public void setWholeRowBackground(int rowIndex, ExcelColor color)
      throws ExcelWriteException;
  
  public void setCellFontBoldStyle(WritableCell cell, boolean isBold) throws ExcelWriteException;
}
