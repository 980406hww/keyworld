package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.QZKeywordRankInfoDao;
import com.keymanager.ckadmin.entity.QZKeywordRankInfo;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.QZChargeRuleService;
import com.keymanager.ckadmin.service.QZKeywordRankInfoService;
import com.keymanager.ckadmin.service.QZOperationTypeService;
import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.ckadmin.vo.ExternalQZKeywordRankInfoResultVO;
import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.vo.ExternalQZKeywordRankInfoVO;
import com.keymanager.ckadmin.vo.ExternalQZSettingVO;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.entity.QZOperationType;
import com.keymanager.ckadmin.vo.QZChargeRuleVO;
import com.keymanager.util.Constants;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service("qzKeywordRankInfoService2")
public class QZKeywordRankInfoServiceImpl extends
    ServiceImpl<QZKeywordRankInfoDao, QZKeywordRankInfo> implements QZKeywordRankInfoService {

    @Resource(name = "qzKeywordRankInfoDao2")
    private QZKeywordRankInfoDao qzKeywordRankInfoDao;

    @Resource(name = "configService2")
    private ConfigService configService;

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    @Resource(name = "qzOperationTypeService2")
    private QZOperationTypeService qzOperationTypeService;

    @Resource(name = "qzChargeRuleService2")
    private QZChargeRuleService qzChargeRuleService;

    @Override
    public List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo(long qzSettingUuid,
        String terminalType, String websiteType) {
        return qzKeywordRankInfoDao
            .searchExistingQZKeywordRankInfo(qzSettingUuid, terminalType, websiteType);
    }

    @Override
    public void addQZKeywordRankInfo(Long uuid, String terminalType, String standardSpecies,
        boolean dataProcessingStatus) {
        QZKeywordRankInfo qzKeywordRankInfo = new QZKeywordRankInfo();
        qzKeywordRankInfo.setQzSettingUuid(uuid);
        qzKeywordRankInfo.setTerminalType(terminalType);
        qzKeywordRankInfo.setWebsiteType(standardSpecies);
        qzKeywordRankInfo.setDataProcessingStatus(dataProcessingStatus);
        qzKeywordRankInfoDao.insert(qzKeywordRankInfo);
    }

    @Override
    public void deleteByQZSettingUuid(Long uuid) {
        qzKeywordRankInfoDao.deleteByQZSettingUuid(uuid);
    }

    @Override
    public List<ExternalQZSettingVO> getQZSettingTask() {
        Config taskNumberConfig = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING, Constants.CONFIG_KEY_QZ_TASKNUMBER);
        Config crawlerHourConfig = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_CRAWLER_HOUR);
        int crawlerHour = Integer.parseInt(crawlerHourConfig.getValue());
        int taskNumber = Integer.parseInt(taskNumberConfig.getValue());

        List<ExternalQZSettingVO> qzSettingTasks = qzSettingService.getQZSettingTask(crawlerHour, taskNumber);
        if (CollectionUtils.isNotEmpty(qzSettingTasks)) {
            List<Long> uuids = new ArrayList<>(taskNumber);
            for (ExternalQZSettingVO qzSettingVo : qzSettingTasks) {
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

    @Override
    public void updateQzKeywordRankInfo(
        ExternalQZKeywordRankInfoResultVO externalQzKeywordRankInfoResultVo) {
        for (ExternalQZKeywordRankInfoVO externalQzKeywordRankInfoVo : externalQzKeywordRankInfoResultVo.getQzKeywordRankInfoVos()) {
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

    private QZKeywordRankInfo getQZKeywordRankInfo(
        ExternalQZKeywordRankInfoVO ExternalQZKeywordRankInfoVO, Long qzSettingUuid) {
        QZKeywordRankInfo qzKeywordRankInfo = new QZKeywordRankInfo();
        qzKeywordRankInfo.setQzSettingUuid(qzSettingUuid);
        qzKeywordRankInfo.setTerminalType(ExternalQZKeywordRankInfoVO.getTerminalType());
        qzKeywordRankInfo.setTopHundred(ExternalQZKeywordRankInfoVO.getTopHundred());
        qzKeywordRankInfo.setTopFifty(ExternalQZKeywordRankInfoVO.getTopFifty());
        qzKeywordRankInfo.setTopForty(ExternalQZKeywordRankInfoVO.getTopForty());
        qzKeywordRankInfo.setTopThirty(ExternalQZKeywordRankInfoVO.getTopThirty());
        qzKeywordRankInfo.setTopTwenty(ExternalQZKeywordRankInfoVO.getTopTwenty());
        qzKeywordRankInfo.setTopTen(ExternalQZKeywordRankInfoVO.getTopTen());
        qzKeywordRankInfo.setFullDate(ExternalQZKeywordRankInfoVO.getFullDate());
        qzKeywordRankInfo.setDate(ExternalQZKeywordRankInfoVO.getDate());
        qzKeywordRankInfo.setWebsiteType(ExternalQZKeywordRankInfoVO.getWebsiteType());
        qzKeywordRankInfo.setDataProcessingStatus(ExternalQZKeywordRankInfoVO.getDataProcessingStatus());
        qzKeywordRankInfo.setBaiduRecord(ExternalQZKeywordRankInfoVO.getBaiduRecord());
        qzKeywordRankInfo.setBaiduRecordFullDate(ExternalQZKeywordRankInfoVO.getBaiduRecordFullDate());

        if (qzKeywordRankInfo.getDataProcessingStatus()) {
            if (StringUtils.isNotBlank(ExternalQZKeywordRankInfoVO.getTopTen())) {
                this.setIncreaseAndTodayDifference(qzKeywordRankInfo);
            }

            List<QZChargeRuleVO> chargeRuleVos = qzChargeRuleService.findQZChargeRules(qzSettingUuid, ExternalQZKeywordRankInfoVO.getTerminalType(), ExternalQZKeywordRankInfoVO.getWebsiteType());
            if (CollectionUtils.isNotEmpty(chargeRuleVos)) {
                Map standard = this.standardCalculation(chargeRuleVos, qzKeywordRankInfo);
                qzKeywordRankInfo.setDifferenceValue(Double.parseDouble(standard.get("differenceValue").toString()));
                qzKeywordRankInfo.setAchieveLevel(Integer.parseInt(standard.get("achieveLevel").toString()));
                qzKeywordRankInfo.setSumSeries(Integer.parseInt(standard.get("sumSeries").toString()));
                qzKeywordRankInfo.setCurrentPrice(Integer.parseInt(standard.get("currentPrice").toString()));

                QZOperationType qzOperationType = qzOperationTypeService.searchQZOperationType(qzSettingUuid, ExternalQZKeywordRankInfoVO.getTerminalType());
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

    private void setIncreaseAndTodayDifference(
        QZKeywordRankInfo qzKeywordRankInfo) {
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

    private QZSetting getQZSetting(Long qzSettingUuid, String crawlerStatus) {
        QZSetting qzSetting = new QZSetting();
        qzSetting.setUuid(qzSettingUuid);
        qzSetting.setCrawlerStatus(crawlerStatus);
        qzSetting.setCrawlerTime(new Date());
        return qzSetting;
    }
}
