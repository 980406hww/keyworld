package com.keymanager.monitoring.service;

import java.util.List;
import java.util.Map;

import com.keymanager.monitoring.dao.KeywordDao;
import com.keymanager.monitoring.entity.Keyword;
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
}
