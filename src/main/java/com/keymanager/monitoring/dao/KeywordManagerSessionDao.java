package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.KeywordManagerSessionCriteria;
import com.keymanager.monitoring.entity.KeywordManagerSession;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoukai
 * @since 2019-08-09
 */
public interface KeywordManagerSessionDao extends BaseMapper<KeywordManagerSession> {

    KeywordManagerSession findExistingKeywordManagerSession(@Param("sessionId") String sessionId, @Param("attributeName") String attributeName);

    List<KeywordManagerSession> searchKeywordManagerSessions(@Param("page") Page<KeywordManagerSession> page, @Param("criteria") KeywordManagerSessionCriteria criteria);
}
