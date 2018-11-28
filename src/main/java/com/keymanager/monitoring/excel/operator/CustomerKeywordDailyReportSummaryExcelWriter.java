package com.keymanager.monitoring.excel.operator;

import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.excel.definition.CustomerKeywordDailyReportSummaryDefinition;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;
import jxl.Sheet;
import jxl.read.biff.BiffException;
import org.apache.commons.collections.MapUtils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class CustomerKeywordDailyReportSummaryExcelWriter {
	private final String fileName = "CustomerKeywordDailyReportSummary.xls";
	protected JXLExcelWriter writer;
	private long dailyReportUuid;
	private String dailyReportFileName;
	private String webRootPath = null;

	public CustomerKeywordDailyReportSummaryExcelWriter(long dailyReportUuid, String loginName) throws BiffException, IOException {
		super();
		this.webRootPath = Utils.getWebRootPath();
		this.dailyReportFileName = "dailyreport/summary/" + loginName + ".xls";
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
		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.Date.getColumnIndex(), rowIndex, day);
		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.BaiduPC.getColumnIndex(), rowIndex, summaryMap.get("百度_PC") == null ? "" : summaryMap.get("百度_PC"));
		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.BaiduPhone.getColumnIndex(), rowIndex, summaryMap.get("百度_Phone") == null ? "" : summaryMap.get("百度_Phone"));
		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.SogouPC.getColumnIndex(), rowIndex, summaryMap.get("搜狗_PC") == null ? "" : summaryMap.get("搜狗_PC"));
		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.SogouPhone.getColumnIndex(), rowIndex, summaryMap.get("搜狗_Phone") == null ? "" : summaryMap.get("搜狗_Phone"));
		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.So.getColumnIndex(), rowIndex, summaryMap.get("360") == null ? "" : summaryMap.get("360"));
		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.UC.getColumnIndex(), rowIndex, summaryMap.get("UC") == null ? "" : summaryMap.get("UC"));
		writer.addLabelCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex(), rowIndex, "SUM(B" + rowIndex + ":G" + rowIndex + ")");

		if(day == 10){
			writer.addFormulanCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, day, "SUM(H2:H11)*" + percentage);
		}else if(day == 20){
			writer.addFormulanCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, day, "SUM(H12:H21)*" + percentage);
		}else if(day == endOfMonth){
			writer.addFormulanCell(CustomerKeywordDailyReportSummaryDefinition.TodayFee.getColumnIndex() + 1, day, "SUM(H22:H" + (day + 1) + ")*" + percentage);
		}
	}

	public void writeDataToExcel(List<CustomerKeyword> views, String loginName, String contactPerson, String terminalType) throws Exception {
		saveAs(dailyReportFileName);
		String fileName = "dailyreport/" + dailyReportUuid + "/" + loginName + "/" + contactPerson;
		if(TerminalTypeEnum.Phone.name().equals(terminalType)){
			fileName = fileName + "(Mobile)";
		}
		fileName = fileName + ".xls";
		FileUtil.copyFile(webRootPath + dailyReportFileName, webRootPath + fileName, true);
	}
}