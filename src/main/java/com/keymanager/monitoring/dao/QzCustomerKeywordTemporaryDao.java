package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.QzCustomerKeywordTemporary;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QzCustomerKeywordTemporaryDao extends BaseMapper<QzCustomerKeywordTemporary> {

    /**
     * 根据客户id，全站id，将数据临时存储在中间表
     */
    void migrationRecordToQzCustomerKeyword(@Param("qsId") Long qsId, @Param("type") String type);

    /**
     * 清空临时表数据
     */
    void cleanQzCustomerKeyword();

    /**
     * 检查是否同步完成
     */
    int searchQzKeywordTemporaryCount();

    /**
     * 修改标识，意为更新中
     */
    void updateQzKeywordMarks(@Param("rows") int rows, @Param("mark") int mark, @Param("targetMark") int targetMark);

    /**
     * 临时存放qz关键词操作状态
     */
    void insertIntoTemporaryData(@Param("qsId") Long qsId);

    void updateQzKeywordOperaStatus(@Param("qsId") long qsId);
}
