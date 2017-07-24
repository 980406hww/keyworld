package com.keymanager.excel.operator;

import java.io.IOException;
import java.io.InputStream;

import jxl.read.biff.BiffException;

import com.keymanager.excel.definition.SuperUserKeywordDefinition;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.JXLExcelReader;
import com.keymanager.value.CustomerKeywordVO;

public class SuperUserKeywordExcelOperator extends AbstractExcelReader {
	
	public SuperUserKeywordExcelOperator() throws BiffException, IOException{
		reader = new JXLExcelReader("D:/dev/Java Project/KeywordManagementSystem/web/SuperUserKeywordList.xls");
		reader.setCurrentWorkSheetWithName("KeywordList");
	}
	
	public SuperUserKeywordExcelOperator(InputStream inputStream) throws BiffException, IOException {
		super(inputStream);
	}
	
	@Override
	public CustomerKeywordVO readRow(int rowIndex){
		CustomerKeywordVO keywordVO = new CustomerKeywordVO();
		keywordVO.setKeyword(getStringValue(SuperUserKeywordDefinition.Keyword.getColumnIndex(), rowIndex));
		if(Utils.isNullOrEmpty(keywordVO.getKeyword())){
			return null;
		}
		keywordVO.setUrl(getStringValue(SuperUserKeywordDefinition.URL.getColumnIndex(), rowIndex));
		if(Utils.isNullOrEmpty(keywordVO.getUrl())){
			return null;
		}
		keywordVO.setSearchEngine(getStringValue(SuperUserKeywordDefinition.SearchEngine.getColumnIndex(), rowIndex));
		if(Utils.isNullOrEmpty(keywordVO.getSearchEngine())){
			return null;
		}
		String startOptimizationTime = getStringValue(SuperUserKeywordDefinition.StartOptimization.getColumnIndex(), rowIndex);
		keywordVO.setStartOptimizedTime(Utils.parseDate(startOptimizationTime, "yyyy-M-d"));
		
		keywordVO.setCollectMethod(getCollectMethodValue(getStringValue(SuperUserKeywordDefinition.CollectMethod.getColumnIndex(), rowIndex)));
		if(Utils.isNullOrEmpty(keywordVO.getCollectMethod())){
			return null;
		}
		keywordVO.setServiceProvider(getStringValue(SuperUserKeywordDefinition.ServiceProvider.getColumnIndex(), rowIndex));
		if(Utils.isNullOrEmpty(keywordVO.getServiceProvider())){
			return null;
		}
		
		keywordVO.setPositionFirstFee(getIntValue(SuperUserKeywordDefinition.FirstFee.getColumnIndex(), rowIndex));
		if(keywordVO.getPositionFirstFee() == 0){
			return null;
		}
		keywordVO.setPositionSecondFee(getIntValue(SuperUserKeywordDefinition.SecondFee.getColumnIndex(), rowIndex));
		keywordVO.setPositionThirdFee(getIntValue(SuperUserKeywordDefinition.ThirdFee.getColumnIndex(), rowIndex));
		keywordVO.setPositionForthFee(getIntValue(SuperUserKeywordDefinition.ForthFee.getColumnIndex(), rowIndex));
		keywordVO.setPositionFifthFee(getIntValue(SuperUserKeywordDefinition.FifthFee.getColumnIndex(), rowIndex));
		
		keywordVO.setPositionFirstCost(getIntValue(SuperUserKeywordDefinition.FirstCost.getColumnIndex(), rowIndex));		
		keywordVO.setPositionSecondCost(getIntValue(SuperUserKeywordDefinition.SecondCost.getColumnIndex(), rowIndex));
		keywordVO.setPositionThirdCost(getIntValue(SuperUserKeywordDefinition.ThirdCost.getColumnIndex(), rowIndex));
		keywordVO.setPositionForthCost(getIntValue(SuperUserKeywordDefinition.ForthCost.getColumnIndex(), rowIndex));
		keywordVO.setPositionFifthCost(getIntValue(SuperUserKeywordDefinition.FifthCost.getColumnIndex(), rowIndex));
		
		keywordVO.setRemarks(getStringValue(SuperUserKeywordDefinition.Remarks.getColumnIndex(), rowIndex));
		
		return keywordVO;
	}
}
