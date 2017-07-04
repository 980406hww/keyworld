package com.keymanager.excel.operator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jxl.read.biff.BiffException;

import com.keymanager.excel.definition.AgenetKeywordDefinition;
import com.keymanager.excel.definition.SuperUserKeywordDefinition;
import com.keymanager.util.Utils;
import com.keymanager.util.excel.JXLExcelReader;
import com.keymanager.value.CustomerKeywordVO;

public class AgentKeywordExcelOperator extends AbstractExcelReader {
	public AgentKeywordExcelOperator() throws BiffException, IOException{
		reader = new JXLExcelReader(new FileInputStream(new File("D:/dev/Java Project/KeywordManagementSystem/web/AgentKeywordList.xls")));
		reader.setCurrentWorkSheetWithName("KeywordList");
	}
	
	public AgentKeywordExcelOperator(InputStream inputStream) throws BiffException, IOException {
		super(inputStream);
	}
	
	@Override
	public CustomerKeywordVO readRow(int rowIndex){
		CustomerKeywordVO keywordVO = new CustomerKeywordVO();
		keywordVO.setKeyword(getStringValue(AgenetKeywordDefinition.Keyword.getColumnIndex(), rowIndex));
		if(Utils.isNullOrEmpty(keywordVO.getKeyword())){
			return null;
		}
		keywordVO.setUrl(getStringValue(AgenetKeywordDefinition.URL.getColumnIndex(), rowIndex));
		if(Utils.isNullOrEmpty(keywordVO.getUrl())){
			return null;
		}
		keywordVO.setSearchEngine(getStringValue(AgenetKeywordDefinition.SearchEngine.getColumnIndex(), rowIndex));
		if(Utils.isNullOrEmpty(keywordVO.getSearchEngine())){
			return null;
		}
		String startOptimizationTime = getStringValue(AgenetKeywordDefinition.StartOptimization.getColumnIndex(), rowIndex);
		keywordVO.setStartOptimizedTime(Utils.parseDate(startOptimizationTime, "yyyy-M-d"));
		
		keywordVO.setCollectMethod(getCollectMethodValue(getStringValue(AgenetKeywordDefinition.CollectMethod.getColumnIndex(), rowIndex)));
		if(Utils.isNullOrEmpty(keywordVO.getCollectMethod())){
			return null;
		}
		keywordVO.setPositionFirstFee(getIntValue(AgenetKeywordDefinition.FirstFee.getColumnIndex(), rowIndex));
		if(keywordVO.getPositionFirstFee() == 0){
			return null;
		}
		keywordVO.setPositionSecondFee(getIntValue(AgenetKeywordDefinition.SecondFee.getColumnIndex(), rowIndex));
		keywordVO.setPositionThirdFee(getIntValue(AgenetKeywordDefinition.ThirdFee.getColumnIndex(), rowIndex));
		keywordVO.setPositionForthFee(getIntValue(AgenetKeywordDefinition.ForthFee.getColumnIndex(), rowIndex));
		keywordVO.setPositionFifthFee(getIntValue(AgenetKeywordDefinition.FifthFee.getColumnIndex(), rowIndex));
		
		keywordVO.setRemarks(getStringValue(AgenetKeywordDefinition.Remarks.getColumnIndex(), rowIndex));
		
		return keywordVO;
	}
}
