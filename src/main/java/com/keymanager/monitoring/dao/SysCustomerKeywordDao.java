package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.SysCustomerKeyword;
import org.apache.ibatis.annotations.Param;

/**
 * @author shunshikj40 
 */
public interface SysCustomerKeywordDao extends BaseMapper<SysCustomerKeyword> {

    /**
     * 批量插入数据
     * @param customerUuid
     * @param qsId
     */
    void batchInsertCustomerKeywordByCustomerUuid(@Param("customerUuid") Long customerUuid, @Param("qsId") Long qsId);

    /**
     * 定时删除创建超过七天的同步关键词
     */
    void cleanSysCustomerKeywordCreateOverOneWeek();
}
