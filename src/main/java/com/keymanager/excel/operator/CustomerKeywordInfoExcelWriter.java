package com.keymanager.excel.operator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;

import com.keymanager.excel.definition.CustomerKeywordInfoDefinition;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;
import com.keymanager.value.CustomerKeywordVO;

public class CustomerKeywordInfoExcelWriter {
	private final String fileName = "CustomerKeywordInfo.xls";
	protected JXLExcelWriter writer;

	public CustomerKeywordInfoExcelWriter() throws BiffException, IOException {
		super();
		writer = new JXLExcelWriter(getTemplateFile());
		writer.setCurrentWorkSheetWithName("KeywordList");
	}
	
	public void saveAs(String outputFileName) throws Exception{
		writer.saveAs(getWebRootPath() + outputFileName);
	}
	
  public byte[] getExcelContentBytes() throws Exception {
  	return writer.getExcelContentBytes();
  }
  	
	private File getTemplateFile(){
		String SERVLET_CONTEXT_PATH = getWebRootPath();

		// 这里 SERVLET_CONTEXT_PATH 就是WebRoot的路径
		String path = SERVLET_CONTEXT_PATH + "/" + fileName;
		path = path.replaceAll("%20", " ");
		return new File(path); 
	}

	private String getWebRootPath() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = ClassLoader.getSystemClassLoader();
		}
		java.net.URL url = classLoader.getResource("");
		String ROOT_CLASS_PATH = url.getPath() + "/";
		File rootFile = new File(ROOT_CLASS_PATH);
		String WEB_INFO_DIRECTORY_PATH = rootFile.getParent() + "/";
		File webInfoDir = new File(WEB_INFO_DIRECTORY_PATH);
		String SERVLET_CONTEXT_PATH = webInfoDir.getParent() + "/";
		return SERVLET_CONTEXT_PATH;
	}
	
	private void writeRow(int rowIndex, CustomerKeywordVO view) throws ExcelWriteException{
		writer.addLabelCell(CustomerKeywordInfoDefinition.Keyword.getColumnIndex(), rowIndex, view.getKeyword());
		writer.addLabelCell(CustomerKeywordInfoDefinition.URL.getColumnIndex(), rowIndex, view.getUrl());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Title.getColumnIndex(), rowIndex, view.getTitle());
		writer.addLabelCell(CustomerKeywordInfoDefinition.OriginalPosition.getColumnIndex(), rowIndex, view.getInitialPosition());
		writer.addLabelCell(CustomerKeywordInfoDefinition.CurrentPosition.getColumnIndex(), rowIndex, view.getCurrentPosition());
		writer.addLabelCell(CustomerKeywordInfoDefinition.PlannedOptimizeCount.getColumnIndex(), rowIndex, view.getOptimizePlanCount());
		writer.addLabelCell(CustomerKeywordInfoDefinition.OptimizedCount.getColumnIndex(), rowIndex, view.getOptimizedCount());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Price1.getColumnIndex(), rowIndex, view.getPositionFirstFeeString());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Price2.getColumnIndex(), rowIndex, view.getPositionSecondFeeString());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Price3.getColumnIndex(), rowIndex, view.getPositionThirdFeeString());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Price4.getColumnIndex(), rowIndex, view.getPositionForthFeeString());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Price5.getColumnIndex(), rowIndex, view.getPositionFifthFeeString());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Price10.getColumnIndex(), rowIndex, view.getPositionFirstPageFeeString());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Index.getColumnIndex(), rowIndex, view.getCurrentIndexCount());
		writer.addLabelCell(CustomerKeywordInfoDefinition.Remarks.getColumnIndex(), rowIndex, view.getRemarks());
	}

	public void writeDataToExcel(List<CustomerKeywordVO> views) throws ExcelWriteException {
		if(!Utils.isEmpty(views)){
			int rowIndex = 1;
			for(CustomerKeywordVO view : views){
				writeRow(rowIndex++, view);
			}
		}
	}

}