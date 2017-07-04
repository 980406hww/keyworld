package com.keymanager.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.keymanager.value.KeywordPositionUrlVO;

public class NewsKeywordPositionCalculator {
	private int returnCount(){
		Random rd = new Random();
		return (1 + rd.nextInt(2));
	}
	
	public List<KeywordPositionUrlVO> calculate(List<KeywordPositionUrlVO> keywordPositionUrlVOs, int position){
		List<KeywordPositionUrlVO> resultKeywordPositionUrlVOs = new ArrayList<KeywordPositionUrlVO>();
		while (true) {
			KeywordPositionUrlVO keywordPositionUrlVO = keywordPositionUrlVOs.get(this.getFirstPosition());
			if (keywordPositionUrlVO.getUrl().indexOf("noclick") < 0) {
				resultKeywordPositionUrlVOs.add(keywordPositionUrlVO);
				break;
			}
		}
		if (this.returnCount() > 1) {
			while (true) {
				KeywordPositionUrlVO keywordPositionUrlVO = keywordPositionUrlVOs.get(this.getSecondPosition());
				if (keywordPositionUrlVO.getUrl().indexOf("noclick") < 0) {
					resultKeywordPositionUrlVOs.add(keywordPositionUrlVO);
					break;
				}
			}
		}
		return resultKeywordPositionUrlVOs;
	}
	
	/*
	 * 第二个点击从第四名到第十名
	 * 4-6占70%
	 * 7-10占30%
	 */
	private int getSecondPosition(){
		Random rd = new Random();
		int randomValue = rd.nextInt(100);
		if(randomValue < 70){
			return 3 + rd.nextInt(3);		
		}else{
			return 6 + rd.nextInt(4);
		}
	}
	
	/*
	 * 第一个点击全部来源于前三
	 * 第一位：70%
	 * 第二位：20%
	 * 第三位：10%
	 */
	private int getFirstPosition(){
		Random rd = new Random();
		int randomValue = rd.nextInt(100);
		if(randomValue < 70){
			return 0;		
		}else if(randomValue < 90){
			return 1;
		}else{
			return 2;
		}
	}
}
