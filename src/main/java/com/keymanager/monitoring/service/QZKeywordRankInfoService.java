package com.keymanager.monitoring.service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.dao.QZKeywordRankInfoDao;
import com.keymanager.monitoring.dao.QZSettingDao;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.QZKeywordRankInfo;
import com.keymanager.monitoring.entity.QZSetting;
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
import java.text.SimpleDateFormat;
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
    private QZSettingDao qzSettingDao;

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
        List<ExternalQzSettingVO> qzSettingTasks = qzKeywordRankInfoDao.getQZSettingTask(Integer.parseInt(config.getValue()),Integer.parseInt(taskNumber.getValue()));
        if (CollectionUtils.isNotEmpty(qzSettingTasks)){
            Long[] uuids = new Long[qzSettingTasks.size()];
            int index = 0;
            for (ExternalQzSettingVO qzSettingTask : qzSettingTasks) {
                uuids[index] = qzSettingTask.getUuid();
                index++;
            }
            qzSettingDao.updateCrawlerStatus(uuids);
        }
        return qzSettingTasks;
    }

    public void updateQzKeywordRankInfo(ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVO) throws Exception {
        QZKeywordRankInfo rankInfo = getQZKeywordRankInfo(externalQzKeywordRankInfoVO);
        QZSetting qzSetting = getQZSetting(externalQzKeywordRankInfoVO);
        List<Long> rankInfoUuids = qzKeywordRankInfoDao.getQzKeywordRankInfos(rankInfo.getQzSettingUuid(), rankInfo.getTerminalType());
        if (CollectionUtils.isNotEmpty(rankInfoUuids)){
            rankInfo.setUuid(rankInfoUuids.get(0));
            qzKeywordRankInfoDao.updateById(rankInfo);
        } else {
            qzKeywordRankInfoDao.insert(rankInfo);
        }
        qzSettingDao.updateQzSetting(qzSetting);
    }

    public QZKeywordRankInfo getQZKeywordRankInfo(ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVO) throws Exception{
        QZKeywordRankInfo qzKeywordRankInfo = new QZKeywordRankInfo();
        List<QZOperationTypeVO> operationTypes = qzOperationTypeService.findQZOperationTypes(externalQzKeywordRankInfoVO.getQzSettingUuid(), externalQzKeywordRankInfoVO.getTerminalType(),externalQzKeywordRankInfoVO.getGroup());

        qzKeywordRankInfo.setQzSettingUuid(externalQzKeywordRankInfoVO.getQzSettingUuid());
        qzKeywordRankInfo.setTerminalType(externalQzKeywordRankInfoVO.getTerminalType());
        qzKeywordRankInfo.setTopFifty(externalQzKeywordRankInfoVO.getTopFifty());
        qzKeywordRankInfo.setTopForty(externalQzKeywordRankInfoVO.getTopForty());
        qzKeywordRankInfo.setTopThirty(externalQzKeywordRankInfoVO.getTopThirty());
        qzKeywordRankInfo.setTopTwenty(externalQzKeywordRankInfoVO.getTopTwenty());
        qzKeywordRankInfo.setTopTen(externalQzKeywordRankInfoVO.getTopTen());
        qzKeywordRankInfo.setFullDate(externalQzKeywordRankInfoVO.getFullDate());
        qzKeywordRankInfo.setDate(externalQzKeywordRankInfoVO.getDate());
        qzKeywordRankInfo.setWebsiteType(externalQzKeywordRankInfoVO.getWebsiteType());
        qzKeywordRankInfo.setIpRoute(externalQzKeywordRankInfoVO.getIpRoute().replaceAll(",", ""));
        qzKeywordRankInfo.setBaiduWeight(externalQzKeywordRankInfoVO.getBaiduWeight());
        qzKeywordRankInfo.setBaiduRecord(externalQzKeywordRankInfoVO.getBaiduRecord());
        qzKeywordRankInfo.setBaiduRecordFullDate(externalQzKeywordRankInfoVO.getBaiduRecordFullDate());
        if (StringUtils.isNotBlank(externalQzKeywordRankInfoVO.getTopTen())) {
            setIncreaseAndTodayDifference(qzKeywordRankInfo);
        }
        if (CollectionUtils.isNotEmpty(operationTypes)) {
            Map standard = standardCalculation(operationTypes,externalQzKeywordRankInfoVO);
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
        QZSettingSearchCriteria countNumOfRankInfoQZSettingSearchCriteria = qzKeywordRankInfoDao.getCountNumOfRankInfo(upperValue, lowerValue, differenceValue, qzSettingSearchCriteria.getTerminalType(), qzSettingSearchCriteria.getLoginName());
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

    public QZSetting getQZSetting(ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVO) throws Exception{
        QZSetting qzSetting = qzSettingDao.findQzSetting(externalQzKeywordRankInfoVO.getQzSettingUuid());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        qzSetting.setUuid(externalQzKeywordRankInfoVO.getQzSettingUuid());
        qzSetting.setCrawlerStatus(externalQzKeywordRankInfoVO.getCrawlerStatus());
        qzSetting.setCrawlerTime(simpleDateFormat.parse(externalQzKeywordRankInfoVO.getCrawlerTime()));
        if (qzSetting.getPcCreateTopTenNum() == null || qzSetting.getPcCreateTopTenNum() == 0) {
            qzSetting.setPcCreateTopTenNum(externalQzKeywordRankInfoVO.getPcCreateTopTenNum());
        }
        if (qzSetting.getPcCreateTopFiftyNum() == null || qzSetting.getPcCreateTopFiftyNum() == 0) {
            qzSetting.setPcCreateTopFiftyNum(externalQzKeywordRankInfoVO.getPcCreateTopFiftyNum());
        }
        if (qzSetting.getPhoneCreateTopFiftyNum() == null || qzSetting.getPhoneCreateTopFiftyNum() == 0) {
            qzSetting.setPhoneCreateTopFiftyNum(externalQzKeywordRankInfoVO.getPhoneCreateTopFiftyNum());
        }
        if (qzSetting.getPhoneCreateTopTenNum() == null || qzSetting.getPhoneCreateTopTenNum() == 0) {
            qzSetting.setPhoneCreateTopTenNum(externalQzKeywordRankInfoVO.getPhoneCreateTopTenNum());
        }
        return qzSetting;
    }

    public Map standardCalculation(List<QZOperationTypeVO> operationTypes, ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVO) throws Exception{
        Map<String, Object> standardInformation = new HashMap<String, Object>(4);
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");

        double topTen = Integer.parseInt(externalQzKeywordRankInfoVO.getTopTen().
                replace("[", "").replace("]", "").split(",")[0]);
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
