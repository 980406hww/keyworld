package com.keymanager.monitoring.excel.operator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;

import com.keymanager.enums.CollectMethod;
import com.keymanager.monitoring.excel.definition.CustomerCollectFeeWithLogDefinition;
import com.keymanager.monitoring.excel.definition.KeywordPositionHistoryLogDefinition;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelColor;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;
import com.keymanager.value.CustomerKeywordPositionHistoryLogView;
import com.keymanager.value.CustomerKeywordPositionView;

public class CustomerCollectFeeWithLogExcelWriter {
	private final String fileName = "CustomerCollectFeeListWithLog.xls";
	protected JXLExcelWriter writer;

	public CustomerCollectFeeWithLogExcelWriter() throws BiffException, IOException {
		super();
		writer = new JXLExcelWriter(getTemplateFile());
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
	
	private void writeCollectFeeRow(int rowIndex, CustomerKeywordPositionView view) throws ExcelWriteException{
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.Uuid.getColumnIndex(), rowIndex, view.getUuid());
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.ContactPerson.getColumnIndex(), rowIndex, view.getContactPerson());
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.Remarks.getColumnIndex(), rowIndex, view.getRemarks());
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.Date.getColumnIndex(), rowIndex, view.getCreateDate() != null ? view.getCreateDate().toString() : "");
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.Keyword.getColumnIndex(), rowIndex, view.getKeyword());
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.OriginalURL.getColumnIndex(), rowIndex, view.getApplicableUrl());
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.OriginalPhoneURL.getColumnIndex(), rowIndex, view.getApplicablePhoneUrl());
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.CollectMethod.getColumnIndex(), rowIndex, getCollectMethodName(view.getCollectMethod()));
		if(view.getPcPosition() > 0 && view.getPcFee() > 0){
			writer.addLabelCell(CustomerCollectFeeWithLogDefinition.PcPosition.getColumnIndex(), rowIndex, view.getPcPosition());
		}
		if(view.getCurrentPosition() > 0 && view.getPcFee() > 0){
			writer.addLabelCell(CustomerCollectFeeWithLogDefinition.PcCurrentPosition.getColumnIndex(), rowIndex, view.getCurrentPosition());
		}
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.PcPositionLog.getColumnIndex(), rowIndex, view.getPcPositionLog());
		if(view.getChupingPosition() > 0 && view.getChupingFee() > 0){
			writer.addLabelCell(CustomerCollectFeeWithLogDefinition.ChupingPosition.getColumnIndex(), rowIndex,  view.getChupingPosition());
		}
		if(view.getChupingCurrentPosition() > 0 && view.getChupingFee() > 0){
			writer.addLabelCell(CustomerCollectFeeWithLogDefinition.ChupingCurrentPosition.getColumnIndex(), rowIndex,  view.getChupingCurrentPosition());
		}
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.ChupingPositionLog.getColumnIndex(), rowIndex, view.getChupingPositionLog());
		if(view.getJisuPosition() > 0 && view.getJisuFee() > 0){
			writer.addLabelCell(CustomerCollectFeeWithLogDefinition.JisuPosition.getColumnIndex(), rowIndex, view.getJisuPosition());
		}
		if(view.getJisuCurrentPosition() > 0 && view.getJisuFee() > 0){
			writer.addLabelCell(CustomerCollectFeeWithLogDefinition.JisuCurrentPosition.getColumnIndex(), rowIndex, view.getJisuCurrentPosition());
		}
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.JisuPositionLog.getColumnIndex(), rowIndex, view.getJisuPositionLog());
		if(view.getPcFee() > 0){
			writer.addLabelCell(CustomerCollectFeeWithLogDefinition.Fee.getColumnIndex(), rowIndex, view.getPcFee());
		}
		if(view.getChupingFee() > 0){
			writer.addLabelCell(CustomerCollectFeeWithLogDefinition.ChupingFee.getColumnIndex(), rowIndex, view.getChupingFee());
		}
		if(view.getJisuFee() > 0){
			writer.addLabelCell(CustomerCollectFeeWithLogDefinition.JisuFee.getColumnIndex(), rowIndex, view.getJisuFee());
		}
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.SubTotal.getColumnIndex(), rowIndex, Double.parseDouble(view.getSubTotalString()));
	}
	
	private void writeCollectFeeTotalFee(int rowIndex, double total) throws ExcelWriteException{
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.JisuFee.getColumnIndex(), rowIndex, "总计应收:");
		writer.addLabelCell(CustomerCollectFeeWithLogDefinition.SubTotal.getColumnIndex(), rowIndex, total);
		writer.setCellFontColor(writer.getWritableCell(CustomerCollectFeeWithLogDefinition.SubTotal.getColumnIndex(), rowIndex), ExcelColor.Red);
	}
	
	protected String getCollectMethodName(String code) {
		CollectMethod collectMethod = CollectMethod.findByCode(code);
		if (collectMethod != null){
			return collectMethod.getName();			
		}
		return "";
	}

	public void writeCollectFeeDataToExcel(List<CustomerKeywordPositionView> views) throws ExcelWriteException {
		if(!Utils.isEmpty(views)){
			writer.setCurrentWorkSheetWithName("KeywordList");
			int rowIndex = 1;
			double total = 0;
			for(CustomerKeywordPositionView view : views){
				writeCollectFeeRow(rowIndex++, view);
				total = total + view.getSubTotal();
			}
			writeCollectFeeTotalFee(rowIndex, total);
		}
	}

	private void writeLogRow(int rowIndex, CustomerKeywordPositionHistoryLogView view) throws ExcelWriteException{
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

	public void writeLogDataToExcel(List<CustomerKeywordPositionHistoryLogView> views) throws ExcelWriteException {
		if(!Utils.isEmpty(views)){
			writer.setCurrentWorkSheetWithName("Log");
			int rowIndex = 1;
			double total = 0;
			for(CustomerKeywordPositionHistoryLogView view : views){
				writeLogRow(rowIndex++, view);
				total = total + view.getSubTotal();
			}
		}
	}

}