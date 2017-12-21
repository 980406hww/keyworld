package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ClientStatusDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.util.Utils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class KeywordOptimizationCountService extends ServiceImpl<ClientStatusDao, ClientStatus>{
	private static Map<String, KeywordOptimizationCount> keywordOptimizationCountMap = new HashMap<String, KeywordOptimizationCount>();

	private synchronized KeywordOptimizationCount get(String groupName){
		KeywordOptimizationCount keywordOptimizationCount = keywordOptimizationCountMap.get(groupName);
		if(keywordOptimizationCount == null){
			keywordOptimizationCount = new KeywordOptimizationCount();
			keywordOptimizationCount.setGroup(groupName);
			keywordOptimizationCount.setLastResetTime(Utils.getCurrentTimestamp());
			keywordOptimizationCountMap.put(groupName, keywordOptimizationCount);
		}
		return keywordOptimizationCount;
	}

	public boolean resetBigKeywordIndicator(String groupName){
		KeywordOptimizationCount keywordOptimizationCount = get(groupName);
		return !(keywordOptimizationCount.fetchBigKeyword() || keywordOptimizationCount.fetchNormalKeyword());
	}

	public boolean optimizeNormalKeyword(String groupName){
		KeywordOptimizationCount keywordOptimizationCount = get(groupName);
		if(keywordOptimizationCount.fetchNormalKeyword()){
			keywordOptimizationCount.setNormalKeywordOptimizedCount(keywordOptimizationCount.getNormalKeywordOptimizedCount() + 1);
			return true;
		}else{
			keywordOptimizationCount.setBigKeywordOptimizedCount(keywordOptimizationCount.getBigKeywordOptimizedCount() + 1);
			return false;
		}
	}

	public void init(String groupName){
		KeywordOptimizationCount keywordOptimizationCount = get(groupName);
		keywordOptimizationCount.setBigKeywordOptimizedCount(0);
		keywordOptimizationCount.setNormalKeywordOptimizedCount(0);
	}

	public boolean allowResetBigKeywordIndicator(String groupName){
		KeywordOptimizationCount keywordOptimizationCount = get(groupName);
		return Utils.getCurrentTimestamp().after(Utils.addMinutes(keywordOptimizationCount.getLastResetTime(), 20));
	}

	public void setLastVisitTime(String groupName){
		KeywordOptimizationCount keywordOptimizationCount = get(groupName);
		keywordOptimizationCount.setLastResetTime(Utils.getCurrentTimestamp());
	}

	class KeywordOptimizationCount {
		private String group;
		private int normalKeywordOptimizedCount;
		private int bigKeywordOptimizedCount;
		private Timestamp lastResetTime;

		public boolean fetchNormalKeyword(){
			return this.normalKeywordOptimizedCount < 30;
		}

		public boolean fetchBigKeyword(){
			return this.bigKeywordOptimizedCount < 20;
		}

		public String getGroup() {
			return group;
		}

		public void setGroup(String group) {
			this.group = group;
		}

		public int getNormalKeywordOptimizedCount() {
			return normalKeywordOptimizedCount;
		}

		public void setNormalKeywordOptimizedCount(int normalKeywordOptimizedCount) {
			this.normalKeywordOptimizedCount = normalKeywordOptimizedCount;
		}

		public int getBigKeywordOptimizedCount() {
			return bigKeywordOptimizedCount;
		}

		public void setBigKeywordOptimizedCount(int bigKeywordOptimizedCount) {
			this.bigKeywordOptimizedCount = bigKeywordOptimizedCount;
		}

		public Timestamp getLastResetTime() {
			return lastResetTime;
		}

		public void setLastResetTime(Timestamp lastResetTime) {
			this.lastResetTime = lastResetTime;
		}
	}
}

