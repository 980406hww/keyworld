package com.keymanager.ckadmin.util.excel;

import java.io.IOException;
import java.io.InputStream;
import jxl.read.biff.BiffException;

public class ExcelReaderFactory {

    public static ExcelReader createExcelReaderInstance(String filePath) throws BiffException, IOException {
        return new JXLExcelReader(filePath);
    }

    public static ExcelReader createExcelReaderInstance(InputStream inputStream) throws BiffException, IOException {
        return new JXLExcelReader(inputStream);
    }
}
