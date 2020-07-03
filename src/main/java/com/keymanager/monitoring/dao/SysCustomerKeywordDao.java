package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.SysCustomerKeyword;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysCustomerKeywordDao extends BaseMapper<SysCustomerKeyword> {

    /**
     * 查询新增的关键词
     */
    List<SysCustomerKeyword> selectNewQzKeyword(@Param("qsId") long qsId);

    /**
     * 查询客户删除的词uuid集
     */
    List<Long> selectCustomerDelKeywords(@Param("qsId") long qsId);

    /**
     * 处理暂不操作的词
     */
    void updateCustomerKeywordDiffStatus(@Param("qsId") long qsId);

    /**
     * 清理销售已删除的词
     */
    void deleteSaleDelKeywords(@Param("qsId") long qsId);

    void updateQzKeywordCurrentPosition(@Param("qsId") long qsId);

    /**
     * 客户更新过的词需同步keyword，url到t_customer_keyword
     */
    void updateCustomerKeyword(@Param("qsId") long qsId);

    void updateQzKeywordOperaStatus(@Param("qsId") long qsId);
}
