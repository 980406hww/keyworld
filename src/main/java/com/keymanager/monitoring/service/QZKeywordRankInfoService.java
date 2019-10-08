package com.keymanager.monitoring.service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.QZSettingCountNumCriteria;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.dao.QZKeywordRankInfoDao;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.QZKeywordRankInfo;
import com.keymanager.monitoring.entity.QZOperationType;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.vo.ExternalQZKeywordRankInfoResultVO;
import com.keymanager.monitoring.vo.ExternalQzKeywordRankInfoVO;
import com.keymanager.monitoring.vo.ExternalQzSettingVO;
import com.keymanager.monitoring.vo.QZChargeRuleVO;
import com.keymanager.util.Constants;
import com.keymanager.util.PaginationRewriteQueryTotalInterceptor;
import java.util.ArrayList;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhoukai
 * @Date 2018/12/6 11:20
 **/
@Service
public class QZKeywordRankInfoService extends ServiceImpl<QZKeywordRankInfoDao, QZKeywordRankInfo> {
    private static final Log logger = LogFactory.getLog(PaginationRewriteQueryTotalInterceptor.class);
    @Autowired
    private QZKeywordRankInfoDao qzKeywordRankInfoDao;

    @Autowired
    private QZSettingService qzSettingService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private QZOperationTypeService qzOperationTypeService;

    @Autowired
    private QZChargeRuleService qzChargeRuleService;

    public List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo (Long qzSettingUuid, String terminalType, String websiteType) {
        return qzKeywordRankInfoDao.searchExistingQZKeywordRankInfo(qzSettingUuid, terminalType, websiteType);
    }

    public void deleteByQZSettingUuid (Long uuid) {
        qzKeywordRankInfoDao.deleteByQZSettingUuid(uuid);
    }

