package com.keymanager.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import jxl.*;
import jxl.biff.EmptyCell;
import jxl.read.biff.BiffException;
import jxl.read.biff.CellValue;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

public class JXLExcelWriter extends AbstractJXLExcel implements ExcelWriter {
  private WritableWorkbook writableWorkbook;
  private WritableSheet currentWritableSheet;
  private ByteArrayOutputStream outputStream;
  private File workbookFile;
  private boolean isClose;

  public JXLExcelWriter(String filePath) throws BiffException, IOException {
    this(new File(filePath));
  }

  public JXLExcelWriter(File file) throws BiffException, IOException {
    workbookFile = file;
    WorkbookSettings settings = new WorkbookSettings ();  
    settings.setWriteAccess(null);  
    if (workbookFile.exists()) {
      Workbook workbook = Workbook.getWorkbook(workbookFile);
      outputStream = new ByteArrayOutputStream();
      writableWorkbook = Workbook.createWorkbook(outputStream, workbook, settings);
      currentWritableSheet = writableWorkbook.getSheet(0);
    } else {
      writableWorkbook = Workbook.createWorkbook(workbookFile, settings);
      currentWritableSheet = writableWorkbook.createSheet("Sheet1", 0);
    }
    isClose = false;
  }

  public JXLExcelWriter(InputStream inputStream) throws BiffException,
      IOException {
    Workbook workbook = Workbook.getWorkbook(inputStream);
    outputStream = new ByteArrayOutputStream();
    writableWorkbook = Workbook.createWorkbook(outputStream, workbook);
    currentWritableSheet = writableWorkbook.getSheet(0);
    isClose = false;
  }

  protected Sheet getCurrentSheet() {
    return currentWritableSheet;
  }

  public Sheet getSheetByName(String sheetName){
    return writableWorkbook.getSheet(sheetName);
  }

  public void close() throws ExcelWriteException {
    if (!isClose) {
      try {
        writableWorkbook.write();
        writableWorkbook.close();
        isClose = true;
      } catch (WriteException e) {
        throw new ExcelWriteException(e);
      } catch (IOException ioException) {
        throw new ExcelWriteException(ioException);
      }
    }
  }

  public void save() throws ExcelWriteException {
    if (!isClose) {
      try {
        writableWorkbook.setOutputFile(workbookFile);
        writableWorkbook.write();
      } catch (IOException ioException) {
        throw new ExcelWriteException(ioException);
      }
    }
  }

  public int getRowCount() {
    return currentWritableSheet.getRows();
  }

  public int getColumnCount() {
    return currentWritableSheet.getColumns();
  }

  public void saveAs(String filePath) throws Exception {
    if (isClose) {
      throw new ExcelWriteException("Excel Writer is Closed!");
    } else {
      close();
      FileOutputStream fileOutputStream = new FileOutputStream(filePath);
      outputStream.writeTo(fileOutputStream);
      outputStream.close();
      fileOutputStream.close();
    }
  }

  public byte[] getExcelContentBytes() throws Exception {
    close();
    byte[] excelContentBytes = outputStream.toByteArray();
    outputStream.close();
    return excelContentBytes;
  }

  public void setCurrentWorkSheetWithName(String sheetName) {
    currentWritableSheet = writableWorkbook.getSheet(sheetName);
  }

  public int getNumberOfSheets(){
    return writableWorkbook.getNumberOfSheets();
  }

  public void removeSheet(int index) {
    this.writableWorkbook.removeSheet(index);
  }

  public void removeSheet(String sheetName) {
    for(int i = 0; i < this.writableWorkbook.getNumberOfSheets(); i++){
      Sheet sheet = this.writableWorkbook.getSheet(i);
      if(sheet.getName().equals(sheetName)){
        writableWorkbook.removeSheet(i);
        return;
      }
    }
  }

  public void createSheetWithNameAndSetAsCurrentSheet(String sheetName,
      int index) {
    currentWritableSheet = writableWorkbook.createSheet(sheetName, index);
  }

  public void addLabelCell(int columnIndex, int rowIndex, String labelString)
      throws ExcelWriteException {
    WritableCell cell = new Label(columnIndex, rowIndex, labelString);
    try {
      currentWritableSheet.addCell(cell);
    } catch (WriteException e) {
      throw new ExcelWriteException(e);
    }
  }

  public void addLabelCell(int columnIndex, int rowIndex, String labelString, boolean isBold)
          throws ExcelWriteException {
    WritableCell cell = new Label(columnIndex, rowIndex, labelString);
    try {
      setCellFontBoldStyle(cell, isBold);
      currentWritableSheet.addCell(cell);
    } catch (WriteException e) {
      throw new ExcelWriteException(e);
    }
  }

  public void addFormulanCell(int columnIndex, int rowIndex, String labelString)
          throws ExcelWriteException {
    WritableCell cell = new Formula(columnIndex, rowIndex, labelString);
    try {
      currentWritableSheet.addCell(cell);
    } catch (WriteException e) {
      throw new ExcelWriteException(e);
    }
  }

  public void addLabelCell(int columnIndex, int rowIndex, double labelDouble)
      throws ExcelWriteException {
    Number cell = new Number(columnIndex, rowIndex, labelDouble);
    try {
      currentWritableSheet.addCell(cell);
    } catch (WriteException e) {
      throw new ExcelWriteException(e);
    }
  }

  public void addDateCell(int columnIndex, int rowIndex,
      ExcelDateFormat dateFormat, Date date) throws ExcelWriteException {
    WritableCell cell = new DateTime(columnIndex, rowIndex, date);
    setCellDateFormat(cell, dateFormat);
    try {
      currentWritableSheet.addCell(cell);
    } catch (WriteException e) {
      throw new ExcelWriteException(e);
    }
  }

