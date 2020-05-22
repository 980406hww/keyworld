package com.keymanager.ckadmin.excel.operator;


import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.enums.TerminalTypeEnum;
import com.keymanager.ckadmin.excel.definition.CustomerKeywordDailyReportDefinition;
import com.keymanager.ckadmin.util.FileUtil;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.ckadmin.util.excel.ExcelWriteException;
import com.keymanager.ckadmin.util.excel.JXLExcelWriter;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import jxl.Sheet;
import jxl.read.biff.BiffException;

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

    private String dailyReportFileName;
    private long dailyReportUuid;
    private String webRootPath = null;

    public CustomerKeywordDailyReportExcelWriter(String terminalType, String customerUuid, long dailyReportUuid) throws BiffException, IOException {
        super();
        this.webRootPath = Utils.getWebRootPath();
        this.dailyReportUuid = dailyReportUuid;
        this.customerUuid = customerUuid;
        this.dailyReportFileName = "dailyreport/" + terminalType + "/" + customerUuid + ".xls";
        File file = getTemplateFile(dailyReportFileName);
        int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (!file.exists() || dayOfMonth == 1 || dayOfMonth == 11 || dayOfMonth == 21) {
            file = getTemplateFile(fileName);
        }
//		File file = getTemplateFile(fileName);
        writer = new JXLExcelWriter(file);
//		writer.setCurrentWorkSheetWithName("Default");
        writer.removeSheet("Default");
    }

    private void createOrReplaceSheet(String sheetName, int index) {
        Sheet sheet = writer.getSheetByName(sheetName);
        if (sheet != null) {
            writer.removeSheet(sheetName);
        }
        writer.createSheetWithNameAndSetAsCurrentSheet(sheetName, index);
    }

    public void saveAs(String outputFileName) throws Exception {
        writer.saveAs(webRootPath + outputFileName);
    }

    public byte[] getExcelContentBytes() throws Exception {
        return writer.getExcelContentBytes();
    }

    private File getTemplateFile(String fileName) {
        String SERVLET_CONTEXT_PATH = webRootPath;
        // 这里 SERVLET_CONTEXT_PATH 就是WebRoot的路径
        String path = SERVLET_CONTEXT_PATH + fileName;
        path = path.replaceAll("%20", " ");
        return new File(path);
    }

    private double writeDailyDetail(List<CustomerKeyword> views) throws Exception {
        if (!Utils.isEmpty(views)) {
            String sheetName = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
            createOrReplaceSheet(sheetName, writer.getNumberOfSheets());

            int rowIndex = 0;
            double totalFee = 0;
            writeDailyTitle(rowIndex++);
            for (CustomerKeyword view : views) {
                if (!"stop".equals(view.getOptimizeGroupName())) {
                    double todayPrice = writeDailyRow(rowIndex++, view);
                    totalFee = totalFee + todayPrice;
                }
            }

//            writer.addFormulanCell(CustomerKeywordDailyReportDefinition.TodayPrice.getColumnIndex(), rowIndex, "SUM(H2:H" + rowIndex + ")");
            writer.addLabelCell(CustomerKeywordDailyReportDefinition.TodayPrice.getColumnIndex(), rowIndex, totalFee);

            writer.setColumnView(CustomerKeywordDailyReportDefinition.Sequence.getColumnIndex(), sequenceWidth + 6);
            writer.setColumnView(CustomerKeywordDailyReportDefinition.Keyword.getColumnIndex(), keywordWidth + 6);
            writer.setColumnView(CustomerKeywordDailyReportDefinition.URL.getColumnIndex(), urlWidth + 6);

            writer.setColumnView(CustomerKeywordDailyReportDefinition.Index.getColumnIndex(), indexWidth + 6);
            writer.setColumnView(CustomerKeywordDailyReportDefinition.Price1.getColumnIndex(), price1Width + 6);
            writer.setColumnView(CustomerKeywordDailyReportDefinition.Price4.getColumnIndex(), price4Width + 6);
            writer.setColumnView(CustomerKeywordDailyReportDefinition.CurrentPosition.getColumnIndex(), currentPositionWidth + 6);
            writer.setColumnView(CustomerKeywordDailyReportDefinition.TodayPrice.getColumnIndex(), todayPriceWidth + 6);

            writeDailySubTotal(rowIndex, totalFee);

            return totalFee;
        }
        return 0;
    }

    private void writeDailyTitle(int rowIndex) throws ExcelWriteException {

        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Sequence.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Sequence
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
        writer
            .addLabelCell(CustomerKeywordDailyReportDefinition.Price1.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Price1.getTitle(), true);
        price1Width = calculateWidth(price1Width, CustomerKeywordDailyReportDefinition.Price1.getTitle());
        writer
            .addLabelCell(CustomerKeywordDailyReportDefinition.Price4.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Price4.getTitle(), true);
        price4Width = calculateWidth(price4Width, CustomerKeywordDailyReportDefinition.Price4.getTitle());
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Index.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Index.getTitle(), true);
        indexWidth = calculateWidth(keywordWidth, CustomerKeywordDailyReportDefinition.Index.getTitle());
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.TodayPrice.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition
            .TodayPrice.getTitle(), true);
        todayPriceWidth = calculateWidth(todayPriceWidth, CustomerKeywordDailyReportDefinition.TodayPrice.getTitle());
    }

    private double writeDailyRow(int rowIndex, CustomerKeyword view) throws ExcelWriteException {
        writer
            .addLabelCell(CustomerKeywordDailyReportDefinition.Sequence.getColumnIndex(), rowIndex, view.getSequence() == null ? "" : view.getSequence() + "");
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Keyword.getColumnIndex(), rowIndex, view.getKeyword());
        keywordWidth = calculateWidth(keywordWidth, view.getKeyword());
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.URL.getColumnIndex(), rowIndex, view.getUrl());
        urlWidth = calculateWidth(keywordWidth, view.getUrl());
        if (view.getCurrentPosition() != null && view.getCurrentPosition() > 0) {
            writer.addLabelCell(CustomerKeywordDailyReportDefinition.CurrentPosition.getColumnIndex(), rowIndex, view.getCurrentPosition());
        }
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Price1.getColumnIndex(), rowIndex, view.getPositionFirstFeeString());
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Price4.getColumnIndex(), rowIndex, view.getPositionForthFeeString());
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Index.getColumnIndex(), rowIndex,
            view.getCurrentIndexCount() == null ? 0 : view.getCurrentIndexCount());
        double todayPrice = 0;
        if (view.getCurrentPosition() != null && view.getCurrentPosition() > 0) {
            if (view.getCurrentPosition() < 4 && view.getPositionFirstFee() != null) {
                todayPrice = view.getPositionFirstFee();
            } else if (view.getCurrentPosition() <= 5 && view.getPositionForthFee() != null) {
                todayPrice = view.getPositionForthFee();
            } else if (view.getCurrentPosition() <= 10 && view.getPositionFirstPageFee() != null) {
                todayPrice = view.getPositionFirstPageFee();
            }
            if (todayPrice > 0) {
                writer.addLabelCell(CustomerKeywordDailyReportDefinition.TodayPrice.getColumnIndex(), rowIndex, todayPrice);
            }
        }
        return todayPrice;
    }


    private void writeDailySubTotal(int rowIndex, double totalFee) throws Exception {
        writer.setCurrentWorkSheetWithName("小计");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int endOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        writer.addLabelCell(0, day, day);
//        writer.addFormulanCell(1, day, "'" + day + "'!H" + (rowIndex + 1));
        writer.addLabelCell(1, day, totalFee);

        if (day == 10) {
            writer.addFormulanCell(2, day, "SUM(B2:B11)");
        } else if (day == 20) {
            writer.addFormulanCell(2, day, "SUM(B12:B21)");
        } else if (day == endOfMonth) {
            writer.addFormulanCell(2, day, "SUM(B22:B" + (day + 1) + ")");
        }
    }

    public int calculateWidth(int existWidth, String label) {
        if (existWidth < label.getBytes().length) {
            existWidth = label.getBytes().length;
        }
        return existWidth;
    }

    private void writeMonthSummary(List<CustomerKeyword> views) throws Exception {
        if (!Utils.isEmpty(views)) {
            String sheetName = (Calendar.getInstance().get(Calendar.MONTH) + 1) + "月";
            createOrReplaceSheet(sheetName, 0);

            int rowIndex = 0;
            writeMonthSummaryTitle(rowIndex++);
            for (CustomerKeyword view : views) {
                writeMonthRow(rowIndex++, view);
            }

            writer.setColumnView(CustomerKeywordDailyReportDefinition.Sequence.getColumnIndex(), sequenceWidth + 6);
            writer.setColumnView(CustomerKeywordDailyReportDefinition.Keyword.getColumnIndex(), keywordWidth + 6);
            writer.setColumnView(CustomerKeywordDailyReportDefinition.URL.getColumnIndex(), urlWidth + 6);

            writer.setColumnView(CustomerKeywordDailyReportDefinition.Index.getColumnIndex(), indexWidth + 6);
            writer.setColumnView(CustomerKeywordDailyReportDefinition.Price1.getColumnIndex(), price1Width + 6);
            writer.setColumnView(CustomerKeywordDailyReportDefinition.Price4.getColumnIndex(), price4Width + 6);
        }
    }

    private void writeMonthSummaryTitle(int rowIndex) throws ExcelWriteException {
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Sequence.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Sequence
            .getTitle(), true);
        sequenceWidth = calculateWidth(sequenceWidth, CustomerKeywordDailyReportDefinition.Sequence.getTitle());
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Keyword.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Keyword
            .getTitle(), true);
        keywordWidth = calculateWidth(keywordWidth, CustomerKeywordDailyReportDefinition.Keyword.getTitle());
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.URL.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.URL.getTitle(), true);
        urlWidth = calculateWidth(keywordWidth, CustomerKeywordDailyReportDefinition.URL.getTitle());
        writer
            .addLabelCell(CustomerKeywordDailyReportDefinition.Price1.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Price1.getTitle(), true);
        price1Width = calculateWidth(price1Width, CustomerKeywordDailyReportDefinition.Price1.getTitle());
        writer
            .addLabelCell(CustomerKeywordDailyReportDefinition.Price4.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Price4.getTitle(), true);
        price4Width = calculateWidth(price4Width, CustomerKeywordDailyReportDefinition.Price4.getTitle());
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Index.getColumnIndex(), rowIndex, CustomerKeywordDailyReportDefinition.Index.getTitle(), true);
        indexWidth = calculateWidth(keywordWidth, CustomerKeywordDailyReportDefinition.Index.getTitle());
    }

    private void writeMonthRow(int rowIndex, CustomerKeyword view) throws ExcelWriteException {
        writer
            .addLabelCell(CustomerKeywordDailyReportDefinition.Sequence.getColumnIndex(), rowIndex, view.getSequence() == null ? "" : view.getSequence() + "");
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Keyword.getColumnIndex(), rowIndex, view.getKeyword());
        keywordWidth = calculateWidth(keywordWidth, view.getKeyword());
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.URL.getColumnIndex(), rowIndex, view.getUrl());
        urlWidth = calculateWidth(keywordWidth, view.getUrl());
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Price1.getColumnIndex(), rowIndex, view.getPositionFirstFeeString());
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Price4.getColumnIndex(), rowIndex, view.getPositionForthFeeString());
        writer.addLabelCell(CustomerKeywordDailyReportDefinition.Index.getColumnIndex(), rowIndex,
            view.getCurrentIndexCount() == null ? 0 : view.getCurrentIndexCount());
    }

    public double writeDataToExcel(List<CustomerKeyword> views, String loginName, String contactPerson, String terminalType) throws Exception {
        double todayFee = writeDailyDetail(views);
        writeMonthSummary(views);

        saveAs(dailyReportFileName);
        if (dailyReportUuid > 0) {
            String fileName = "dailyreport/" + dailyReportUuid + "/" + loginName + "/" + contactPerson;
            if (TerminalTypeEnum.Phone.name().equals(terminalType)) {
                fileName = fileName + "(Mobile)";
            }
            fileName = fileName + ".xls";
            FileUtil.copyFile(webRootPath + dailyReportFileName, webRootPath + fileName, true);
        }
        return todayFee;
    }
}