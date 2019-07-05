package com.keymanager.monitoring.excel.operator;

import com.keymanager.monitoring.excel.definition.CustomerKeywordDailyReportSummaryDefinition;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;
import jxl.Sheet;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

public class CustomerKeywordDailyReportTotalExcelWriter {
	private final String fileName = "CustomerKeywordDailyReportTotal.xls";
	protected JXLExcelWriter writer;
	private long dailyReportUuid;
	private String dailyReportFileName;
	private String webRootPath = null;
	private int dateWidth;
	private int baiduPCWidth;
	private int baiduPhoneWidth;
	private int sogouPCWidth;
	private int sogouPhoneWidth;
	private int soWidth;
	private int ucWidth;
	private int todayFeeWidth;

	public CustomerKeywordDailyReportTotalExcelWriter(long dailyReportUuid) throws BiffException, IOException {
		super();
		this.webRootPath = Utils.getWebRootPath();
		this.dailyReportFileName = "dailyreport/summary/total.xls";
		File file = getTemplateFile(dailyReportFileName);
		int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (!file.exists() || dayOfMonth == 1 || dayOfMonth == 11 || dayOfMonth == 21) {
			file = getTemplateFile(fileName);
		}
		writer = new JXLExcelWriter(file);
		this.dailyReportUuid = dailyReportUuid;
	}

	public void initSheet(String externalAccount) {
		writer.removeSheet("Default");
		Sheet sheet = writer.getSheetByName(externalAccount);
		if(sheet != null){
			writer.setCurrentWorkSheetWithName(externalAccount);
		}else{
			writer.createSheetWithNameAndSetAsCurrentSheet(externalAccount, 0);
		}
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
	
	public void writeDailyTotalRow(Map<String, String> summaryMap, double percentage) throws ExcelWriteException{
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
			writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.So.getColumnIndex(), rowIndex, summaryMap.get("360_PC"));
			total = total + Double.parseDouble(summaryMap.get("360_PC"));
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
			writer.addFormulanCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, day, "SUM(I2:I11)*" + percentage);
		}else if(day == 20){
			writer.addFormulanCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, day, "SUM(I12:I21)*" + percentage);
		}else if(day == endOfMonth){
			writer.addFormulanCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, day, "SUM(I22:I" + (day + 1) + ")*" + percentage);
		}
	}

	public int calculateWidth(int existWidth, String label){
		if(existWidth < label.getBytes().length){
			existWidth = label.getBytes().length;
		}
		return existWidth;
	}

	public void writeDailyTotalTitle(int rowIndex) throws ExcelWriteException{
		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.Date.getColumnIndex(), rowIndex,CustomerKeywordDailyReportSummaryDefinition.Date
				.getTitle(), true);
		dateWidth = calculateWidth(dateWidth, CustomerKeywordDailyReportSummaryDefinition.Date.getTitle());
		writer.setColumnView(CustomerKeywordDailyReportSummaryDefinition.Date.getColumnIndex(), dateWidth + 6);

		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.BaiduPC.getColumnIndex(), rowIndex, CustomerKeywordDailyReportSummaryDefinition.BaiduPC
				.getTitle(), true);
		baiduPCWidth = calculateWidth(baiduPCWidth, CustomerKeywordDailyReportSummaryDefinition.BaiduPC.getTitle());
		writer.setColumnView(CustomerKeywordDailyReportSummaryDefinition.BaiduPC.getColumnIndex(), baiduPCWidth + 6);

		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.BaiduPhone.getColumnIndex(), rowIndex, CustomerKeywordDailyReportSummaryDefinition.BaiduPhone.getTitle(), true);
		baiduPhoneWidth = calculateWidth(baiduPhoneWidth, CustomerKeywordDailyReportSummaryDefinition.BaiduPhone.getTitle());
		writer.setColumnView(CustomerKeywordDailyReportSummaryDefinition.BaiduPhone.getColumnIndex(), baiduPhoneWidth + 6);

		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.SogouPC.getColumnIndex(), rowIndex, CustomerKeywordDailyReportSummaryDefinition
				.SogouPC.getTitle(), true);
		sogouPCWidth = calculateWidth(sogouPCWidth, CustomerKeywordDailyReportSummaryDefinition.SogouPC.getTitle());
		writer.setColumnView(CustomerKeywordDailyReportSummaryDefinition.SogouPC.getColumnIndex(), sogouPCWidth + 6);

		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.SogouPhone.getColumnIndex(), rowIndex, CustomerKeywordDailyReportSummaryDefinition.SogouPhone.getTitle(), true);
		sogouPhoneWidth = calculateWidth(sogouPhoneWidth, CustomerKeywordDailyReportSummaryDefinition.SogouPhone.getTitle());
		writer.setColumnView(CustomerKeywordDailyReportSummaryDefinition.SogouPhone.getColumnIndex(), sogouPhoneWidth + 6);

		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.So.getColumnIndex(), rowIndex, CustomerKeywordDailyReportSummaryDefinition.So.getTitle(), true);
		soWidth = calculateWidth(soWidth, CustomerKeywordDailyReportSummaryDefinition.So.getTitle());
		writer.setColumnView(CustomerKeywordDailyReportSummaryDefinition.So.getColumnIndex(), soWidth + 6);

		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.UC.getColumnIndex(), rowIndex, CustomerKeywordDailyReportSummaryDefinition.UC.getTitle(), true);
		ucWidth = calculateWidth(ucWidth, CustomerKeywordDailyReportSummaryDefinition.UC.getTitle());
		writer.setColumnView(CustomerKeywordDailyReportSummaryDefinition.UC.getColumnIndex(), ucWidth + 6);

		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.BingCN.getColumnIndex(), rowIndex, CustomerKeywordDailyReportSummaryDefinition.BingCN.getTitle(), true);
		ucWidth = calculateWidth(ucWidth, CustomerKeywordDailyReportSummaryDefinition.BingCN.getTitle());
		writer.setColumnView(CustomerKeywordDailyReportSummaryDefinition.BingCN.getColumnIndex(), ucWidth + 6);

		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex(), rowIndex, CustomerKeywordDailyReportSummaryDefinition
				.TodayFee.getTitle(), true);
		todayFeeWidth = calculateWidth(todayFeeWidth, CustomerKeywordDailyReportSummaryDefinition.TodayFee.getTitle());
		writer.setColumnView(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex(), todayFeeWidth + 6);

		writer.setColumnView(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, todayFeeWidth + 20);
	}

	public void writeDataToExcel() throws Exception {
		saveAs(dailyReportFileName);
		String fileName = "dailyreport/" + dailyReportUuid + "/Total.xls";
		FileUtil.copyFile(webRootPath + dailyReportFileName, webRootPath + fileName, true);
	}
}