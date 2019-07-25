package com.keymanager.monitoring.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.monitoring.criteria.ExternalCaptureJobCriteria;
import com.keymanager.monitoring.dao.CaptureRankJobDao;
import com.keymanager.monitoring.dao.CustomerKeywordDao;
import com.keymanager.monitoring.dao.QZKeywordRankInfoDao;
import com.keymanager.monitoring.entity.CaptureRankJob;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.QZKeywordRankInfo;
import com.keymanager.monitoring.entity.QZOperationType;
import com.keymanager.monitoring.enums.CaptureRankExectionStatus;
import com.keymanager.monitoring.vo.QZChargeRuleVO;
import com.keymanager.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj24 on 2017/9/26.
 */
@Service
public class CaptureRankJobService extends ServiceImpl<CaptureRankJobDao, CaptureRankJob> {
    @Autowired
    private CaptureRankJobDao captureRankJobDao;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private QZKeywordRankInfoDao qzKeywordRankInfoDao;

    @Autowired
    private QZOperationTypeService qzOperationTypeService;

    @Autowired
    private QZChargeRuleService qzChargeRuleService;

    @Autowired
    private CustomerKeywordDao customerKeywordDao;

    public synchronized CaptureRankJob provideCaptureRankJob(ExternalCaptureJobCriteria captureJobCriteria) {
        if (captureJobCriteria.getRankJobArea() == null || captureJobCriteria.getRankJobArea().equals("")) {
            captureJobCriteria.setRankJobArea("China");
        }
        // 取 Processing
        CaptureRankJob captureRankJob = captureRankJobDao.getProcessingJob(captureJobCriteria);
        if (captureRankJob == null) {
            if (captureJobCriteria.getRankJobType() != null && captureJobCriteria.getRankJobType().equals("Common")) {
                // 先取普通
                captureRankJob = captureRankJobDao.provideCaptureRankJob(0, captureJobCriteria);
                if (captureRankJob == null) {
                    // 再取全站
                    captureRankJob = captureRankJobDao.provideCaptureRankJob(1, captureJobCriteria);
                }
            } else {
                captureRankJob = captureRankJobDao.provideCaptureRankJob(null, captureJobCriteria);
            }
            if (captureRankJob != null) {
                captureRankJob.setStartTime(new Date());
                captureRankJob.setExectionStatus(CaptureRankExectionStatus.Processing.name());
                captureRankJobDao.updateById(captureRankJob);
            }
        }
        return captureRankJob;
    }

    public Page<CaptureRankJob> searchCaptureRankJob(Page<CaptureRankJob> page, CaptureRankJobSearchCriteria captureRankJobSearchCriteria) {
        List<CaptureRankJob> captureRankJobs = captureRankJobDao.searchCaptureRankJobs(page, captureRankJobSearchCriteria);
        Map<Long, Customer> customerMap = new HashMap<Long, Customer>();
        for (CaptureRankJob captureRankJob : captureRankJobs) {
            if (captureRankJob.getCustomerUuid() != null && !captureRankJob.getCustomerUuid().equals("")) {
                Customer customer = customerMap.get(captureRankJob.getCustomerUuid());
                if (customer == null) {
                    customer = customerService.selectById(captureRankJob.getCustomerUuid());
                    customerMap.put(captureRankJob.getCustomerUuid(), customer);
                }
                if (customer != null) {
                    captureRankJob.setContactPerson(customer.getContactPerson());
                }
            }
        }
        page.setRecords(captureRankJobs);
        return page;
    }

    public CaptureRankJob getCaptureRankJobAndCustomerName(Long uuid) {
        CaptureRankJob captureRankJob = captureRankJobDao.selectById(uuid);
        if (captureRankJob.getCustomerUuid() != null) {
            Customer customer = customerService.selectById(captureRankJob.getCustomerUuid());
            captureRankJob.setContactPerson(customer.getContactPerson());
        }
        return captureRankJob;
    }

    public void saveCaptureRankJob(Map map, String terminalType, String loginName) {
        CaptureRankJob captureRankJob = JSON.parseObject(JSON.toJSONString(map.get("captureRankJob")), CaptureRankJob.class);
        captureRankJob.setUpdateTime(new Date());
        captureRankJob.setUpdateBy(loginName);
        captureRankJob.setOperationType(terminalType);

        List list = (List) map.get("executeTimes");
        if (captureRankJob.getUuid() != null) {
            Date date = Utils.parseDate((String) list.get(0), "HH:mm:ss");
            captureRankJob.setExectionTime(new Time(date != null ? date.getTime() : 0));
            captureRankJobDao.updateById(captureRankJob);
        } else {
            for (Object strTime : list) {
                Date date = Utils.parseDate((String) strTime, "HH:mm:ss");
                captureRankJob.setExectionTime(new Time(date != null ? date.getTime() : 0));
                captureRankJob.setExectionStatus(CaptureRankExectionStatus.New.name());
                captureRankJob.setCreateBy(loginName);
                captureRankJobDao.insert(captureRankJob);
                captureRankJob.setUuid(null);
            }
        }
    }

