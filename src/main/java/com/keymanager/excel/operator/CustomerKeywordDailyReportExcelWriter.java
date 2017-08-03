package com.keymanager.excel.operator;

import com.keymanager.excel.definition.CustomerKeywordDailyReportDefinition;
import com.keymanager.manager.CustomerManager;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;
import com.keymanager.value.CustomerKeywordVO;
import com.keymanager.value.CustomerVO;
import jxl.Sheet;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class CustomerKeywordDailyReportExcelWriter {
	private final String fileName = "CustomerKeywordDailyReport.xls";
	protected JXLExcelWriter writer;
	private String customerUuid;
	private int keywordWidth;
	private int urlWidth;
	private int sequenceWidth;
	private int indexWidth;
	private int price1Width;
	private int price4Width;
	private int currentPositionWidth;
	private int todayPriceWidth;

	private String dailyReportFielName;
	private long dailyReportUuid;
	private String webRootPath = null;

	public CustomerKeywordDailyReportExcelWriter(String terminalType, String customerUuid, long dailyReportUuid) throws BiffException, IOException {
		super();
		this.webRootPath = Utils.getWebRootPath();
		this.dailyReportUuid = dailyReportUuid;
		this.customerUuid = customerUuid;
		this.dailyReportFielName = "dailyreport/" + terminalType + "/" + customerUuid + ".xls";
		File file = getTemplateFile(dailyReportFielName);
		if(!file.exists()){
			file = getTemplateFile(fileName);
		}
//		File file = getTemplateFile(fileName);
		writer = new JXLExcelWriter(file);
//		writer.setCurrentWorkSheetWithName("Default");
		String sheetName = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
		Sheet sheet = writer.getSheetByName(sheetName);
		if(sheet != null){
			writer.removeSheet(sheetName);
		}

		writer.createSheetWithNameAndSetAsCurrentSheet(sheetName, writer.getNumberOfSheets());
		writer.removeSheet("Default");
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

	private void writeTitle(int rowIndex) throws ExcelWriteException{
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.Sequence.getColumnIndex(), rowIndex,CustomerKeywordDailyReportDefinition.Sequence
				.getTitle(), true);
		sequenceWidth = calculateWidth(sequenceWidth, CustomerKeywordDailyReportDefinition.Sequence.getTitle());
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.Keyword.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Keyword
				.getTitle(), true);
		keywordWidth = calculateWidth(keywordWidth, CustomerKeywordDailyReportDefinition.Keyword.getTitle());
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.URL.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.URL.getTitle(), true);
		urlWidth = calculateWidth(keywordWidth, CustomerKeywordDailyReportDefinition.URL.getTitle());
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.CurrentPosition.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition
				.CurrentPosition.getTitle(), true);
		currentPositionWidth = calculateWidth(currentPositionWidth, CustomerKeywordDailyReportDefinition.CurrentPosition.getTitle());
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.Price1.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Price1.getTitle(), true);
		price1Width = calculateWidth(price1Width, CustomerKeywordDailyReportDefinition.Price1.getTitle());
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.Price4.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Price4.getTitle(), true);
		price4Width = calculateWidth(price4Width, CustomerKeywordDailyReportDefinition.Price4.getTitle());
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.Index.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Index.getTitle(), true);
		indexWidth = calculateWidth(keywordWidth, CustomerKeywordDailyReportDefinition.Index.getTitle());
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.TodayPrice.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition
				.TodayPrice.getTitle(), true);
		todayPriceWidth = calculateWidth(todayPriceWidth, CustomerKeywordDailyReportDefinition.TodayPrice.getTitle());
	}

	private double writeRow(int rowIndex, CustomerKeywordVO view) throws ExcelWriteException{
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.Sequence.getColumnIndex(), rowIndex, view.getSequence());
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.Keyword.getColumnIndex(), rowIndex, view.getKeyword());
		keywordWidth = calculateWidth(keywordWidth, view.getKeyword());
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.URL.getColumnIndex(), rowIndex, view.getUrl());
		urlWidth = calculateWidth(keywordWidth, view.getUrl());
		if(view.getCurrentPosition() > 0) {
			writer.addLabelCell(CustomerKeywordDailyReportDefinition.CurrentPosition.getColumnIndex(), rowIndex, view.getCurrentPosition());
		}
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.Price1.getColumnIndex(), rowIndex, view.getPositionFirstFee());
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.Price4.getColumnIndex(), rowIndex, view.getPositionForthFee());
		writer.addLabelCell(CustomerKeywordDailyReportDefinition.Index.getColumnIndex(), rowIndex, view.getCurrentIndexCount());
		double todayPrice = 0;
		if(view.getCurrentPosition() > 0) {
			if (view.getCurrentPosition() < 4) {
				todayPrice = view.getPositionFirstFee();
			}else if(view.getCurrentPosition() <= 5){
				todayPrice = view.getPositionForthFee();
			}
			if(todayPrice > 0) {
				writer.addLabelCell(CustomerKeywordDailyReportDefinition.TodayPrice.getColumnIndex(), rowIndex, todayPrice);
			}
		}
		return todayPrice;
	}

	public int calculateWidth(int existWidth, String label){
		if(existWidth < label.getBytes().length){
			existWidth = label.getBytes().length;
		}
		return existWidth;
	}

	public void writeSubTotal(int rowIndex) throws Exception {
		writer.setCurrentWorkSheetWithName("小计");
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int endOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		writer.addLabelCell(0, day, day);
		writer.addFormulanCell(1, day, "'" + day + "'!H" + (rowIndex + 1));

		if(day == 10){
			writer.addFormulanCell(2, day, "SUM(B2:B11)");
		}else if(day == 20){
			writer.addFormulanCell(2, day, "SUM(B12:B21)");
		}else if(day == endOfMonth){
			writer.addFormulanCell(2, day, "SUM(B22:B" + (day + 1) + ")");
		}
	}

	public void writeDataToExcel(List<CustomerKeywordVO> views) throws Exception {
		if(!Utils.isEmpty(views)){
			int rowIndex = 1;
			double totalFee = 0;
			writeTitle(0);
			for(CustomerKeywordVO view : views){
				double todayPrice = writeRow(rowIndex++, view);
				totalFee = totalFee + todayPrice;
			}
			writer.addFormulanCell(CustomerKeywordDailyReportDefinition.TodayPrice.getColumnIndex(), rowIndex, "SUM(H2:H" + rowIndex + ")");
			writer.setColumnView(CustomerKeywordDailyReportDefinition.Sequence.getColumnIndex(), sequenceWidth + 6);
			writer.setColumnView(CustomerKeywordDailyReportDefinition.Keyword.getColumnIndex(), keywordWidth + 6);
			writer.setColumnView(CustomerKeywordDailyReportDefinition.URL.getColumnIndex(), urlWidth + 6);

			writer.setColumnView(CustomerKeywordDailyReportDefinition.Index.getColumnIndex(), indexWidth + 6);
			writer.setColumnView(CustomerKeywordDailyReportDefinition.Price1.getColumnIndex(), price1Width + 6);
			writer.setColumnView(CustomerKeywordDailyReportDefinition.Price4.getColumnIndex(), price4Width + 6);
			writer.setColumnView(CustomerKeywordDailyReportDefinition.CurrentPosition.getColumnIndex(), currentPositionWidth + 6);
			writer.setColumnView(CustomerKeywordDailyReportDefinition.TodayPrice.getColumnIndex(), todayPriceWidth + 6);

			writeSubTotal(rowIndex);
		}
		saveAs(dailyReportFielName);

		if(dailyReportUuid > 0) {
			CustomerManager customerManager = new CustomerManager();
			CustomerVO customerVO = customerManager.getCustomerByUuid("keyword", customerUuid);
			String fileName = "dailyreport/" + dailyReportUuid + "/" + customerVO.getContactPerson() + "_" + Utils.formatDatetime(Utils
					.getCurrentTimestamp(), "yyyy.MM.dd") + ".xls";

			FileUtil.copyFile(webRootPath + dailyReportFielName, webRootPath + fileName, true);
		}
	}
}