  public void addDateCell(int columnIndex, int rowIndex,
      ExcelDateFormat dateFormat, java.sql.Date date)
      throws ExcelWriteException {

    WritableCellFormat wcfDF = new WritableCellFormat(
        new DateFormat(dateFormat.getJxlDateFormat()));
    DateTime labelDTF = new DateTime(columnIndex, rowIndex,
        date, wcfDF);
    try {
      currentWritableSheet.addCell(labelDTF);
    } catch (WriteException e) {
      throw new ExcelWriteException(e);
    }
  }

  public void setCellDateFormat(WritableCell cell, ExcelDateFormat dateFormat) {
    WritableCellFormat writableCellFormat = new WritableCellFormat(
        new DateFormat(dateFormat.getJxlDateFormat()));
    cell.setCellFormat(writableCellFormat);
  }

  public void setCellFontColor(int columnIndex, int rowIndex, int pointSize,
      ExcelFontName fontName, ExcelFontBoldStyle boldStyle,
      ExcelUnderlineStyle underlineStyle, ExcelColor color)
      throws ExcelWriteException {
    setCellFontColor(getWritableCell(columnIndex, rowIndex), pointSize,
        fontName, boldStyle, underlineStyle, color);
  }

  public void setCellFontColor(WritableCell cell, int pointSize,
      ExcelFontName fontName, ExcelFontBoldStyle boldStyle,
      ExcelUnderlineStyle underlineStyle, ExcelColor color) {
    WritableFont writableFont;
    if (ExcelFontBoldStyle.BOLD == boldStyle) {
      writableFont = new WritableFont(fontName.getJxlFontName(), pointSize,
          WritableFont.BOLD, false, underlineStyle.getJxlUnderlineStyle(),
          color.getJxlColor());
    } else {
      writableFont = new WritableFont(fontName.getJxlFontName(), pointSize,
          WritableFont.NO_BOLD, false, underlineStyle.getJxlUnderlineStyle(),
          color.getJxlColor());
    }
    WritableCellFormat writableCellFormat = new WritableCellFormat();
    writableCellFormat.setFont(writableFont);
    cell.setCellFormat(writableCellFormat);
  }

  public void setCellFontColor(WritableCell cell, ExcelColor color)
      throws ExcelWriteException {
    try {
      WritableCellFormat writableCellFormat = new WritableCellFormat(cell
          .getCellFormat());
      WritableFont writableFont = new WritableFont(writableCellFormat.getFont());
      writableFont.setColour(color.getJxlColor());
      writableCellFormat.setFont(writableFont);
      cell.setCellFormat(writableCellFormat);
    } catch (WriteException writeException) {
      throw new ExcelWriteException(writeException);
    }
  }

  public void setBackground(int columnIndex, int rowIndex, ExcelColor color)
      throws ExcelWriteException {
    setBackground(getWritableCell(columnIndex, rowIndex), color);
  }

  public void setBackground(WritableCell cell, ExcelColor color)
      throws ExcelWriteException {
    try {
      WritableCellFormat writableCellFormat = new WritableCellFormat(cell
          .getCellFormat());
      writableCellFormat.setBackground(color.getJxlColor());
      cell.setCellFormat(writableCellFormat);
    } catch (WriteException writeException) {
      throw new ExcelWriteException(writeException);
    }
  }

  public WritableCell getWritableCell(int columnIndex, int rowIndex)
      throws ExcelWriteException {
    try {
      WritableCell writableCell = currentWritableSheet.getWritableCell(
          columnIndex, rowIndex);
      if (null == writableCell || writableCell instanceof EmptyCell) {
        writableCell = new Blank(columnIndex, rowIndex);
        currentWritableSheet.addCell(writableCell);
      }

      return writableCell;
    } catch (RowsExceededException rowsExceededException) {
      throw new ExcelWriteException(rowsExceededException);
    } catch (WriteException writeException) {
      throw new ExcelWriteException(writeException);
    }
  }

  public void addBlankCell(int columnIndex, int rowIndex)
      throws ExcelWriteException {
    WritableCell cell = new Blank(columnIndex, rowIndex);
    addCell(cell);
  }

  public void addCell(WritableCell cell) throws ExcelWriteException {
    try {
      currentWritableSheet.addCell(cell);
    } catch (WriteException e) {
      throw new ExcelWriteException(e);
    }
  }

  public void setWholeRowBackground(int rowIndex, ExcelColor color)
      throws ExcelWriteException {
    for (Cell cell : currentWritableSheet.getRow(rowIndex)) {
      if (null != cell && cell instanceof WritableCell) {
        this.setBackground((WritableCell) cell, color);
      }
    }
  }

  public void setCellFontBoldStyle(WritableCell cell, boolean isBold)
      throws ExcelWriteException {
    try {
      WritableCellFormat writableCellFormat = new WritableCellFormat(cell
          .getCellFormat());
      WritableFont writableFont = new WritableFont(writableCellFormat.getFont());
      if(isBold){
        writableFont.setBoldStyle(WritableFont.BOLD);
      }else{
        writableFont.setBoldStyle(writableFont.NO_BOLD);
      }
      writableCellFormat.setFont(writableFont);
      cell.setCellFormat(writableCellFormat);
    } catch (WriteException writeException) {
      throw new ExcelWriteException(writeException);
    }
  }

  public void setColumnView(int column, int width) throws ExcelWriteException{
      currentWritableSheet.setColumnView(column, width);
  }
}
