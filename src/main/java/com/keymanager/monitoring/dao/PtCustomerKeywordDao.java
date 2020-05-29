package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.PtCustomerKeyword;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PtCustomerKeywordDao extends BaseMapper<PtCustomerKeyword> {

    /**
     * 根据customerKeywordId查询
     */
    PtCustomerKeyword selectExistingCmsKeyword(@Param("customerKeywordId") Long customerKeywordId);

    /**
     * 检查操作中的关键词排名是否爬取完成, 关闭开关
     */
    int checkFinishedCapturePosition();

    /**
     * 查询新增的关键词
     */
    List<PtCustomerKeyword> selectNewPtKeyword(@Param("customerName") String customerName);

    /**
     * 查询客户删除的词uuid集
     */
    List<Long> selectCustomerDelKeywords();

    /**
     * 更新状态不同的关键词（不包含物理删除的）
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
}
