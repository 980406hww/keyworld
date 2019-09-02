package com.keymanager.monitoring.excel.operator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.keymanager.monitoring.entity.CustomerKeyword;
import jxl.read.biff.BiffException;

import com.keymanager.monitoring.excel.definition.CustomerKeywordInfoDefinition;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;

public class CustomerKeywordInfoExcelWriter {
	private final String fileName = "CustomerKeywordInfo.xls";
	protected JXLExcelWriter writer;

	public CustomerKeywordInfoExcelWriter() throws BiffException, IOException {
		super();
		writer = new JXLExcelWriter(getTemplateFile());
		writer.setCurrentWorkSheetWithName("KeywordList");
	}
	
	public void saveAs(String outputFileName) throws Exception{
		writer.saveAs(Utils.getWebRootPath() + outputFileName);
	}
	
	public byte[] getExcelContentBytes() throws Exception {
		return writer.getExcelContentBytes();
	}
  	
	private File getTemplateFile(){
		String SERVLET_CONTEXT_PATH = Utils.getWebRootPath();

		// 这里 SERVLET_CONTEXT_PATH 就是WebRoot的路径
		String path = SERVLET_CONTEXT_PATH + fileName;
		path = path.replaceAll("%20", " ");
		return new File(path); 
	}
	
	private void writeRow(int rowIndex, CustomerKeyword view) throws ExcelWriteException{
		writer.addLabelCell(CustomerKeywordInfoDefinition.Keyword.getColumnIndex(), rowIndex, view.getKeyword());
		writer.addLabelCell(CustomerKeywordInfoDefinition.URL.getColumnIndex(), rowIndex, view.getUrl());
		writer.addLabelCell(CustomerKeywordInfoDefinition.OriginalUrl.getColumnIndex(), rowIndex, view.getOriginalUrl());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Title.getColumnIndex(), rowIndex, view.getTitle());
		writer.addLabelCell(CustomerKeywordInfoDefinition.BearPawNumber.getColumnIndex(), rowIndex, view.getBearPawNumber());
		writer.addLabelCell(CustomerKeywordInfoDefinition.OriginalPosition.getColumnIndex(), rowIndex, view.getInitialPosition()==null?0:view.getInitialPosition());
		writer.addLabelCell(CustomerKeywordInfoDefinition.CurrentPosition.getColumnIndex(), rowIndex, view.getCurrentPosition()==null?0:view.getCurrentPosition());
		writer.addLabelCell(CustomerKeywordInfoDefinition.PlannedOptimizeCount.getColumnIndex(), rowIndex, view.getOptimizePlanCount());
		writer.addLabelCell(CustomerKeywordInfoDefinition.OptimizedCount.getColumnIndex(), rowIndex, view.getOptimizedCount());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Price1.getColumnIndex(), rowIndex, view.getPositionFirstFeeString());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Price2.getColumnIndex(), rowIndex, view.getPositionSecondFeeString());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Price3.getColumnIndex(), rowIndex, view.getPositionThirdFeeString());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Price4.getColumnIndex(), rowIndex, view.getPositionForthFeeString());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Price5.getColumnIndex(), rowIndex, view.getPositionFifthFeeString());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Price10.getColumnIndex(), rowIndex, view.getPositionFirstPageFeeString());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Index.getColumnIndex(), rowIndex, view.getCurrentIndexCount()+"");
		writer.addLabelCell(CustomerKeywordInfoDefinition.Remarks.getColumnIndex(), rowIndex, view.getRemarks());
	}

	public void writeDataToExcel(List<CustomerKeyword> views) throws ExcelWriteException {
		if(!Utils.isEmpty(views)){
			int rowIndex = 1;
			for(CustomerKeyword view : views){
				writeRow(rowIndex++, view);
			}
		}
	}

}