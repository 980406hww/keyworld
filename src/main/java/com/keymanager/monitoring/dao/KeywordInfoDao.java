package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.KeywordInfo;
import com.keymanager.monitoring.vo.KeywordInfoVO;
import org.apache.ibatis.annotations.Param;

public interface KeywordInfoDao extends BaseMapper<KeywordInfo> {

    void batchInsertKeyword(@Param("keywordInfoVO") KeywordInfoVO keywordInfoVO);
}
