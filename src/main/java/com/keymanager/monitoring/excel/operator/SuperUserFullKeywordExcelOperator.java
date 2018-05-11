package com.keymanager.monitoring.excel.operator;

import java.io.IOException;
import java.io.InputStream;

import com.keymanager.monitoring.entity.CustomerKeyword;
import jxl.read.biff.BiffException;

import com.keymanager.monitoring.excel.definition.SuperUserFullKeywordDefinition;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.JXLExcelReader;

public class SuperUserFullKeywordExcelOperator extends AbstractExcelReader {
	
	public SuperUserFullKeywordExcelOperator() throws BiffException, IOException{
		reader = new JXLExcelReader("C:/dev/KeywordManagementSystemShouji/WebRoot/SuperUserFullKeywordList.xls");
		reader.setCurrentWorkSheetWithName("KeywordList");
	}
	
	public SuperUserFullKeywordExcelOperator(InputStream inputStream) throws BiffException, IOException {
		super(inputStream);
	}
	
	@Override
	public CustomerKeyword readRow(int rowIndex){
		CustomerKeyword customerKeyword = new CustomerKeyword();
		customerKeyword.setKeyword(getStringValue(SuperUserFullKeywordDefinition.Keyword.getColumnIndex(), rowIndex).trim());
		if(Utils.isNullOrEmpty(customerKeyword.getKeyword())){
			return null;
		}
		customerKeyword.setCollectMethod(getCollectMethodValue(getStringValue(SuperUserFullKeywordDefinition.CollectMethod.getColumnIndex(), rowIndex)));
		if(Utils.isNullOrEmpty(customerKeyword.getCollectMethod())){
			return null;
		}

		customerKeyword.setOriginalUrl(getStringValue(SuperUserFullKeywordDefinition.OriginalURL.getColumnIndex(), rowIndex).trim());
		customerKeyword.setUrl(getStringValue(SuperUserFullKeywordDefinition.URL.getColumnIndex(), rowIndex).trim());
		
		customerKeyword.setPositionFirstFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionFirstFee.getColumnIndex(), rowIndex));
		customerKeyword.setPositionSecondFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionSecondFee.getColumnIndex(), rowIndex));
		customerKeyword.setPositionThirdFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionThirdFee.getColumnIndex(), rowIndex));
		customerKeyword.setPositionForthFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionForthFee.getColumnIndex(), rowIndex));
		customerKeyword.setPositionFifthFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionFifthFee.getColumnIndex(), rowIndex));
		customerKeyword.setPositionFirstPageFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionFirstPageFee.getColumnIndex(), rowIndex));
		
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
		customerKeyword.setManualCleanTitle(true);
		customerKeyword.setServiceProvider("baidutop123");
		
		Integer indexCount = getIntValue(SuperUserFullKeywordDefinition.IndexCount.getColumnIndex(), rowIndex);
		customerKeyword.setCurrentIndexCount(indexCount);

		Integer sequence = getIntValue(SuperUserFullKeywordDefinition.Sequnce.getColumnIndex(), rowIndex);
		customerKeyword.setSequence(sequence);

		Integer optimizePlanCount = getIntValue(SuperUserFullKeywordDefinition.OptimizePlanCount.getColumnIndex(), rowIndex);
		customerKeyword.setOptimizePlanCount(optimizePlanCount);
		
		customerKeyword.setOptimizeGroupName(getStringValue(SuperUserFullKeywordDefinition.OptimizeGroupName.getColumnIndex(), rowIndex).trim());
		customerKeyword.setTitle(getStringValue(SuperUserFullKeywordDefinition.Title.getColumnIndex(), rowIndex).trim());
		customerKeyword.setOrderNumber(getStringValue(SuperUserFullKeywordDefinition.OrderNumber.getColumnIndex(), rowIndex).trim());
		customerKeyword.setRemarks(getStringValue(SuperUserFullKeywordDefinition.Remarks.getColumnIndex(), rowIndex));
		return customerKeyword;
	}
}