    public void completeCaptureRankJob(CaptureRankJob captureRankJob) {
        captureRankJob = captureRankJobDao.selectById(captureRankJob.getUuid());
        captureRankJob.setExectionStatus(CaptureRankExectionStatus.Complete.name());
        captureRankJob.setEndTime(new Date());
        captureRankJob.setLastExecutionDate(new java.sql.Date(new Date().getTime()));
        captureRankJobDao.updateById(captureRankJob);
    }

    public void completeCaptureRankJobTemp(CaptureRankJob captureRankJob) {
        captureRankJob = captureRankJobDao.selectById(captureRankJob.getUuid());
        if (captureRankJob.getQzSettingUuid() != null) {
            // 生成曲线
            updateGenerationCurve(captureRankJob);
        }
        captureRankJob.setExectionStatus(CaptureRankExectionStatus.Complete.name());
        captureRankJob.setEndTime(new Date());
        captureRankJob.setLastExecutionDate(new java.sql.Date(new Date().getTime()));
        captureRankJobDao.updateById(captureRankJob);
    }

    public void completeCaptureRankJobTempTwo(CaptureRankJob captureRankJob) {
        captureRankJob = captureRankJobDao.selectById(captureRankJob.getUuid());
        captureRankJob.setExectionStatus(CaptureRankExectionStatus.Checking.name());
        captureRankJob.setEndTime(new Date());
        captureRankJobDao.updateById(captureRankJob);
    }

    public void searchFiveMiniSetCheckingJobs() {
        List<CaptureRankJob> captureRankJobs = captureRankJobDao.searchFiveMiniSetCheckingJobs();
        if (captureRankJobs != null && captureRankJobs.size() != 0) {
            for (CaptureRankJob captureRankJob : captureRankJobs) {
                if (captureRankJobDao.searchThreeMiniStatusEqualsOne(captureRankJob) > 0 ) {
                    captureRankJob.setExectionStatus(CaptureRankExectionStatus.Processing.name());
                } else {
                    if (captureRankJob.getQzSettingUuid() != null) {
                        updateGenerationCurve(captureRankJob);
                    }
                    captureRankJob.setEndTime(new Date());
                    captureRankJob.setLastExecutionDate(new java.sql.Date(new Date().getTime()));
                    captureRankJob.setExectionStatus(CaptureRankExectionStatus.Complete.name());
                }
                captureRankJobDao.updateById(captureRankJob);
            }
        }
    }

