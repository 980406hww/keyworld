package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.RelatedKeywordWithTypeCriteria;
import com.keymanager.monitoring.dao.RelatedKeywordWithTypeDao;
import com.keymanager.monitoring.entity.RelatedKeyWordWithType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RelatedKeywordWithTypeService extends ServiceImpl<RelatedKeywordWithTypeDao, RelatedKeyWordWithType> {
    private static Logger logger = LoggerFactory.getLogger(RelatedKeywordWithTypeService.class);

    @Autowired
    private RelatedKeywordWithTypeDao relatedKeywordWithTypeDao;

    public List<RelatedKeyWordWithType> findRelatedKeywordWithType(String mainKeyword){
        List<RelatedKeyWordWithType> relatedKeywordWordWithTypes = relatedKeywordWithTypeDao.findRelatedKeywordWithType(mainKeyword);
        return relatedKeywordWordWithTypes;
    }

    public void saveRelatedKeywordWithType(RelatedKeywordWithTypeCriteria relatedKeywordWithTypeCriteria){
        RelatedKeyWordWithType relatedKeyWordWithTypeCriteria = new RelatedKeyWordWithType();
        relatedKeyWordWithTypeCriteria.setMainKeyword(relatedKeywordWithTypeCriteria.getMainKeyword());
        relatedKeyWordWithTypeCriteria.setRelatedKeyword(relatedKeywordWithTypeCriteria.getRelatedKeyword());
        RelatedKeyWordWithType existRelatedKeywordWithType = relatedKeywordWithTypeDao.selectOne(relatedKeyWordWithTypeCriteria);

        if (existRelatedKeywordWithType == null){
            relatedKeyWordWithTypeCriteria.setCreateTime(new Date());
            relatedKeyWordWithTypeCriteria.setType(relatedKeywordWithTypeCriteria.getType());
            relatedKeywordWithTypeDao.insert(relatedKeyWordWithTypeCriteria);
        }
        else{
            existRelatedKeywordWithType.setUpdateTime(new Date());
            existRelatedKeywordWithType.setType(relatedKeywordWithTypeCriteria.getType());
            relatedKeywordWithTypeDao.updateById(existRelatedKeywordWithType);
        }
    }

    public void deleteRelatedKeywordWithType(String mainKeyword, String relatedKeyword){
        relatedKeywordWithTypeDao.deleteRelatedKeywordWithType(mainKeyword, relatedKeyword);
    }
}