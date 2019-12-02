package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.CustomerKeywordCleanTitleCriteria;
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
import com.keymanager.ckadmin.vo.KeywordStandardVO;
import com.keymanager.ckadmin.vo.KeywordStatusBatchUpdateVO;

import com.keymanager.ckadmin.vo.PTkeywordCountVO;
import com.keymanager.ckadmin.vo.QZRateKeywordCountVO;
import com.keymanager.ckadmin.vo.SearchEngineResultVO;
import com.keymanager.value.CustomerKeywordForCapturePosition;
import com.keymanager.ckadmin.vo.MachineGroupQueueVO;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 关键字接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
public interface CustomerKeywordService extends IService<CustomerKeyword> {

    List<MachineGroupQueueVO> getMachineGroupAndSize();

    void cacheCustomerKeywords();

    void updateOptimizationQueryTime(List<Long> customerKeywordUuids);

    List<Map> getCustomerKeywordsCount(List<Long> customerUuids, String terminalType, String entryType);

    void changeCustomerKeywordStatus(String terminalType, String entryType, Long customerUuid, Integer status);

    void deleteCustomerKeywords(long customerUuid);

    int getCustomerKeywordCount(String terminalType, String entryType, long customerUuid);

    void excludeCustomerKeyword(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria);

    void addCustomerKeyword(List<CustomerKeyword> customerKeywords, String userName);

    Integer getCustomerKeywordCountByOptimizeGroupName(String groupName);

    List<CustomerKeywordSummaryInfoVO> searchCustomerKeywordSummaryInfo(String entryType, long customerUuid);

    Map<String, Object>  getCustomerKeywordsCountByCustomerUuid(Long customerUuid, String type);

    void resetInvalidRefreshCount(RefreshStatisticsCriteria criteria);

    Page<CustomerKeyword> searchKeywords(Page<CustomerKeyword> page, KeywordCriteria keywordCriteria);

    void updateCustomerKeywordStatus(List<Long> customerKeywordUuids, Integer status);

    void updateOptimizeGroupName(KeywordCriteria keywordCriteria);

    void updateMachineGroup(KeywordCriteria keywordCriteria);

    void updateBearPawNumber(KeywordCriteria keywordCriteria);

    void updCustomerKeywordFormQz(List<Long> ckUuids, Long qzUuid);

    void deleteCustomerKeywordsByDeleteType(KeywordCriteria keywordCriteria);

    Page<CustomerKeyword> searchCustomerKeywords(Page<CustomerKeyword> page, KeywordCriteria keywordCriteria);

    void saveCustomerKeyword(CustomerKeyword customerKeyword, String userName);

    void updateCustomerKeywordFromUI(CustomerKeyword customerKeyword, String userName);

    void updateOptimizePlanCount(KeywordCriteria keywordCriteria);

    boolean handleExcel(InputStream inputStream, String excelType, long customerUuid, Long qzUuid, String entry, String terminalType, String userName) throws Exception;

    List<CustomerKeyword> searchCustomerKeywordsForDailyReport(KeywordCriteria keywordCriteria);

    List<String> getGroups(List<Long> customerUuids);

    List<Long> getCustomerUuids(String entryType, String terminalType);

    List<CustomerKeyword> searchCustomerKeywordInfo(KeywordCriteria keywordCriteria);

    List<Map> searchAllKeywordAndUrl(Long customerUuid, String terminalType);

    void updateSearchEngine(KeywordCriteria keywordCriteria);

    void changeCustomerKeywordStatusInCKPage(CustomerKeywordUpdateStatusCriteria customerKeywordUpdateStatusCriteria);

    void updCKStatusFromQZ(Map<String, Object> condition);

    void cleanTitle(CustomerKeywordCleanTitleCriteria customerKeywordCleanTitleCriteria);

    void deleteDuplicateKeywords(CustomerKeywordUpdateStatusCriteria customerKeywordUpdateStatusCriteria);

    CustomerKeyword getKeywordInfoByUuid(Long uuid);

    void batchUpdateKeywords(KeywordStatusBatchUpdateVO keywordStatusBatchUpdateVO);

    KeywordStandardVO searchCustomerKeywordForNoReachStandard(KeywordStandardCriteria keywordStandardCriteria);

    List<CodeNameVo> searchGroupsByTerminalType(String terminalType);

    List<GroupVO> getAvailableOptimizationGroups(GroupSettingCriteria groupSettingCriteria);

    Page<PTkeywordCountVO> searchPTKeywordCount(Page<PTkeywordCountVO> page, PTKeywordCountCriteria keywordCriteria);

    void updateKeywordCustomerUuid(List<String> keywordUuids, String customerUuid, String terminalType);

    void updateCustomerUuidByQzUuids(Long customerUuid, List<Long> qzUuids);

    void editOptimizePlanCountByCustomerUuid(String terminalType, String entryType, Long customerUuid, Integer optimizePlanCount, String settingType);

    void editCustomerOptimizePlanCount(Integer optimizePlanCount, String settingType, List<String> uuids);

    void updateSelectFailReason(KeywordCriteria keywordCriteria);

    Integer getMaxInvalidCountByMachineGroup(String machineGroup);

    Page<QZRateKeywordCountVO> getQZRateKeywordCountList(Page<QZRateKeywordCountVO> page, QZRateKewordCountCriteria qzRateKewordCountCriteria) throws CloneNotSupportedException;

    void addCustomerKeywordsFromSimpleUI(List<CustomerKeyword> customerKeywords, String terminalType, String entryType, String userName);

    Map<String, Object> getCustomerKeywordStatusCount(String loginName);

    Map<String, Object> getUseMachineProportion(String username);
    
    List<CustomerKeywordForCapturePosition> getCustomerKeywordForCapturePositionTemp(Long qzSettingUuid, String terminalType, String groupName, Long customerUuid, Date startTime, Long captureRankJobUuid,Boolean saveTopThree);

    void updateCustomerKeywordPosition(Long customerKeywordUuid, int position, Date capturePositionQueryTime, String ip, String city);

    void updateCustomerKeywordQueryTime(Long customerKeywordUuid, Integer capturePositionFailIdentify, Date date);

    void cacheCrawlRankCustomerQZKeywords();

    void cacheCrawlRankCustomerPTKeywords();

    List<CustomerKeyWordCrawlRankVO> getCrawlRankKeyword();

    List<String> getGroupsByUser(String username, String type);

    void addCustomerKeywords(SearchEngineResultVO searchEngineResultVo, String userName);
}