    public void updateGenerationCurve(CaptureRankJob captureRankJob) {
        QZKeywordRankInfo qzKeywordRankInfo = qzKeywordRankInfoDao.selectByQZSettingUuid(captureRankJob.getQzSettingUuid(), captureRankJob.getOperationType());
        if (qzKeywordRankInfo == null) {
            captureRankJobDao.deleteById(captureRankJob.getUuid());
            return;
        }
        int topTenNum = captureRankJobDao.searchCountByPosition(captureRankJob, 10);
        int topTwentyNum = captureRankJobDao.searchCountByPosition(captureRankJob, 20);
        int topThirtyNum = captureRankJobDao.searchCountByPosition(captureRankJob, 30);
        int topFortyNum = captureRankJobDao.searchCountByPosition(captureRankJob, 40);
        int topFiftyNum = captureRankJobDao.searchCountByPosition(captureRankJob, 50);

        String dateString = qzKeywordRankInfo.getDate();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

        if (dateString == null || "".equals(dateString)) {
            // 首次任务，直接添加
            qzKeywordRankInfo.setTopTen("[" + topTenNum + "]");
            qzKeywordRankInfo.setTopTwenty("[" + topTwentyNum + "]");
            qzKeywordRankInfo.setTopThirty("[" + topThirtyNum + "]");
            qzKeywordRankInfo.setTopForty("[" + topFortyNum + "]");
            qzKeywordRankInfo.setTopFifty("[" + topFiftyNum + "]");
            qzKeywordRankInfo.setDate("['" + sdf.format(date) + "']");
        } else {
            // 非首次，累加或者替换
            String[] dateStrings = dateString.substring(1, dateString.length() - 1).split(", ");
            String nowDate = dateStrings[0];
            if (nowDate.equals("'" + sdf.format(date) + "'")) {
                // 为当天时间，替换数据
                if (dateStrings.length == 1) {
                    // 只有一个数据
                    qzKeywordRankInfo.setTopTen("[" + topTenNum + "]");
                    qzKeywordRankInfo.setTopTwenty("[" + topTwentyNum + "]");
                    qzKeywordRankInfo.setTopThirty("[" + topThirtyNum + "]");
                    qzKeywordRankInfo.setTopForty("[" + topFortyNum + "]");
                    qzKeywordRankInfo.setTopFifty("[" + topFiftyNum + "]");
                } else {
                    qzKeywordRankInfo.setTopTen(replaceData(qzKeywordRankInfo.getTopTen(), topTenNum + ""));
                    qzKeywordRankInfo.setTopTwenty(replaceData(qzKeywordRankInfo.getTopTwenty(), topTwentyNum + ""));
                    qzKeywordRankInfo.setTopThirty(replaceData(qzKeywordRankInfo.getTopThirty(), topThirtyNum + ""));
                    qzKeywordRankInfo.setTopForty(replaceData(qzKeywordRankInfo.getTopForty(), topFortyNum + ""));
                    qzKeywordRankInfo.setTopFifty(replaceData(qzKeywordRankInfo.getTopFifty(), topFiftyNum + ""));
                }
            } else {
                // 不为当天，添加数据
                qzKeywordRankInfo.setTopTen(addData(qzKeywordRankInfo.getTopTen(), topTenNum + ""));
                qzKeywordRankInfo.setTopTwenty(addData(qzKeywordRankInfo.getTopTwenty(), topTwentyNum + ""));
                qzKeywordRankInfo.setTopThirty(addData(qzKeywordRankInfo.getTopThirty(), topThirtyNum + ""));
                qzKeywordRankInfo.setTopForty(addData(qzKeywordRankInfo.getTopForty(), topFortyNum + ""));
                qzKeywordRankInfo.setTopFifty(addData(qzKeywordRankInfo.getTopFifty(), topFiftyNum + ""));
                qzKeywordRankInfo.setDate(addData(qzKeywordRankInfo.getDate(), "'" + sdf.format(date) + "'"));
            }
        }

        if (qzKeywordRankInfo.getCreateTopTenNum() == null) {
            qzKeywordRankInfo.setCreateTopTenNum(topTenNum);
        }
        if (qzKeywordRankInfo.getCreateTopFiftyNum() == null) {
            qzKeywordRankInfo.setCreateTopFiftyNum(topFiftyNum);
        }
        String weekDataString = qzKeywordRankInfo.getTopTen().substring(1, qzKeywordRankInfo.getTopTen().length() - 1);
        String[] weekData = weekDataString.split(", ");
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        String increase;
        int oneWeekDiff;
        if (weekData.length < 7) {
            int lastDayNum = Integer.parseInt(weekData[weekData.length - 1]);
            if (lastDayNum == 0) {
                increase = topTenNum == 0 ? "0" : "1";
                oneWeekDiff = topTenNum;
            } else {
                increase = decimalFormat.format((double) (topTenNum - Integer.parseInt(weekData[weekData.length - 1])) / lastDayNum);
                oneWeekDiff = topTenNum - Integer.parseInt(weekData[weekData.length - 1]);
            }
        } else {
            int weekDayNum = Integer.parseInt(weekData[6]);
            if (weekDayNum == 0) {
                increase = topTenNum == 0 ? "0" : "1";
                oneWeekDiff = topTenNum;
            } else {
                increase = decimalFormat.format((double) (topTenNum - Integer.parseInt(weekData[6])) / weekDayNum);
                oneWeekDiff = topTenNum - Integer.parseInt(weekData[6]);
            }
        }
        qzKeywordRankInfo.setIncrease(Double.parseDouble(increase));
        qzKeywordRankInfo.setOneWeekDifference(oneWeekDiff);
        qzKeywordRankInfo.setTodayDifference(weekData.length > 1 ? topTenNum - Integer.parseInt(weekData[1]) : 0);

        // 判断是否达标，返回达标等级，近下一达标比率
        qzKeywordRankInfo = calculateData(qzKeywordRankInfo, topTenNum);

        qzKeywordRankInfo.setUpdateTime(new Date());
        qzKeywordRankInfoDao.updateById(qzKeywordRankInfo);
    }

    public String addData(String string, String num) {
        if (string == null) {
            return "[" + num + "]";
        } else {
            String[] topTenStrings = string.split(", ");
            StringBuilder sb = new StringBuilder(string);
            sb.replace(0, 1, "[" + num + ", ");
            if (topTenStrings.length >= 90) {
                int end = sb.lastIndexOf(",");
                sb.replace(end, sb.length(), "]");
            }
            return sb.toString();
        }
    }

    public String replaceData(String string, String num) {
        StringBuilder sb = new StringBuilder(string);
        int index = sb.indexOf(",");
        sb.replace(0, index, "[" + num);
        return sb.toString();
    }

