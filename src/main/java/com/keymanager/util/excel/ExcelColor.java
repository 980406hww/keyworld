package com.keymanager.util.excel;

import jxl.format.Colour;

public enum ExcelColor {
  White(Colour.WHITE), Gray(Colour.GRAY_50), Yellow(Colour.YELLOW), Red(Colour.RED);
  
  private Colour jxlColor;
  
  private ExcelColor(Colour jxlColor) {
    this.jxlColor = jxlColor;
  }

  public Colour getJxlColor() {
    return jxlColor;
  }
}
