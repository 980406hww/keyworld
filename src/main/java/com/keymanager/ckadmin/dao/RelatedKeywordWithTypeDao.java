package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.RelatedKeyWordWithType;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("relatedKeywordWithTypeDao2")
public interface RelatedKeywordWithTypeDao extends BaseMapper<RelatedKeyWordWithType> {

    List<RelatedKeyWordWithType> findRelatedKeywordWithType(@Param("mainKeyword") String mainKeyword);
}
