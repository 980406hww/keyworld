package com.keymanager.monitoring.excel.operator;

import com.keymanager.monitoring.excel.definition.CustomerKeywordDailyReportSummaryDefinition;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.nio.DoubleBuffer;
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
		if(summaryMap.get("360") != null) {
			writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.So.getColumnIndex(), rowIndex, summaryMap.get("360"));
			total = total + Double.parseDouble(summaryMap.get("360"));
		}
		if(summaryMap.get("UC") != null) {
			writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.UC.getColumnIndex(), rowIndex, summaryMap.get("UC"));
			total = total + Double.parseDouble(summaryMap.get("UC"));
		}
		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex(), rowIndex, total);

		if(day == 10){
			writer.addFormulanCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, day, "SUM(H2:H11)*" + percentage);
		}else if(day == 20){
			writer.addFormulanCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, day, "SUM(H12:H21)*" + percentage);
		}else if(day == endOfMonth){
			writer.addFormulanCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, day, "SUM(H22:H" + (day + 1) + ")*" + percentage);
		}
	}

	public void writeDataToExcel(String externalAccount) throws Exception {
		saveAs(dailyReportFileName);
		String fileName = "dailyreport/" + dailyReportUuid + "/" + externalAccount + "/" + externalAccount + "_小计.xls";
		FileUtil.copyFile(webRootPath + dailyReportFileName, webRootPath + fileName, true);
	}
}