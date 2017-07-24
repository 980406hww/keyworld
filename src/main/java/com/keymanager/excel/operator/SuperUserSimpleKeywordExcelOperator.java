package com.keymanager.excel.operator;

import java.io.IOException;
import java.io.InputStream;

import jxl.read.biff.BiffException;

import com.keymanager.excel.definition.SuperUserFullKeywordDefinition;
import com.keymanager.excel.definition.SuperUserSimpleKeywordDefinition;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.JXLExcelReader;
import com.keymanager.value.CustomerKeywordVO;

public class SuperUserSimpleKeywordExcelOperator extends AbstractExcelReader {
	
	public SuperUserSimpleKeywordExcelOperator() throws BiffException, IOException{
		reader = new JXLExcelReader("C:/dev/KeywordManagementSystemShouji/WebRoot/SuperUserSimpleKeywordList.xls");
		reader.setCurrentWorkSheetWithName("KeywordList");
	}
	
	public SuperUserSimpleKeywordExcelOperator(InputStream inputStream) throws BiffException, IOException {
		super(inputStream);
	}
	
	@Override
	public CustomerKeywordVO readRow(int rowIndex){
		CustomerKeywordVO keywordVO = new CustomerKeywordVO();
		keywordVO.setKeyword(getStringValue(SuperUserSimpleKeywordDefinition.Keyword.getColumnIndex(), rowIndex));
		if(Utils.isNullOrEmpty(keywordVO.getKeyword())){
			return null;
		}
		keywordVO.setUrl(getStringValue(SuperUserSimpleKeywordDefinition.URL.getColumnIndex(), rowIndex));
		if(Utils.isNullOrEmpty(keywordVO.getUrl())){
			return null;
		}
		keywordVO.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
		keywordVO.setStartOptimizedTime(Utils.getCurrentTimestamp());
		
		keywordVO.setCollectMethod(getCollectMethodValue(getStringValue(SuperUserSimpleKeywordDefinition.CollectMethod.getColumnIndex(), rowIndex)));
		if(Utils.isNullOrEmpty(keywordVO.getCollectMethod())){
			return null;
		}
		keywordVO.setServiceProvider("baidutop123");
		
		double pcFee = getDoubleValue(SuperUserSimpleKeywordDefinition.Fee.getColumnIndex(), rowIndex);
		keywordVO.setPositionFirstFee(pcFee);
		keywordVO.setPositionSecondFee(pcFee);
		keywordVO.setPositionThirdFee(pcFee);
		
		int indexCount = getIntValue(SuperUserSimpleKeywordDefinition.IndexCount.getColumnIndex(), rowIndex);
		keywordVO.setCurrentIndexCount(indexCount);

		int sequence = getIntValue(SuperUserSimpleKeywordDefinition.Sequnce.getColumnIndex(), rowIndex);
		keywordVO.setSequence(sequence);

		int optimizePlanCount = getIntValue(SuperUserFullKeywordDefinition.OptimizePlanCount.getColumnIndex(), rowIndex);
		keywordVO.setOptimizePlanCount(optimizePlanCount);
		
		keywordVO.setOriginalUrl(getStringValue(SuperUserSimpleKeywordDefinition.OriginalURL.getColumnIndex(), rowIndex));
		keywordVO.setOptimizeGroupName(getStringValue(SuperUserSimpleKeywordDefinition.OptimizeGroupName.getColumnIndex(), rowIndex));
		
		keywordVO.setTitle(getStringValue(SuperUserSimpleKeywordDefinition.Title.getColumnIndex(), rowIndex));
		keywordVO.setRemarks(getStringValue(SuperUserSimpleKeywordDefinition.Remarks.getColumnIndex(), rowIndex));
		
		return keywordVO;
	}
}
