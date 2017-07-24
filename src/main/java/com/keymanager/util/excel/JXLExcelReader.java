package com.keymanager.util.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class JXLExcelReader extends AbstractJXLExcel implements ExcelReader {
  private Workbook workbook;
  private Sheet currentSheet;

  public JXLExcelReader(String filePath) throws BiffException, IOException {
    this(new FileInputStream(filePath));
  }

  public JXLExcelReader(InputStream inputStream) throws BiffException, IOException {
    this.workbook = Workbook.getWorkbook(inputStream);
    currentSheet = workbook.getSheet(0);
  }

  public void close() {
    workbook.close();
  }

  protected Workbook getWorkbook() {
    return workbook;
  }

  public Sheet getCurrentSheet() {
    return currentSheet;
  }

  public void setCurrentWorkSheetWithName(String sheetName) {
    currentSheet = workbook.getSheet(sheetName);
  }
}
