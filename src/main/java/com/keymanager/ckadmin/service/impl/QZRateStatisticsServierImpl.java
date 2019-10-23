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
//                只有一条记录的时候该设什么值？ 目前先设0
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
        List<Long> qzUuids = qzSettingService.getQZUuidsByUserID(qzRateStatisticsCountCriteria.getUserID(),qzRateStatisticsCountCriteria.getSearchEngine());
        qzRateStatisticsCountCriteria.setQzUuids(qzUuids);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String gtDate = f.format(new Date());
        qzRateStatisticsCountCriteria.setGtRateFullDate(gtDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, qzRateStatisticsCountCriteria.getQzRange());
        String ltDate = f.format(calendar.getTime());
        qzRateStatisticsCountCriteria.setLtRateFullDate(ltDate);
        return qzRateStatisticsDao.getQZRateStatisticCount(qzRateStatisticsCountCriteria);
    }

    @Override
    public Map generateEchartsData(List<QZRateStatisticsCountVO> qzRateStatisticsCountVOS) {
        Map echartsDataMap = new HashMap();
        List<String> dateList = new ArrayList<>();
        List<Integer> totalCountList = new ArrayList<>();
        List<Integer> riseCountList = new ArrayList<>();
        List<Integer> unchangeCountList = new ArrayList<>();
        List<Integer> fallCountList = new ArrayList<>();
        for (QZRateStatisticsCountVO qzRateStatisticsCountVO : qzRateStatisticsCountVOS) {
            dateList.add(qzRateStatisticsCountVO.getRateDate());
            totalCountList.add(qzRateStatisticsCountVO.getTotalCount());
            riseCountList.add(qzRateStatisticsCountVO.getRiseCount());
            unchangeCountList.add(qzRateStatisticsCountVO.getUnchangedCount());
            fallCountList.add(qzRateStatisticsCountVO.getFallCount());
        }
        echartsDataMap.put("date", dateList);
        echartsDataMap.put("totalCount", totalCountList);
        echartsDataMap.put("riseCount", riseCountList);
        echartsDataMap.put("unchangeCount", unchangeCountList);
        echartsDataMap.put("fallCount", fallCountList);
        return echartsDataMap;
    }
}
