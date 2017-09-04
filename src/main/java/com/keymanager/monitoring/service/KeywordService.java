package com.keymanager.monitoring.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.keymanager.db.DBUtil;
import com.keymanager.monitoring.dao.KeywordDao;
import com.keymanager.monitoring.entity.Keyword;
import com.keymanager.util.Utils;
import com.keymanager.value.KeywordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

@Service
public class KeywordService extends ServiceImpl<KeywordDao, Keyword> {
	
	@Autowired
	private KeywordDao keywordDao;
	
	public List<Map> findKeywords(String keyword, String type, Long userId){
		return keywordDao.findKeywords(keyword, type, userId);
	}

	public List<Map> findKeywordsForAutoComplete(Long userId){
		return keywordDao.findKeywordsForAutoComplete(userId);
	}

	public void addKeywordVOs(String keywords, String searchEngine, String type) throws Exception{
		if (!Utils.isNullOrEmpty(keywords)){
			String[] keywordArrays = keywords.trim().split(",");
			for(String keyword : keywordArrays){
				if(!Utils.isNullOrEmpty(keyword) && !existKeyword(keyword, searchEngine)){
					KeywordVO keywordVO = new KeywordVO(keyword, searchEngine, type);
//					addKeywordVO(keywordVO);
				}
			}
		}
	}

	private boolean existKeyword(String keyword, String searchEngine) throws Exception {
		try {
			Keyword keywordObj = keywordDao.existKeyword(keyword,searchEngine);
			if(keywordObj!=null){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		}
		return false;
	}
}
