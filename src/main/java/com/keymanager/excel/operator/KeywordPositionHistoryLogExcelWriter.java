package com.keymanager.excel.operator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;

import com.keymanager.excel.definition.KeywordPositionHistoryLogDefinition;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;
import com.keymanager.value.CustomerKeywordPositionHistoryLogView;

public class KeywordPositionHistoryLogExcelWriter {
	private final String fileName = "KeywordPositionHistoryLog.xls";
	protected JXLExcelWriter writer;

	public KeywordPositionHistoryLogExcelWriter() throws BiffException, IOException {
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
	
	private void writeRow(int rowIndex, CustomerKeywordPositionHistoryLogView view) throws ExcelWriteException{
		writer.addLabelCell(KeywordPositionHistoryLogDefinition.Uuid.getColumnIndex(), rowIndex, view.getUuid());
		writer.addLabelCell(KeywordPositionHistoryLogDefinition.ContactPerson.getColumnIndex(), rowIndex, view.getContactPerson());
		writer.addLabelCell(KeywordPositionHistoryLogDefinition.Keyword.getColumnIndex(), rowIndex, view.getKeyword());
		writer.addLabelCell(KeywordPositionHistoryLogDefinition.OriginalURL.getColumnIndex(), rowIndex, view.getApplicableUrl());
		writer.addLabelCell(KeywordPositionHistoryLogDefinition.OriginalPhoneURL.getColumnIndex(), rowIndex, view.getApplicablePhoneUrl());
		writer.addLabelCell(KeywordPositionHistoryLogDefinition.Type.getColumnIndex(), rowIndex, view.getType());
		writer.addLabelCell(KeywordPositionHistoryLogDefinition.PositionNumber.getColumnIndex(), rowIndex, view.getPositionNumber());
		writer.addLabelCell(KeywordPositionHistoryLogDefinition.IP.getColumnIndex(), rowIndex, view.getIp());
		writer.addLabelCell(KeywordPositionHistoryLogDefinition.CreateTime.getColumnIndex(), rowIndex, view.getCreateTime().toString());
	}
	
	private void writeTotalFee(int rowIndex, String total) throws ExcelWriteException{
	}
	

	public void writeDataToExcel(List<CustomerKeywordPositionHistoryLogView> views) throws ExcelWriteException {
		if(!Utils.isEmpty(views)){
			int rowIndex = 1;
			double total = 0;
			for(CustomerKeywordPositionHistoryLogView view : views){
				writeRow(rowIndex++, view);
				total = total + view.getSubTotal();
			}
			writeTotalFee(rowIndex, Utils.formatDouble(total));
		}
	}

}