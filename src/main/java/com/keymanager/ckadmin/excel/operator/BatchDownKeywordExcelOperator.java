package com.keymanager.ckadmin.excel.operator;


import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.entity.IndustryInfo;
import com.keymanager.ckadmin.excel.definition.BatchDownKeywordDefinition;
import com.keymanager.ckadmin.util.Utils;
import java.io.IOException;
import java.io.InputStream;
import jxl.read.biff.BiffException;

public class BatchDownKeywordExcelOperator extends AbstractExcelReader {

    public BatchDownKeywordExcelOperator(InputStream inputStream) throws BiffException, IOException {
        super(inputStream);
    }

    @Override
    public CustomerKeyword readRow(int rowIndex) {
        CustomerKeyword customerKeyword = new CustomerKeyword();
        customerKeyword.setKeyword(getStringValue(BatchDownKeywordDefinition.Keyword.getColumnIndex(), rowIndex).trim());
        if (Utils.isNullOrEmpty(customerKeyword.getKeyword())) {
            return null;
        }
        String url = getStringValue(BatchDownKeywordDefinition.URL.getColumnIndex(), rowIndex);
        if (Utils.isNullOrEmpty(url)) {
            return null;
        }
        if (url.substring(url.length() - 1).equals("/")) {
            url = url.substring(0, url.length() - 1);
        }
        customerKeyword.setUrl(url);
        String searchEngine = getStringValue(BatchDownKeywordDefinition.SearchEngine.getColumnIndex(), rowIndex);
        if (Utils.isNullOrEmpty(searchEngine)) {
            return null;
        }
        customerKeyword.setSearchEngine(searchEngine);
        return customerKeyword;
    }

    @Override
    public IndustryInfo readRowForIndustry(int rowIndex) {
        return null;
    }
}
