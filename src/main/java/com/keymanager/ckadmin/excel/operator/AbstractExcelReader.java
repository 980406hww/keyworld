package com.keymanager.ckadmin.excel.operator;


import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.enums.CollectMethod;
import com.keymanager.ckadmin.entity.IndustryInfo;
import com.keymanager.ckadmin.util.Constants;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.ckadmin.util.excel.JXLExcelReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import jxl.Cell;
import jxl.read.biff.BiffException;

public abstract class AbstractExcelReader {

    protected JXLExcelReader reader;

    public AbstractExcelReader() {
        super();
    }

    public static AbstractExcelReader createExcelOperator(InputStream inputStream, String excelType) throws BiffException, IOException {
        if (Constants.EXCEL_TYPE_SUPER_USER_SIMPLE.equals(excelType)) {
            return new SuperUserSimpleKeywordExcelOperator(inputStream);
        } else if (Constants.EXCEL_TYPE_SUPER_USER_FULL.equals(excelType)) {
            return new SuperUserFullKeywordExcelOperator(inputStream);
        } else if (Constants.EXCEL_TYPE_SUPER_INDUSTRY_SIMPLE.equals(excelType)) {
            return new SimpleIndustryExcelOperator(inputStream);
        }
        return null;
    }

    public AbstractExcelReader(InputStream inputStream) throws BiffException, IOException {
        super();
        reader = new JXLExcelReader(inputStream);
        reader.setCurrentWorkSheetWithName("KeywordList");
    }

    public abstract CustomerKeyword readRow(int rowIndex);

    public abstract IndustryInfo readRowForIndustry(int rowIndex);

    protected String getStringValue(int columnIndex, int rowIndex) {
        Cell cell = reader.getCell(columnIndex, rowIndex);
        if (cell != null) {
            return cell.getContents().trim();
        }
        return null;
    }

    protected Integer getIntValue(int columnIndex, int rowIndex) {
        Cell cell = reader.getCell(columnIndex, rowIndex);
        if (cell != null && !Utils.isNullOrEmpty(cell.getContents())) {
            return Integer.parseInt(cell.getContents().trim());
        }
        return null;
    }

    protected Double getDoubleValue(int columnIndex, int rowIndex) {
        Cell cell = reader.getCell(columnIndex, rowIndex);
        if (cell != null && !Utils.isNullOrEmpty(cell.getContents())) {
            return Double.parseDouble(cell.getContents().trim());
        }
        return null;
    }

    protected String getCollectMethodValue(String name) {
        CollectMethod collectMethod = CollectMethod.findByName(name);
        if (collectMethod != null) {
            return collectMethod.getCode();
        }
        return "";
    }

    public List<CustomerKeyword> readDataFromExcel() {
        List<CustomerKeyword> customerKeywords = new ArrayList<>();
        for (int i = 1; i < reader.getCurrentSheet().getRows(); i++) {
            CustomerKeyword customerKeyword = this.readRow(i);
            if (customerKeyword != null) {
                customerKeywords.add(customerKeyword);
            }
        }
        return customerKeywords;
    }

    public List<IndustryInfo> readIndustryDataFromExcel() {
        List<IndustryInfo> industryInfos = new ArrayList<>();
        for (int i = 1; i < reader.getCurrentSheet().getRows(); i++) {
            IndustryInfo industryInfo = this.readRowForIndustry(i);
            if (null != industryInfo) {
                industryInfos.add(industryInfo);
            }
        }
        return industryInfos;
    }
}