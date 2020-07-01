package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.PtCustomerKeywordTemporary;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PtCustomerKeywordTemporaryDao extends BaseMapper<PtCustomerKeywordTemporary> {

    /**
     * 根据客户id，将数据临时存储在中间表
     */
    void migrationRecordToPtCustomerKeyword(@Param("cusId") Long cusId, @Param("type") String type);

    /**
     * 清空临时表数据
     */
    void cleanPtCustomerKeyword();

    /**
     * 检查是否同步完成
     */
    int searchPtKeywordTemporaryCount();

    /**
     * 修改标识，意为更新中
     */
    void updatePtKeywordMarks(@Param("rows") int rows, @Param("mark") int mark);

    /**
     * 临时存放关键词操作状态
     */
    void insertIntoTemporaryData(@Param("cusId") Long cusId, @Param("type") String type);
}
