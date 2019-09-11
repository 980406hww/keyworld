package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.dao.QZSettingDao;
import com.keymanager.ckadmin.entity.*;
import com.keymanager.ckadmin.enums.TerminalTypeEnum;
import com.keymanager.ckadmin.service.*;

import com.keymanager.ckadmin.vo.QZSearchEngineVO;
import com.keymanager.ckadmin.vo.QZSettingVO;
import com.keymanager.ckadmin.vo.QZKeywordRankInfoVO;
import com.keymanager.enums.CollectMethod;
import com.keymanager.monitoring.criteria.QZSettingSaveCustomerKeywordsCriteria;
import com.keymanager.monitoring.enums.CustomerKeywordSourceEnum;
import com.keymanager.util.Constants;
import com.keymanager.util.common.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.annotation.Resources;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 新关键字表 服务实现类
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
            (!"".equals(criteria.getDomain()) || !"".equals(criteria.getCustomerUuid()))
                && record == 1;
        if (displayExistTabFlag) {
            List<QZSettingVO> qzSettingVos = qzSettingDao
                .searchQZSettingSearchEngines(criteria.getCustomerUuid(), criteria.getDomain());
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
                this.initQZKeywordRankInfoVo(qzKeywordRankInfo, qzKeywordRankInfoVo);
                price += qzKeywordRankInfo.getCurrentPrice() == null ? 0
                    : qzKeywordRankInfo.getCurrentPrice();
                rankInfoVoMap.put(qzKeywordRankInfo.getWebsiteType(), qzKeywordRankInfoVo);
            }
            rankInfoVoMap.put("price", price);
            this.getQZSettingGroupInfo(rankInfoVoMap, uuid, terminalType, optimizeGroupName);
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
        return customerExcludeKeywordService.echoExcludeKeyword(qzSettingExcludeCustomerKeywordsCriteria);
    }

    @Override
    public void excludeQZSettingCustomerKeywords (QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        qzSettingExcludeCustomerKeywordsCriteria.setDomain(qzSettingExcludeCustomerKeywordsCriteria.getDomain().replace("http://","").replace("https://","").replace("www.","").split("/")[0]);
        customerKeywordService.excludeCustomerKeyword(qzSettingExcludeCustomerKeywordsCriteria);
        customerExcludeKeywordService.excludeCustomerKeywords(qzSettingExcludeCustomerKeywordsCriteria);
    }

    @Override
    public void saveQZSettingCustomerKeywords (
        QZSettingSaveCustomerKeywordsCriteria qzSettingSaveCustomerKeywordsCriteria, String userName) {
        for (String terminalType : qzSettingSaveCustomerKeywordsCriteria.getTerminalTypes()) {
            List<CustomerKeyword> customerKeywords = new ArrayList<>();
            String customerExcludeKeywords = customerExcludeKeywordService.getCustomerExcludeKeyword(qzSettingSaveCustomerKeywordsCriteria.getCustomerUuid(), qzSettingSaveCustomerKeywordsCriteria.getQzSettingUuid(), terminalType, qzSettingSaveCustomerKeywordsCriteria.getDomain());
            Set<String> excludeKeyword = new HashSet<String>();
            if (null != customerExcludeKeywords) {
                excludeKeyword.addAll(Arrays.asList(customerExcludeKeywords.split(",")));
            }
            for (String keyword : qzSettingSaveCustomerKeywordsCriteria.getKeywords()) {
                CustomerKeyword customerKeyword = new CustomerKeyword();
                customerKeyword.setQzSettingUuid(qzSettingSaveCustomerKeywordsCriteria.getQzSettingUuid());
                customerKeyword.setCustomerUuid(qzSettingSaveCustomerKeywordsCriteria.getCustomerUuid());
                customerKeyword.setType(qzSettingSaveCustomerKeywordsCriteria.getType());
                customerKeyword.setSearchEngine(qzSettingSaveCustomerKeywordsCriteria.getSearchEngine());
                customerKeyword.setTerminalType(terminalType);
                customerKeyword.setUrl(qzSettingSaveCustomerKeywordsCriteria.getDomain());
                customerKeyword.setKeywordEffect(qzSettingSaveCustomerKeywordsCriteria.getKeywordEffect());
                customerKeyword.setServiceProvider("baidutop123");
                customerKeyword.setManualCleanTitle(true);
                customerKeyword.setCollectMethod(CollectMethod.PerMonth.name());
                customerKeyword.setCurrentIndexCount(-1);
                customerKeyword.setPositionFirstFee(-1d);
                customerKeyword.setBearPawNumber(qzSettingSaveCustomerKeywordsCriteria.getBearPawNumber());
                customerKeyword.setCustomerKeywordSource(CustomerKeywordSourceEnum.Specify.name());
                customerKeyword.setOptimizeGroupName(qzSettingSaveCustomerKeywordsCriteria.getOptimizeGroupName());
                customerKeyword.setMachineGroup(qzSettingSaveCustomerKeywordsCriteria.getMachineGroupName());
                if (!excludeKeyword.isEmpty()){
                    if (excludeKeyword.contains(keyword)){
                        customerKeyword.setStatus(0);
                    }
                }
                customerKeyword.setKeyword(keyword);
                customerKeyword.setOptimizePlanCount(10);
                customerKeywords.add(customerKeyword);
            }
            customerKeywordService.addCustomerKeyword(customerKeywords, userName);
        }
    }

    @Override
    public void deleteOne(Long uuid){
        //根据数据库中的uuid去查询
        List<QZOperationType> qzOperationTypes =  qzOperationTypeService.searchQZOperationTypesByQZSettingUuid(uuid);
        if(CollectionUtils.isNotEmpty(qzOperationTypes)){
            for(QZOperationType qzOperationType : qzOperationTypes){
                qzChargeRuleService.deleteByQZOperationTypeUuid(qzOperationType.getUuid());
            }
            qzOperationTypeService.deleteByQZSettingUuid(uuid);
        }
        qzKeywordRankInfoService.deleteByQZSettingUuid(uuid);
        List<QZCategoryTag> qzCategoryTags = qzCategoryTagService.searchCategoryTagByQZSettingUuid(uuid);
        if (CollectionUtils.isNotEmpty(qzCategoryTags)) {
            for (QZCategoryTag qzCategoryTag : qzCategoryTags) {
                qzCategoryTagService.deleteById(qzCategoryTag.getUuid());
            }
        }
        captureRankJobService.deleteCaptureRankJob(uuid, null);
        qzSettingDao.deleteById(uuid);
    }

    @Override
    public void deleteAll(List<Integer> uuids){
        for(Integer uuid : uuids){
            deleteOne(Long.valueOf(uuid));
        }
    }

}
