package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.PtCustomerKeyword;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PtCustomerKeywordDao extends BaseMapper<PtCustomerKeyword> {

    /**
     * 查询新增的关键词
     */
    List<PtCustomerKeyword> selectNewPtKeyword(@Param("userId") long userId, @Param("searchEngine") String searchEngine);

    /**
     * 查询客户删除的词uuid集
     */
    List<Long> selectCustomerDelKeywords(@Param("userId") long userId, @Param("searchEngine") String searchEngine);

    void updatePtKeywordCurrentPosition(@Param("userId") long userId);

    void updatePtKeywordOperaStatus(@Param("userId") long userId);

    void updatePtKeywordStatus(@Param("userId") long userId, @Param("searchEngine") String searchEngine, @Param("rows") int rows);
}
