package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.QZRateStatisticsCountCriteria;
import com.keymanager.ckadmin.entity.QZKeywordRankInfo;
import com.keymanager.ckadmin.entity.QZRateStatistics;
import com.keymanager.ckadmin.service.QZKeywordRankInfoService;
import com.keymanager.ckadmin.service.QZRateStatisticsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ExternalQZRateStatisticsController
 * @Description TODO
 * @Author lhc
 * @Date 2019/10/22 10:44
 * @Version 1.0
 */
@RestController
@RequestMapping("/external/qzRateStatistics")
public class ExternalQZRateStatisticsController {


    @Resource(name = "qzRateStatisticsService2")
    private QZRateStatisticsService qzRateStatisticsService;

    @Resource(name = "qzKeywordRankInfoService2")
    private QZKeywordRankInfoService qzKeywordRankInfoService;


    @RequestMapping("/getExistingQZKeywordRankInfo")
    public Map getExistingQZKeywordRankInfo(@RequestBody Map requestMap) {
        String qzUuid = (String) requestMap.get("qzUuid");
        String terminalType = (String) requestMap.get("terminalType");
        String websiteType = (String) requestMap.get("websiteType");

        List<QZKeywordRankInfo> list = new ArrayList();
        list = qzKeywordRankInfoService.searchExistingQZKeywordRankInfo(Long.valueOf(qzUuid), terminalType, websiteType);
        List<QZRateStatistics> qzRateStatisticsList = new ArrayList<>();
        for (QZKeywordRankInfo qzKeywordRankInfo : list) {
            String[] dateList = qzKeywordRankInfo.getDate().replace("[", "").replace("]", "").replace("'", "").replace(" ", "").split(",");
            String[] curveData = qzKeywordRankInfo.getTopTen().replace("[", "").replace("]", "").replace(" ", "").split(",");
            String totalRecord = qzKeywordRankInfo.getTopTen();
            QZRateStatistics qzRateStatistics = new QZRateStatistics();
            qzRateStatistics.setQzSettingUuid(Long.parseLong(qzUuid));
            qzRateStatistics.setTerminalType(terminalType);
            qzRateStatistics.setRate(Integer.parseInt(curveData[dateList.length - 2]) - Integer.parseInt(curveData[dateList.length - 1]));
            qzRateStatistics.setRateDate(dateList[dateList.length - 1]);
            qzRateStatisticsList.add(qzRateStatistics);
            for (int i = dateList.length - 2; i > 0; i--) {
                QZRateStatistics qzRateStatistics2 = new QZRateStatistics();
                qzRateStatistics2.setQzSettingUuid(Long.parseLong(qzUuid));
                qzRateStatistics2.setTerminalType(terminalType);
                qzRateStatistics2.setRate(Integer.parseInt(curveData[i - 1]) - Integer.parseInt(curveData[i]));
                qzRateStatistics2.setRateDate(dateList[0]);
                qzRateStatisticsList.add(qzRateStatistics2);
            }
        }
        Map map = new HashMap();
        map.put("qzKeywordRankInfoList", list);
        map.put("qzRateStatisticsList", qzRateStatisticsList);

        return map;

    }

    @RequestMapping("/excuteTask")
    public void excuteTask(){
        qzRateStatisticsService.generateQZRateStatistics();
    }

    @RequestMapping("/getQZRateStatisticCount")
    public List getQZRateStatisticCount(@RequestBody QZRateStatisticsCountCriteria qzRateStatisticsCountCriteria){

       return qzRateStatisticsService.getQZRateStatisticCount(qzRateStatisticsCountCriteria);
    }

    @RequestMapping("/generateEchartsData")
    public ResultBean generateEchartsData(@RequestBody QZRateStatisticsCountCriteria qzRateStatisticsCountCriteria){
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            resultBean.setData(qzRateStatisticsService.generateEchartsData(qzRateStatisticsService.getQZRateStatisticCount(qzRateStatisticsCountCriteria)));
            return resultBean;
        }catch (Exception e){
            e.printStackTrace();
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }

    }
    @RequestMapping("fixQZXTRankDate")
    public void fixQZXTRankDate(){
        List<QZKeywordRankInfo>  qzKeywordRankInfos = qzKeywordRankInfoService.getXTRankInfos();
        for (QZKeywordRankInfo qzKeywordRankInfo : qzKeywordRankInfos) {
            String[] dateList = qzKeywordRankInfo.getDate().replace("[", "").replace("]", "").replace("'", "").replace(" ", "").split(",");
            StringBuilder sb = new StringBuilder("[");
            for (String dateStr : dateList){
                sb.append("'2019-"+dateStr+"',");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("]");
            qzKeywordRankInfoService.fixQZXTRankDate(qzKeywordRankInfo.getUuid(),sb.toString());
        }
    }
}
