package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.KeywordInfoCriteria;
import com.keymanager.monitoring.entity.KeywordInfo;
import com.keymanager.monitoring.vo.KeywordInfoVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;


public interface KeywordInfoDao extends BaseMapper<KeywordInfo> {

    void batchInsertKeyword(@Param("keywordInfoVO") KeywordInfoVO keywordInfoVO);

    List<KeywordInfo> searchKeywordInfos(Page<KeywordInfo> page, @Param("keywordInfoCriteria") KeywordInfoCriteria keywordInfoCriteria);

}
