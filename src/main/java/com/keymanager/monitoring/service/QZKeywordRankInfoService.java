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
import com.keymanager.monitoring.vo.ExternalQzSettingVo;
import com.keymanager.util.Constants;
import com.keymanager.util.PaginationRewriteQueryTotalInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

    public List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo (Long uuid, Boolean increaseType) {
        return qzKeywordRankInfoDao.searchExistingQZKeywordRankInfo(uuid, increaseType);
    }

    public void deleteByQZSettingUuid (Long uuid) {
        qzKeywordRankInfoDao.deleteByQZSettingUuid(uuid);
    }

    public List<ExternalQzSettingVo> getQZSettingTask(){
        Config config = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_CRAWLER_HOUR);
        List<ExternalQzSettingVo> qzSettingTasks = qzKeywordRankInfoDao.getQZSettingTask(Integer.parseInt(config.getValue()));
        if (qzSettingTasks.size()>0){
            Long[] uuids=new Long[qzSettingTasks.size()];
            int index = 0;
            for (ExternalQzSettingVo qzSettingTask:qzSettingTasks) {
                uuids[index] = qzSettingTask.getUuid();
                index++;
            }
            qzSettingDao.updateCrawlerStatus(uuids);
        }
        return qzSettingTasks;
    }

    public void updateQzKeywordRankInfo(Map<String, Object> resultMap){
        QZKeywordRankInfo rankInfo = getQZKeywordRankInfo(resultMap);
        QZSetting qzSetting = getQZSetting(resultMap);
        try {
            List<QZKeywordRankInfo> rankInfos= qzKeywordRankInfoDao.getQzKeywordRankInfoID();
            for (QZKeywordRankInfo rank:rankInfos) {
                if (rank.getQzSettingUuid().toString().equals
                        (resultMap.get("fQZSettingUuid").toString())
                        && rank.getTerminalType().equals(resultMap.get("fTerminalType"))){
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

    public QZKeywordRankInfo getQZKeywordRankInfo(Map<String, Object> resultMap){
        QZKeywordRankInfo qzKeywordRankInfo = new QZKeywordRankInfo();
        try {
            qzKeywordRankInfo.setQzSettingUuid(Long.valueOf(resultMap.get("fQZSettingUuid").toString()));
            qzKeywordRankInfo.setTerminalType((String) resultMap.get("fTerminalType"));
            qzKeywordRankInfo.setFullDate((String) resultMap.get("fFullDate"));
            qzKeywordRankInfo.setIncrease(Double.valueOf(resultMap.get("fIncrease").toString()));
            qzKeywordRankInfo.setTopTen((String) resultMap.get("fTopTen"));
            qzKeywordRankInfo.setTopFifty((String) resultMap.get("fTopFifty"));
            qzKeywordRankInfo.setTopForty((String) resultMap.get("fTopForty"));
            qzKeywordRankInfo.setTopThirty((String) resultMap.get("fTopThirty"));
            qzKeywordRankInfo.setTopTwenty((String) resultMap.get("fTopTwenty"));
            qzKeywordRankInfo.setWebsiteType((String) resultMap.get("fWebsiteType"));
            qzKeywordRankInfo.setDate((String) resultMap.get("fDate"));
        }catch (Exception e){
            logger.error("数据封装异常"+e.getMessage());
        }
        return qzKeywordRankInfo;
    }

    public QZSettingSearchCriteria getCountDownAndUp(QZSettingSearchCriteria qzSettingSearchCriteria){
        Config uppperConfig = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_UPPER_VALUE);
        Config lowerConfig = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_LOWER_VALUE);
        QZSettingSearchCriteria countDownAndUpQZSettingSearchCriteria = qzKeywordRankInfoDao.getCountDownAndUp(Double.parseDouble(uppperConfig.getValue()), Double.parseDouble(lowerConfig.getValue()));
        qzSettingSearchCriteria.setUpperValue(Double.parseDouble(uppperConfig.getValue()));
        qzSettingSearchCriteria.setLowerValue(Double.parseDouble(lowerConfig.getValue()));
        qzSettingSearchCriteria.setUpNum(countDownAndUpQZSettingSearchCriteria.getUpNum());
        qzSettingSearchCriteria.setDownNum(countDownAndUpQZSettingSearchCriteria.getDownNum());
        return qzSettingSearchCriteria;
    }

    public QZSetting getQZSetting(Map<String, Object> resultMap){
        QZSetting qzSetting = qzSettingDao.findQzSetting(Long.valueOf(resultMap.get("fQZSettingUuid").toString()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Integer fpcCreateTopTenNum = Integer.parseInt(resultMap.get("fpcCreateTopTenNum").toString());
            Integer fpcCreateTopFiftyNum = Integer.parseInt(resultMap.get("fpcCreateTopFiftyNum").toString());
            Integer fphoneCreateTopFiftyNum = Integer.parseInt(resultMap.get("fphoneCreateTopFiftyNum").toString());
            Integer fphoneCreateTopTenNum = Integer.parseInt(resultMap.get("fphoneCreateTopTenNum").toString());
            qzSetting.setUuid(Long.valueOf(resultMap.get("fQZSettingUuid").toString()));
            qzSetting.setCrawlerStatus("finish");
            qzSetting.setCrawlerTime(simpleDateFormat.parse((resultMap.get("fCrawlTime").toString())));
            if (qzSetting.getPcCreateTopTenNum() == null || qzSetting.getPcCreateTopTenNum() == 0) {
                qzSetting.setPcCreateTopTenNum(fpcCreateTopTenNum);
            }
            if (qzSetting.getPcCreateTopFiftyNum() == null || qzSetting.getPcCreateTopFiftyNum() == 0) {
                qzSetting.setPcCreateTopFiftyNum(fpcCreateTopFiftyNum);
            }
            if (qzSetting.getPhoneCreateTopFiftyNum() == null || qzSetting.getPhoneCreateTopFiftyNum() == 0) {
                qzSetting.setPhoneCreateTopFiftyNum(fphoneCreateTopFiftyNum);
            }
            if (qzSetting.getPhoneCreateTopTenNum() == null || qzSetting.getPhoneCreateTopTenNum() == 0) {
                qzSetting.setPhoneCreateTopTenNum(fphoneCreateTopTenNum);
            }
        }catch (Exception e){
            logger.error("数据封装异常"+e.getMessage());
        }
        return qzSetting;
    }
}
