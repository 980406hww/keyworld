package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerKeywordCriteria;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.criteria.CustomerKeywordUpdateCriteria;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.NegativeList;
import com.keymanager.monitoring.entity.QZCaptureTitleLog;
import com.keymanager.monitoring.vo.*;
import com.keymanager.value.CustomerKeywordForCaptureTitle;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface
CustomerKeywordDao extends BaseMapper<CustomerKeyword> {
    void cleanSelectedCustomerKeywordTitle(@Param("uuids")List<String> uuids);

    void cleanCustomerTitle(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") String customerUuid);

    void cleanCaptureTitleFlag(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") String customerUuid);

    void cleanCaptureTitleBySelected(@Param("customerKeywordUuids")List<String> customerKeywordUuids);

    int getCustomerKeywordCount(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    List<Map> getCustomerKeywordsCount(@Param("customerUuids") List<Long> customerUuids, @Param("terminalType") String terminalType, @Param
            ("entryType") String entryType);

    int getSimilarCustomerKeywordCount(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid, @Param("keyword") String
            keyword, @Param("originalUrl") String originalUrl);

    Integer getSameCustomerKeywordCount(@Param("terminalType")String terminalType, @Param("customerUuid") long customerUuid, @Param("keyword") String keyword, @Param("url")String url);

    int getMaxSequence(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    List<CustomerKeyword> searchSameCustomerKeywords(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid,
                                                     @Param("keyword") String keyword);

    List<CustomerKeyword> searchCustomerKeywordsForUpdateIndex(@Param("keyword") String keyword);

    CustomerKeyword getCustomerKeywordsForCaptureIndex();

    void updateCaptureIndexQueryTime(@Param("keyword") String keyword);

    List<CustomerKeyword> searchCustomerKeywordsPage(Page<CustomerKeyword> page, @Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    List<CustomerKeyword> searchCustomerKeywordsPageForCustomer(Page<CustomerKeyword> page, @Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    List<CustomerKeyword> searchCustomerKeywordInfo(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    int searchCustomerKeywordsCount(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    List<CustomerKeyword> searchCustomerKeywordsForDailyReport(@Param("customerKeywordCriteria") CustomerKeywordCriteria customerKeywordCriteria);

    void deleteCustomerKeywordsByUuid(@Param("customerKeywordUuids")List<String> customerKeywordUuids);

    //重构部分
    //修改该用户关键字组名
    void updateCustomerKeywordGroupName(@Param("customerKeywordUpdateCriteria")CustomerKeywordUpdateCriteria customerKeywordUpdateCriteria);

    void updateCustomerKeywordSearchEngine(@Param("customerKeywordUpdateCriteria")CustomerKeywordUpdateCriteria customerKeywordUpdateCriteria);

    void updateCustomerKeywordGroupNameByRank(@Param("customerKeywordUuids") List<Long> customerKeywordUuids,@Param("targetGroupName") String targetGroupName);

    List<Long> searchCustomerKeywordUuidByRank(@Param("resultMap")Map<String,Object> resultMap);

    void deleteCustomerKeywordsWhenEmptyTitleAndUrl(@Param("terminalType")String terminalType, @Param("entryType")String entryType, @Param("customerUuid")String customerUuid);

    void deleteCustomerKeywordsWhenEmptyTitle(@Param("terminalType")String terminalType, @Param("entryType")String entryType, @Param("customerUuid")String customerUuid);

    void deleteCustomerKeywordsByCustomerUuid(@Param("customerUuid")Long customerUuid);

    void deleteCustomerKeywords(@Param("terminalType")String terminalType, @Param("groupName")String groupName, @Param("keyword")String keyword);

    List<String> getGroups();

    List<Map> searchRemainingOptimizationCount(@Param("groupName")String groupName, @Param("maxInvalidCount")int maxInvalidCount, @Param("noPositionMaxInvalidCount")int noPositionMaxInvalidCount);

    void cleanBigKeywordIndicator(@Param("groupName")String groupName);

    void setBigKeywordIndicator(@Param("uuids")List<Long> uuids);

    List<String> getEntryTypes(@Param("groupName")String groupName);

    void resetOptimizationInfo();

    Long getCustomerKeywordUuidForOptimization(@Param("terminalType")String terminalType, @Param("groupName")String groupName,
                                                      @Param("maxInvalidCount")int maxInvalidCount, @Param("noPositionMaxInvalidCount") int noPositionMaxInvalidCount, @Param("bigKeyword")boolean bigKeyword);

    CustomerKeyword getCustomerKeywordForOptimization(@Param("uuid")Long uuid);

    void updateOptimizationQueryTime(@Param("customerKeywordUuid")Long customerKeywordUuid);

    void updateOptimizationResult(@Param("customerKeywordUuid")Long customerKeywordUuid, @Param("count")int count);

    List<Map> searchCustomerKeywordsForAdjustingOptimizationCount(@Param("groupNames")List<String> groupNames);

    List<Map> searchPTKeywordsForAdjustingOptimizationCount();

    void adjustOptimizePlanCount(@Param("customerKeywordUuid")Long customerKeywordUuid, @Param("optimizationPlanCount")int optimizationPlanCount,
                                 @Param("queryInterval")int queryInterval);

    Long getCustomerKeywordUuidForCapturePosition(@Param("terminalType")String terminalType, @Param("groupNames")List<String> groupNames,
                                                         @Param("customerUuid")Long customerUuid, @Param("startTime")Date startTime);

    CustomerKeyword getCustomerKeywordForCapturePosition(@Param("uuid")Long uuid);

    void updateCapturePositionQueryTime(@Param("uuid")Long uuid);

    void resetInvalidRefreshCount(@Param("customerKeywordRefreshStatInfoCriteria")CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria);

    void updateCustomerKeywordStatus(@Param("uuids")List<Long> uuids, @Param("status")Integer status);

    void updateCustomerKeywordIndex(@Param("customerKeyword")CustomerKeyword customerKeyword);

    CustomerKeyword searchTitleAndUrl(@Param("groupNames") String[] groupNames,@Param("customerUuid") Long customerUuid);

    List<CodeNameVo> searchGroups();

    SearchEngineResultVO getCustomerKeywordForAutoUpdateNegative(@Param("terminalType")String terminalType, @Param("groupName")String groupName);

    void updateAutoUpdateNegativeTime(@Param("terminalType")String terminalType, @Param("groupName")String groupName, @Param("keyword")String keyword);

    void updateAutoUpdateNegativeTimeAs4MinutesAgo(@Param("terminalType")String terminalType, @Param("groupName")String groupName);

    Long searchCustomerKeywordUuidForCaptureTitle(@Param("qzCaptureTitleLog") QZCaptureTitleLog qzCaptureTitleLog,@Param("searchEngine")String searchEngine);

    CustomerKeywordForCaptureTitle searchCustomerKeywordForCaptureTitle(@Param("uuid") Long uuid);

    void  deleteEmptyTitleCustomerKeyword(@Param("qzCaptureTitleLog")QZCaptureTitleLog qzCaptureTitleLog,@Param("searchEngine")String searchEngine);

    List<String> searchCustomerKeywordSummaryInfo(@Param("entryType")String entryType, @Param("customerUuid")Long customerUuid);

    String[] searchCustomerNegativeKeywords(@Param("customerUuid")long customerUuid);

    void updateOptimizePlanCountForBaiduMap();

    void updatePosition(@Param("uuid")Long uuid, @Param("position")Integer position, @Param("capturePositionQueryTime")Date capturePositionQueryTime, @Param("todayFee") Double todayFee);

    List<OptimizationCountVO> takeOptimizationCountExceptionUsers();

    List<String> fetchOptimizationCompletedGroupNames(@Param("typesStr")String typesStr, @Param("maxInvalidCount")Integer maxInvalidCount);
    List<OptimizationCountVO> observeGroupOptimizationCount(@Param("userID") String userID);

    List<OptimizationCountVO> observeKeywordOptimizationCount(@Param("userID") String userID);

    void editCustomerOptimizePlanCount(@Param("optimizePlanCount")Integer optimizePlanCount, @Param("settingType")String settingType, @Param("uuids")List<String> uuids);

    void editOptimizePlanCountByCustomerUuid(@Param("terminalType")String terminalType, @Param("entryType")String entryType, @Param("customerUuid")Long customerUuid, @Param("optimizePlanCount")Integer optimizePlanCount, @Param("settingType")String settingType);

    void changeCustomerKeywordStatus(@Param("terminalType")String terminalType, @Param("entryType")String entryType, @Param("customerUuid")Long customerUuid, @Param("status")Integer status);

    void batchChangeCustomerKeywordStatus(@Param("entryType")String entryType, @Param("customerUuids")List<Long> customerUuids, @Param("status")Integer status);

    void updateOptimizeGroupName(@Param("customerKeywordCriteria")CustomerKeywordCriteria customerKeywordCriteria);

    void updateOptimizePlanCountForPrice(@Param("uuids") List<Long> uuids);

    int searchCustomerKeywordForNoReachStandard(@Param("customerKeywordCriteria")CustomerKeywordCriteria customerKeywordCriteria);

    void batchUpdateOptimizedCount(@Param("customerKeywordUuids")List<Long> customerKeywordUuids);

    List<NegativeList> getCustomerKeywordSummaryInfos(@Param("terminalType")String terminalType, @Param("keyword")String keyword);

    List<KeywordSimpleVO> getQZCustomerKeywordSummaryInfos(@Param("terminalType")String terminalType, @Param("optimizeGroupName")String optimizeGroupName);

    List<String> searchKeywordUrlByGroup(@Param("terminalType")String terminalType, @Param("entryType")String entryType, @Param("optimizeGroupName")String optimizeGroupName);

    void batchUpdatePosition(@Param("terminalType")String terminalType, @Param("entryType")String entryType, @Param("searchEngine")String searchEngine, @Param("reachStandardPosition")int reachStandardPosition, @Param("positionVOs")List<PositionVO> positionVOs);

    void batchUpdateRequireDalete(@Param("requireDeleteKeywordVOs")List<RequireDeleteKeywordVO> requireDeleteKeywordVOs);
    void updateCustomerKeywordQueryTime(@Param("customerKeywordUuid")Long customerKeywordUuid, @Param("capturePositionQueryTime")Date capturePositionQueryTime);

    void updateKeywordCustomerUuid(@Param("keywordUuids")List<String> keywordUuids,@Param("customerUuid")String customerUuid, @Param("terminalType")String terminalType);

    void moveOutNoRankingCustomerKeyword(@Param("monitoringOptimizeGroupName")String[] monitoringOptimizeGroupName, @Param("noRankingOptimizeGroupName")String noRankingOptimizeGroupName);

    void moveOutMonitoringCustomerKeyword(@Param("defaultOptimizeGroupNameConfigType")String defaultOptimizeGroupNameConfigType, @Param("noRankingOptimizeGroupName")String noRankingOptimizeGroupName);

    List<CustomerKeywordSortVO> sortCustomerKeywordForOptimize(@Param("monitoringOptimizeGroupName")String[] monitoringOptimizeGroupName);

    void setNoRankingCustomerKeyword(@Param("needMoveUuids")List<String> needMoveUuids, @Param("noRankingOptimizeGroupName")String noRankingOptimizeGroupName);
}