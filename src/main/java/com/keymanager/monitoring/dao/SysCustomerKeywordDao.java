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
     * 清理已删除的词
     */
    void delBeDeletedKeyword(@Param("qsId") long qsId);

    void updateQzKeywordCurrentPosition(@Param("qsId") long qsId);

    void updateQzKeywordOperaStatus(@Param("qsId") long qsId);
}
