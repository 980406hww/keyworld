package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.PtCustomerKeyword;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
}
