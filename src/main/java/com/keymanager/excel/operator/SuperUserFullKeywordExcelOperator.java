package com.keymanager.excel.operator;

import java.io.IOException;
import java.io.InputStream;

import jxl.read.biff.BiffException;

import com.keymanager.excel.definition.SuperUserFullKeywordDefinition;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.JXLExcelReader;
import com.keymanager.value.CustomerKeywordVO;

public class SuperUserFullKeywordExcelOperator extends AbstractExcelReader {
	
	public SuperUserFullKeywordExcelOperator() throws BiffException, IOException{
		reader = new JXLExcelReader("C:/dev/KeywordManagementSystemShouji/WebRoot/SuperUserFullKeywordList.xls");
		reader.setCurrentWorkSheetWithName("KeywordList");
	}
	
	public SuperUserFullKeywordExcelOperator(InputStream inputStream) throws BiffException, IOException {
		super(inputStream);
	}
	
	@Override
	public CustomerKeywordVO readRow(int rowIndex){
		CustomerKeywordVO keywordVO = new CustomerKeywordVO();
		keywordVO.setKeyword(getStringValue(SuperUserFullKeywordDefinition.Keyword.getColumnIndex(), rowIndex));
		if(Utils.isNullOrEmpty(keywordVO.getKeyword())){
			return null;
		}
		keywordVO.setCollectMethod(getCollectMethodValue(getStringValue(SuperUserFullKeywordDefinition.CollectMethod.getColumnIndex(), rowIndex)));
		if(Utils.isNullOrEmpty(keywordVO.getCollectMethod())){
			return null;
		}

		keywordVO.setOriginalUrl(getStringValue(SuperUserFullKeywordDefinition.OriginalURL.getColumnIndex(), rowIndex));
		keywordVO.setUrl(getStringValue(SuperUserFullKeywordDefinition.URL.getColumnIndex(), rowIndex));
		
		keywordVO.setPositionFirstFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionFirstFee.getColumnIndex(), rowIndex));
		keywordVO.setPositionSecondFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionSecondFee.getColumnIndex(), rowIndex));
		keywordVO.setPositionThirdFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionThirdFee.getColumnIndex(), rowIndex));
		keywordVO.setPositionForthFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionForthFee.getColumnIndex(), rowIndex));
		keywordVO.setPositionFifthFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionFifthFee.getColumnIndex(), rowIndex));
		keywordVO.setPositionFirstPageFee(getDoubleValue(SuperUserFullKeywordDefinition.PositionFirstPageFee.getColumnIndex(), rowIndex));
		
		if(Utils.isNullOrEmpty(keywordVO.getUrl())){
			return null;
		}
		
		keywordVO.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
		keywordVO.setStartOptimizedTime(Utils.getCurrentTimestamp());
		
		keywordVO.setServiceProvider("baidutop123");
		
		int indexCount = getIntValue(SuperUserFullKeywordDefinition.IndexCount.getColumnIndex(), rowIndex);
		keywordVO.setCurrentIndexCount(indexCount);

		int sequence = getIntValue(SuperUserFullKeywordDefinition.Sequnce.getColumnIndex(), rowIndex);
		keywordVO.setSequence(sequence);

		int optimizePlanCount = getIntValue(SuperUserFullKeywordDefinition.OptimizePlanCount.getColumnIndex(), rowIndex);
		keywordVO.setOptimizePlanCount(optimizePlanCount);
		
		keywordVO.setOptimizeGroupName(getStringValue(SuperUserFullKeywordDefinition.OptimizeGroupName.getColumnIndex(), rowIndex));
		keywordVO.setTitle(getStringValue(SuperUserFullKeywordDefinition.Title.getColumnIndex(), rowIndex));
		keywordVO.setRemarks(getStringValue(SuperUserFullKeywordDefinition.Remarks.getColumnIndex(), rowIndex));
		return keywordVO;
	}
}
