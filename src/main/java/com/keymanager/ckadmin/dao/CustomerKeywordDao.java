package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.vo.CustomerKeywordSummaryInfoVO;
import com.keymanager.ckadmin.vo.OptimizationKeywordVO;
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

    List<String> getMachineGroups();

    void changeCustomerKeywordStatus(@Param("terminalType") String terminalType,
        @Param("entryType") String entryType,
        @Param("customerUuid") Long customerUuid, @Param("status") Integer status);

    void deleteCustomerKeywordsByCustomerUuid(long customerUuid);

    void updateOptimizationQueryTime(@Param("customerKeywordUuids") List<Long> customerKeywordUuids);

    int getCustomerKeywordCount(@Param("terminalType") String terminalType,
        @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    void excludeCustomerKeyword(
        @Param("qzSettingExcludeCustomerKeywordsCriteria") QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria);

    void addCustomerKeywords(
        @Param("customerKeywords") ArrayList<CustomerKeyword> customerKeywords);

    List<OptimizationKeywordVO> fetchCustomerKeywordsForCache(@Param("terminalType") String terminalType, @Param("machineGroup") String machineGroup, @Param("batchCount") int batchCount);

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

    void resetInvalidRefreshCount(@Param("criteria") RefreshStatisticsCriteria criteria);
}