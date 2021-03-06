package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.*;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.vo.*;
import com.keymanager.ckadmin.entity.Config;
import com.keymanager.monitoring.vo.UpdateOptimizedCountSimpleVO;
import com.keymanager.value.CustomerKeywordForCapturePosition;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("customerKeywordDao2")
public interface CustomerKeywordDao extends BaseMapper<CustomerKeyword> {

    List<Map> getCustomerKeywordsCount(@Param("customerUuids") List<Long> customerUuids, @Param("terminalType") String terminalType, @Param("entryType") String entryType);

    List<String> getMachineGroups();

    void changeCustomerKeywordStatus(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") Long customerUuid, @Param("status") Integer status);

    void deleteCustomerKeywordsByCustomerUuid(long customerUuid);

    void updateOptimizationQueryTime(@Param("customerKeywordUuids") List<Long> customerKeywordUuids);

    int getCustomerKeywordCount(@Param("terminalType") String terminalType, @Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    void excludeCustomerKeyword(@Param("criteria") QZSettingExcludeCustomerKeywordsCriteria criteria);

    void addCustomerKeywords(@Param("customerKeywords") ArrayList<CustomerKeyword> customerKeywords);

    List<OptimizationKeywordVO> fetchCustomerKeywordsForCache(@Param("terminalType") String terminalType, @Param("machineGroup") String machineGroup, @Param("batchCount") int batchCount);

    CustomerKeyword getOneSameCustomerKeyword(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid,
        @Param("qzSettingUuid") Long qzSettingUuid, @Param("keyword") String keyword, @Param("url") String url, @Param("title") String title);

    void updateSameCustomerKeyword(@Param("customerKeyword") CustomerKeyword customerKeyword);

    CustomerKeyword getOneSimilarCustomerKeyword(@Param("terminalType") String terminalType, @Param("customerUuid") long customerUuid,
        @Param("qzSettingUuid") Long qzSettingUuid, @Param("keyword") String keyword, @Param("originalUrl") String originalUrl, @Param("title") String title);

    void updateSimilarCustomerKeyword(@Param("customerKeyword") CustomerKeyword customerKeyword);

    Integer getCustomerKeywordCountByOptimizeGroupName(@Param("groupName") String groupName);

    List<CustomerKeywordSummaryInfoVO> searchCustomerKeywordSummaryInfo(@Param("entryType") String entryType, @Param("customerUuid") long customerUuid);

    List<KeywordCountVO> getCustomerKeywordsCountByCustomerUuid(@Param("customerUuid") Long customerUuid, @Param("type") String type, @Param("invalidMaxDays") Integer invalidMaxDays);

    void resetInvalidRefreshCount(@Param("criteria") RefreshStatisticsCriteria criteria);

    List<CustomerKeyword> searchKeywords(Page<CustomerKeyword> page, @Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void updateCustomerKeywordStatus(@Param("uuids") List<Long> uuids, @Param("status") Integer status);

    int getKeywordCountByKeywordCriteria(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void updateOptimizeGroupName(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void updateMachineGroup(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void updateBearPawNumber(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void updCustomerKeywordFormQz(@Param("ckUuids") List<Long> ckUuids, @Param("qzUuid") Long qzUuid);

    void deleteCustomerKeywordsByUuids(@Param("customerKeywordUuids") List<Long> customerKeywordUuids);

    void deleteCustomerKeywordsWhenEmptyTitle(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void deleteCustomerKeywordsWhenEmptyTitleAndUrl(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    List<CustomerKeyword> searchCustomerKeywords(Page<CustomerKeyword> page, @Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void updateOptimizePlanCount(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    List<CustomerKeyword> searchCustomerKeywordsForDailyReport(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    List<String> getGroups(@Param("customerUuids") List<Long> customerUuids);

    List<Long> getCustomerUuids(@Param("entryType") String entryType, @Param("terminalType") String terminalType);

    List<CustomerKeyword> searchCustomerKeywordInfo(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    List<Map> selectAllKeywordAndUrl(@Param("customerUuid") Long customerUuid, @Param("terminalType") String terminalType, @Param("type") String type);

    void updateSearchEngine(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    void changeCustomerKeywordStatusInCKPage(@Param("customerKeywordUpdateStatusCriteria") CustomerKeywordUpdateStatusCriteria customerKeywordUpdateStatusCriteria);

    void updCKStatusFromQZ(@Param("condition") Map<String, Object> condition);

    void cleanSelectedCustomerKeywordTitle(@Param("uuids") List<Long> uuids);

    void cleanCustomerTitle(@Param("terminalType") String terminalType, @Param("type") String type, @Param("customerUuid") Long customerUuid, @Param("qzUuid") Long qzUuid);

    void cleanCaptureTitleFlag(@Param("terminalType") String terminalType, @Param("type") String type, @Param("customerUuid") Long customerUuid, @Param("qzUuid") Long qzUuid);

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

    Map<String, Object> getCustomerKeywordStatusCount(@Param("loginName") String loginName);

    Map<String, Object> getUseMachineProportion(@Param("username") String username);
    
    List<Long> getCustomerKeywordUuidForCapturePositionTemp(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType, @Param("groupName") String groupName, @Param("customerUuid") Long customerUuid, @Param("startTime") Date startTime, @Param("captureStatus") Integer captureStatus, @Param("saveTopThree") Boolean saveTopThree);    

    List<CustomerKeywordForCapturePosition> getCustomerKeywordForCapturePositionTemp(@Param("uuids") List uuids);

    void updateCapturePositionQueryTimeAndCaptureStatusTemp(@Param("uuids") List uuids);

    CustomerKeyword getCustomerKeywordFee(@Param("uuid") Long uuid);

    void updatePosition(@Param("uuid") Long uuid, @Param("position") Integer position, @Param("existsTimestamp") Integer existsTimestamp, @Param("capturePositionQueryTime") Date capturePositionQueryTime, @Param("todayFee") Double todayFee, @Param("ip") String ip, @Param("city") String city);

    void updateNewPosition(@Param("uuid") Long uuid, @Param("position") Integer position, @Param("existsTimestamp") Integer existsTimestamp, @Param("capturePositionQueryTime") Date capturePositionQueryTime, @Param("todayFee") Double todayFee, @Param("ip") String ip, @Param("city") String city);

    void updateCustomerKeywordQueryTime(@Param("customerKeywordUuid") Long customerKeywordUuid, @Param("capturePositionFailIdentify") Integer capturePositionFailIdentify, @Param("capturePositionQueryTime") Date capturePositionQueryTime);

    List<CustomerKeyWordCrawlRankVO> getCrawlRankKeywords(@Param("type") String type, @Param("captureStatus") int captureStatus);

    void updateCrawlRankKeywordTimeByUuids(@Param("uuids") List<Long> uuids);

    List<String> getGroupsByUser(@Param("username") String username, @Param("type") String type);

    void deleteSysCustomerKeywordByQzId(@Param("uuid") Long uuid);

    void batchUpdateOptimizedCountFromCache(@Param("updateOptimizedCountVOs") Collection<UpdateOptimizedCountSimpleVO> updateOptimizedCountVOs);

    List<CustomerKeywordForCapturePosition> cacheCustomerKeywordForCapturePosition(@Param("terminalType") String terminalType, @Param("groupNames") List<String> groupNames,
                                                                                   @Param("customerUuid") Long customerUuid, @Param("startTime") Date startTime,
                                                                                   @Param("captureStatus") Integer captureStatus, @Param("saveTopThree") Boolean saveTopThree);

    List<CustomerKeywordForCapturePosition> cacheCustomerKeywordForCapturePosition2(@Param("terminalType") String terminalType, @Param("groupName") String groupName,
                                                                                    @Param("customerUuid") Long customerUuid, @Param("startTime") Date startTime,
                                                                                    @Param("captureStatus") Integer captureStatus, @Param("saveTopThree") Boolean saveTopThree);

    List<Long> getCustomerKeywordUuidsForBDExcel(@Param("customerKeyword") CustomerKeyword customerKeyword, @Param("username") String username);

    void batchDownKeywordByExcel(@Param("uuids") List<Long> uuids);

    List<ExternalCustomerKeywordIndexVO> getCustomerKeywordsForCaptureIndex();

    void updateCaptureIndexQueryTimeByKeywords(@Param("customerKeywordUuids") List<Long> customerKeywordUuids);

    void resetOptimizationInfo(@Param("invalidMaxDays") int invalidMaxDays);

    void resetOptimizationInfoForNoOptimizeDate(@Param("invalidMaxDays") int invalidMaxDays);

    void updateCustomerKeywordInvalidDays(@Param("invalidMaxDays") int invalidMaxDays);

    void resetInvalidDays(@Param("keywordCriteria") KeywordCriteria keywordCriteria);

    int getNotResetKeywordCount();

    List<CustomerKeywordRepeatedVO> getRepeatedKeyword(@Param("invalidMaxDays") int invalidMaxDays, @Param("loginNames") List<String> loginNames);

    void updateCustomerKeywordOptimizeStatus(@Param("customerKeywordUuids") List<Long> customerKeywordUuids);

    void moveOutNoRankingCustomerKeyword(@Param("monitorConfigs") List<String> monitorConfigs, @Param("noRankOptimizeGroupNameConfigType") String noRankOptimizeGroupNameConfigType);

    void moveOutDefaultCustomerKeyword(@Param("noRankConfigs") List<Config> noRankConfigs, @Param("defaultOptimizeGroupNameConfigType") String defaultOptimizeGroupNameConfigType);
}