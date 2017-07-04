package com.keymanager.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.keymanager.value.KeywordPositionUrlVO;

/**
 * 首页：点击前面1次
 * 第二页：点击前面
 * @author Administrator
 *
 */
public class PreviousKeywordPositionCalculator {
	private int returnCount(int count){
		if(count < 11){
			return 1;
		}else if(count < 21){
			return 2;
		}else{
			return 4;
		}
	}
	
	public List<KeywordPositionUrlVO> calculate(List<KeywordPositionUrlVO> keywordPositionUrlVOs, int position){
		int count = this.returnCount(position);
		List<KeywordPositionUrlVO> resultKeywordPositionUrlVOs = new ArrayList<KeywordPositionUrlVO>();		
		int pageIndex = 0;
		for(int i = 1; i <= count; i++){
			int modValue = i % 2;
			int retrieveTimes = 0;
			if(modValue == 1){
				while (true) {
					if (retrieveTimes > 3){
						break;
					}
					KeywordPositionUrlVO keywordPositionUrlVO = keywordPositionUrlVOs.get(this.getFirstPosition() + (pageIndex/2) * 10);
					if (keywordPositionUrlVO.getUrl().indexOf("noclick") < 0) {
						resultKeywordPositionUrlVOs.add(keywordPositionUrlVO);
						break;
					}
					retrieveTimes++;
				}
			}else{
				while (true) {
					if (retrieveTimes > 3){
						break;
					}
					KeywordPositionUrlVO keywordPositionUrlVO = keywordPositionUrlVOs.get(this.getSecondPosition() + (pageIndex/2) * 10);
					if (keywordPositionUrlVO.getUrl().indexOf("noclick") < 0) {
						resultKeywordPositionUrlVOs.add(keywordPositionUrlVO);
						break;
					}
					retrieveTimes++;
				}
			}
			pageIndex++;
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
