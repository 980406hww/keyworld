package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerKeyword;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerKeywordDao extends BaseMapper<CustomerKeyword> {
    void clearTitleByUuids(String[] uuids);

    void clearTitleByCustomerUuidAndTerminalType(@Param("terminalType") String terminalType, @Param("customerUuid") String customerUuid);

    int getCustomerKeywordCount(@Param("customerUuid") long customerUuid);

    int getSimilarCustomerKeywordCount(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid, @Param("keyword") String
            keyword, @Param("originalUrl") String originalUrl);

    int getMaxSequence(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    List<CustomerKeyword> searchSameCustomerKeywords(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid,
            @Param("keyword") String keyword);

    List<CustomerKeyword> searchCustomerKeywordsForUpdateIndex(@Param("keyword") String keyword);

    CustomerKeyword getCustomerKeywordsForCaptureIndex();

    void updateCaptureIndexQueryTime(@Param("keyword") String keyword);

    void deleteByCustomerUuid(@Param("customerUuid") long customerUuid);
}
