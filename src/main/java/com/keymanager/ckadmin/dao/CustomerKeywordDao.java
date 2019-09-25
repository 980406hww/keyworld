package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.KeywordCriteria;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.vo.CustomerKeywordSummaryInfoVO;
import com.keymanager.monitoring.criteria.CustomerKeywordCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.keymanager.ckadmin.vo.KeywordCountVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("customerKeywordDao2")
public interface CustomerKeywordDao extends BaseMapper<CustomerKeyword> {

    List<Map> getCustomerKeywordsCount(@Param("customerUuids") List<Long> customerUuids,
        @Param("terminalType") String terminalType, @Param
        ("entryType") String entryType);

    void changeCustomerKeywordStatus(@Param("terminalType") String terminalType,
        @Param("entryType") String entryType,
        @Param("customerUuid") Long customerUuid, @Param("status") Integer status);

    void deleteCustomerKeywordsByCustomerUuid(long customerUuid);

    int getCustomerKeywordCount(@Param("terminalType") String terminalType,
        @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    void excludeCustomerKeyword(
        @Param("qzSettingExcludeCustomerKeywordsCriteria") QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria);

    void addCustomerKeywords(
        @Param("customerKeywords") ArrayList<CustomerKeyword> customerKeywords);

    CustomerKeyword getOneSameCustomerKeyword(@Param("terminalType") String terminalType,
        @Param("customerUuid") long customerUuid, @Param("keyword") String keyword,
        @Param("url") String url, @Param("title") String title);

    void updateSameCustomerKeyword(@Param("customerKeyword") CustomerKeyword customerKeyword);

    CustomerKeyword getOneSimilarCustomerKeyword(@Param("terminalType") String terminalType,
        @Param("customerUuid") long customerUuid, @Param("keyword") String keyword,
        @Param("originalUrl") String originalUrl, @Param("title") String title);

    void updateSimilarCustomerKeyword(@Param("customerKeyword") CustomerKeyword customerKeyword);

    Integer getCustomerKeywordCountByOptimizeGroupName(@Param("groupName") String groupName);

    List<CustomerKeywordSummaryInfoVO> searchCustomerKeywordSummaryInfo(
        @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    KeywordCountVO getCustomerKeywordsCountByCustomerUuid(@Param("customerUuid") Long customerUuid, @Param("terminalType") String terminalType);

    List<CustomerKeyword> searchKeywords(Page<CustomerKeyword> page, @Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void updateCustomerKeywordStatus(@Param("uuids") List<Long> uuids, @Param("status") Integer status);

    int getKeywordCountByKeywordCriteria(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void updateOptimizeGroupName(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void updateMachineGroup(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void updateBearPawNumber(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void deleteCustomerKeywordsByUuids(@Param("customerKeywordUuids") List<Long> customerKeywordUuids);

    void deleteCustomerKeywordsWhenEmptyTitle(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void deleteCustomerKeywordsWhenEmptyTitleAndUrl(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    List<CustomerKeyword> searchCustomerKeywords(Page<CustomerKeyword> page, @Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void updateOptimizePlanCount(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    List<CustomerKeyword> searchCustomerKeywordsForDailyReport(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    List<String> getGroups(@Param("customerUuids") List<Long> customerUuids);

    List<Long> getCustomerUuids(@Param("entryType") String entryType, @Param("terminalType") String terminalType);

    List<CustomerKeyword> searchCustomerKeywordInfo(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    List<Map> selectAllKeywordAndUrl(@Param("customerUuid") Long customerUuid, @Param("terminalType") String terminalType);

    void updateSearchEngine(@Param("keywordCriteria") KeywordCriteria keywordCriteria);
}