package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.RelatedKeywordWithTypeCriteria;
import com.keymanager.monitoring.dao.RelatedKeywordWithTypeDao;
import com.keymanager.monitoring.entity.RelatedKeyWordWithType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RelatedKeywordWithTypeService extends ServiceImpl<RelatedKeywordWithTypeDao, RelatedKeyWordWithType> {
    private static Logger logger = LoggerFactory.getLogger(RelatedKeywordWithTypeService.class);

    @Autowired
    private RelatedKeywordWithTypeDao relatedKeywordWithTypeDao;

    public List<String> findRelatedKeywordWithType(String mainKeyword){
        List<RelatedKeyWordWithType> relatedKeywordWordWithTypes = relatedKeywordWithTypeDao.findRelatedKeywordWithType(mainKeyword);
        List<String> relatedKeywords = new ArrayList<String>();

        for (RelatedKeyWordWithType relatedKeyWordWithType : relatedKeywordWordWithTypes) {
            relatedKeywords.add(relatedKeyWordWithType.getRelatedKeyword()+","+relatedKeyWordWithType.getType());
        }
        return relatedKeywords;
    }

    public void saveRelatedKeywordWithType(RelatedKeywordWithTypeCriteria relatedKeywordWithTypeCriteria){
        RelatedKeyWordWithType relatedKeyWordWithType = new RelatedKeyWordWithType();
        relatedKeyWordWithType.setMainKeyword(relatedKeywordWithTypeCriteria.getMainKeyword());
        relatedKeyWordWithType.setRelatedKeyword(relatedKeywordWithTypeCriteria.getRelatedKeyword());
        RelatedKeyWordWithType getRelatedKeywordWithType = relatedKeywordWithTypeDao.selectOne(relatedKeyWordWithType);

        if (getRelatedKeywordWithType == null){
            relatedKeyWordWithType.setCreateTime(new Date());
            relatedKeyWordWithType.setType(relatedKeywordWithTypeCriteria.getType());
            relatedKeywordWithTypeDao.insert(relatedKeyWordWithType);
        }
        else{
            getRelatedKeywordWithType.setUpdateTime(new Date());
            getRelatedKeywordWithType.setType(relatedKeywordWithTypeCriteria.getType());
            relatedKeywordWithTypeDao.updateById(getRelatedKeywordWithType);
        }
    }

    public void deleteRelatedKeywordWithType(String relatedKeyword){
        relatedKeywordWithTypeDao.deleteRelatedKeywordWithType(relatedKeyword);
    }
}