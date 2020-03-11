package com.keymanager.ckadmin.excel.operator;


import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.entity.IndustryInfo;
import com.keymanager.ckadmin.excel.definition.SuperUserSimpleKeywordDefinition;
import com.keymanager.ckadmin.util.Constants;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.ckadmin.util.excel.JXLExcelReader;
import java.io.IOException;
import java.io.InputStream;
import jxl.read.biff.BiffException;

public class SuperUserSimpleKeywordExcelOperator extends AbstractExcelReader {

    public SuperUserSimpleKeywordExcelOperator() throws BiffException, IOException {
        reader = new JXLExcelReader("C:/dev/KeywordManagementSystemShouji/WebRoot/SuperUserSimpleKeywordList.xls");
        reader.setCurrentWorkSheetWithName("KeywordList");
    }

    public SuperUserSimpleKeywordExcelOperator(InputStream inputStream) throws BiffException, IOException {
        super(inputStream);
    }

    @Override
    public CustomerKeyword readRow(int rowIndex) {
        CustomerKeyword customerKeyword = new CustomerKeyword();
        customerKeyword.setKeyword(getStringValue(SuperUserSimpleKeywordDefinition.Keyword.getColumnIndex(), rowIndex).trim());
        if (Utils.isNullOrEmpty(customerKeyword.getKeyword())) {
            return null;
        }

        String url = getStringValue(SuperUserSimpleKeywordDefinition.URL.getColumnIndex(), rowIndex);
        if (url.substring(url.length() - 1).equals("/")) {
            url = url.substring(0, url.length() - 1);
        }
        customerKeyword.setUrl(url);
        if (Utils.isNullOrEmpty(customerKeyword.getUrl())) {
            return null;
        }
        String searchEngine = getStringValue(SuperUserSimpleKeywordDefinition.SearchEngine.getColumnIndex(), rowIndex);
        if (Utils.isNullOrEmpty(searchEngine)) {
            customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
        } else {
            customerKeyword.setSearchEngine(searchEngine);
        }
        customerKeyword.setStartOptimizedTime(Utils.getCurrentTimestamp());

        customerKeyword.setCollectMethod(getCollectMethodValue(getStringValue(SuperUserSimpleKeywordDefinition.CollectMethod.getColumnIndex(), rowIndex)));
        if (Utils.isNullOrEmpty(customerKeyword.getCollectMethod())) {
            return null;
        }
        customerKeyword.setManualCleanTitle(true);
        customerKeyword.setServiceProvider("baidutop123");

        Double pcFee = getDoubleValue(SuperUserSimpleKeywordDefinition.Fee.getColumnIndex(), rowIndex);
        customerKeyword.setPositionFirstFee(pcFee);
        customerKeyword.setPositionSecondFee(pcFee);
        customerKeyword.setPositionThirdFee(pcFee);

        Integer indexCount = getIntValue(SuperUserSimpleKeywordDefinition.IndexCount.getColumnIndex(), rowIndex);
        customerKeyword.setCurrentIndexCount(indexCount);

        Integer sequence = getIntValue(SuperUserSimpleKeywordDefinition.Sequnce.getColumnIndex(), rowIndex);
        customerKeyword.setSequence(sequence);

        Integer optimizePlanCount = getIntValue(SuperUserSimpleKeywordDefinition.OptimizePlanCount.getColumnIndex(), rowIndex);
        // null是脏数据，故设置默认要刷数：50
        optimizePlanCount = optimizePlanCount == null ? 50 : optimizePlanCount;
        customerKeyword.setOptimizePlanCount(optimizePlanCount);
        customerKeyword.setOptimizeRemainingCount(optimizePlanCount);

        customerKeyword.setOriginalUrl(getStringValue(SuperUserSimpleKeywordDefinition.OriginalURL.getColumnIndex(), rowIndex).trim());
        customerKeyword.setMachineGroup(getStringValue(SuperUserSimpleKeywordDefinition.MachineGroupName.getColumnIndex(), rowIndex).trim());
        customerKeyword.setOptimizeGroupName(getStringValue(SuperUserSimpleKeywordDefinition.OptimizeGroupName.getColumnIndex(), rowIndex).trim());

        customerKeyword.setBearPawNumber(getStringValue(SuperUserSimpleKeywordDefinition.BearPawNumber.getColumnIndex(), rowIndex).trim());
        customerKeyword.setTitle(getStringValue(SuperUserSimpleKeywordDefinition.Title.getColumnIndex(), rowIndex).trim());
        customerKeyword.setRunImmediate(getStringValue(SuperUserSimpleKeywordDefinition.RunImmediate.getColumnIndex(), rowIndex));

        customerKeyword.setOrderNumber(getStringValue(SuperUserSimpleKeywordDefinition.OrderNumber.getColumnIndex(), rowIndex).trim());
        customerKeyword.setRemarks(getStringValue(SuperUserSimpleKeywordDefinition.Remarks.getColumnIndex(), rowIndex));
        customerKeyword.setKeywordEffect(getStringValue(SuperUserSimpleKeywordDefinition.KeywordEffect.getColumnIndex(), rowIndex));
        return customerKeyword;
    }

    @Override
    public IndustryInfo readRowForIndustry(int rowIndex) {
        return null;
    }
}
