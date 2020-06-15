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
    List<PtCustomerKeyword> selectNewPtKeyword(@Param("customerName") String customerName);

    /**
     * 查询客户删除的词uuid集
     */
    List<Long> selectCustomerDelKeywords();

    /**
     * 处理暂不操作的词
     */
    void updateCustomerKeywordDiffStatus();

    /**
     * 清理销售已删除的词
     */
    void deleteSaleDelKeywords();

    /**
     * 清理不再需要同步的客户数据
     */
    void cleanNotExistCustomerKeyword(@Param("customerNames") String[] customerNames);

    void updatePtKeywordCurrentPosition();

    /**
     * 客户更新过的词需同步keyword，url，title到t_customer_keyword
     */
    void updateCustomerKeyword();
}
