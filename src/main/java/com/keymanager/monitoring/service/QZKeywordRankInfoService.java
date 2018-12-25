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

    public List<QZKeywordRankInfo> searchExistingQZKeywordRankInfo (Long uuid, Boolean increaseType, String terminalType) {
        return qzKeywordRankInfoDao.searchExistingQZKeywordRankInfo(uuid, increaseType, terminalType);
    }

    public void deleteByQZSettingUuid (Long uuid) {
        qzKeywordRankInfoDao.deleteByQZSettingUuid(uuid);
    }

    public List<ExternalQzSettingVO> getQZSettingTask(){
        Config config = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_CRAWLER_HOUR);
        List<ExternalQzSettingVO> qzSettingTasks = qzKeywordRankInfoDao.getQZSettingTask(Integer.parseInt(config.getValue()));
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
        }catch (Exception e){
            logger.error("数据封装异常"+e.getMessage());
        }
        return qzKeywordRankInfo;
    }

    public QZSettingSearchCriteria getCountDownAndUp(QZSettingSearchCriteria qzSettingSearchCriteria){
        Config uppperConfig = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_UPPER_VALUE);
        Config lowerConfig = configService.getConfig(Constants.CONFIG_TYPE_QZSETTING_KEYWORD_RANK, Constants.CONFIG_KEY_LOWER_VALUE);
        if (null == qzSettingSearchCriteria.getTerminalType()) {
            qzSettingSearchCriteria.setTerminalType("PC");
        }
        QZSettingSearchCriteria countDownAndUpQZSettingSearchCriteria = qzKeywordRankInfoDao.getCountDownAndUp(Double.parseDouble(uppperConfig.getValue()), Double.parseDouble(lowerConfig.getValue()), qzSettingSearchCriteria.getTerminalType());
        qzSettingSearchCriteria.setUpperValue(Double.parseDouble(uppperConfig.getValue()));
        qzSettingSearchCriteria.setLowerValue(Double.parseDouble(lowerConfig.getValue()));
        qzSettingSearchCriteria.setUpNum(countDownAndUpQZSettingSearchCriteria.getUpNum());
        qzSettingSearchCriteria.setDownNum(countDownAndUpQZSettingSearchCriteria.getDownNum());
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
}
