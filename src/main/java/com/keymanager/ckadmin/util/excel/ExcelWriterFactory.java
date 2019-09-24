package com.keymanager.ckadmin.util.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import jxl.read.biff.BiffException;

public class ExcelWriterFactory {

    public static ExcelWriter createExcelWriterInstance(String filePath) throws BiffException, IOException {
        return new JXLExcelWriter(filePath);
    }

    public static ExcelWriter createExcelWriterInstance(File file) throws BiffException, IOException {
        return new JXLExcelWriter(file);
    }

    public static ExcelWriter createExcelWriterInstance(InputStream inputStream) throws BiffException, IOException {
        return new JXLExcelWriter(inputStream);
    }
}
