package com.keymanager.monitoring.excel.operator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.keymanager.monitoring.entity.CustomerKeyword;
import jxl.Cell;
import jxl.read.biff.BiffException;

import com.keymanager.enums.CollectMethod;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.JXLExcelReader;

public abstract class AbstractExcelReader {
	protected JXLExcelReader reader;
	public AbstractExcelReader() {
		super();
	}
	public static AbstractExcelReader createExcelOperator(InputStream inputStream, String excelType) throws BiffException, IOException{
		if (Constants.EXCEL_TYPE_SUPER_USER_SIMPLE.equals(excelType)){
			return new SuperUserSimpleKeywordExcelOperator(inputStream);
		}else if(Constants.EXCEL_TYPE_SUPER_USER_FULL.equals(excelType)){
			return new SuperUserFullKeywordExcelOperator(inputStream);
		}
		return null;
	}

	public AbstractExcelReader(InputStream inputStream) throws BiffException, IOException {
		super();
		reader = new JXLExcelReader(inputStream);
		reader.setCurrentWorkSheetWithName("KeywordList");
	}
	
	public abstract CustomerKeyword readRow(int rowIndex);
	
	protected String getStringValue(int columnIndex, int rowIndex) {
		Cell cell = reader.getCell(columnIndex, rowIndex);
		if (cell != null){
			return cell.getContents().trim();
		}
		return null;
	}

	protected int getIntValue(int columnIndex, int rowIndex) {
		Cell cell = reader.getCell(columnIndex, rowIndex);
		if (cell != null && !Utils.isNullOrEmpty(cell.getContents())){
			return Integer.parseInt(cell.getContents().trim());
		}
		return 0;
	}
	
	protected double getDoubleValue(int columnIndex, int rowIndex) {
		Cell cell = reader.getCell(columnIndex, rowIndex);
		if (cell != null && !Utils.isNullOrEmpty(cell.getContents())){
			return Double.parseDouble(cell.getContents().trim());
		}
		return 0;
	}

	protected String getCollectMethodValue(String name) {
		CollectMethod collectMethod = CollectMethod.findByName(name);
		if (collectMethod != null){
			return collectMethod.getCode();			
		}
		return "";
	}

	public List<CustomerKeyword> readDataFromExcel() {
		List<CustomerKeyword> customerKeywords= new ArrayList<CustomerKeyword>();
		for (int i = 1; i < reader.getCurrentSheet().getRows(); i++){
			CustomerKeyword customerKeyword = this.readRow(i);
			if(customerKeyword != null){
				customerKeywords.add(customerKeyword);
			}
		}
		return customerKeywords;
	}

}