    public QZKeywordRankInfo calculateData(QZKeywordRankInfo qzKeywordRankInfo, int topTenNum) {
        List<QZChargeRuleVO> qzChargeRules = qzChargeRuleService.findQZChargeRules(qzKeywordRankInfo.getQzSettingUuid(), qzKeywordRankInfo.getTerminalType(), qzKeywordRankInfo.getWebsiteType());
        // 初始化达标等级，总等级
        qzKeywordRankInfo.setAchieveLevel(0);
        qzKeywordRankInfo.setSumSeries(qzChargeRules.size());
        qzKeywordRankInfo.setDifferenceValue(1.0);
        for (int i = 0; i < qzChargeRules.size(); i++) {
            if (topTenNum >= Integer.parseInt(qzChargeRules.get(i).getStartKeywordCount())) {
                qzKeywordRankInfo.setAchieveLevel(i + 1);
                qzKeywordRankInfo.setCurrentPrice(Integer.parseInt(qzChargeRules.get(i).getAmount()));
            } else {
                DecimalFormat decimalFormat = new DecimalFormat("0.0000");
                int target = Integer.parseInt(qzChargeRules.get(i).getStartKeywordCount());
                String diffValue = decimalFormat.format((double) (target - topTenNum) / target);
                qzKeywordRankInfo.setDifferenceValue(Double.parseDouble(diffValue));
                break;
            }
        }

        QZOperationType qzOperationType = qzOperationTypeService.searchQZOperationTypeByQZSettingAndTerminalType(qzKeywordRankInfo.getQzSettingUuid(), qzKeywordRankInfo.getTerminalType());
        int lastAchieve = qzOperationType.getStandardTime() == null ? 0 : 1; // 1代表上次达标过，0为未达标或者掉过
        int updateFlag = 0; // 1为要更新成最新达标时间，0为本次未达标时间置空
        if (qzKeywordRankInfo.getAchieveLevel() != null && qzKeywordRankInfo.getAchieveLevel() > 0) {
            qzKeywordRankInfo.setAchieveTime(new Date());
            updateFlag = 1;
        } else {
            qzKeywordRankInfo.setAchieveTime(null);
        }
        qzOperationTypeService.updateStandardTimeByUuid(qzOperationType.getUuid(), updateFlag, lastAchieve);
        return qzKeywordRankInfo;
    }

    public Boolean getCaptureRankJobStatus(Long captureRankJobUuid) {
        return captureRankJobDao.getCaptureRankJobStatus(captureRankJobUuid);
    }

    public void changeCaptureRankJobStatus(Long uuid, String status) {
        CaptureRankJob captureRankJob = captureRankJobDao.selectById(uuid);
        captureRankJob.setCaptureRankJobStatus(status.equals("true") ? true : false);
        captureRankJobDao.updateById(captureRankJob);
    }

    public void resetCaptureRankJobs(List uuids) {
        captureRankJobDao.resetCaptureRankJobs(uuids);
    }

    public Boolean hasUncompletedCaptureRankJob(List<String> groupNames, String rankJobArea) {
        return captureRankJobDao.hasUncompletedCaptureRankJob(groupNames, rankJobArea) != null;
    }

    public Boolean hasCaptureRankJob() {
        return captureRankJobDao.fetchCaptureRankJob() != null;
    }

    public void deleteCaptureRankJob(Long qzSettingUuid, String operationType) {
        captureRankJobDao.deleteCaptureRankJob(qzSettingUuid, operationType);
    }

    public void qzAddCaptureRankJob(String group, long qzSettingUuid, long customerUuid, String operationType, String userName) {
        CaptureRankJob captureRankJob = new CaptureRankJob();
        captureRankJob.setGroupNames(group);
        captureRankJob.setQzSettingUuid(qzSettingUuid);
        captureRankJob.setCustomerUuid(customerUuid);
        captureRankJob.setOperationType(operationType);
        captureRankJob.setExectionType("Everyday");
        captureRankJob.setCaptureInterval(500);
        captureRankJob.setPageSize(50);
        captureRankJob.setRowNumber(50);
        captureRankJob.setExectionTime(Time.valueOf("00:05:00"));
        captureRankJob.setExectionStatus(CaptureRankExectionStatus.New.name());
        captureRankJob.setCreateBy(userName);
        captureRankJob.setUpdateBy(userName);
        captureRankJob.setRankJobType("Common");// 配上QZSettingUuid确定为整站任务
        captureRankJob.setUpdateTime(new Date());
        captureRankJobDao.insert(captureRankJob);
    }

    public CaptureRankJob findExistCaptureRankJob(Long qzSettingUuid, String operationType) {
        return captureRankJobDao.findExistCaptureRankJob(qzSettingUuid, operationType);
    }

    public void updateCaptureRankJobsStatus(List<Long> uuids, String updateBy, boolean captureRankJobStatus) {
        captureRankJobDao.updateCaptureRankJobsStatus(uuids, updateBy, captureRankJobStatus);
    }
}
