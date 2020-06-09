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
import com.keymanager.util.common.StringUtil;
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

    public Boolean getCaptureRankJobStatus(Long captureRankJobUuid) {
        return captureRankJobDao.getCaptureRankJobStatus(captureRankJobUuid);
    }

    public void changeCaptureRankJobStatus(Long uuid, String status) {
        CaptureRankJob captureRankJob = captureRankJobDao.selectById(uuid);
        captureRankJob.setCaptureRankJobStatus("true".equals(status));
        captureRankJobDao.updateById(captureRankJob);
    }

    public void resetCaptureRankJobs(List uuids) {
        captureRankJobDao.resetCaptureRankJobs(uuids);
    }

    public Boolean hasUncompletedCaptureRankJob(List<String> groupNames, String rankJobArea) {
        return captureRankJobDao.hasUncompletedCaptureRankJob(groupNames, rankJobArea) != null;
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
        // 配上QZSettingUuid确定为整站任务
        captureRankJob.setRankJobType("Common");
        captureRankJob.setRankJobArea("China");
        captureRankJob.setUpdateTime(new Date());
        captureRankJobDao.insert(captureRankJob);
    }

    public CaptureRankJob findExistCaptureRankJob(Long qzSettingUuid, String operationType) {
        return captureRankJobDao.findExistCaptureRankJob(qzSettingUuid, operationType);
    }

    public void updateCaptureRankJobsStatus(List<Long> uuids, String updateBy, boolean captureRankJobStatus) {
        captureRankJobDao.updateCaptureRankJobsStatus(uuids, updateBy, captureRankJobStatus);
    }

    public CaptureRankJob checkingCaptureRankJobCompleted(ExternalCaptureJobCriteria captureJobCriteria) {
        return captureRankJobDao.provideCaptureRankJob(1, captureJobCriteria);
    }

    public int checkCaptureJobCompletedByCustomerUuid(Long customerUuid) {
        return captureRankJobDao.checkCaptureJobCompletedByCustomerUuid(customerUuid);
    }
}
