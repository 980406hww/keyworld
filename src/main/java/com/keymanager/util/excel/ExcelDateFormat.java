package com.keymanager.util.excel;

public enum ExcelDateFormat {
  MM_dd_yyyy_HH_MM("MM/dd/yyyy HH:mm"),
  M_d_yy_h_mm("m/d/yy h:mm"),
  m_d_yy("m/d/yy");

  private String jxlDateFormat;

  private ExcelDateFormat(String dateFormat) {
    this.jxlDateFormat = dateFormat;
  }

  public String getJxlDateFormat() {
    return jxlDateFormat;
  }
}