    public synchronized List<ExternalQzSettingVO> getQZSettingTask() {
        Config taskNumber = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING, Constants.CONFIG_KEY_QZ_TASKNUMBER);
        Config config = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_CRAWLER_HOUR);

        List<ExternalQzSettingVO> qzSettingTasks = qzSettingService.getQZSettingTask(Integer.parseInt(config.getValue()), Integer.parseInt(taskNumber.getValue()));

        if (CollectionUtils.isNotEmpty(qzSettingTasks)) {
            List<Long> uuids = new ArrayList<>();
            for (ExternalQzSettingVO qzSettingVo : qzSettingTasks) {
                List<String> types = qzKeywordRankInfoDao.getQZKeywordRankInfoTypes(qzSettingVo.getUuid());
                if (CollectionUtils.isNotEmpty(types)) {
                    qzSettingVo.setTypeList(types);
                    uuids.add(qzSettingVo.getUuid());
                }
            }
            qzSettingService.updateCrawlerStatus(uuids);
        }
        return qzSettingTasks;
    }

    public void updateQzKeywordRankInfo(ExternalQZKeywordRankInfoResultVO externalQzKeywordRankInfoResultVo) {
        for (ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVo : externalQzKeywordRankInfoResultVo.getQzKeywordRankInfoVos()) {
            QZKeywordRankInfo rankInfo = this.getQZKeywordRankInfo(externalQzKeywordRankInfoVo, externalQzKeywordRankInfoResultVo.getQzSettingUuid());
            QZKeywordRankInfo qzKeywordRankInfo = qzKeywordRankInfoDao.getQZKeywordRankInfo(rankInfo.getQzSettingUuid(), rankInfo.getTerminalType(), rankInfo.getWebsiteType());
            if (null != qzKeywordRankInfo){
                if (null == qzKeywordRankInfo.getCreateTopTenNum()) {
                    String[] dateArr = rankInfo.getDate().replace("[", "").replace("]", "").split(", ");
                    int index = ArrayUtils.indexOf(dateArr, qzKeywordRankInfo.getCreateMonthDay());
                    if (index > -1) {
                        rankInfo.setCreateTopTenNum(Integer.parseInt(rankInfo.getTopTen().replace("[", "").
                                replace("]", "").split(", ")[index]));
                        rankInfo.setCreateTopFiftyNum(Integer.parseInt(rankInfo.getTopFifty().replace("[", "")
                                .replace("]", "").split(", ")[index]));
                    } else {
                        rankInfo.setCreateTopTenNum(Integer.parseInt(rankInfo.getTopTen().replace("[", "").
                                replace("]", "").split(", ")[dateArr.length - 1]));
                        rankInfo.setCreateTopFiftyNum(Integer.parseInt(rankInfo.getTopFifty().replace("[", "")
                                .replace("]", "").split(", ")[dateArr.length - 1]));
                    }
                }
                rankInfo.setUuid(qzKeywordRankInfo.getUuid());
                rankInfo.setUpdateTime(new Date());
                qzKeywordRankInfoDao.updateById(rankInfo);
            } else {
                rankInfo.setCreateTopTenNum(externalQzKeywordRankInfoVo.getCreateTopTenNum());
                rankInfo.setCreateTopFiftyNum(externalQzKeywordRankInfoVo.getCreateTopTenNum());
                qzKeywordRankInfoDao.insert(rankInfo);
            }
        }
        QZSetting qzSetting = this.getQZSetting(externalQzKeywordRankInfoResultVo.getQzSettingUuid(), externalQzKeywordRankInfoResultVo.getCrawlerStatus());
        qzSettingService.updateQzSetting(qzSetting);
    }

    private QZKeywordRankInfo getQZKeywordRankInfo(ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVo, Long qzSettingUuid) {
        QZKeywordRankInfo qzKeywordRankInfo = new QZKeywordRankInfo();
        qzKeywordRankInfo.setQzSettingUuid(qzSettingUuid);
        qzKeywordRankInfo.setTerminalType(externalQzKeywordRankInfoVo.getTerminalType());
        qzKeywordRankInfo.setTopHundred(externalQzKeywordRankInfoVo.getTopHundred());
        qzKeywordRankInfo.setTopFifty(externalQzKeywordRankInfoVo.getTopFifty());
        qzKeywordRankInfo.setTopForty(externalQzKeywordRankInfoVo.getTopForty());
        qzKeywordRankInfo.setTopThirty(externalQzKeywordRankInfoVo.getTopThirty());
        qzKeywordRankInfo.setTopTwenty(externalQzKeywordRankInfoVo.getTopTwenty());
        qzKeywordRankInfo.setTopTen(externalQzKeywordRankInfoVo.getTopTen());
        qzKeywordRankInfo.setFullDate(externalQzKeywordRankInfoVo.getFullDate());
        qzKeywordRankInfo.setDate(externalQzKeywordRankInfoVo.getDate());
        qzKeywordRankInfo.setWebsiteType(externalQzKeywordRankInfoVo.getWebsiteType());
        qzKeywordRankInfo.setDataProcessingStatus(externalQzKeywordRankInfoVo.getDataProcessingStatus());
        qzKeywordRankInfo.setBaiduRecord(externalQzKeywordRankInfoVo.getBaiduRecord());
        qzKeywordRankInfo.setBaiduRecordFullDate(externalQzKeywordRankInfoVo.getBaiduRecordFullDate());

        if (qzKeywordRankInfo.getDataProcessingStatus()) {
            if (StringUtils.isNotBlank(externalQzKeywordRankInfoVo.getTopTen())) {
                this.setIncreaseAndTodayDifference(qzKeywordRankInfo);
            }

            List<QZChargeRuleVO> chargeRuleVos = qzChargeRuleService.findQZChargeRules(qzSettingUuid, externalQzKeywordRankInfoVo.getTerminalType(), externalQzKeywordRankInfoVo.getWebsiteType());
            if (CollectionUtils.isNotEmpty(chargeRuleVos)) {
                Map standard = this.standardCalculation(chargeRuleVos, qzKeywordRankInfo);
                qzKeywordRankInfo.setDifferenceValue(Double.parseDouble(standard.get("differenceValue").toString()));
                qzKeywordRankInfo.setAchieveLevel(Integer.parseInt(standard.get("achieveLevel").toString()));
                qzKeywordRankInfo.setSumSeries(Integer.parseInt(standard.get("sumSeries").toString()));
                qzKeywordRankInfo.setCurrentPrice(Integer.parseInt(standard.get("currentPrice").toString()));

                QZOperationType qzOperationType = qzOperationTypeService.searchQZOperationTypeByQZSettingAndTerminalType(qzSettingUuid, externalQzKeywordRankInfoVo.getTerminalType());
                // 0 代表没有达标, 1 代表已经达标, 2 代表第一次达标
                int isStandardFlag = 0;
                if (qzKeywordRankInfo.getAchieveLevel() > 0) {
                    qzKeywordRankInfo.setAchieveTime(new Date());
                    isStandardFlag = qzOperationType.getStandardTime() == null ? 2 : 1;
                } else {
                    qzKeywordRankInfo.setAchieveTime(null);
                }
                qzOperationTypeService.updateQZOperationTypeStandardTime(qzOperationType.getUuid(), isStandardFlag);
            }
        } else {
            List<QZKeywordRankInfo> qzKeywordRankInfos = qzKeywordRankInfoDao.searchExistingExtraQZKeywordRankInfo(qzKeywordRankInfo.getQzSettingUuid(), qzKeywordRankInfo.getTerminalType());
            if (qzKeywordRankInfos.size() > 1) {
                for (int i = 1; i < qzKeywordRankInfos.size(); i++) {
                    qzKeywordRankInfoDao.deleteById(qzKeywordRankInfos.get(i).getUuid());
                }
            }
        }
        return qzKeywordRankInfo;
    }

    public QZSettingCountNumCriteria searchCountNumOfQZKeywordRankInfo(QZSettingSearchCriteria criteria){
        double upperValue = Double.parseDouble(configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_UPPER_VALUE).getValue());
        double differenceValue = Double.parseDouble(configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_DIFFERENCEVALUE_VALUE).getValue());
        int oneWeekDiff = Integer.parseInt(configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_ONE_WEEK_DIFF).getValue());

        QZSettingCountNumCriteria qzSettingCountNumCriteria = qzKeywordRankInfoDao.getCountNumOfRankInfo(-upperValue, upperValue, differenceValue, -oneWeekDiff, oneWeekDiff, criteria);
        qzSettingCountNumCriteria.setUpperValue(upperValue);
        qzSettingCountNumCriteria.setDifferenceValue(differenceValue);
        qzSettingCountNumCriteria.setUpOneWeekDiff(oneWeekDiff);
        return qzSettingCountNumCriteria;
    }

    private QZSetting getQZSetting(Long qzSettingUuid, String crawlerStatus) {
        QZSetting qzSetting = new QZSetting();
        qzSetting.setUuid(qzSettingUuid);
        qzSetting.setCrawlerStatus(crawlerStatus);
        qzSetting.setCrawlerTime(new Date());
        return qzSetting;
    }

    private Map standardCalculation(List<QZChargeRuleVO> chargeRuleVos, QZKeywordRankInfo qzKeywordRankInfo) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        int topTen = Integer.parseInt(qzKeywordRankInfo.getTopTen().replace("[", "").replace("]", "").split(",")[0]);
        int beginStartKeywordCount = Integer.parseInt(chargeRuleVos.get(0).getStartKeywordCount());
        int lastStartKeywordCount = Integer.parseInt(chargeRuleVos.get(chargeRuleVos.size() - 1).getStartKeywordCount());

        Map<String, Object> standardInformation = new HashMap<>(4);
        standardInformation.put("sumSeries", chargeRuleVos.size());
        if (topTen < beginStartKeywordCount) {
            standardInformation.put("achieveLevel", 0);
            standardInformation.put("differenceValue", decimalFormat.format(((beginStartKeywordCount - topTen)*1.0) / beginStartKeywordCount));
            standardInformation.put("currentPrice", 0);
            return standardInformation;
        } else if (topTen >= lastStartKeywordCount) {
            standardInformation.put("achieveLevel", chargeRuleVos.size());
            standardInformation.put("differenceValue", 1);
            standardInformation.put("currentPrice", chargeRuleVos.get(chargeRuleVos.size() - 1).getAmount());
            return standardInformation;
        } else {
            for (int i = 0; i < chargeRuleVos.size(); i++) {
                int currentStartKeywordCount = Integer.parseInt(chargeRuleVos.get(i).getStartKeywordCount());
                if (topTen >= currentStartKeywordCount) {
                    int nextStartKeywordCount = Integer.parseInt(chargeRuleVos.get(i + 1).getStartKeywordCount());
                    standardInformation.put("achieveLevel", i + 1);
                    standardInformation.put("differenceValue", decimalFormat.format(((nextStartKeywordCount - topTen)*1.0) / nextStartKeywordCount));
                    standardInformation.put("currentPrice", chargeRuleVos.get(i).getAmount());
                }
            }
            return standardInformation;
        }
    }

    private void setIncreaseAndTodayDifference(QZKeywordRankInfo qzKeywordRankInfo) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        String[] arr = qzKeywordRankInfo.getTopTen().replace("[", "").replace("]", "").split(", ");
        int oneWeekDiff = Integer.parseInt(arr[0]) - Integer.parseInt(arr[6]);
        if (Integer.parseInt(arr[6]) > 0) {
            String increase = decimalFormat.format(oneWeekDiff * 1.0 / Integer.parseInt(arr[6]));
            qzKeywordRankInfo.setIncrease(Double.parseDouble(increase));
        } else {
            if (oneWeekDiff > 0) {
                qzKeywordRankInfo.setIncrease(1.0);
            } else {
                qzKeywordRankInfo.setIncrease(0.0);
            }
        }
        Integer todayDifference = Integer.parseInt(arr[0]) - Integer.parseInt(arr[1]);
        qzKeywordRankInfo.setTodayDifference(todayDifference);
        qzKeywordRankInfo.setOneWeekDifference(oneWeekDiff);
    }

    public void addQZKeywordRankInfo(Long uuid, String terminalType, String standardSpecies, boolean dataProcessingStatus) {
        QZKeywordRankInfo qzKeywordRankInfo = new QZKeywordRankInfo();
        qzKeywordRankInfo.setQzSettingUuid(uuid);
        qzKeywordRankInfo.setTerminalType(terminalType);
        qzKeywordRankInfo.setWebsiteType(standardSpecies);
        qzKeywordRankInfo.setDataProcessingStatus(dataProcessingStatus);
        qzKeywordRankInfoDao.insert(qzKeywordRankInfo);
    }

    public void saveQZKeywordRankInfo(QZKeywordRankInfo rankInfo) {
        if (null == rankInfo.getUuid()) {
            qzKeywordRankInfoDao.insert(rankInfo);
        } else {
            rankInfo.setUpdateTime(new Date());
            qzKeywordRankInfoDao.updateById(rankInfo);
        }
    }
}
