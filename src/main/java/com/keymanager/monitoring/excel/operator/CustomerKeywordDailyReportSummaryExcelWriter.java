package com.keymanager.monitoring.excel.operator;

import com.keymanager.monitoring.excel.definition.CustomerKeywordDailyReportSummaryDefinition;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

public class CustomerKeywordDailyReportSummaryExcelWriter {
	private final String fileName = "CustomerKeywordDailyReportSummary.xls";
	protected JXLExcelWriter writer;
	private long dailyReportUuid;
	private String dailyReportFileName;
	private String webRootPath = null;

	public CustomerKeywordDailyReportSummaryExcelWriter(long dailyReportUuid, String externalAccount) throws BiffException, IOException {
		super();
		this.webRootPath = Utils.getWebRootPath();
		this.dailyReportFileName = "dailyreport/summary/" + externalAccount + ".xls";
		File file = getTemplateFile(dailyReportFileName);
		int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if(!file.exists() || dayOfMonth == 1 || dayOfMonth == 11 || dayOfMonth == 21){
			file = getTemplateFile(fileName);
		}
		writer = new JXLExcelWriter(file);
		this.dailyReportUuid = dailyReportUuid;
	}

	public void saveAs(String outputFileName) throws Exception{
		writer.saveAs(webRootPath + outputFileName);
	}
	
  public byte[] getExcelContentBytes() throws Exception {
  	return writer.getExcelContentBytes();
  }
  	
	private File getTemplateFile(String fileName){
		String SERVLET_CONTEXT_PATH = webRootPath;
		// 这里 SERVLET_CONTEXT_PATH 就是WebRoot的路径
		String path = SERVLET_CONTEXT_PATH + fileName;
		path = path.replaceAll("%20", " ");
		return new File(path);
	}
	
	public void writeDailySummaryRow(Map<String, String> summaryMap, double percentage) throws ExcelWriteException{
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int endOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		int rowIndex = day;
		double total = 0d;
		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.Date.getColumnIndex(), rowIndex, day);
		if(summaryMap.get("百度_PC") != null) {
			writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.BaiduPC.getColumnIndex(), rowIndex, summaryMap.get("百度_PC"));
			total = total + Double.parseDouble(summaryMap.get("百度_PC"));
		}
		if(summaryMap.get("百度_Phone") != null) {
			writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.BaiduPhone.getColumnIndex(), rowIndex, summaryMap.get("百度_Phone"));
			total = total + Double.parseDouble(summaryMap.get("百度_Phone"));
		}
		if(summaryMap.get("搜狗_PC") != null) {
			writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.SogouPC.getColumnIndex(), rowIndex, summaryMap.get("搜狗_PC"));
			total = total + Double.parseDouble(summaryMap.get("搜狗_PC"));
		}
		if(summaryMap.get("搜狗_Phone") != null) {
			writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.SogouPhone.getColumnIndex(), rowIndex, summaryMap.get("搜狗_Phone"));
			total = total + Double.parseDouble(summaryMap.get("搜狗_Phone"));
		}
		if(summaryMap.get("360_PC") != null) {
			writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.SoPC.getColumnIndex(), rowIndex, summaryMap.get("360_PC"));
			total = total + Double.parseDouble(summaryMap.get("360_PC"));
		}
		if(summaryMap.get("360_Phone") != null) {
			writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.SoPhone.getColumnIndex(), rowIndex, summaryMap.get("360_Phone"));
			total = total + Double.parseDouble(summaryMap.get("360_Phone"));
		}
		if(summaryMap.get("神马_Phone") != null) {
			writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.UC.getColumnIndex(), rowIndex, summaryMap.get("神马_Phone"));
			total = total + Double.parseDouble(summaryMap.get("神马_Phone"));
		}
		if(summaryMap.get("必应中国_PC") != null) {
			writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.BingCN.getColumnIndex(), rowIndex, summaryMap.get("必应中国_PC"));
			total = total + Double.parseDouble(summaryMap.get("必应中国_PC"));
		}
		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex(), rowIndex, total);

		if(day == 10){
			writer.addFormulanCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, day, "SUM(J2:J11)*" + percentage);
		}else if(day == 20){
			writer.addFormulanCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, day, "SUM(J12:J21)*" + percentage);
		}else if(day == endOfMonth){
			writer.addFormulanCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, day, "SUM(J22:J" + (day + 1) + ")*" + percentage);
		}
	}

	public void writeDataToExcel(String externalAccount) throws Exception {
		saveAs(dailyReportFileName);
		String fileName = "dailyreport/" + dailyReportUuid + "/" + externalAccount + "/" + externalAccount + "_Total.xls";
		FileUtil.copyFile(webRootPath + dailyReportFileName, webRootPath + fileName, true);
	}
}