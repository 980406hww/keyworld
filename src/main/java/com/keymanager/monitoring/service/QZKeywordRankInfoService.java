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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    public List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo (Long uuid, Integer checkStatus, String terminalType) {
        return qzKeywordRankInfoDao.searchExistingQZKeywordRankInfo(uuid, checkStatus, terminalType);
    }

    public void deleteByQZSettingUuid (Long uuid) {
        qzKeywordRankInfoDao.deleteByQZSettingUuid(uuid);
    }

    public List<ExternalQzSettingVO> getQZSettingTask(){
        Config taskNumber = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING, Constants.CONFIG_KEY_QZ_TASKNUMBER);
        Config config = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_CRAWLER_HOUR);
        List<ExternalQzSettingVO> qzSettingTasks = qzKeywordRankInfoDao.getQZSettingTask(Integer.parseInt(config.getValue()),Integer.parseInt(taskNumber.getValue()));
        if (qzSettingTasks.size()>0){
            Long[] uuids=new Long[qzSettingTasks.size()];
            int index = 0;
            for (ExternalQzSettingVO qzSettingTask:qzSettingTasks) {
                uuids[index] = qzSettingTask.getUuid();
                index++;
            }
            qzSettingDao.updateCrawlerStatus(uuids);
        }
        return qzSettingTasks;
    }

    public void updateQzKeywordRankInfo(ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVO){

        QZKeywordRankInfo rankInfo = getQZKeywordRankInfo(externalQzKeywordRankInfoVO);
        QZSetting qzSetting = getQZSetting(externalQzKeywordRankInfoVO);
        try {
            List<QZKeywordRankInfo> rankInfos= qzKeywordRankInfoDao.getQzKeywordRankInfos();
            for (QZKeywordRankInfo rank:rankInfos) {
                if (rank.getQzSettingUuid().toString().equals
                        (externalQzKeywordRankInfoVO.getQzSettingUuid().toString())
                        && rank.getTerminalType().equals(externalQzKeywordRankInfoVO.getTerminalType())){
                    rankInfo.setUuid(rank.getUuid());
                    qzKeywordRankInfoDao.updateById(rankInfo);
                    qzSettingDao.updateQzSetting(qzSetting);
                    return;
                }
            }
            qzKeywordRankInfoDao.insert(rankInfo);
            qzSettingDao.updateQzSetting(qzSetting);
        }catch (Exception e){
            qzSettingDao.updateQzSetting(qzSetting);
            logger.error("数据更新异常"+e.getMessage());
        }
    }

    public QZKeywordRankInfo getQZKeywordRankInfo(ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVO){
        QZKeywordRankInfo qzKeywordRankInfo = new QZKeywordRankInfo();
        List<QZOperationTypeVO> operationTypes=qzOperationTypeService.findQZOperationTypes(externalQzKeywordRankInfoVO.getQzSettingUuid(),
                                                    externalQzKeywordRankInfoVO.getTerminalType(),externalQzKeywordRankInfoVO.getGroup());
        Map standard = standardCalculation(operationTypes,externalQzKeywordRankInfoVO);
        try {
            qzKeywordRankInfo.setQzSettingUuid(externalQzKeywordRankInfoVO.getQzSettingUuid());
            qzKeywordRankInfo.setTerminalType(externalQzKeywordRankInfoVO.getTerminalType());
            qzKeywordRankInfo.setFullDate(externalQzKeywordRankInfoVO.getFullDate());
            qzKeywordRankInfo.setIncrease(externalQzKeywordRankInfoVO.getIncrease());
            qzKeywordRankInfo.setTopTen(externalQzKeywordRankInfoVO.getTopTen());
            qzKeywordRankInfo.setTopFifty(externalQzKeywordRankInfoVO.getTopFifty());
            qzKeywordRankInfo.setTopForty(externalQzKeywordRankInfoVO.getTopForty());
            qzKeywordRankInfo.setTopThirty(externalQzKeywordRankInfoVO.getTopThirty());
            qzKeywordRankInfo.setTopTwenty(externalQzKeywordRankInfoVO.getTopTwenty());
            qzKeywordRankInfo.setWebsiteType(externalQzKeywordRankInfoVO.getWebsiteType());
            qzKeywordRankInfo.setDate(externalQzKeywordRankInfoVO.getDate());
            qzKeywordRankInfo.setIpRoute(externalQzKeywordRankInfoVO.getIpRoute());
            qzKeywordRankInfo.setBaiduWeight(externalQzKeywordRankInfoVO.getBaiduWeight());
            qzKeywordRankInfo.setBaiduRecord(externalQzKeywordRankInfoVO.getBaiduRecord());
            qzKeywordRankInfo.setBaiduRecordFullDate(externalQzKeywordRankInfoVO.getBaiduRecordFullDate());
            qzKeywordRankInfo.setDifferenceValue(Double.parseDouble(standard.get("differenceValue").toString()));
            qzKeywordRankInfo.setAchieveLevel(Integer.parseInt(standard.get("achieveLevel").toString()));
            qzKeywordRankInfo.setSumSeries(Integer.parseInt(standard.get("sumSeries").toString()));
            qzKeywordRankInfo.setCurrentPrice(Integer.parseInt(standard.get("currentPrice").toString()));
        }catch (Exception e){
            logger.error("数据封装异常"+e.getMessage());
        }
        return qzKeywordRankInfo;
    }

    public QZSettingSearchCriteria getCountNumOfRankInfo(QZSettingSearchCriteria qzSettingSearchCriteria){
        Config upperConfig = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_UPPER_VALUE);
        Config lowerConfig = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_LOWER_VALUE);
        Config differenceValueConfig = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_DIFFERENCEVALUE_VALUE);
        if (null == qzSettingSearchCriteria.getTerminalType()) {
            qzSettingSearchCriteria.setTerminalType("PC");
        }
        double upperValue = Double.parseDouble(upperConfig.getValue());
        double lowerValue = Double.parseDouble(lowerConfig.getValue());
        double differenceValue = Double.parseDouble(differenceValueConfig.getValue());
        QZSettingSearchCriteria countNumOfRankInfoQZSettingSearchCriteria = qzKeywordRankInfoDao.getCountNumOfRankInfo(upperValue, lowerValue, differenceValue, qzSettingSearchCriteria.getTerminalType());
        qzSettingSearchCriteria.setUpperValue(upperValue);
        qzSettingSearchCriteria.setLowerValue(lowerValue);
        qzSettingSearchCriteria.setDifferenceValue(differenceValue);
        qzSettingSearchCriteria.setUpNum(countNumOfRankInfoQZSettingSearchCriteria.getUpNum());
        qzSettingSearchCriteria.setDownNum(countNumOfRankInfoQZSettingSearchCriteria.getDownNum());
        qzSettingSearchCriteria.setAtLeastStandardNum(countNumOfRankInfoQZSettingSearchCriteria.getAtLeastStandardNum());
        qzSettingSearchCriteria.setNeverStandardNum(countNumOfRankInfoQZSettingSearchCriteria.getNeverStandardNum());
        qzSettingSearchCriteria.setCloseStandardNum(countNumOfRankInfoQZSettingSearchCriteria.getCloseStandardNum());
        return qzSettingSearchCriteria;
    }

    public QZSetting getQZSetting(ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVO){
        QZSetting qzSetting = qzSettingDao.findQzSetting(externalQzKeywordRankInfoVO.getQzSettingUuid());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
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
        }catch (Exception e){
            logger.error("数据封装异常"+e.getMessage());
        }
        return qzSetting;
    }
    public Map standardCalculation(List<QZOperationTypeVO> operationTypes,ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVO){
        Map<String,Object> standardInformation = new HashMap<String, Object>();
         if(operationTypes.size()==0){
             standardInformation.put("achieveLevel",null);
             standardInformation.put("sumSeries",null);
             standardInformation.put("differenceValue",null);
             standardInformation.put("currentPrice",null);
             return standardInformation;
         }
         for(int i=0;i<operationTypes.size();i++){
             if (operationTypes.get(i).getEndKeywordCount() == null){
                 standardInformation.put("achieveLevel",i+1);
                 standardInformation.put("sumSeries",operationTypes.size());
                 standardInformation.put("differenceValue",1);
                 standardInformation.put("currentPrice",operationTypes.get(i).getAmount());
                 return standardInformation;
             }else {
                 if (operationTypes.size() >=1 ){
                     try {
                         double topTen = Integer.parseInt(externalQzKeywordRankInfoVO.getTopTen().
                                 replace("[","").replace("]","").split(",")[0]);
                         double endKeywordCount = Integer.parseInt(operationTypes.get(i).getEndKeywordCount());
                         if (operationTypes.get(operationTypes.size()-1).getEndKeywordCount()!=null
                                 && topTen >= Integer.parseInt(operationTypes.get(operationTypes.size()-1).getEndKeywordCount())){
                             standardInformation.put("achieveLevel",operationTypes.size());
                             standardInformation.put("sumSeries",operationTypes.size());
                             standardInformation.put("differenceValue",2);
                             standardInformation.put("currentPrice",operationTypes.get(i).getAmount());
                             return standardInformation;
                         }
                         if (topTen < Integer.parseInt(operationTypes.get(i).getEndKeywordCount())){
                             DecimalFormat decimalFormat = new DecimalFormat("#.0000");
                             standardInformation.put("achieveLevel",i+1);
                             standardInformation.put("sumSeries",operationTypes.size());
                             double differenceValue = (endKeywordCount - topTen) / endKeywordCount;
                             standardInformation.put("differenceValue",decimalFormat.format(differenceValue));
                             standardInformation.put("currentPrice",operationTypes.get(i).getAmount());
                             return standardInformation;
                         }
                     }catch (NumberFormatException e){
                         standardInformation.put("differenceValue",null);
                     }
                 }
             }
         }
         return standardInformation;
    }
}
