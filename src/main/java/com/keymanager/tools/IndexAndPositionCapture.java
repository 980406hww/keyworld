package com.keymanager.tools;

import java.util.ArrayList;

import com.keymanager.manager.CustomerKeywordManager;
import com.keymanager.util.IndexAndPositionHelper;
import com.keymanager.value.CustomerKeywordVO;

public class IndexAndPositionCapture {
	public static void capture(int customerUuid, boolean restart) throws Exception{		
		CustomerKeywordManager ckm = new CustomerKeywordManager();
		String order = " ORDER BY fInitialPosition desc ";
		String condition = " and fCustomerUuid = " + customerUuid;
		if(!restart){
//			condition = condition + " and ((fCurrentPosition = 0) OR (fCurrentIndexCount = 0))";
			condition = condition + " and ((fCurrentPosition = 0))";
		}
		
		for( int i = 0; i < 10; i++){
			ArrayList customerKeywordVOs = ckm.searchCustomerKeywords("keyword", 1000, 1, condition, order, 0);
			if (customerKeywordVOs.size() == 0){
				break;
			}
			for(Object obj : customerKeywordVOs){
				CustomerKeywordVO customerKeywordVO = (CustomerKeywordVO)obj;
				
				int indexCount = IndexAndPositionHelper.getIndexCount(customerKeywordVO.getSearchEngine(), customerKeywordVO.getKeyword());
				int positionNumber = IndexAndPositionHelper.getPosition(customerKeywordVO.getSearchEngine(), customerKeywordVO.getKeyword(), customerKeywordVO.getUrl());
				customerKeywordVO.setCurrentPosition(positionNumber);
				customerKeywordVO.setCurrentIndexCount(indexCount);
				ckm.updateCustomerKeyword(customerKeywordVO, "keyword");
			}
		}
	}
}
