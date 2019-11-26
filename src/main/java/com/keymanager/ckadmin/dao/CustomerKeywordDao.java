package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.CustomerKeywordUpdateStatusCriteria;
import com.keymanager.ckadmin.criteria.GroupSettingCriteria;
import com.keymanager.ckadmin.criteria.KeywordCriteria;
import com.keymanager.ckadmin.criteria.KeywordStandardCriteria;
import com.keymanager.ckadmin.criteria.PTKeywordCountCriteria;
import com.keymanager.ckadmin.criteria.QZRateKewordCountCriteria;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.vo.CodeNameVo;
import com.keymanager.ckadmin.vo.CustomerKeyWordCrawlRankVO;
import com.keymanager.ckadmin.vo.CustomerKeywordSummaryInfoVO;
import com.keymanager.ckadmin.vo.GroupVO;
import com.keymanager.ckadmin.vo.KeywordCountVO;
import com.keymanager.ckadmin.vo.OptimizationKeywordVO;
import com.keymanager.ckadmin.vo.PTkeywordCountVO;
import com.keymanager.ckadmin.vo.QZRateKeywordCountVO;
import com.keymanager.value.CustomerKeywordForCapturePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    int getCustomerKeywordCount(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    void excludeCustomerKeyword(@Param("qzSettingExcludeCustomerKeywordsCriteria") QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria);

    void addCustomerKeywords(@Param("customerKeywords") ArrayList<CustomerKeyword> customerKeywords);

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

    List<CustomerKeywordSummaryInfoVO> searchCustomerKeywordSummaryInfo(@Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    List<KeywordCountVO> getCustomerKeywordsCountByCustomerUuid(@Param("customerUuid") Long customerUuid, @Param("type") String type);

    void resetInvalidRefreshCount(@Param("criteria") RefreshStatisticsCriteria criteria);

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

    void changeCustomerKeywordStatusInCKPage(@Param("customerKeywordUpdateStatusCriteria") CustomerKeywordUpdateStatusCriteria customerKeywordUpdateStatusCriteria);

    void cleanSelectedCustomerKeywordTitle(@Param("uuids") List<Long> uuids);

    void cleanCustomerTitle(@Param("terminalType") String terminalType, @Param("type") String type, @Param("customerUuid") Long customerUuid);

    void cleanCaptureTitleFlag(@Param("terminalType") String terminalType, @Param("type") String type, @Param("customerUuid") Long customerUuid);

    void cleanCaptureTitleBySelected(@Param("uuids") List<Long> uuids);

    List<String> searchDuplicateKeywords(@Param("customerKeywordUpdateStatusCriteria") CustomerKeywordUpdateStatusCriteria customerKeywordUpdateStatusCriteria);

    void batchUpdateKeywords(@Param("uuids") List<String> uuids, @Param("keywordChecks") CustomerKeyword keywordChecks, @Param("keywordValues") CustomerKeyword keywordValues);

    int searchCustomerKeywordForNoReachStandard(@Param("keywordStandardCriteria") KeywordStandardCriteria keywordStandardCriteria);

    List<CodeNameVo> searchGroupsByTerminalType(@Param("terminalType") String terminalType);

    List<GroupVO> getAvailableOptimizationGroups(@Param("groupSettingCriteria") GroupSettingCriteria groupSettingCriteria);

    List<PTkeywordCountVO> searchPTKeywordCount(Page<PTkeywordCountVO> page, @Param("keywordCriteria") PTKeywordCountCriteria keywordCriteria);

    void updateKeywordCustomerUuid(@Param("keywordUuids") List<String> keywordUuids, @Param("customerUuid") String customerUuid, @Param("terminalType") String terminalType);

    void updateCustomerUuidByQzUuids(@Param("customerUuid") Long customerUuid, @Param("qzUuids") List<Long> qzUuids);

    void editOptimizePlanCountByCustomerUuid(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") Long customerUuid, @Param("optimizePlanCount") Integer optimizePlanCount, @Param("settingType") String settingType);

    void editCustomerOptimizePlanCount(@Param("optimizePlanCount") Integer optimizePlanCount, @Param("settingType") String settingType, @Param("uuids") List<String> uuids);

    void updateSelectFailReason(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    Integer getMaxInvalidCountByMachineGroup(@Param("machineGroup") String machineGroup);
    
    List<QZRateKeywordCountVO> getQZRateKeywordCount(Page<QZRateKeywordCountVO> page, @Param("qzRateKewordCountCriteria") QZRateKewordCountCriteria qzRateKewordCountCriteria);
    
    Integer getMaxSequence(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    int getQZRateKeywordCountByCriteria(@Param("criteria") QZRateKewordCountCriteria criteria);
    
    List<CustomerKeyword> searchSameCustomerKeywords(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid, @Param("keyword") String keyword, @Param("searchEngine") String searchEngine);
    
    Map<String, Object> getCustomerKeywordStatusCount();

    List<Long> getCustomerKeywordUuidForCapturePositionTemp(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType, @Param("groupName") String groupName, @Param("customerUuid") Long customerUuid, @Param("startTime") Date startTime, @Param("captureStatus") Integer captureStatus, @Param("saveTopThree") Boolean saveTopThree);

    List<CustomerKeywordForCapturePosition> getCustomerKeywordForCapturePositionTemp(@Param("uuids") List uuids);

    void updateCapturePositionQueryTimeAndCaptureStatusTemp(@Param("uuids") List uuids);

    CustomerKeyword getCustomerKeywordFee(@Param("uuid") Long uuid);

    void updatePosition(@Param("uuid") Long uuid, @Param("position") Integer position, @Param("capturePositionQueryTime") Date capturePositionQueryTime, @Param("todayFee") Double todayFee, @Param("ip") String ip, @Param("city") String city);

    void updateCustomerKeywordQueryTime(@Param("customerKeywordUuid") Long customerKeywordUuid, @Param("capturePositionQueryTime") Date capturePositionQueryTime);

    List<CustomerKeyWordCrawlRankVO> getCrawlRankKeywords(@Param("type") String type, @Param("captureStatus") int captureStatus);

    void updateCrawlRankKeywordTimeByUuids(@Param("uuids") List<Long> uuids);
}