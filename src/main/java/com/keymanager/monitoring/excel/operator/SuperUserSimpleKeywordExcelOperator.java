package com.keymanager.monitoring.excel.operator;

import java.io.IOException;
import java.io.InputStream;

import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.IndustryInfo;
import jxl.read.biff.BiffException;

import com.keymanager.monitoring.excel.definition.SuperUserFullKeywordDefinition;
import com.keymanager.monitoring.excel.definition.SuperUserSimpleKeywordDefinition;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.JXLExcelReader;

public class SuperUserSimpleKeywordExcelOperator extends AbstractExcelReader {

	public SuperUserSimpleKeywordExcelOperator() throws BiffException, IOException{
		reader = new JXLExcelReader("C:/dev/KeywordManagementSystemShouji/WebRoot/SuperUserSimpleKeywordList.xls");
		reader.setCurrentWorkSheetWithName("KeywordList");
	}

	public SuperUserSimpleKeywordExcelOperator(InputStream inputStream) throws BiffException, IOException {
		super(inputStream);
	}

	@Override
	public CustomerKeyword readRow(int rowIndex){
		CustomerKeyword customerKeyword = new CustomerKeyword();
		customerKeyword.setKeyword(getStringValue(SuperUserSimpleKeywordDefinition.Keyword.getColumnIndex(), rowIndex).trim());
		if(Utils.isNullOrEmpty(customerKeyword.getKeyword())){
			return null;
		}

		String url = getStringValue(SuperUserSimpleKeywordDefinition.URL.getColumnIndex(), rowIndex);
		if(url.substring(url.length() - 1).equals("/")) {
			url = url.substring(0, url.length() - 1);
		}
		customerKeyword.setUrl(url);
		if(Utils.isNullOrEmpty(customerKeyword.getUrl())){
			return null;
		}
		String searchEngine = getStringValue(SuperUserFullKeywordDefinition.SearchEngine.getColumnIndex(), rowIndex);
		if(Utils.isNullOrEmpty(searchEngine)) {
			customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
		} else {
			customerKeyword.setSearchEngine(searchEngine);
		}
		customerKeyword.setStartOptimizedTime(Utils.getCurrentTimestamp());

		customerKeyword.setCollectMethod(getCollectMethodValue(getStringValue(SuperUserSimpleKeywordDefinition.CollectMethod.getColumnIndex(), rowIndex)));
		if(Utils.isNullOrEmpty(customerKeyword.getCollectMethod())){
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

		Integer optimizePlanCount = getIntValue(SuperUserFullKeywordDefinition.OptimizePlanCount.getColumnIndex(), rowIndex);
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
		return customerKeyword;
	}

	@Override
	public IndustryInfo readRowForIndustry(int rowIndex) {
		return null;
	}
}
