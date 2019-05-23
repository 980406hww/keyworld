package com.keymanager.monitoring.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.monitoring.dao.CaptureRankJobDao;
import com.keymanager.monitoring.entity.CaptureRankJob;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.enums.CaptureRankExectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
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

    public synchronized CaptureRankJob provideCaptureRankJob() {
        CaptureRankJob captureRankJob = captureRankJobDao.getProcessingJob();
        if (captureRankJob == null) {
            captureRankJob = captureRankJobDao.provideCaptureRankJob();
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
        for (Object strTime : list) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            try {
                Date d = format.parse((String) strTime);
                captureRankJob.setExectionTime(new Time(d.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (captureRankJob.getUuid() != null) {
                captureRankJobDao.updateById(captureRankJob);
                return;
            }
            captureRankJob.setExectionStatus(CaptureRankExectionStatus.New.name());
            captureRankJob.setCreateBy(loginName);
            captureRankJobDao.insert(captureRankJob);
            captureRankJob.setUuid(null);
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
        captureRankJob.setExectionStatus(CaptureRankExectionStatus.Checking.name());
        captureRankJob.setEndTime(new Date());
        captureRankJobDao.updateById(captureRankJob);
    }

    public synchronized void searchFiveMiniSetCheckingJobs() {
        List<CaptureRankJob> captureRankJobs = captureRankJobDao.searchFiveMiniSetCheckingJobs();
        if (captureRankJobs != null && captureRankJobs.size() != 0) {
            for (CaptureRankJob captureRankJob : captureRankJobs) {
                if (captureRankJobDao.searchThreeMiniStatusEqualsOne(captureRankJob.getOperationType(), captureRankJob.getGroupNames()) > 0) {
                    captureRankJob.setExectionStatus(CaptureRankExectionStatus.Processing.name());
                } else {
                    captureRankJob.setEndTime(new Date());
                    captureRankJob.setLastExecutionDate(new java.sql.Date(new Date().getTime()));
                    captureRankJob.setExectionStatus(CaptureRankExectionStatus.Complete.name());
                }
                captureRankJobDao.updateById(captureRankJob);
            }
        }
    }

    public Boolean getCaptureRankJobStatus(Long captureRankJobUuid) {
        return captureRankJobDao.getCaptureRankJobStatus(captureRankJobUuid);
    }

    public void changeCaptureRankJobStatus(Long uuid, String status) {
        CaptureRankJob captureRankJob = captureRankJobDao.selectById(uuid);
        captureRankJob.setCaptureRankJobStatus(status.equals("true") ? true : false);
        captureRankJobDao.updateById(captureRankJob);
    }

    public Boolean hasCaptureRankJob() {
        return captureRankJobDao.fetchCaptureRankJob() != null;
    }
}
