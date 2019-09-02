package com.keymanager.monitoring.excel.operator;

import com.keymanager.monitoring.entity.NegativeKeywordName;
import com.keymanager.monitoring.excel.definition.NegativeKeywordNameDefinition;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class NegativeKeywordNameExcelWriter {
	private final String fileName = "NegativeKeywordName.xls";
	protected JXLExcelWriter writer;

	public NegativeKeywordNameExcelWriter() throws BiffException, IOException {
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
	
	private void writeRow(int rowIndex, NegativeKeywordName view) throws ExcelWriteException{
		writer.addLabelCell(NegativeKeywordNameDefinition.Name.getColumnIndex(), rowIndex, view.getName());
		writer.addLabelCell(NegativeKeywordNameDefinition.OfficialUrl.getColumnIndex(), rowIndex, view.getOfficialUrl());
		writer.addLabelCell(NegativeKeywordNameDefinition.Email.getColumnIndex(), rowIndex, view.getEmail());
	}

	public void writeDataToExcel(List<NegativeKeywordName> views) throws ExcelWriteException {
		if(!Utils.isEmpty(views)){
			int rowIndex = 1;
			for(NegativeKeywordName view : views){
				writeRow(rowIndex++, view);
			}
		}
	}

}