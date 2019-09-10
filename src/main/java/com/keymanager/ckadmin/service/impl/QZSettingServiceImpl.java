package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.dao.QZSettingDao;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.enums.TerminalTypeEnum;
import com.keymanager.ckadmin.service.QZSettingService;

import com.keymanager.ckadmin.vo.QZSearchEngineVO;
import com.keymanager.ckadmin.vo.QZSettingVO;
import com.keymanager.ckadmin.entity.QZKeywordRankInfo;
import com.keymanager.ckadmin.service.QZKeywordRankInfoService;
import com.keymanager.ckadmin.vo.QZKeywordRankInfoVO;
import com.keymanager.ckadmin.service.QZCategoryTagService;
import com.keymanager.ckadmin.service.OperationCombineService;
import com.keymanager.ckadmin.service.QZOperationTypeService;
import com.keymanager.util.Constants;
import com.keymanager.util.common.StringUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
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
}
