package com.keymanager.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.read.biff.BiffException;

public class CustomerKeywordOperator {
	public static void main(String[] args) throws BiffException, IOException{
		List<Integer> customerKeywordIDs = new ArrayList<Integer>();
		customerKeywordIDs.add(1);
		customerKeywordIDs.add(2);
		customerKeywordIDs.add(3);
		System.out.println(customerKeywordIDs);
		
//		
//		AgentKeywordExcelOperator operator = new AgentKeywordExcelOperator();
//		List<CustomerKeywordCriteria> list = operator.readDataFromExcel();
//		System.out.println(list.size());
//		
//		
//		SuperUserKeywordExcelOperator operator2 = new SuperUserKeywordExcelOperator();
//		List<CustomerKeywordCriteria> list2 = operator2.readDataFromExcel();
//		System.out.println(list2.size());
	}
}
