package com.keymanager.ckadmin.util.excel;

import java.io.IOException;
import java.io.ObjectInputStream;
import jxl.format.UnderlineStyle;

public enum ExcelUnderlineStyle {
    NO_UNDERLINE(UnderlineStyle.NO_UNDERLINE),
    SINGLE(UnderlineStyle.SINGLE),
    DOUBLE(UnderlineStyle.DOUBLE),
    SINGLE_ACCOUNTING(UnderlineStyle.SINGLE_ACCOUNTING),
    DOUBLE_ACCOUNTING(UnderlineStyle.DOUBLE_ACCOUNTING);

    private transient UnderlineStyle jxlUnderlineStyle;
    private int value;

    private ExcelUnderlineStyle(UnderlineStyle underlineStyle) {
        this.jxlUnderlineStyle = underlineStyle;
        this.value = underlineStyle.getValue();
    }

    public UnderlineStyle getJxlUnderlineStyle() {
        return jxlUnderlineStyle;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (value == UnderlineStyle.NO_UNDERLINE.getValue()) {
            this.jxlUnderlineStyle = UnderlineStyle.NO_UNDERLINE;
        } else if (value == UnderlineStyle.SINGLE.getValue()) {
            this.jxlUnderlineStyle = UnderlineStyle.SINGLE;
        } else if (value == UnderlineStyle.DOUBLE.getValue()) {
            this.jxlUnderlineStyle = UnderlineStyle.DOUBLE;
        } else if (value == UnderlineStyle.SINGLE_ACCOUNTING.getValue()) {
            this.jxlUnderlineStyle = UnderlineStyle.SINGLE_ACCOUNTING;
        } else if (value == UnderlineStyle.DOUBLE_ACCOUNTING.getValue()) {
            this.jxlUnderlineStyle = UnderlineStyle.DOUBLE_ACCOUNTING;
        }
    }
}
