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
    List<PtCustomerKeyword> selectNewPtKeyword(@Param("userId") long userId);

    /**
     * 查询客户删除的词uuid集
     */
    List<Long> selectCustomerDelKeywords(@Param("userId") long userId);

    /**
     * 处理暂不操作的词
     */
    void updateCustomerKeywordDiffStatus(@Param("userId") long userId);

    /**
     * 清理销售已删除的词
     */
    void deleteSaleDelKeywords(@Param("userId") long userId);

    void updatePtKeywordCurrentPosition(@Param("userId") long userId);

    /**
     * 客户更新过的词需同步keyword，url，title到t_customer_keyword
     */
    void updateCustomerKeyword(@Param("userId") long userId);

    void updatePtKeywordOperaStatus(@Param("userId") long userId);
}
