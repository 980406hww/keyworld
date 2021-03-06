package com.keymanager.ckadmin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.ckadmin.criteria.ExternalCaptureJobCriteria;
import com.keymanager.ckadmin.dao.CaptureRankJobDao;
import com.keymanager.ckadmin.dao.QZKeywordRankInfoDao;
import com.keymanager.ckadmin.entity.CaptureRankJob;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.QZKeywordRankInfo;
import com.keymanager.ckadmin.entity.QZOperationType;
import com.keymanager.ckadmin.enums.CaptureRankExectionStatus;
import com.keymanager.ckadmin.service.CaptureRankJobService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.QZChargeRuleService;
import com.keymanager.ckadmin.service.QZOperationTypeService;
import com.keymanager.ckadmin.util.StringUtil;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.ckadmin.vo.QZChargeRuleVO;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service("captureRankJobService2")
public class CaptureRankJobServiceImpl extends ServiceImpl<CaptureRankJobDao, CaptureRankJob> implements CaptureRankJobService {

    @Resource(name = "captureRankJobDao2")
    private CaptureRankJobDao captureRankJobDao;

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @Resource(name = "qzKeywordRankInfoDao2")
    private QZKeywordRankInfoDao qzKeywordRankInfoDao;

    @Resource(name = "qzChargeRuleService2")
    private QZChargeRuleService qzChargeRuleService;

    @Resource(name = "qzOperationTypeService2")
    private QZOperationTypeService qzOperationTypeService;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Override
    public void qzAddCaptureRankJob(String group, long qzSettingUuid, long customerUuid,
        String operationType, String userName) {
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
        captureRankJob.setRankJobArea("China");
        captureRankJob.setUpdateTime(new Date());
        captureRankJobDao.insert(captureRankJob);
    }

    @Override
    public CaptureRankJob findExistCaptureRankJob(Long qzSettingUuid, String operationType) {
        return captureRankJobDao.findExistCaptureRankJob(qzSettingUuid, operationType);
    }

    @Override
    public void deleteCaptureRankJob(Long qzSettingUuid, String operationType) {
        captureRankJobDao.deleteCaptureRankJob(qzSettingUuid, operationType);
    }

    @Override
    public Boolean hasUncompletedCaptureRankJob(List<String> groupNames, String rankJobArea) {
        return captureRankJobDao.hasUncompletedCaptureRankJob(groupNames, rankJobArea) != null;
    }

