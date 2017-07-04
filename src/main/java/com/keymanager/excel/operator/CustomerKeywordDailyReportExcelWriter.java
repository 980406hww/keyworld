package com.keymanager.excel.operator;

import com.keymanager.excel.definition.CustomerKeywordDailyReportDefinition;
import com.keymanager.excel.definition.CustomerKeywordInfoDefinition;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.ExcelWriteException;
import com.keymanager.util.excel.JXLExcelWriter;
import com.keymanager.value.CustomerKeywordVO;
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

	public CustomerKeywordDailyReportExcelWriter(String customerUuid) throws BiffException, IOException {
		super();
		this.customerUuid = customerUuid;
//		this.dailyReportFielName = "dailyreport/" + customerUuid + ".xls";
//		File file = getTemplateFile(dailyReportFielName);
//		if(!file.exists()){
//			file = getTemplateFile(fileName);
//		}
		File file = getTemplateFile(fileName);
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
		writer.saveAs(getWebRootPath() + outputFileName);
	}
	
  public byte[] getExcelContentBytes() throws Exception {
  	return writer.getExcelContentBytes();
  }
  	
	private File getTemplateFile(String fileName){
		String SERVLET_CONTEXT_PATH = getWebRootPath();

		// 这里 SERVLET_CONTEXT_PATH 就是WebRoot的路径
		String path = SERVLET_CONTEXT_PATH + fileName;
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
		}
		//saveAs(dailyReportFielName);
	}

}