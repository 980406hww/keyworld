package com.keymanager.util.excel;

import com.keymanager.util.common.StringUtil;

public class ExcelCommonValidation {
  private static final String EXCEL_FILE_TYPE = ExcelConstants.EXCEL_2003_FILE_TYPE;
  private static final String EXCEL_2007_FILE_TYPE = ExcelConstants.EXCEL_2007_FILE_TYPE;
  
  public static boolean isDocumentFormatValid(String fileName) {
    String fileExtension = getFileExtensionByFileName(fileName);
    if (StringUtil.isNullOrEmpty(fileExtension)) {
      return false;
    }
    return EXCEL_FILE_TYPE.indexOf(fileExtension) >= 0 || EXCEL_2007_FILE_TYPE.indexOf(fileExtension) >= 0;
  }
  
  public static boolean isDocumentExcel2003(String fileName) {
    String fileExtension = getFileExtensionByFileName(fileName);
    if (StringUtil.isNullOrEmpty(fileExtension)) {
      return false;
    }
    return EXCEL_FILE_TYPE.indexOf(fileExtension) >= 0;
  }

  public static boolean isDocumentExcel2007(String fileName) {
    String fileExtension = getFileExtensionByFileName(fileName);
    if (StringUtil.isNullOrEmpty(fileExtension)) {
      return false;
    }
    return EXCEL_2007_FILE_TYPE.indexOf(fileExtension) >= 0;
  }

  private static String getFileExtensionByFileName(String fileName) {
    if(fileName == null){
      return StringUtil.EMPTY_STRING;
    }
    int lastIndex = fileName.lastIndexOf('.');
    if (lastIndex < 0 || lastIndex == fileName.length() - 1) {
      return StringUtil.EMPTY_STRING;
    }
    return fileName.substring(lastIndex + 1).toLowerCase();
  }
}
