package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.KeywordInfoCriteria;
import com.keymanager.ckadmin.entity.KeywordInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("keywordInfoDao2")
public interface KeywordInfoDao extends BaseMapper<KeywordInfo> {

    List<KeywordInfo> searchKeywordInfos(Page<KeywordInfo> page, @Param("criteria") KeywordInfoCriteria criteria);
}
