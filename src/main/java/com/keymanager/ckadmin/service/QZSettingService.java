package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.ExternalQZSettingCriteria;
import com.keymanager.ckadmin.criteria.GroupSettingCriteria;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.criteria.QZSettingSaveCustomerKeywordsCriteria;
import com.keymanager.ckadmin.criteria.QZSettingSearchCriteria;
import com.keymanager.ckadmin.entity.CustomerExcludeKeyword;
import com.keymanager.ckadmin.entity.QZCategoryTag;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.vo.ExternalQZSettingVO;
import com.keymanager.ckadmin.vo.GroupVO;
import com.keymanager.ckadmin.vo.QZSearchEngineVO;
import com.keymanager.ckadmin.vo.QZSettingCountVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新全站 服务类
 * </p>
 *
 * @author lhc
 * @since 2019-09-05
 */
public interface QZSettingService extends IService<QZSetting> {

    Page<QZSetting> searchQZSetting(Page<QZSetting> page, QZSettingSearchCriteria qzSettingCriteria);

    List<QZSearchEngineVO> searchQZSettingSearchEngineMap(QZSettingCriteria criteria, Integer record);

    Map<String, Object> getQZKeywordRankInfo(long uuid, String terminalType, String optimizeGroupName, Long customerUuid);

    CustomerExcludeKeyword echoExcludeKeyword(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria);

    void excludeQZSettingCustomerKeywords(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria);

    void saveQZSettingCustomerKeywords(QZSettingSaveCustomerKeywordsCriteria qzSettingSaveCustomerKeywordsCriteria, String userName);

    Long saveQZSetting(QZSetting qzSetting, String userName);

    void deleteOne(Long uuid);

    void deleteAll(List<Integer> uuids);

    QZSetting getQZSetting(Long uuid);

    void batchUpdateQZSettingUpdateStatus(String uuids);

    void updateQzCategoryTags(List<String> uuids, List<QZCategoryTag> targetQZCategoryTags, String userName);

    void batchUpdateRenewalStatus(String uuids, int renewalStatus);

    void updRenewalStatus(Long uuid, int renewalStatus);

    Map<String, String> getPCPhoneGroupByUuid(Long uuid);

    Map<String, Object> getQZSettingForAutoOperate();

    void updateQZSettingKeywords(ExternalQZSettingCriteria qzSettingCriteria);

    List<ExternalQZSettingVO> getQZSettingTask(int crawlerHour, int taskNumber);

    void updateCrawlerStatus(List<Long> uuids);

    void updateQzSetting(QZSetting qzSetting);

    QZSettingCountVO getQZSettingsCountByCustomerUuid(Long customerUuid);

    String findQZCustomer(String domain);

    List<GroupVO> getAvailableOptimizationGroups(GroupSettingCriteria groupSettingCriteria);

    void updateQZSettingFrom(Long customerUuid, List<Long> uuids);
}
