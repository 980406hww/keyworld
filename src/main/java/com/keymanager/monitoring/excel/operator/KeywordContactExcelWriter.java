package com.keymanager.monitoring.excel.operator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;

import com.keymanager.enums.CollectMethod;
import com.keymanager.monitoring.excel.definition.CustomerCollectFeeDefinition;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelColor;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;
import com.keymanager.value.CustomerKeywordPositionView;

public class KeywordContactExcelWriter {
	private final String fileName = "KeywordContactTemplate.xls";
	protected JXLExcelWriter writer;

	public KeywordContactExcelWriter() throws BiffException, IOException {
		super();
		writer = new JXLExcelWriter(getTemplateFile());
		writer.setCurrentWorkSheetWithName("KeywordContact");
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
		String path = SERVLET_CONTEXT_PATH + "/" + fileName;
		path = path.replaceAll("%20", " ");
		return new File(path); 
	}
	
	private void writeRow(int rowIndex, CustomerKeywordPositionView view) throws ExcelWriteException{
		writer.addLabelCell(CustomerCollectFeeDefinition.Uuid.getColumnIndex(), rowIndex, view.getUuid());
		writer.addLabelCell(CustomerCollectFeeDefinition.ContactPerson.getColumnIndex(), rowIndex, view.getContactPerson());
		writer.addLabelCell(CustomerCollectFeeDefinition.Remarks.getColumnIndex(), rowIndex, view.getRemarks());
		writer.addLabelCell(CustomerCollectFeeDefinition.Date.getColumnIndex(), rowIndex, view.getCreateDate() != null ? view.getCreateDate().toString() : "");
		writer.addLabelCell(CustomerCollectFeeDefinition.Keyword.getColumnIndex(), rowIndex, view.getKeyword());
		writer.addLabelCell(CustomerCollectFeeDefinition.OriginalURL.getColumnIndex(), rowIndex, view.getApplicableUrl());
		writer.addLabelCell(CustomerCollectFeeDefinition.OriginalPhoneURL.getColumnIndex(), rowIndex, view.getApplicablePhoneUrl());
		writer.addLabelCell(CustomerCollectFeeDefinition.CollectMethod.getColumnIndex(), rowIndex, getCollectMethodName(view.getCollectMethod()));
		writer.addLabelCell(CustomerCollectFeeDefinition.PcPosition.getColumnIndex(), rowIndex, view.getPcPosition() > 0 ? view.getPcPosition() + "" : "");
		writer.addLabelCell(CustomerCollectFeeDefinition.ChupingPosition.getColumnIndex(), rowIndex, view.getChupingPosition() > 0 ? view.getChupingPosition() + "" : "");
		writer.addLabelCell(CustomerCollectFeeDefinition.JisuPosition.getColumnIndex(), rowIndex, view.getJisuPosition() > 0 ? view.getJisuPosition() + "" : "");
		writer.addLabelCell(CustomerCollectFeeDefinition.Fee.getColumnIndex(), rowIndex, view.getPcFeeString());
		writer.addLabelCell(CustomerCollectFeeDefinition.ChupingFee.getColumnIndex(), rowIndex, view.getChupingFeeString());
		writer.addLabelCell(CustomerCollectFeeDefinition.JisuFee.getColumnIndex(), rowIndex, view.getJisuFeeString());
		writer.addLabelCell(CustomerCollectFeeDefinition.SubTotal.getColumnIndex(), rowIndex, view.getSubTotalString());
	}
	
	private void writeTotalFee(int rowIndex, double total) throws ExcelWriteException{
		writer.addLabelCell(CustomerCollectFeeDefinition.JisuFee.getColumnIndex(), rowIndex, "总计应收:");
		writer.addLabelCell(CustomerCollectFeeDefinition.SubTotal.getColumnIndex(), rowIndex, total + "");
		writer.setCellFontColor(writer.getWritableCell(CustomerCollectFeeDefinition.SubTotal.getColumnIndex(), rowIndex), ExcelColor.Red);
	}
	
	protected String getCollectMethodName(String code) {
		CollectMethod collectMethod = CollectMethod.findByCode(code);
		if (collectMethod != null){
			return collectMethod.getName();			
		}
		return "";
	}

	public void writeDataToExcel(List<CustomerKeywordPositionView> views) throws ExcelWriteException {
		if(!Utils.isEmpty(views)){
			int rowIndex = 1;
			double total = 0;
			for(CustomerKeywordPositionView view : views){
				writeRow(rowIndex++, view);
				total = total + view.getSubTotal();
			}
			writeTotalFee(rowIndex, total);
		}
	}

}