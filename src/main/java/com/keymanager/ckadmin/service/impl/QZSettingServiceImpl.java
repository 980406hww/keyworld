package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.criteria.QZSettingSaveCustomerKeywordsCriteria;
import com.keymanager.ckadmin.dao.QZSettingDao;
import com.keymanager.ckadmin.entity.CaptureRankJob;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.CustomerExcludeKeyword;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.entity.QZCategoryTag;
import com.keymanager.ckadmin.entity.QZChargeRule;
import com.keymanager.ckadmin.entity.QZKeywordRankInfo;
import com.keymanager.ckadmin.entity.QZOperationType;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.enums.CustomerKeywordSourceEnum;
import com.keymanager.ckadmin.enums.TerminalTypeEnum;
import com.keymanager.ckadmin.service.*;
import com.keymanager.ckadmin.vo.QZKeywordRankInfoVO;
import com.keymanager.ckadmin.vo.QZSearchEngineVO;
import com.keymanager.ckadmin.vo.QZSettingVO;
import com.keymanager.enums.CollectMethod;
import com.keymanager.util.Constants;
import com.keymanager.util.common.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 新全站表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-09-05
 */
@Service("qzSettingService2")
public class QZSettingServiceImpl extends
    ServiceImpl<QZSettingDao, QZSetting> implements QZSettingService {

    @Resource(name = "qzSettingDao2")
    private QZSettingDao qzSettingDao;

    @Resource(name = "qzKeywordRankInfoService2")
    private QZKeywordRankInfoService qzKeywordRankInfoService;

    @Resource(name = "qzOperationTypeService2")
    private QZOperationTypeService qzOperationTypeService;

    @Resource(name = "qzCategoryTagService2")
    private QZCategoryTagService qzCategoryTagService;

    @Resource(name = "operationCombineService2")
    private OperationCombineService operationCombineService;

    @Resource(name = "customerExcludeKeywordService2")
    private CustomerExcludeKeywordService customerExcludeKeywordService;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Resource(name = "qzChargeRuleService2")
    private QZChargeRuleService qzChargeRuleService;

    @Resource(name = "captureRankJobService2")
    private CaptureRankJobService captureRankJobService;

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @Resource(name = "configService2")
    private ConfigService configService;

    @Resource(name = "groupService2")
    private GroupService groupService;

    @Override
    public Page<QZSetting> searchQZSetting(Page<QZSetting> page,
        QZSettingCriteria qzSettingCriteria) {
        page.setRecords(qzSettingDao.searchQZSettings(page, qzSettingCriteria));
        return page;
    }

    @Override
    public List<QZSearchEngineVO> searchQZSettingSearchEngineMap(QZSettingCriteria criteria,
        Integer record) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(Constants.ALL_SEARCH_ENGINE, TerminalTypeEnum.PC.name());
        map.put(Constants.ALL_SEARCH_ENGINE + TerminalTypeEnum.Phone.name(),
            TerminalTypeEnum.Phone.name());
        boolean displayExistTabFlag =
            (!"".equals(criteria.getDomain()) || !"".equals(criteria.getCustomerInfo()))
                && record == 1;
        if (displayExistTabFlag) {
            List<QZSettingVO> qzSettingVos = qzSettingDao
                .searchQZSettingSearchEngines(criteria.getCustomerInfo(), criteria.getDomain());
            for (QZSettingVO qzSettingVo : qzSettingVos) {
                if (null != qzSettingVo.getPcGroup()) {
                    map.put(qzSettingVo.getSearchEngine(), TerminalTypeEnum.PC.name());
                }
                if (null != qzSettingVo.getPhoneGroup()) {
                    map.put(qzSettingVo.getSearchEngine() + TerminalTypeEnum.Phone.name(),
                        TerminalTypeEnum.Phone.name());
                }
            }
        } else {
            map.putAll(Constants.SEARCH_ENGINE_MAP);
        }
        if (!map.containsKey(criteria.getSearchEngine()) && !map
            .containsKey(criteria.getSearchEngine() + criteria.getTerminalType())) {
            Map.Entry<String, String> next = map.entrySet().iterator().next();
            if ("".equals(criteria.getSearchEngine()) && TerminalTypeEnum.Phone.name()
                .equals(criteria.getTerminalType())) {
                criteria.setSearchEngine(Constants.ALL_SEARCH_ENGINE);
                criteria.setTerminalType(TerminalTypeEnum.Phone.name());
            } else {
                criteria.setSearchEngine(next.getKey().substring(0,
                    next.getKey().contains("P") ? next.getKey().indexOf('P')
                        : next.getKey().length()));
                criteria.setTerminalType(next.getValue());
            }
        }
        List<QZSearchEngineVO> qzSearchEngineVOS = new LinkedList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            QZSearchEngineVO qzSearchEngineVO = new QZSearchEngineVO();
            qzSearchEngineVO.setSearchEngineName(entry.getKey());
            qzSearchEngineVO.setTerminalType(entry.getValue());
            qzSearchEngineVOS.add(qzSearchEngineVO);
        }
        return qzSearchEngineVOS;
    }

    @Override
    public Map<String, Object> getQZKeywordRankInfo(long uuid, String terminalType,
        String optimizeGroupName) {
        List<QZKeywordRankInfo> qzKeywordRankInfos = qzKeywordRankInfoService
            .searchExistingQZKeywordRankInfo(uuid, terminalType, null);
        Map<String, Object> rankInfoVoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(qzKeywordRankInfos)) {
            int price = 0;
            for (QZKeywordRankInfo qzKeywordRankInfo : qzKeywordRankInfos) {
                QZKeywordRankInfoVO qzKeywordRankInfoVo = new QZKeywordRankInfoVO();
                initQZKeywordRankInfoVo(qzKeywordRankInfo, qzKeywordRankInfoVo);
                price += qzKeywordRankInfo.getCurrentPrice() == null ? 0
                    : qzKeywordRankInfo.getCurrentPrice();
                rankInfoVoMap.put(qzKeywordRankInfo.getWebsiteType(), qzKeywordRankInfoVo);
            }
            rankInfoVoMap.put("price", price);
            getQZSettingGroupInfo(rankInfoVoMap, uuid, terminalType, optimizeGroupName);
        }
        return rankInfoVoMap;
    }

    private void initQZKeywordRankInfoVo(QZKeywordRankInfo qzKeywordRankInfo,
        QZKeywordRankInfoVO rankInfoVo) {
        rankInfoVo.setUuid(qzKeywordRankInfo.getUuid());
        rankInfoVo.setQzSettingUuid(qzKeywordRankInfo.getQzSettingUuid());
        rankInfoVo.setTerminalType(qzKeywordRankInfo.getTerminalType());
        rankInfoVo.setWebsiteType(qzKeywordRankInfo.getWebsiteType());
        rankInfoVo.setDataProcessingStatus(qzKeywordRankInfo.getDataProcessingStatus());
        String[] topTen = calculate(qzKeywordRankInfo.getTopTen());
        String[] topFifty = calculate(qzKeywordRankInfo.getTopFifty());
        if (null != topTen) {
            rankInfoVo.setTopTenNum(Integer.parseInt(topTen[topTen.length - 1]));
            rankInfoVo.setTopFiftyNum(Integer.parseInt(topFifty[topFifty.length - 1]));
        } else {
            rankInfoVo.setTopTenNum(0);
            rankInfoVo.setTopFiftyNum(0);
        }
        rankInfoVo.setCreateTopTenNum(qzKeywordRankInfo.getCreateTopTenNum() == null ? 0
            : qzKeywordRankInfo.getCreateTopTenNum());
        rankInfoVo.setCreateTopFiftyNum(qzKeywordRankInfo.getCreateTopFiftyNum() == null ? 0
            : qzKeywordRankInfo.getCreateTopFiftyNum());
        rankInfoVo.setTopTen(topTen);
        rankInfoVo.setTopTwenty(calculate(qzKeywordRankInfo.getTopTwenty()));
        rankInfoVo.setTopThirty(calculate(qzKeywordRankInfo.getTopThirty()));
        rankInfoVo.setTopForty(calculate(qzKeywordRankInfo.getTopForty()));
        rankInfoVo.setTopFifty(topFifty);
        rankInfoVo.setTopHundred(calculate(qzKeywordRankInfo.getTopHundred()));
        rankInfoVo.setDate(calculate(qzKeywordRankInfo.getDate()));
        rankInfoVo.setBaiduRecord(calculate(qzKeywordRankInfo.getBaiduRecord()));
        rankInfoVo.setBaiduRecordFullDate(calculate(qzKeywordRankInfo.getBaiduRecordFullDate()));
        rankInfoVo.setAchieveTime(qzKeywordRankInfo.getAchieveTime());
    }

    public String[] calculate(String targetStr) {
        String[] split = null;
        if (StringUtil.isNotNullNorEmpty(targetStr)) {
            split = targetStr.replace("[", "").replace("]", "").replaceAll("'", "").split(", ");
            Collections.reverse(Arrays.asList(split));
        }
        return split;
    }

    private void getQZSettingGroupInfo(Map<String, Object> rankInfoVoMap, long uuid,
        String terminalType, String optimizeGroupName) {
        rankInfoVoMap.put("customerKeywordCount",
            qzSettingDao.getQZSettingGroupInfo(terminalType, optimizeGroupName));
        rankInfoVoMap.put("operationCombineName",
            operationCombineService.getOperationCombineName(optimizeGroupName));
        rankInfoVoMap.put("categoryTagNames", qzCategoryTagService.findTagNames(uuid));
        rankInfoVoMap
            .put("standardTime", qzOperationTypeService.getStandardTime(uuid, terminalType));
    }

    @Override
    public CustomerExcludeKeyword echoExcludeKeyword(
        QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        return customerExcludeKeywordService
            .echoExcludeKeyword(qzSettingExcludeCustomerKeywordsCriteria);
    }

    @Override
    public void excludeQZSettingCustomerKeywords(
        QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        qzSettingExcludeCustomerKeywordsCriteria.setDomain(
            qzSettingExcludeCustomerKeywordsCriteria.getDomain().replace("http://", "")
                .replace("https://", "").replace("www.", "").split("/")[0]);
        customerKeywordService.excludeCustomerKeyword(qzSettingExcludeCustomerKeywordsCriteria);
        customerExcludeKeywordService
            .excludeCustomerKeywords(qzSettingExcludeCustomerKeywordsCriteria);
    }

    @Override
    public void saveQZSettingCustomerKeywords(
        QZSettingSaveCustomerKeywordsCriteria qzSettingSaveCustomerKeywordsCriteria,
        String userName) {
        for (String terminalType : qzSettingSaveCustomerKeywordsCriteria.getTerminalTypes()) {
            List<CustomerKeyword> customerKeywords = new ArrayList<>();
            String customerExcludeKeywords = customerExcludeKeywordService
                .getCustomerExcludeKeyword(qzSettingSaveCustomerKeywordsCriteria.getCustomerUuid(),
                    qzSettingSaveCustomerKeywordsCriteria.getQzSettingUuid(), terminalType,
                    qzSettingSaveCustomerKeywordsCriteria.getDomain());
            Set<String> excludeKeyword = new HashSet<>();
            if (null != customerExcludeKeywords) {
                excludeKeyword.addAll(Arrays.asList(customerExcludeKeywords.split(",")));
            }
            for (String keyword : qzSettingSaveCustomerKeywordsCriteria.getKeywords()) {
                CustomerKeyword customerKeyword = new CustomerKeyword();
                customerKeyword
                    .setQzSettingUuid(qzSettingSaveCustomerKeywordsCriteria.getQzSettingUuid());
                customerKeyword
                    .setCustomerUuid(qzSettingSaveCustomerKeywordsCriteria.getCustomerUuid());
                customerKeyword.setType(qzSettingSaveCustomerKeywordsCriteria.getType());
                customerKeyword
                    .setSearchEngine(qzSettingSaveCustomerKeywordsCriteria.getSearchEngine());
                customerKeyword.setTerminalType(terminalType);
                customerKeyword.setUrl(qzSettingSaveCustomerKeywordsCriteria.getDomain());
                customerKeyword
                    .setKeywordEffect(qzSettingSaveCustomerKeywordsCriteria.getKeywordEffect());
                customerKeyword.setServiceProvider("baidutop123");
                customerKeyword.setManualCleanTitle(true);
                customerKeyword.setCollectMethod(CollectMethod.PerMonth.name());
                customerKeyword.setCurrentIndexCount(-1);
                customerKeyword.setPositionFirstFee(-1d);
                customerKeyword
                    .setBearPawNumber(qzSettingSaveCustomerKeywordsCriteria.getBearPawNumber());
                customerKeyword.setCustomerKeywordSource(CustomerKeywordSourceEnum.Specify.name());
                customerKeyword.setOptimizeGroupName(
                    qzSettingSaveCustomerKeywordsCriteria.getOptimizeGroupName());
                customerKeyword
                    .setMachineGroup(qzSettingSaveCustomerKeywordsCriteria.getMachineGroupName());
                if (!excludeKeyword.isEmpty()) {
                    if (excludeKeyword.contains(keyword)) {
                        customerKeyword.setStatus(0);
                    }
                }
                customerKeyword.setKeyword(keyword);
                if ("Important".equals(qzSettingSaveCustomerKeywordsCriteria.getKeywordEffect())) {
                    Integer optimizePlanCount = Integer.valueOf(configService
                        .getConfig("KeywordEffectOptimizePlanCount", "ImportantKeyword")
                        .getValue());
                    customerKeyword.setOptimizePlanCount(optimizePlanCount);
                } else {
                    customerKeyword.setOptimizePlanCount(10);
                }
                customerKeywords.add(customerKeyword);
            }
            customerKeywordService.addCustomerKeyword(customerKeywords, userName);
        }
    }

    @Override
    public Long saveQZSetting(QZSetting qzSetting, String userName) {
        Long qzSettingUuid = null;
        if (qzSetting.getUuid() != null) {
            //修改qzSetting表
            QZSetting existingQZSetting = qzSettingDao.selectById(qzSetting.getUuid());
            existingQZSetting.setDomain(qzSetting.getDomain());
            existingQZSetting.setSearchEngine(qzSetting.getSearchEngine());
            existingQZSetting.setBearPawNumber(qzSetting.getBearPawNumber());
            existingQZSetting.setUpdateStatus(qzSetting.getUpdateStatus());
            existingQZSetting.setCustomerUuid(qzSetting.getCustomerUuid());
            existingQZSetting.setPcGroup(qzSetting.getPcGroup());
            existingQZSetting.setPhoneGroup(qzSetting.getPhoneGroup());
            existingQZSetting.setType(qzSetting.getType());
            existingQZSetting.setAutoCrawlKeywordFlag(qzSetting.getAutoCrawlKeywordFlag());
            existingQZSetting.setIgnoreNoIndex(qzSetting.getIgnoreNoIndex());
            existingQZSetting.setIgnoreNoOrder(qzSetting.getIgnoreNoOrder());
            existingQZSetting.setUpdateInterval(qzSetting.getUpdateInterval());
            existingQZSetting.setUpdateTime(new Date());
            existingQZSetting
                .setCaptureCurrentKeywordCountTime(qzSetting.getCaptureCurrentKeywordCountTime());
            existingQZSetting
                .setCaptureCurrentKeywordStatus(qzSetting.getCaptureCurrentKeywordStatus());
            existingQZSetting.setCrawlerStatus(Constants.QZ_SETTING_CRAWLER_STATUS_NEW);
            existingQZSetting.setCaptureTerminalType(qzSetting.getCaptureTerminalType());

            //修改部分
            List<QZOperationType> oldOperationTypes = qzOperationTypeService
                .searchQZOperationTypesIsDelete(qzSetting.getUuid());
            List<QZOperationType> updOperationTypes = qzSetting.getQzOperationTypes();
            updateOperationTypeAndChargeRule(oldOperationTypes, updOperationTypes,
                qzSetting.getUuid(), qzSetting.getCustomerUuid(), userName);
            List<QZKeywordRankInfo> existingQZKeywordRankInfos = qzKeywordRankInfoService
                .searchExistingQZKeywordRankInfo(qzSetting.getUuid(), null, null);
            updateQZKeywordRankInfo(existingQZKeywordRankInfos, updOperationTypes,
                existingQZSetting);
            // 修改标签
            List<QZCategoryTag> existingQZCategoryTags = qzCategoryTagService
                .searchCategoryTagByQZSettingUuid(qzSetting.getUuid());
            List<QZCategoryTag> updateQZCategoryTags = qzSetting.getQzCategoryTags();
            qzCategoryTagService.updateQZCategoryTag(existingQZCategoryTags, updateQZCategoryTags,
                qzSetting.getUuid());
            qzSettingDao.updateById(existingQZSetting);
        } else {
            qzSetting.setUpdateTime(new Date());
            qzSettingDao.insert(qzSetting);
            qzSettingUuid = qzSetting.getUuid();
            // 插入qzSetting是的uuid
            for (QZOperationType qzOperationType : qzSetting.getQzOperationTypes()) {
                qzOperationType.setQzSettingUuid(qzSettingUuid);
                qzOperationTypeService.insert(qzOperationType);
                String standardSpecies = null;
                boolean isExtraRankInfo = false;
                if (CollectionUtils.isNotEmpty(qzOperationType.getQzChargeRules())) {
                    standardSpecies = qzOperationType.getQzChargeRules().iterator().next()
                        .getStandardSpecies();
                    for (QZChargeRule qzChargeRule : qzOperationType.getQzChargeRules()) {
                        qzChargeRule.setQzOperationTypeUuid(qzOperationType.getUuid());
                        qzChargeRuleService.insert(qzChargeRule);
                    }
                } else {
                    isExtraRankInfo = true;
                    standardSpecies = "aiZhan";
                }
                if (null != standardSpecies) {
                    qzKeywordRankInfoService.addQZKeywordRankInfo(qzSetting.getUuid(),
                        qzOperationType.getOperationType(), standardSpecies, !isExtraRankInfo);
                    if (standardSpecies
                        .equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
                        if (qzSetting.getSearchEngine().equals(Constants.SEARCH_ENGINE_BAIDU)) {
                            qzKeywordRankInfoService.addQZKeywordRankInfo(qzSetting.getUuid(),
                                qzOperationType.getOperationType(), "aiZhan", false);
                        }
                        captureRankJobService
                            .qzAddCaptureRankJob(qzOperationType.getGroup(), qzSettingUuid,
                                qzSetting.getCustomerUuid(), qzOperationType.getOperationType(),
                                userName);
                    }
                }
            }
            // 添加标签
            for (QZCategoryTag qzCategoryTag : qzSetting.getQzCategoryTags()) {
                qzCategoryTag.setQzSettingUuid(qzSettingUuid);
                qzCategoryTagService.insert(qzCategoryTag);
            }
        }
        return qzSettingUuid;
    }

    private void updateQZKeywordRankInfo(List<QZKeywordRankInfo> existingQZKeywordRankInfos,
        List<QZOperationType> qzOperationTypes, QZSetting qzSetting) {
        Map<String, Map<String, QZKeywordRankInfo>> existingQZKeywordRankInfoMap = new HashMap<>(2);
        for (QZKeywordRankInfo qzKeywordRankInfo : existingQZKeywordRankInfos) {
            Map<String, QZKeywordRankInfo> qzKeywordRankInfoMap = existingQZKeywordRankInfoMap
                .get(qzKeywordRankInfo.getTerminalType());
            if (null != qzKeywordRankInfoMap) {
                qzKeywordRankInfoMap.put(qzKeywordRankInfo.getWebsiteType(), qzKeywordRankInfo);
            } else {
                qzKeywordRankInfoMap = new HashMap<>(4);
                qzKeywordRankInfoMap.put(qzKeywordRankInfo.getWebsiteType(), qzKeywordRankInfo);
            }
            existingQZKeywordRankInfoMap
                .put(qzKeywordRankInfo.getTerminalType(), qzKeywordRankInfoMap);
        }

        Map<String, String> standardSpeciesMap = new HashMap<>(2);
        for (QZOperationType qzOperationType : qzOperationTypes) {
            Map<String, QZKeywordRankInfo> qzKeywordRankInfoMap = existingQZKeywordRankInfoMap
                .get(qzOperationType.getOperationType());
            String standardSpecies = null;
            Set<String> existingStandardSpeciesSet = new HashSet<>();
            if (null != qzKeywordRankInfoMap) {
                if (CollectionUtils.isNotEmpty(qzOperationType.getQzChargeRules())) {
                    QZChargeRule qzChargeRule = qzOperationType.getQzChargeRules().iterator()
                        .next();
                    String newStandardSpecies = qzChargeRule.getStandardSpecies();
                    QZKeywordRankInfo qzKeywordRankInfo = qzKeywordRankInfoMap
                        .get(newStandardSpecies);
                    if (null != qzKeywordRankInfo && qzKeywordRankInfo.getDataProcessingStatus()) {
                        existingStandardSpeciesSet.add(newStandardSpecies);
                        if (newStandardSpecies
                            .equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)
                            && qzSetting.getSearchEngine().equals(Constants.SEARCH_ENGINE_BAIDU)) {
                            existingStandardSpeciesSet.add("aiZhan");
                        }
                    } else {
                        if (!"other".equals(newStandardSpecies)) {
                            standardSpecies = newStandardSpecies;
                        }
                    }
                } else {
                    existingStandardSpeciesSet.add("aiZhan");
                }
                existingStandardSpeciesSet.add("xt");
                for (String existingStandardSpecies : existingStandardSpeciesSet) {
                    qzKeywordRankInfoMap.remove(existingStandardSpecies);
                }
            } else {
                if (CollectionUtils.isNotEmpty(qzOperationType.getQzChargeRules())) {
                    QZChargeRule qzChargeRule = qzOperationType.getQzChargeRules().iterator()
                        .next();
                    if (!"other".equals(qzChargeRule.getStandardSpecies())) {
                        standardSpecies = qzChargeRule.getStandardSpecies();
                    }
                } else { // 辅助优化, 采集爱站曲线，不计算达标
                    if (qzSetting.getSearchEngine().equals(Constants.SEARCH_ENGINE_BAIDU)) {
                        qzKeywordRankInfoService.addQZKeywordRankInfo(qzSetting.getUuid(),
                            qzOperationType.getOperationType(), "aiZhan", false);
                    }
                }
            }
            if (null != standardSpecies) {
                standardSpeciesMap.put(qzOperationType.getOperationType(), standardSpecies);
            }
        }

        for (Map.Entry<String, String> entry : standardSpeciesMap.entrySet()) {
            qzKeywordRankInfoService
                .addQZKeywordRankInfo(qzSetting.getUuid(), entry.getKey(), entry.getValue(), true);
            if (entry.getValue()
                .equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
                if (qzSetting.getSearchEngine().equals(Constants.SEARCH_ENGINE_BAIDU)) {
                    qzKeywordRankInfoService
                        .addQZKeywordRankInfo(qzSetting.getUuid(), entry.getKey(), "aiZhan", false);
                }
            }
        }

        for (Map.Entry<String, Map<String, QZKeywordRankInfo>> entry : existingQZKeywordRankInfoMap
            .entrySet()) {
            for (QZKeywordRankInfo qzKeywordRankInfo : entry.getValue().values()) {
                qzKeywordRankInfoService.deleteById(qzKeywordRankInfo);
            }
        }
    }

    private void updateOperationTypeAndChargeRule(List<QZOperationType> oldOperationTypes,
        List<QZOperationType> newOperationTypes, Long qzSettingUuid, long customerUuid,
        String userName) {
        Map<String, QZOperationType> oldOperationTypeMap = new HashMap<>();
        for (QZOperationType qzOperationType : oldOperationTypes) {
            oldOperationTypeMap.put(qzOperationType.getOperationType(), qzOperationType);
        }

        for (QZOperationType newOperationType : newOperationTypes) {
            QZOperationType oldOperationType = oldOperationTypeMap
                .get(newOperationType.getOperationType());
            if (oldOperationType != null) {
                updateOperationTypeAndChargeRuleEqual(oldOperationType, newOperationType,
                    qzSettingUuid, customerUuid, userName);
                oldOperationTypeMap.remove(newOperationType.getOperationType());
            } else {
                //添加一条
                newOperationType.setQzSettingUuid(qzSettingUuid);
                qzOperationTypeService.insert(newOperationType);
                Long uuid = (long) qzOperationTypeService.selectLastId();
                if (newOperationType.getQzChargeRules().iterator().next().getStandardSpecies()
                    .equals(Constants.
                        QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
                    captureRankJobService
                        .qzAddCaptureRankJob(newOperationType.getGroup(), qzSettingUuid,
                            customerUuid, newOperationType.getOperationType(), userName);
                }
                for (QZChargeRule qzChargeRule : newOperationType.getQzChargeRules()) {
                    qzChargeRule.setQzOperationTypeUuid(uuid);
                    qzChargeRuleService.insert(qzChargeRule);
                }
            }
        }

        for (QZOperationType oldOperationType : oldOperationTypeMap.values()) {
            if (CollectionUtils.isNotEmpty(oldOperationType.getQzChargeRules())) {
                if (oldOperationType.getQzChargeRules().iterator().next().getStandardSpecies()
                    .equals(Constants.
                        QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
                    captureRankJobService
                        .deleteCaptureRankJob(qzSettingUuid, oldOperationType.getOperationType());
                }
            }
            qzChargeRuleService.deleteByQZOperationTypeUuid(oldOperationType.getUuid());
            oldOperationType.setUpdateTime(new Date());
            oldOperationType.setIsDeleted(1);
            qzOperationTypeService.updateById(oldOperationType);
            QZSetting qzSetting = qzSettingDao.selectById(oldOperationType.getQzSettingUuid());
            if (oldOperationType.getOperationType().equals(TerminalTypeEnum.PC.name())) {
                qzSetting.setPcGroup(null);
            } else {
                qzSetting.setPhoneGroup(null);
            }
            qzSettingDao.updateQZSettingGroup(qzSetting);
        }
    }

    private void updateOperationTypeAndChargeRuleEqual(QZOperationType oldOperationType,
        QZOperationType newOperationType, Long qzSettingUuid, long customerUuid, String userName) {
        oldOperationType.setUpdateTime(new Date());
        oldOperationType.setOperationType(newOperationType.getOperationType());
        oldOperationType.setOptimizationType(newOperationType.getOptimizationType());
        oldOperationType.setStandardType(newOperationType.getStandardType());
        oldOperationType.setGroup(newOperationType.getGroup());
        oldOperationType.setSubDomainName(newOperationType.getSubDomainName());
        oldOperationType.setMonitorRemark(newOperationType.getMonitorRemark());
        oldOperationType.setMaxKeywordCount(newOperationType.getMaxKeywordCount());
        // 只要是发生改变那么就让它的状态为0
        oldOperationType.setIsDeleted(0);
        qzOperationTypeService.updateById(oldOperationType);

        boolean isDeleteDesignationWord = false;
        if (CollectionUtils.isNotEmpty(oldOperationType.getQzChargeRules())) {
            QZChargeRule qzChargeRule = oldOperationType.getQzChargeRules().iterator().next();
            if (qzChargeRule.getStandardSpecies()
                .equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
                if (!newOperationType.getQzChargeRules().isEmpty() && !newOperationType
                    .getQzChargeRules().iterator().next().getStandardSpecies()
                    .equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
                    CaptureRankJob existCaptureRankJob = captureRankJobService
                        .findExistCaptureRankJob(qzSettingUuid,
                            newOperationType.getOperationType());
                    if (null != existCaptureRankJob) {
                        isDeleteDesignationWord = true;
                    }
                }
            }
        }

        if (isDeleteDesignationWord) {
            captureRankJobService
                .deleteCaptureRankJob(qzSettingUuid, newOperationType.getOperationType());
        }
        //删除规则
        qzChargeRuleService.deleteByQZOperationTypeUuid(oldOperationType.getUuid());

        // 保存新规则
        boolean hasDesignationWord = false;
        for (QZChargeRule qzChargeRule : newOperationType.getQzChargeRules()) {
            if (!hasDesignationWord && qzChargeRule.getStandardSpecies()
                .equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
                hasDesignationWord = true;
            }
            qzChargeRule.setQzOperationTypeUuid(oldOperationType.getUuid());
            qzChargeRuleService.insert(qzChargeRule);
        }
        if (hasDesignationWord) {
            CaptureRankJob existCaptureRankJob = captureRankJobService
                .findExistCaptureRankJob(qzSettingUuid, newOperationType.getOperationType());
            if (null == existCaptureRankJob) {
                captureRankJobService
                    .qzAddCaptureRankJob(newOperationType.getGroup(), qzSettingUuid, customerUuid,
                        newOperationType.getOperationType(), userName);
            }
        }
    }

    @Override
    public void deleteOne(Long uuid) {
        //根据数据库中的uuid去查询
        List<QZOperationType> qzOperationTypes = qzOperationTypeService
            .searchQZOperationTypesByQZSettingUuid(uuid);
        if (CollectionUtils.isNotEmpty(qzOperationTypes)) {
            for (QZOperationType qzOperationType : qzOperationTypes) {
                qzChargeRuleService.deleteByQZOperationTypeUuid(qzOperationType.getUuid());
            }
            qzOperationTypeService.deleteByQZSettingUuid(uuid);
        }
        qzKeywordRankInfoService.deleteByQZSettingUuid(uuid);
        List<QZCategoryTag> qzCategoryTags = qzCategoryTagService
            .searchCategoryTagByQZSettingUuid(uuid);
        if (CollectionUtils.isNotEmpty(qzCategoryTags)) {
            for (QZCategoryTag qzCategoryTag : qzCategoryTags) {
                qzCategoryTagService.deleteById(qzCategoryTag.getUuid());
            }
        }
        captureRankJobService.deleteCaptureRankJob(uuid, null);
        Map<String, String> groupMap = qzSettingDao.getPCPhoneGroupByUuid(uuid);
        String pcGroup = groupMap.get("pcGroup");
        if (pcGroup != null
            && customerKeywordService.getCustomerKeywordCountByOptimizeGroupName(pcGroup) == 0) {
            groupService.deleteByGroupName(pcGroup);
        }
        String phoneGroup = groupMap.get("phoneGroup");
        if (phoneGroup != null
            && customerKeywordService.getCustomerKeywordCountByOptimizeGroupName(pcGroup) == 0) {
            groupService.deleteByGroupName(phoneGroup);
        }
        qzSettingDao.deleteById(uuid);
    }

    @Override
    public void deleteAll(List<Integer> uuids) {
        for (Integer uuid : uuids) {
            deleteOne(Long.valueOf(uuid));
        }
    }

    @Override
    public QZSetting getQZSetting(Long uuid) {
        QZSetting qzSetting = qzSettingDao.selectById(uuid);
        if (qzSetting != null) {
            Customer customer = customerService.getCustomer(qzSetting.getCustomerUuid());
            if (customer != null) {
                qzSetting.setContactPerson(customer.getContactPerson());
            }
            List<QZOperationType> qzOperationTypes = qzOperationTypeService
                .searchQZOperationTypesIsDelete(qzSetting.getUuid());
            qzSetting.setQzOperationTypes(qzOperationTypes);
            qzSetting.setQzCategoryTags(
                qzCategoryTagService.searchCategoryTagByQZSettingUuid(qzSetting.getUuid()));
        }
        return qzSetting;
    }

    @Override
    public Map<String, String> getPCPhoneGroupByUuid(Long uuid) {
        return qzSettingDao.getPCPhoneGroupByUuid(uuid);
    }

    @Override
    public void batchUpdateQZSettingUpdateStatus(String uuids) {
        qzSettingDao.batchUpdateQZSettingUpdateStatus(uuids);
    }

    @Override
    public void batchUpdateRenewalStatus(String uuids, int renewalStatus) {
        qzSettingDao.batchUpdateRenewalStatus(uuids, renewalStatus);
    }

    @Override
    public void updRenewalStatus(Long uuid, int renewalStatus) {
        QZSetting qzSetting = new QZSetting();
        qzSetting.setUuid(uuid);
        qzSetting.setRenewalStatus(renewalStatus);
        qzSettingDao.updateById(qzSetting);
    }

    @Override
    public void updateQzCategoryTags(List<String> uuids, List<QZCategoryTag> targetQZCategoryTags) {
        for (String uuid : uuids) {
            List<QZCategoryTag> existingQZCategoryTags = qzCategoryTagService
                .searchCategoryTagByQZSettingUuid(Long.parseLong(uuid));
            qzCategoryTagService.updateQZCategoryTag(existingQZCategoryTags, targetQZCategoryTags,
                Long.parseLong(uuid));
        }
    }

    @Override
    public Map<String, Object> getQZSettingForAutoOperate() {
        Map<String, Object> map = qzSettingDao.selectQZSettingForAutoOperate();
        if (null != map) {
            long qzSettingUuid = (long) map.get("uuid");
            String captureTerminalType = (String) map.get("captureTerminalType");
            map.remove("captureTerminalType");
            String[] terminalTypes = captureTerminalType.split(",");
            List<String> standardSpecieList = qzOperationTypeService
                .getQZSettngStandardSpecie(qzSettingUuid, terminalTypes);
            map.put("standardSpecieList", standardSpecieList);
        }
        return map;
    }
}
