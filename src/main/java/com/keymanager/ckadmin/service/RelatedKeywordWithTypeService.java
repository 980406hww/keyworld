package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.RelatedKeyWordWithType;
import java.util.List;

public interface RelatedKeywordWithTypeService extends IService<RelatedKeyWordWithType> {

    List<RelatedKeyWordWithType> findRelatedKeywordWithType(String mainKeyword);
}
