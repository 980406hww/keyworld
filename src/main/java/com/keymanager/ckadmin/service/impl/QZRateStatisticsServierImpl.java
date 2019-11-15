package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.QZRateStatisticsCountCriteria;
import com.keymanager.ckadmin.dao.QZRateStatisticsDao;
import com.keymanager.ckadmin.entity.QZKeywordRankInfo;
import com.keymanager.ckadmin.entity.QZRateStatistics;
import com.keymanager.ckadmin.service.QZKeywordRankInfoService;
import com.keymanager.ckadmin.service.QZRateStatisticsService;
import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.ckadmin.vo.QZRateStatisticsCountVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service("qzRateStatisticsService2")
public class QZRateStatisticsServierImpl extends ServiceImpl<QZRateStatisticsDao, QZRateStatistics> implements QZRateStatisticsService {

    @Resource(name = "qzKeywordRankInfoService2")
    private QZKeywordRankInfoService qzKeywordRankInfoService;

    @Resource(name = "qzRateStatisticsDao2")
    private QZRateStatisticsDao qzRateStatisticsDao;

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    @Override
    public void generateQZRateStatistics() {
        List<QZKeywordRankInfo> qzKeywordRankInfos = qzKeywordRankInfoService.getXTRankInfos();
        List<QZRateStatistics> qzRateStatisticsList = new ArrayList<>();
        for (QZKeywordRankInfo qzKeywordRankInfo : qzKeywordRankInfos) {
            String[] dateList = qzKeywordRankInfo.getDate().replace("[", "").replace("]", "").replace("'", "").replace(" ", "").split(",");
            String[] fullDateList = qzKeywordRankInfo.getFullDate().replace("[", "").replace("]", "").replace("'", "").replace(" ", "").split(",");
            String[] curveData = qzKeywordRankInfo.getTopTen().replace("[", "").replace("]", "").replace(" ", "").split(",");
            String totalRecord = qzKeywordRankInfo.getTopTen();
            if (dateList.length >= 2) {
                QZRateStatistics qzRateStatistics = new QZRateStatistics();
                qzRateStatistics.setQzSettingUuid(qzKeywordRankInfo.getQzSettingUuid());
                qzRateStatistics.setTerminalType(qzKeywordRankInfo.getTerminalType());
                qzRateStatistics.setRate(0);
                qzRateStatistics.setRateDate(dateList[dateList.length - 1]);
                qzRateStatistics.setRateFullDate(fullDateList[fullDateList.length - 1]);
                qzRateStatisticsList.add(qzRateStatistics);
                for (int i = dateList.length - 2; i >= 0; i--) {
                    QZRateStatistics qzRateStatistics2 = new QZRateStatistics();
                    qzRateStatistics2.setQzSettingUuid(qzKeywordRankInfo.getQzSettingUuid());
                    qzRateStatistics2.setTerminalType(qzKeywordRankInfo.getTerminalType());
                    qzRateStatistics2.setRate(Integer.parseInt(curveData[i + 1]) - Integer.parseInt(curveData[i]));
                    qzRateStatistics2.setRateDate(dateList[i]);
                    qzRateStatistics2.setRateFullDate(fullDateList[i]);
                    qzRateStatisticsList.add(qzRateStatistics2);
                }
            } else {
                QZRateStatistics qzRateStatistics = new QZRateStatistics();
                qzRateStatistics.setQzSettingUuid(qzKeywordRankInfo.getQzSettingUuid());
                qzRateStatistics.setTerminalType(qzKeywordRankInfo.getTerminalType());
                // 只有一条记录的时候该设什么值？ 目前先设0
                qzRateStatistics.setRate(0);
                qzRateStatistics.setRateDate(dateList[0]);
                qzRateStatistics.setRateFullDate(fullDateList[0]);
                qzRateStatisticsList.add(qzRateStatistics);
            }
            qzRateStatisticsDao.insertOrUpdateQZRateStatistics(qzRateStatisticsList);
            qzRateStatisticsList.clear();
        }
    }

    @Override
    public List<QZRateStatisticsCountVO> getQZRateStatisticCount(QZRateStatisticsCountCriteria qzRateStatisticsCountCriteria) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String gtDate = f.format(new Date());
        qzRateStatisticsCountCriteria.setGtRateFullDate(gtDate + " 23:59:59");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, qzRateStatisticsCountCriteria.getQzRateRange());
        String ltDate = f.format(calendar.getTime());
        qzRateStatisticsCountCriteria.setLtRateFullDate(ltDate);
        return qzRateStatisticsDao.getQZRateStatisticCount(qzRateStatisticsCountCriteria);
    }

    @Override
    public Map generateEchartsData(List<QZRateStatisticsCountVO> qzRateStatisticsCountVos) {
        Map<String, Object> echartsDataMap = new HashMap<>(5);
        if (CollectionUtils.isNotEmpty(qzRateStatisticsCountVos)) {
            List<String> date = new ArrayList<>();
            List<Integer> totalCountList = new ArrayList<>();
            List<Integer> riseCountList = new ArrayList<>();
            List<Integer> unchangedCountList = new ArrayList<>();
            List<Integer> fallCountList = new ArrayList<>();
            for (QZRateStatisticsCountVO qzRateStatisticsCountVo : qzRateStatisticsCountVos) {
                date.add(qzRateStatisticsCountVo.getRateDate());
                totalCountList.add(qzRateStatisticsCountVo.getTotalCount());
                riseCountList.add(qzRateStatisticsCountVo.getRiseCount());
                unchangedCountList.add(qzRateStatisticsCountVo.getUnchangedCount());
                fallCountList.add(qzRateStatisticsCountVo.getFallCount());
            }
            echartsDataMap.put("date", date);
            echartsDataMap.put("totalCount", totalCountList);
            echartsDataMap.put("riseCount", riseCountList);
            echartsDataMap.put("unchangedCount", unchangedCountList);
            echartsDataMap.put("fallCount", fallCountList);
        }
        return echartsDataMap;
    }

    @Override
    public Integer getRate(Long qzUuid, String terminalType, String rateFullDate) {
        return qzRateStatisticsDao.getRate(qzUuid, terminalType, rateFullDate);
    }

    @Override
    public Map getQzRateHistory(String qzUuid, String terminalType) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date);
        String threeMonAgo = sdf.format(calendar.getTime());
        return qzRateStatisticsDao.getQzRateHistory(Long.parseLong(qzUuid), terminalType, today, threeMonAgo);
    }
}
