package com.keymanager.util.excel;

import jxl.write.WritableFont;

public enum ExcelFontName {
  ARIAL,
  TIMES,
  COURIER,
  TAHOMA;

  public WritableFont.FontName getJxlFontName() {
    switch (this) {
      case ARIAL: return WritableFont.ARIAL;
      case TIMES: return WritableFont.TIMES;
      case COURIER: return WritableFont.COURIER;
      case TAHOMA: return WritableFont.TAHOMA;
    }
    throw new RuntimeException("Missing enum for Excel Font Name");
  }
}
