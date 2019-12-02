package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.RelatedKeywordWithTypeCriteria;
import com.keymanager.ckadmin.entity.RelatedKeyWordWithType;
import java.util.List;

public interface RelatedKeywordWithTypeService extends IService<RelatedKeyWordWithType> {

    List<RelatedKeyWordWithType> findRelatedKeywordWithType(String mainKeyword);

    void saveRelatedKeywordWithType(RelatedKeywordWithTypeCriteria criteria);

    void deleteRelatedKeywordWithType(String mainKeyword, String relatedKeyword);
}
