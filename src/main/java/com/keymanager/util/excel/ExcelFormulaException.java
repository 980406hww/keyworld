package com.keymanager.util.excel;

public class ExcelFormulaException extends RuntimeException {

	private static final long serialVersionUID = 930154356892426761L;

	protected ExcelFormulaException(String message) {
		super(message);
	}

	public ExcelFormulaException(Throwable exception) {
		super(exception);
	}

}
