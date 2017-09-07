package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerKeywordCrilteria;
import com.keymanager.monitoring.criteria.CustomerKeywordUpdateGroupCriteria;
import com.keymanager.monitoring.entity.CustomerKeyword;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomerKeywordDao extends BaseMapper<CustomerKeyword> {
    void cleanSelectedCustomerKeywordTitle(@Param("uuids")List<String> uuids);

    void cleanCustomerTitle(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") String customerUuid);

    void cleanCaptureTitleFlag(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") String customerUuid);

    int getCustomerKeywordCount(@Param("customerUuid") long customerUuid);

    List<Map> getCustomerKeywordsCount(@Param("customerUuids") List<Long> customerUuids, @Param("terminalType") String terminalType, @Param
            ("entryType") String entryType);

    int getSimilarCustomerKeywordCount(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid, @Param("keyword") String
            keyword, @Param("originalUrl") String originalUrl);

    int getMaxSequence(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    List<CustomerKeyword> searchSameCustomerKeywords(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid,
                                                     @Param("keyword") String keyword);

    List<CustomerKeyword> searchCustomerKeywordsForUpdateIndex(@Param("keyword") String keyword);

    CustomerKeyword getCustomerKeywordsForCaptureIndex();

    void updateCaptureIndexQueryTime(@Param("keyword") String keyword);

    List<CustomerKeyword> searchCustomerKeywords(Page<CustomerKeyword> page, @Param("customerKeywordCrilteria") CustomerKeywordCrilteria customerKeywordCrilteria);

    //重构部分
    //修改该用户关键字组名
    void updateCustomerKeywordGroupName(@Param("customerKeywordUpdateGroupCriteria")CustomerKeywordUpdateGroupCriteria customerKeywordUpdateGroupCriteria);

    void changeOptimizationGroup(@Param("customerKeyword")CustomerKeyword customerKeyword);

    void deleteCustomerKeywordsByUuid(@Param("customerKeywordUuids")List<String> customerKeywordUuids);

    void deleteCustomerKeywordsWhenEmptyTitleAndUrl(@Param("terminalType")String terminalType, @Param("entryType")String entryType, @Param("customerUuid")String customerUuid);

    void deleteCustomerKeywordsWhenEmptyTitle(@Param("terminalType")String terminalType, @Param("entryType")String entryType, @Param("customerUuid")String customerUuid);
}
