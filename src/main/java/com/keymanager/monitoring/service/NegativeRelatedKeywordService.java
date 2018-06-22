package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.NegativeRelatedKeywordCriteria;
import com.keymanager.monitoring.dao.NegativeRelatedKeywordDao;
import com.keymanager.monitoring.entity.NegativeRelatedKeyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NegativeRelatedKeywordService extends ServiceImpl<NegativeRelatedKeywordDao, NegativeRelatedKeyword> {

    private static Logger logger = LoggerFactory.getLogger(NegativeRelatedKeywordService.class);

    @Autowired
    private NegativeRelatedKeywordDao negativeRelatedKeywordDao;

    public List<String> findNegativeRelatedKeyword(String mainKeyword) {
        return negativeRelatedKeywordDao.findNegativeRelatedKeyword(mainKeyword);
    }

    public void saveNegativeRelatedKeyword(NegativeRelatedKeywordCriteria negativeRelatedKeywordCriteria) {
        NegativeRelatedKeyword negativeRelatedKeyword = new NegativeRelatedKeyword();
        negativeRelatedKeyword.setMainKeyword(negativeRelatedKeywordCriteria.getMainKeyword());
        negativeRelatedKeyword.setRelatedKeyword(negativeRelatedKeywordCriteria.getRelatedKeyword());
        NegativeRelatedKeyword getNegativeRelatedKeyword = negativeRelatedKeywordDao.selectOne(negativeRelatedKeyword);
        if (getNegativeRelatedKeyword==null){
            negativeRelatedKeyword.setCreateTime(new Date());
            negativeRelatedKeywordDao.insert(negativeRelatedKeyword);
        }
    }

    public void deleteNegativeRelatedKeywords(String relatedKeyword) {
        negativeRelatedKeywordDao.deleteNegativeRelatedKeywords(relatedKeyword);
    }

}
