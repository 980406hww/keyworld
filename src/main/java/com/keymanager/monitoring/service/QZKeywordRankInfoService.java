package com.keymanager.monitoring.service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.dao.QZKeywordRankInfoDao;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.QZKeywordRankInfo;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.vo.ExternalQZKeywordRankInfoResultVO;
import com.keymanager.monitoring.vo.ExternalQzKeywordRankInfoVO;
import com.keymanager.monitoring.vo.ExternalQzSettingVO;
import com.keymanager.monitoring.vo.QZOperationTypeVO;
import com.keymanager.util.Constants;
import com.keymanager.util.PaginationRewriteQueryTotalInterceptor;
import org.apache.commons.collections.CollectionUtils;
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

    public List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo (Long qzSettingUuid, QZSettingSearchCriteria qzSettingSearchCriteria) {
        return qzKeywordRankInfoDao.searchExistingQZKeywordRankInfo(qzSettingUuid, qzSettingSearchCriteria);
    }

    public void deleteByQZSettingUuid (Long uuid) {
        qzKeywordRankInfoDao.deleteByQZSettingUuid(uuid);
    }

    public List<ExternalQzSettingVO> getQZSettingTask(){
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
            Long uuid = qzKeywordRankInfoDao.getQZKeywordRankInfo(rankInfo.getQzSettingUuid(), rankInfo.getTerminalType(), rankInfo.getWebsiteType());
            if (null != uuid){
                rankInfo.setUuid(uuid);
                qzKeywordRankInfoDao.updateById(rankInfo);
            } else {
                rankInfo.setCreateTopTenNum(externalQzKeywordRankInfoVo.getCreateTopTenNum());
                rankInfo.setCreateTopFiftyNum(externalQzKeywordRankInfoVo.getCreateTopTenNum());
                qzKeywordRankInfoDao.insert(rankInfo);
            }
        }
        QZSetting qzSetting = this.getQZSetting(externalQzKeywordRankInfoResultVo.getQzSettingUuid(), externalQzKeywordRankInfoResultVo.getCrawlerStatus());
        qzSettingService.updateById(qzSetting);
    }

    private QZKeywordRankInfo getQZKeywordRankInfo(ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVO, Long qzSettingUuid) {
        QZKeywordRankInfo qzKeywordRankInfo = new QZKeywordRankInfo();
        qzKeywordRankInfo.setQzSettingUuid(qzSettingUuid);
        qzKeywordRankInfo.setTerminalType(externalQzKeywordRankInfoVO.getTerminalType());
        qzKeywordRankInfo.setTopHundred(externalQzKeywordRankInfoVO.getTopHundred());
        qzKeywordRankInfo.setTopFifty(externalQzKeywordRankInfoVO.getTopFifty());
        qzKeywordRankInfo.setTopForty(externalQzKeywordRankInfoVO.getTopForty());
        qzKeywordRankInfo.setTopThirty(externalQzKeywordRankInfoVO.getTopThirty());
        qzKeywordRankInfo.setTopTwenty(externalQzKeywordRankInfoVO.getTopTwenty());
        qzKeywordRankInfo.setTopTen(externalQzKeywordRankInfoVO.getTopTen());
        qzKeywordRankInfo.setFullDate(externalQzKeywordRankInfoVO.getFullDate());
        qzKeywordRankInfo.setDate(externalQzKeywordRankInfoVO.getDate());
        qzKeywordRankInfo.setWebsiteType(externalQzKeywordRankInfoVO.getWebsiteType());
        qzKeywordRankInfo.setIpRoute(externalQzKeywordRankInfoVO.getIpRoute() == null ? externalQzKeywordRankInfoVO.getIpRoute() : externalQzKeywordRankInfoVO.getIpRoute().replaceAll(",", ""));
        qzKeywordRankInfo.setBaiduRecord(externalQzKeywordRankInfoVO.getBaiduRecord());
        qzKeywordRankInfo.setBaiduRecordFullDate(externalQzKeywordRankInfoVO.getBaiduRecordFullDate());
        if (StringUtils.isNotBlank(externalQzKeywordRankInfoVO.getTopTen())) {
            this.setIncreaseAndTodayDifference(qzKeywordRankInfo);
        }
        List<QZOperationTypeVO> operationTypes = qzOperationTypeService.findQZOperationTypes(qzSettingUuid, externalQzKeywordRankInfoVO.getTerminalType());
        if (CollectionUtils.isNotEmpty(operationTypes)) {
            Map standard = this.standardCalculation(operationTypes, externalQzKeywordRankInfoVO);
            qzKeywordRankInfo.setDifferenceValue(Double.parseDouble(standard.get("differenceValue").toString()));
            qzKeywordRankInfo.setAchieveLevel(Integer.parseInt(standard.get("achieveLevel").toString()));
            qzKeywordRankInfo.setSumSeries(Integer.parseInt(standard.get("sumSeries").toString()));
            qzKeywordRankInfo.setCurrentPrice(Integer.parseInt(standard.get("currentPrice").toString()));
        }
        return qzKeywordRankInfo;
    }

    public QZSettingSearchCriteria getCountNumOfRankInfo(QZSettingSearchCriteria qzSettingSearchCriteria){
        Config upperConfig = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_UPPER_VALUE);
        Config lowerConfig = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_LOWER_VALUE);
        Config differenceValueConfig = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_DIFFERENCEVALUE_VALUE);
        double upperValue = Double.parseDouble(upperConfig.getValue());
        double lowerValue = Double.parseDouble(lowerConfig.getValue());
        double differenceValue = Double.parseDouble(differenceValueConfig.getValue());
        QZSettingSearchCriteria countNumOfRankInfoQZSettingSearchCriteria = qzKeywordRankInfoDao.getCountNumOfRankInfo(upperValue, lowerValue, differenceValue, qzSettingSearchCriteria.getTerminalType());
        qzSettingSearchCriteria.setUpperValue(upperValue);
        qzSettingSearchCriteria.setLowerValue(lowerValue);
        qzSettingSearchCriteria.setDifferenceValue(differenceValue);
        qzSettingSearchCriteria.setUnchangedNum(countNumOfRankInfoQZSettingSearchCriteria.getUnchangedNum());
        qzSettingSearchCriteria.setUpNum(countNumOfRankInfoQZSettingSearchCriteria.getUpNum());
        qzSettingSearchCriteria.setDownNum(countNumOfRankInfoQZSettingSearchCriteria.getDownNum());
        qzSettingSearchCriteria.setAtLeastStandardNum(countNumOfRankInfoQZSettingSearchCriteria.getAtLeastStandardNum());
        qzSettingSearchCriteria.setNeverStandardNum(countNumOfRankInfoQZSettingSearchCriteria.getNeverStandardNum());
        qzSettingSearchCriteria.setCloseStandardNum(countNumOfRankInfoQZSettingSearchCriteria.getCloseStandardNum());
        qzSettingSearchCriteria.setUnchangedDifferenceNum(countNumOfRankInfoQZSettingSearchCriteria.getUnchangedDifferenceNum());
        qzSettingSearchCriteria.setUpDifferenceNum(countNumOfRankInfoQZSettingSearchCriteria.getUpDifferenceNum());
        qzSettingSearchCriteria.setDownDifferenceNum(countNumOfRankInfoQZSettingSearchCriteria.getDownDifferenceNum());
        return qzSettingSearchCriteria;
    }

    private QZSetting getQZSetting(Long qzSettingUuid, String crawlerStatus) {
        QZSetting qzSetting = qzSettingService.findQzSetting(qzSettingUuid);
        qzSetting.setUuid(qzSettingUuid);
        qzSetting.setCrawlerStatus(crawlerStatus);
        qzSetting.setCrawlerTime(new Date());
        return qzSetting;
    }

    private Map standardCalculation(List<QZOperationTypeVO> operationTypes, ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVO) {
        Map<String, Object> standardInformation = new HashMap<>(4);
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");

        double topTen = Integer.parseInt(externalQzKeywordRankInfoVO.getTopTen().replace("[", "").replace("]", "").split(",")[0]);
        int beginStartKeywordCount = Integer.parseInt(operationTypes.get(0).getStartKeywordCount());
        int lastStartKeywordCount = Integer.parseInt(operationTypes.get(operationTypes.size() - 1).getStartKeywordCount());

        standardInformation.put("sumSeries", operationTypes.size());
        if (topTen < beginStartKeywordCount) {
            standardInformation.put("achieveLevel", 0);
            standardInformation.put("differenceValue", decimalFormat.format(((beginStartKeywordCount - topTen)*1.0) / beginStartKeywordCount));
            standardInformation.put("currentPrice", 0);
            return standardInformation;
        } else if (topTen >= lastStartKeywordCount) {
            standardInformation.put("achieveLevel", operationTypes.size());
            standardInformation.put("differenceValue", 1);
            standardInformation.put("currentPrice", operationTypes.get(operationTypes.size() - 1).getAmount());
            return standardInformation;
        } else {
            for (int i = 0; i < operationTypes.size(); i++) {
                int currentStartKeywordCount = Integer.parseInt(operationTypes.get(i).getStartKeywordCount());
                if (topTen >= currentStartKeywordCount) {
                    int nextStartKeywordCount = Integer.parseInt(operationTypes.get(i + 1).getStartKeywordCount());
                    standardInformation.put("achieveLevel", i + 1);
                    standardInformation.put("differenceValue", decimalFormat.format(((nextStartKeywordCount - topTen)*1.0) / nextStartKeywordCount));
                    standardInformation.put("currentPrice", operationTypes.get(i).getAmount());
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
