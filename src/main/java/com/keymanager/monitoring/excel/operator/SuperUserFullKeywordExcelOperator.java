package com.keymanager.monitoring.excel.operator;

import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.IndustryInfo;
import com.keymanager.monitoring.excel.definition.SuperUserFullKeywordDefinition;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.JXLExcelReader;

import java.io.IOException;
import java.io.InputStream;

import jxl.read.biff.BiffException;

public class SuperUserFullKeywordExcelOperator extends AbstractExcelReader {

    public SuperUserFullKeywordExcelOperator() throws BiffException, IOException {
        reader = new JXLExcelReader("C:/dev/KeywordManagementSystemShouji/WebRoot/SuperUserFullKeywordList.xls");
        reader.setCurrentWorkSheetWithName("KeywordList");
    }

    public SuperUserFullKeywordExcelOperator(InputStream inputStream) throws BiffException, IOException {
        super(inputStream);
    }

    @Override
    public CustomerKeyword readRow(int rowIndex) {
        CustomerKeyword customerKeyword = new CustomerKeyword();
        customerKeyword.setKeyword(getStringValue(SuperUserFullKeywordDefinition.Keyword.getColumnIndex(), rowIndex).trim());
        if (Utils.isNullOrEmpty(customerKeyword.getKeyword())) {
            return null;
        }
        customerKeyword.setCollectMethod(getCollectMethodValue(getStringValue(SuperUserFullKeywordDefinition.CollectMethod.getColumnIndex(), rowIndex)));
        if (Utils.isNullOrEmpty(customerKeyword.getCollectMethod())) {
            return null;
        }

        customerKeyword.setOriginalUrl(getStringValue(SuperUserFullKeywordDefinition.OriginalURL.getColumnIndex(), rowIndex).trim());

        String url = getStringValue(SuperUserFullKeywordDefinition.URL.getColumnIndex(), rowIndex);
        if (url.substring(url.length() - 1).equals("/")) {
            url = url.substring(0, url.length() - 1);
        }
        customerKeyword.setUrl(url);

        customerKeyword.setPositionFirstFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionFirstFee.getColumnIndex(), rowIndex));
        customerKeyword.setPositionSecondFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionSecondFee.getColumnIndex(), rowIndex));
        customerKeyword.setPositionThirdFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionThirdFee.getColumnIndex(), rowIndex));
        customerKeyword.setPositionForthFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionForthFee.getColumnIndex(), rowIndex));
        customerKeyword.setPositionFifthFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionFifthFee.getColumnIndex(), rowIndex));
        customerKeyword.setPositionFirstPageFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionFirstPageFee.getColumnIndex(), rowIndex));

        if (Utils.isNullOrEmpty(customerKeyword.getUrl())) {
            return null;
        }

        String searchEngine = getStringValue(SuperUserFullKeywordDefinition.SearchEngine.getColumnIndex(), rowIndex);
        if (Utils.isNullOrEmpty(searchEngine)) {
            customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
        } else {
            customerKeyword.setSearchEngine(searchEngine);
        }
        customerKeyword.setStartOptimizedTime(Utils.getCurrentTimestamp());
        customerKeyword.setManualCleanTitle(true);
        customerKeyword.setServiceProvider("baidutop123");

        Integer indexCount = getIntValue(SuperUserFullKeywordDefinition.IndexCount.getColumnIndex(), rowIndex);
        customerKeyword.setCurrentIndexCount(indexCount);

        Integer sequence = getIntValue(SuperUserFullKeywordDefinition.Sequnce.getColumnIndex(), rowIndex);
        customerKeyword.setSequence(sequence);

        Integer optimizePlanCount = getIntValue(SuperUserFullKeywordDefinition.OptimizePlanCount.getColumnIndex(), rowIndex);
        customerKeyword.setOptimizePlanCount(optimizePlanCount);
        customerKeyword.setOptimizeRemainingCount(optimizePlanCount);

        customerKeyword.setOptimizeGroupName(getStringValue(SuperUserFullKeywordDefinition.OptimizeGroupName.getColumnIndex(), rowIndex).trim());

        customerKeyword.setBearPawNumber(getStringValue(SuperUserFullKeywordDefinition.BearPawNumber.getColumnIndex(), rowIndex).trim());
        customerKeyword.setTitle(getStringValue(SuperUserFullKeywordDefinition.Title.getColumnIndex(), rowIndex).trim());

        customerKeyword.setRunImmediate(getStringValue(SuperUserFullKeywordDefinition.RunImmediate.getColumnIndex(), rowIndex));

        customerKeyword.setOrderNumber(getStringValue(SuperUserFullKeywordDefinition.OrderNumber.getColumnIndex(), rowIndex).trim());
        customerKeyword.setRemarks(getStringValue(SuperUserFullKeywordDefinition.Remarks.getColumnIndex(), rowIndex));
        return customerKeyword;
    }

    @Override
    public IndustryInfo readRowForIndustry(int rowIndex) {
        return null;
    }
}
