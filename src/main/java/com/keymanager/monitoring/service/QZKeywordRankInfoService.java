package com.keymanager.monitoring.service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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

    public List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo (Long qzSettingUuid, QZSettingSearchCriteria qzSettingSearchCriteria) {
        return qzKeywordRankInfoDao.searchExistingQZKeywordRankInfo(qzSettingUuid, qzSettingSearchCriteria);
    }

    public void deleteByQZSettingUuid (Long uuid) {
        qzKeywordRankInfoDao.deleteByQZSettingUuid(uuid);
    }

    public synchronized List<ExternalQzSettingVO> getQZSettingTask(){
        Config taskNumber = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING, Constants.CONFIG_KEY_QZ_TASKNUMBER);
        Config config = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_CRAWLER_HOUR);
        List<ExternalQzSettingVO> qzSettingTasks = qzSettingService.getQZSettingTask(Integer.parseInt(config.getValue()),Integer.parseInt(taskNumber.getValue()));
        if (CollectionUtils.isNotEmpty(qzSettingTasks)){
            Long[] uuids = new Long[qzSettingTasks.size()];
            for (int i = 0; i < qzSettingTasks.size(); i++) {
                uuids[i] = qzSettingTasks.get(i).getUuid();
            }
            qzSettingService.updateCrawlerStatus(uuids);
            for (ExternalQzSettingVO qzSettingVO : qzSettingTasks) {
                List<String> types = qzKeywordRankInfoDao.getQZKeywordRankInfoTypes(qzSettingVO.getUuid());
                if (CollectionUtils.isNotEmpty(types)) {
                    qzSettingVO.setTypeList(types);
                }
            }
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
                    }
                }
                rankInfo.setUuid(qzKeywordRankInfo.getUuid());
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
                QZKeywordRankInfo otherRankInfo = qzKeywordRankInfoDao.getQZKeywordRankInfo(qzSettingUuid, externalQzKeywordRankInfoVo.getTerminalType(), null);
                int isStandardFlag = 0;
                if (qzKeywordRankInfo.getAchieveLevel() > 0) {
                    qzKeywordRankInfo.setAchieveTime(new Date());
                    if (null == otherRankInfo){
                        isStandardFlag = 1;
                    } else if (null != otherRankInfo.getAchieveLevel() && otherRankInfo.getAchieveLevel() > 0) {
                        isStandardFlag = 1;
                    }
                } else {
                    qzKeywordRankInfo.setAchieveTime(null);
                }
                if (isStandardFlag == 1 && null == qzOperationType.getStandardTime()) {
                    isStandardFlag = 2;
                }
                qzOperationTypeService.updateQZOperationTypeStandardTime(qzSettingUuid, externalQzKeywordRankInfoVo.getTerminalType(), isStandardFlag);
            }
        }
        return qzKeywordRankInfo;
    }

    public QZSettingSearchCriteria getCountNumOfRankInfo(QZSettingSearchCriteria qzSettingSearchCriteria){
        double upperValue = Double.parseDouble(configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_UPPER_VALUE).getValue());
        double lowerValue = Double.parseDouble(configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_LOWER_VALUE).getValue());
        double differenceValue = Double.parseDouble(configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_DIFFERENCEVALUE_VALUE).getValue());
        qzSettingSearchCriteria.setUpperValue(upperValue);
        qzSettingSearchCriteria.setLowerValue(lowerValue);
        qzSettingSearchCriteria.setDifferenceValue(differenceValue);

        List<QZSettingSearchCriteria> countNumOfRankInfos = qzKeywordRankInfoDao.getCountNumOfRankInfo(qzSettingSearchCriteria);
        for (QZSettingSearchCriteria countNumOfQZSettingRankInfo : countNumOfRankInfos) {
            qzSettingSearchCriteria.setUnchangedNum(qzSettingSearchCriteria.getUnchangedNum() + (countNumOfQZSettingRankInfo.getUnchangedNum() == 2 ? 1 : countNumOfQZSettingRankInfo.getUnchangedNum()));
            qzSettingSearchCriteria.setUpNum(qzSettingSearchCriteria.getUpNum() + (countNumOfQZSettingRankInfo.getUpNum() == 2 ? 1 : countNumOfQZSettingRankInfo.getUpNum()));
            qzSettingSearchCriteria.setDownNum(qzSettingSearchCriteria.getDownNum() + (countNumOfQZSettingRankInfo.getDownNum() == 2 ? 1 : countNumOfQZSettingRankInfo.getDownNum()));
            if (countNumOfQZSettingRankInfo.getAtLeastStandardNum() > 0) {
                qzSettingSearchCriteria.setAtLeastStandardNum(qzSettingSearchCriteria.getAtLeastStandardNum() + 1);
            } else {
                qzSettingSearchCriteria.setNeverStandardNum(qzSettingSearchCriteria.getNeverStandardNum() + 1);
            }
            qzSettingSearchCriteria.setCloseStandardNum(qzSettingSearchCriteria.getCloseStandardNum() + (countNumOfQZSettingRankInfo.getCloseStandardNum() == 2 ? 1: countNumOfQZSettingRankInfo.getCloseStandardNum()));
        }
        return qzSettingSearchCriteria;
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
        double topTen = Integer.parseInt(qzKeywordRankInfo.getTopTen().replace("[", "").replace("]", "").split(",")[0]);
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

    private QZKeywordRankInfo setIncreaseAndTodayDifference(QZKeywordRankInfo qzKeywordRankInfo) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        String[] arr = qzKeywordRankInfo.getTopTen().replace("[", "").replace("]", "").split(", ");
        if (Integer.parseInt(arr[6]) > 0) {
            String increase = decimalFormat.format((Integer.parseInt(arr[0]) - Integer.parseInt(arr[6])) * 1.0 / Integer.parseInt(arr[6]));
            qzKeywordRankInfo.setIncrease(Double.parseDouble(increase));
        } else {
            if ((Integer.parseInt(arr[0]) - Integer.parseInt(arr[6])) > 0) {
                qzKeywordRankInfo.setIncrease(1.0);
            } else {
                qzKeywordRankInfo.setIncrease(0.0);
            }
        }
        Integer todayDifference = Integer.parseInt(arr[0]) - Integer.parseInt(arr[1]);
        qzKeywordRankInfo.setTodayDifference(todayDifference);
        return qzKeywordRankInfo;
    }
}
