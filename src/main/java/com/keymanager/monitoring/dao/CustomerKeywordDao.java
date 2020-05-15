package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.vo.*;
import com.keymanager.value.CustomerKeywordForCapturePosition;
import com.keymanager.value.CustomerKeywordForCaptureTitle;
import java.util.Collection;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CustomerKeywordDao extends BaseMapper<CustomerKeyword> {
    void cleanSelectedCustomerKeywordTitle(@Param("uuids") List<String> uuids);

    void cleanCustomerTitle(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") String customerUuid);

    void cleanCaptureTitleFlag(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") String customerUuid);

    void cleanCaptureTitleBySelected(@Param("customerKeywordUuids") List<String> customerKeywordUuids);

    int getCustomerKeywordCount(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    List<Map> getCustomerKeywordsCount(@Param("customerUuids") List<Long> customerUuids, @Param("terminalType") String terminalType, @Param
            ("entryType") String entryType);

    int getSimilarCustomerKeywordCount(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid, @Param("keyword") String
            keyword, @Param("originalUrl") String originalUrl, @Param("title") String title);

    Integer getSameCustomerKeywordCount(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid, @Param("keyword") String keyword, @Param("url") String url, @Param("title") String title);

    int getMaxSequence(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    List<CustomerKeyword> searchSameCustomerKeywords(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid,
                                                     @Param("keyword") String keyword, @Param("searchEngine") String searchEngine);

    List<CustomerKeyword> searchCustomerKeywordsForUpdateIndex(@Param("keyword") String keyword);

    CustomerKeyword getCustomerKeywordsForCaptureIndex();

    void updateCaptureIndexQueryTime(@Param("keyword") String keyword);

    List<CustomerKeyword> searchCustomerKeywordsPage(Page<CustomerKeyword> page, @Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    List<CustomerKeyword> searchCustomerKeywordsPageForCustomer(Page<CustomerKeyword> page, @Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    List<CustomerKeyword> searchCustomerKeywordInfo(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    int searchCustomerKeywordsCount(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    List<CustomerKeyword> searchCustomerKeywordsForDailyReport(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    void deleteCustomerKeywordsByUuid(@Param("customerKeywordUuids") List<String> customerKeywordUuids);

    List<Map> selectAllKeywordAndUrl(@Param("customerUuid") Long customerUuid, @Param("terminalType") String terminalType);

    //重构部分
    //修改该用户关键字组名
    void updateCustomerKeywordGroupName(@Param("customerKeywordUpdateCriteria") CustomerKeywordUpdateCriteria customerKeywordUpdateCriteria);

    void updateCustomerKeywordSearchEngine(@Param("customerKeywordUpdateCriteria") CustomerKeywordUpdateCriteria customerKeywordUpdateCriteria);

    void updateCustomerKeywordGroupNameByRank(@Param("customerKeywordUuids") List<Long> customerKeywordUuids, @Param("targetGroupName") String targetGroupName);

    List<Long> searchCustomerKeywordUuidByRank(@Param("resultMap") Map<String, Object> resultMap);

    void deleteCustomerKeywordsWhenEmptyTitleAndUrl(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") String customerUuid);

    void deleteCustomerKeywordsWhenEmptyTitle(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") String customerUuid);

    void deleteCustomerKeywordsByCustomerUuid(@Param("customerUuid") Long customerUuid);

    void deleteCustomerKeywords(@Param("terminalType") String terminalType, @Param("groupName") String groupName, @Param("keyword") String keyword);

    List<String> getGroups(@Param("customerUuids") List<Long> customerUuids);

    List<Map> searchRemainingOptimizationCount(@Param("groupName") String groupName, @Param("maxInvalidCount") int maxInvalidCount, @Param("noPositionMaxInvalidCount") int noPositionMaxInvalidCount);

    void cleanBigKeywordIndicator(@Param("groupName") String groupName);

    void setBigKeywordIndicator(@Param("uuids") List<Long> uuids);

    List<String> getEntryTypes(@Param("groupName") String groupName);

    void resetOptimizationInfo();

    void resetOptimizationInfoForNoOptimizeDate();

    Long getCustomerKeywordUuidForOptimization(@Param("terminalType") String terminalType, @Param("groupName") String groupName, @Param("bigKeyword") boolean bigKeyword);

    CustomerKeyword getCustomerKeywordForOptimization(@Param("uuid") Long uuid);

    void updateOptimizationQueryTime(@Param("customerKeywordUuids") List<Long> customerKeywordUuids);

    void updateOptimizationQueryTimeSingle(@Param("customerKeywordUuid") Long customerKeywordUuid, @Param("maxInvalidCount") int maxInvalidCount);

    void updateOptimizationResult(@Param("customerKeywordUuid") Long customerKeywordUuid, @Param("count") int count);

    List<Map> searchCustomerKeywordsForAdjustingOptimizationCount(@Param("groupNames") List<String> groupNames);

    List<Map> searchKeywordsForAdjustingOptimizationCount(@Param("type") String type);

    void adjustOptimizePlanCount(@Param("customerKeywordUuid") Long customerKeywordUuid, @Param("queryInterval") int queryInterval, @Param("optimizeTodayCount") int optimizeTodayCount);

    List<CustomerKeywordForCapturePosition> cacheCustomerKeywordForCapturePosition(@Param("terminalType") String terminalType, @Param("groupNames") List<String> groupNames,
                                                  @Param("customerUuid") Long customerUuid, @Param("startTime") Date startTime,
                                                  @Param("captureStatus") Integer captureStatus);

    List<Long> getCustomerKeywordUuidForCapturePositionTemp(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType,
                                                            @Param("groupName") String groupName, @Param("customerUuid") Long customerUuid,
                                                            @Param("startTime") Date startTime, @Param("captureStatus") Integer captureStatus, @Param("saveTopThree") Boolean saveTopThree);

    CustomerKeyword getCustomerKeywordForCapturePosition(@Param("uuid") Long uuid);

    List<CustomerKeywordForCapturePosition> getCustomerKeywordForCapturePositionTemp(@Param("uuids") List uuids);

    void updateCapturePositionQueryTimeAndCaptureStatus(@Param("uuid") Long uuid);

    void updateCapturePositionQueryTimeAndCaptureStatusTemp(@Param("uuids") List uuids);

    void resetInvalidRefreshCount(@Param("customerKeywordRefreshStatInfoCriteria") CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria);

    void updateCustomerKeywordStatus(@Param("uuids") List<Long> uuids, @Param("status") Integer status);

    void updateCustomerKeywordIndex(@Param("customerKeyword") CustomerKeyword customerKeyword);

    CustomerKeyword searchTitleAndUrl(@Param("groupNames") String[] groupNames, @Param("customerUuid") Long customerUuid);

    List<CodeNameVo> searchGroups();

    List<CodeNameVo> searchGroupsByTerminalType(@Param("terminalType") String terminalType);

    SearchEngineResultVO getCustomerKeywordForAutoUpdateNegative(@Param("terminalType") String terminalType, @Param("groupName") String groupName);

    void updateAutoUpdateNegativeTime(@Param("terminalType") String terminalType, @Param("groupName") String groupName, @Param("keyword") String keyword);

    void updateAutoUpdateNegativeTimeAs4MinutesAgo(@Param("terminalType") String terminalType, @Param("groupName") String groupName);

    List<Long> searchCustomerKeywordsUuidForCaptureTitle(@Param("qzCaptureTitleLog") QZCaptureTitleLog qzCaptureTitleLog, @Param("searchEngine") String searchEngine, @Param("batchCount") Integer batchCount);

    CustomerKeywordForCaptureTitle searchCustomerKeywordForCaptureTitle(@Param("uuid") Long uuid);

    void deleteEmptyTitleCustomerKeyword(@Param("qzCaptureTitleLog") QZCaptureTitleLog qzCaptureTitleLog, @Param("searchEngine") String searchEngine);

    List<CustomerKeywordSummaryInfoVO> searchCustomerKeywordSummaryInfo(@Param("entryType") String entryType, @Param("customerUuid") Long customerUuid);

    String[] searchCustomerNegativeKeywords(@Param("customerUuid") long customerUuid);

    void updateOptimizePlanCountForBaiduMap();

    void updatePosition(@Param("uuid") Long uuid, @Param("position") Integer position, @Param("capturePositionQueryTime") Date capturePositionQueryTime, @Param("todayFee") Double todayFee,
                        @Param("ip") String ip, @Param("city") String city);

    List<OptimizationCountVO> takeOptimizationCountExceptionUsers();

    List<String> fetchOptimizationCompletedGroupNames(@Param("typesStr") String typesStr, @Param("maxInvalidCount") Integer maxInvalidCount);

    List<OptimizationCountVO> observeGroupOptimizationCount(@Param("userID") String userID);

    List<OptimizationCountVO> observeKeywordOptimizationCount(@Param("userID") String userID);

    void editCustomerOptimizePlanCount(@Param("optimizePlanCount") Integer optimizePlanCount, @Param("settingType") String settingType, @Param("uuids") List<String> uuids);

    void editOptimizePlanCountByCustomerUuid(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") Long customerUuid, @Param("optimizePlanCount") Integer optimizePlanCount, @Param("settingType") String settingType);

    void changeCustomerKeywordStatus(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") Long customerUuid, @Param("status") Integer status);

    void batchChangeCustomerKeywordStatus(@Param("entryType") String entryType, @Param("customerUuids") List<Long> customerUuids, @Param("status") Integer status);

    void updateOptimizeGroupName(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    void updateBearPawNumber(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    void updateOptimizePlanCountForPrice(@Param("uuids") List<Long> uuids);

    int searchCustomerKeywordForNoReachStandard(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    void batchUpdateOptimizedCount(@Param("customerKeywordUuids") List<Long> customerKeywordUuids);

    List<NegativeList> getCustomerKeywordSummaryInfos(@Param("terminalType") String terminalType, @Param("keyword") String keyword);

    List<KeywordSimpleVO> getQZCustomerKeywordSummaryInfos(@Param("terminalType") String terminalType, @Param("optimizeGroupName") String optimizeGroupName);

    List<String> searchKeywordUrlByGroup(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("optimizeGroupName") String optimizeGroupName);

    void batchUpdatePosition(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("searchEngine") String searchEngine, @Param("reachStandardPosition") int reachStandardPosition, @Param("positionVOs") List<PositionVO> positionVOs);

    String getBearPawNumberByCustomerUuid(@Param("customerUuid") int customerUuid, @Param("entryType") String entryType, @Param("terminalType") String terminalType);

    void batchUpdateRequireDalete(@Param("requireDeleteKeywordVOs") List<RequireDeleteKeywordVO> requireDeleteKeywordVOs);

    void updateCustomerKeywordQueryTime(@Param("customerKeywordUuid") Long customerKeywordUuid, @Param("capturePositionQueryTime") Date capturePositionQueryTime);

    void updateKeywordCustomerUuid(@Param("keywordUuids") List<String> keywordUuids, @Param("customerUuid") String customerUuid, @Param("terminalType") String terminalType);

    List<String> getCustomerKeywordInfo(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    void moveOutNoRankingCustomerKeyword(@Param("monitorConfigs") List<String> monitorConfigs, @Param("noRankOptimizeGroupNameConfigType") String noRankOptimizeGroupNameConfigType);

    void moveOutDefaultCustomerKeyword(@Param("noRankConfigs") List<Config> noRankConfigs, @Param("defaultOptimizeGroupNameConfigType") String defaultOptimizeGroupNameConfigType);

    List<CustomerKeywordSortVO> sortCustomerKeywordForOptimize(@Param("monitorConfigs") List<String> monitorConfigs);

    void setNoRankingCustomerKeyword(@Param("needMoveUuids") List<String> needMoveUuids, @Param("noRankOptimizeGroupNameConfigType") String noRankOptimizeGroupNameConfigType);

    void updateCustomerKeywordCt(@Param("customerKeywordUuid") Long customerKeywordUuid, @Param("ct") String ct);

    void updateCustomerKeywordFromSource(@Param("customerKeywordUuid") Long customerKeywordUuid, @Param("fromSource") String fromSource);

    List<CustomerKeyword> searchCustomerKeywordsPageForCustomer(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    //客户关键字批量设�
    void batchUpdateKeywordStatus(@Param("keywordIDs") String[] keywordIDs, @Param("keywordChecks") CustomerKeyword keywordChecks, @Param("keywordStatus") CustomerKeyword keywordStatus);

    void updateCustomerKeywordsTitle(@Param("searchEngineResultItemVOs") List<SearchEngineResultItemVO> searchEngineResultItemVOs);

    List<String> searchDuplicateKeywords(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    CustomerKeyword getCustomerKeywordFee(@Param("uuid") Long uuid);

    List<Long> getCustomerUuids(@Param("entryType") String entryType, @Param("terminalType") String terminalType);

    void excludeCustomerKeyword(@Param("qzSettingExcludeCustomerKeywordsCriteria") QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria);

    List<CustomerKeywordEnteredVO> getCheckEnteredKeywords();

    void updateCheckingEnteredKeywords(@Param("customerKeywords") List<CustomerKeyword> customerKeywords);

    void updateVerifyEnteredKeywordTimeByUuids(@Param("uuids") List<Long> uuids);

    void addCustomerKeywords(@Param("customerKeywords") List<CustomerKeyword> customerKeywords);

    List<String> getAvailableOptimizationGroups(@Param("groupSettingCriteria") GroupSettingCriteria groupSettingCriteria);

    void updateSameCustomerKeywordSource(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid, @Param("keyword") String keyword, @Param("url") String url, @Param("title") String title, @Param("customerKeywordSource") String customerKeywordSource);

    void updateSimilarCustomerKeywordSource(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid, @Param("keyword") String keyword, @Param("originalUrl") String originalUrl, @Param("title") String title, @Param("customerKeywordSource") String customerKeywordSource);

    void updateMachineGroup(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    List<String> getMachineGroups();

    List<OptimizationKeywordVO> fetchCustomerKeywordsForCache(@Param("terminalType") String terminalType, @Param("machineGroup") String machineGroup, @Param("batchCount") int batchCount);

    CustomerKeywordRankingCountVO getCustomerKeywordRankingCount(@Param("terminalType") String terminalType,@Param("customerUuid") int customerUuid, @Param("qzSettingUuid") Long qzSettingUuid);

    List<CustomerKeyWordCrawlRankVO> getCrawlRankKeywords(@Param("type") String type, @Param("captureStatus") int captureStatus);

    void updateCrawlRankKeywordTimeByUuids(@Param("uuids") List<Long> uuids);

    int getQZSettingKeywordCount(@Param("terminalType") String terminalType,@Param("customerUuid") int customerUuid, @Param("qzSettingUuid") Long qzSettingUuid);

    List<keywordAmountCountVo> searchKeywordAmountCountList(Page<keywordAmountCountVo> customerKeywordPage, @Param("keywordAmountCountCriteria") KeywordAmountCountCriteria keywordAmountCountCriteria);

    List<ExternalCustomerKeywordVO> getTenCustomerKeywordsForCaptureIndex();

    void updateCaptureIndexQueryTimeByKeywords(@Param("customerKeywords") List<ExternalCustomerKeywordVO> customerKeywords);

    void batchUpdateIndexAndOptimizePlanCount(@Param("customerKeywords") List<CustomerKeyword> customerKeywords);

    void batchUpdateOptimizedCountFromCache(@Param("updateOptimizedCountVOs") Collection<UpdateOptimizedCountSimpleVO> updateOptimizedCountVOs);

    /**
     * 批量插入数据
     */
    void batchInsertCustomerKeywordByCustomerUuid(@Param("customerUuid") Long customerUuid, @Param("qsId") Long qsId);

    /**
     * 根据qsId判断站点关键词当天是否已同步
     */
    Long searchExistingSysCustomerKeywordQsId(@Param("qsId") Long qsId);
}