    @Override
    public Page<CaptureRankJob> selectPageByCriteria(CaptureRankJobSearchCriteria criteria) {
        Page<CaptureRankJob> page = new Page<>(criteria.getPage(), criteria.getLimit());
        page.setRecords(captureRankJobDao.selectPageByCriteria(page, criteria));
        Map<Long, Customer> customerMap = new HashMap<>(page.getRecords().size());
        for (CaptureRankJob captureRankJob : page.getRecords()) {
            if (captureRankJob.getCustomerUuid() != null) {
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
        return page;
    }

    @Override
    public void saveCaptureRankJob(Map map, String loginName) {
        CaptureRankJob captureRankJob = JSON.parseObject(JSON.toJSONString(map.get("captureRankJob")), CaptureRankJob.class);
        captureRankJob.setUpdateTime(new Date());
        captureRankJob.setUpdateBy(loginName);
        List list = (List) map.get("executeTimes");
        if (captureRankJob.getUuid() != null) {
            Date date = Utils.parseDate((String) list.get(0), "HH:mm:ss");
            captureRankJob.setExectionTime(new Time(date != null ? date.getTime() : 0));
            captureRankJobDao.updateById(captureRankJob);
        } else {
            Utils.handleObject(captureRankJob);
            for (Object strTime : list) {
                Date date = Utils.parseDate((String) strTime, "HH:mm:ss");
                captureRankJob.setExectionTime(new Time(date != null ? date.getTime() : 0));
                captureRankJob.setExectionStatus(CaptureRankExectionStatus.New.name());
                captureRankJob.setCreateBy(loginName);
                captureRankJobDao.insert(captureRankJob);
            }
        }
    }

    @Override
    public CaptureRankJob getCaptureRankJobAndCustomerName(Long uuid) {
        CaptureRankJob captureRankJob = captureRankJobDao.selectById(uuid);
        if (captureRankJob.getCustomerUuid() != null) {
            Customer customer = customerService.selectById(captureRankJob.getCustomerUuid());
            captureRankJob.setContactPerson(customer.getContactPerson());
        }
        return captureRankJob;
    }

    @Override
    public void changeCaptureRankJobStatus(CaptureRankJob captureRankJob, String loginName) {
        Boolean status = captureRankJob.getCaptureRankJobStatus();
        captureRankJob = captureRankJobDao.selectById(captureRankJob.getUuid());
        captureRankJob.setCaptureRankJobStatus(status);
        captureRankJob.setUpdateBy(loginName);
        captureRankJob.setUpdateTime(new Date());
        captureRankJobDao.updateById(captureRankJob);
    }

    @Override
    public void changeCaptureRankJobStatuses(List<Long> uuids, String loginName, boolean status) {
        captureRankJobDao.changeCaptureRankJobStatuses(uuids, loginName, status);
    }

    @Override
    public void resetCaptureRankJobs(List<Long> uuids) {
        captureRankJobDao.resetCaptureRankJobs(uuids);
    }

    @Override
    public List<Long> getCaptureRankJobUuids(List<Long> uuids) {
        return captureRankJobDao.getCaptureRankJobUuids(uuids);
    }

    @Override
    public void updateCaptureRankJobCustomerUuids(List<Long> jobUuids, Long customerUuid) {
        captureRankJobDao.updateCaptureRankJobCustomerUuids(jobUuids, customerUuid);
    }

    @Override
    public void searchFiveMiniSetCheckingJobs() {
        List<CaptureRankJob> captureRankJobs = captureRankJobDao.searchFiveMiniSetCheckingJobs();
        if (CollectionUtils.isNotEmpty(captureRankJobs)) {
            for (CaptureRankJob captureRankJob : captureRankJobs) {
                if (captureRankJobDao.searchThreeMiniStatusEqualsOne(captureRankJob) > 0) {
                    captureRankJob.setExectionStatus(CaptureRankExectionStatus.Processing.name());
                } else {
                    if (captureRankJob.getQzSettingUuid() != null) {
                        updateGenerationCurve(captureRankJob);
                    }
                    captureRankJob.setEndTime(new Date());
                    captureRankJob.setLastExecutionDate(new java.sql.Date(System.currentTimeMillis()));
                    captureRankJob.setExectionStatus(CaptureRankExectionStatus.Complete.name());
                    String key = captureRankJob.getOperationType() + "_" + captureRankJob.getGroupNames() + "_" + captureRankJob.getCustomerUuid();
                    customerKeywordService.clearOptimizeGroupNameQueueForKey(key);
                }
                captureRankJobDao.updateById(captureRankJob);
            }
        }
    }

    @Override
    public CaptureRankJob provideCaptureRankJob(ExternalCaptureJobCriteria captureJobCriteria) {
        if (StringUtil.isNullOrEmpty(captureJobCriteria.getRankJobArea())) {
            captureJobCriteria.setRankJobArea("China");
        }
        // 取 Processing
        CaptureRankJob captureRankJob = captureRankJobDao.getProcessingJob(captureJobCriteria);
        if (captureRankJob == null) {
            if (captureJobCriteria.getRankJobType() != null && "Common".equals(captureJobCriteria.getRankJobType())) {
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

    @Override
    public Boolean getCaptureRankJobStatus(Long captureRankJobUuid) {
        return captureRankJobDao.getCaptureRankJobStatus(captureRankJobUuid);
    }

    @Override
    public void completeCaptureRankJob(CaptureRankJob captureRankJob) {
        captureRankJob = captureRankJobDao.selectById(captureRankJob.getUuid());
        captureRankJob.setExectionStatus(com.keymanager.monitoring.enums.CaptureRankExectionStatus.Complete.name());
        captureRankJob.setEndTime(new Date());
        captureRankJob.setLastExecutionDate(new java.sql.Date(System.currentTimeMillis()));
        captureRankJobDao.updateById(captureRankJob);
    }

    @Override
    public void completeCaptureRankJobTempTwo(CaptureRankJob captureRankJob) {
        captureRankJob = captureRankJobDao.selectById(captureRankJob.getUuid());
        captureRankJob.setExectionStatus(CaptureRankExectionStatus.Checking.name());
        captureRankJob.setEndTime(new Date());
        captureRankJobDao.updateById(captureRankJob);
    }

    @Override
    public CaptureRankJob checkingCaptureRankJobCompleted(ExternalCaptureJobCriteria captureJobCriteria) {
        return captureRankJobDao.provideCaptureRankJob(1, captureJobCriteria);
    }

    private void updateGenerationCurve(CaptureRankJob captureRankJob) {
        QZKeywordRankInfo qzKeywordRankInfo = qzKeywordRankInfoDao.selectByQZSettingUuid(captureRankJob.getQzSettingUuid(), captureRankJob.getOperationType());
        if (qzKeywordRankInfo == null) {
            captureRankJobDao.deleteById(captureRankJob.getUuid());
            return;
        }
        Map<String, Long> map = captureRankJobDao.searchCountByPosition(captureRankJob);
        int topTenNum = Integer.parseInt(map.get("topTenNum").toString());
        int topTwentyNum = Integer.parseInt(map.get("topTwentyNum").toString());
        int topThirtyNum = Integer.parseInt(map.get("topThirtyNum").toString());
        int topFortyNum = Integer.parseInt(map.get("topFortyNum").toString());
        int topFiftyNum = Integer.parseInt(map.get("topFiftyNum").toString());

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
        this.calculateData(qzKeywordRankInfo, topTenNum);

        qzKeywordRankInfo.setUpdateTime(new Date());
        qzKeywordRankInfoDao.updateById(qzKeywordRankInfo);
    }

    private String addData(String string, String num) {
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

    private String replaceData(String string, String num) {
        StringBuilder sb = new StringBuilder(string);
        int index = sb.indexOf(",");
        sb.replace(0, index, "[" + num);
        return sb.toString();
    }

    private void calculateData(QZKeywordRankInfo qzKeywordRankInfo, int topTenNum) {
        List<QZChargeRuleVO> qzChargeRules = qzChargeRuleService.findQZChargeRules(qzKeywordRankInfo.getQzSettingUuid(), qzKeywordRankInfo.getTerminalType(), qzKeywordRankInfo.getWebsiteType());
        // 初始化达标等级，总等级
        qzKeywordRankInfo.setAchieveLevel(0);
        qzKeywordRankInfo.setSumSeries(qzChargeRules.size());
        qzKeywordRankInfo.setDifferenceValue(1.0);
        qzKeywordRankInfo.setCurrentPrice(0);
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
        // 1代表上次达标过，0为未达标或者掉过
        int lastAchieve = qzOperationType.getStandardTime() == null ? 0 : 1;
        // 1为要更新成最新达标时间，0为本次未达标时间置空
        int updateFlag = 0;
        if (qzKeywordRankInfo.getAchieveLevel() != null && qzKeywordRankInfo.getAchieveLevel() > 0) {
            qzKeywordRankInfo.setAchieveTime(new Date());
            updateFlag = 1;
        } else {
            qzKeywordRankInfo.setAchieveTime(null);
        }
        qzOperationTypeService.updateStandardTimeByUuid(qzOperationType.getUuid(), updateFlag, lastAchieve);
    }
}
