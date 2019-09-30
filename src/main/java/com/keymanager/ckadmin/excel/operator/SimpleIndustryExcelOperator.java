package com.keymanager.ckadmin.excel.operator;


import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.entity.IndustryInfo;
import com.keymanager.ckadmin.excel.definition.SuperIndustrySimpleDefinition;
import com.keymanager.ckadmin.util.Constants;
import com.keymanager.ckadmin.util.Utils;
import java.io.IOException;
import java.io.InputStream;
import jxl.read.biff.BiffException;

public class SimpleIndustryExcelOperator extends AbstractExcelReader {

    public SimpleIndustryExcelOperator(InputStream inputStream) throws IOException, BiffException {
        super(inputStream);
    }

    @Override
    public CustomerKeyword readRow(int rowIndex) {
        return null;
    }

    @Override
    public IndustryInfo readRowForIndustry(int rowIndex) {
        IndustryInfo industryInfo = new IndustryInfo();
        String industryName = getStringValue(SuperIndustrySimpleDefinition.IndustryName.getColumnIndex(), rowIndex).trim();
        if (Utils.isNullOrEmpty(industryName)) {
            return null;
        }
        industryInfo.setIndustryName(industryName);
        String searchEngine = getStringValue(SuperIndustrySimpleDefinition.SearchEngine.getColumnIndex(), rowIndex).trim();
        if (Utils.isNullOrEmpty(searchEngine)) {
            searchEngine = Constants.SEARCH_ENGINE_BAIDU;
        }
        industryInfo.setSearchEngine(searchEngine);
        industryInfo.setTargetUrl(getStringValue(SuperIndustrySimpleDefinition.TargetUrl.getColumnIndex(), rowIndex).trim());
        Integer pageNum = getIntValue(SuperIndustrySimpleDefinition.PageNum.getColumnIndex(), rowIndex);
        if (null == pageNum) {
            pageNum = 2;
        }
        industryInfo.setPageNum(pageNum);
        Integer pagePerNum = getIntValue(SuperIndustrySimpleDefinition.PagePerNum.getColumnIndex(), rowIndex);
        if (null == pagePerNum) {
            pagePerNum = 10;
        }
        industryInfo.setPagePerNum(pagePerNum);
        return industryInfo;
    }
}
