package com.keymanager.ckadmin.util.excel;

public class ExcelWriteException extends Exception {

    protected ExcelWriteException(String message) {
        super(message);
    }

    public ExcelWriteException(Throwable exception) {
        super(exception);
    }
}
