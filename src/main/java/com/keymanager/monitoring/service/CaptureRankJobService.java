package com.keymanager.monitoring.service;

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
           if(captureRankJob != null){
               captureRankJob.setStartTime(new Date());
               captureRankJob.setExectionStatus(CaptureRankExectionStatus.Processing.name());
               captureRankJobDao.updateById(captureRankJob);
           }
        }
        return captureRankJob;
    }

    public Page<CaptureRankJob> searchCaptureRankJob(Page<CaptureRankJob> page, CaptureRankJobSearchCriteria captureRankJobSearchCriteria) {
        List<CaptureRankJob> captureRankJobs = captureRankJobDao.searchCaptureRankJobs(page, captureRankJobSearchCriteria);
        Map<Long,Customer> customerMap = new HashMap<Long,Customer>();
        for(CaptureRankJob captureRankJob : captureRankJobs){
            if(captureRankJob.getCustomerUuid() != null &&  !captureRankJob.getCustomerUuid() .equals("")) {
                Customer customer = customerMap.get(captureRankJob.getCustomerUuid());
                if(customer == null) {
                    customer = customerService.selectById(captureRankJob.getCustomerUuid());
                    customerMap.put(captureRankJob.getCustomerUuid(),customer);
                }
                if(customer != null) {
                    captureRankJob.setContactPerson(customer.getContactPerson());
                }
            }
        }
        page.setRecords(captureRankJobs);
        return page;
    }

    public void saveCaptureRankJob(CaptureRankJob captureRankJob, String terminalType, String loginName)
    {
        captureRankJob.setUpdateTime(new Date());
        captureRankJob.setUpdateBy(loginName);
        captureRankJob.setOperationType(terminalType);
        if (captureRankJob.getUuid() == null) {
            captureRankJob.setExectionStatus(CaptureRankExectionStatus.New.name());
            captureRankJob.setCreateBy(loginName);
            captureRankJobDao.insert(captureRankJob);
        } else {
            captureRankJobDao.updateById(captureRankJob);
        }
    }

    public void completeCaptureRankJob(CaptureRankJob captureRankJob)
    {
        captureRankJob = captureRankJobDao.selectById(captureRankJob.getUuid());
        captureRankJob.setExectionStatus(CaptureRankExectionStatus.Complete.name());
        captureRankJob.setEndTime(new Date());
        captureRankJob.setLastExecutionDate(new java.sql.Date(new Date().getTime()));
        captureRankJobDao.updateById(captureRankJob);
    }

    public Boolean getCaptureRankJobStatus(Long captureRankJobUuid) {
        return captureRankJobDao.getCaptureRankJobStatus(captureRankJobUuid);
    }

    public void changeCaptureRankJobStatus(Long uuid, String status) {
        CaptureRankJob captureRankJob = captureRankJobDao.selectById(uuid);
        captureRankJob.setCaptureRankJobStatus(status.equals("true")?true:false);
        captureRankJobDao.updateById(captureRankJob);
    }

    public Boolean hasCaptureRankJob(){
        return captureRankJobDao.fetchCaptureRankJob() != null;
    }

    public void deleteCaptureRankJob (Long qzSettingUuid, String operationType) {
        captureRankJobDao.deleteCaptureRankJob(qzSettingUuid, operationType);
    }

    public void qzAddCaptureRankJob (String group, Long qzSettingUuid, String operationType, String userName) {
        CaptureRankJob captureRankJob = new CaptureRankJob();
        captureRankJob.setGroupNames(group);
        captureRankJob.setQzSettingUuid(qzSettingUuid);
        captureRankJob.setOperationType(operationType);
        captureRankJob.setExectionType("Everyday");
        captureRankJob.setCaptureInterval(500);
        captureRankJob.setPageSize(50);
        captureRankJob.setExectionTime(Time.valueOf("00:05:00"));
        captureRankJob.setExectionStatus("new");
        captureRankJob.setCreateBy(userName);
        captureRankJob.setUpdateBy(userName);
        captureRankJob.setUpdateTime(new Date());
        captureRankJobDao.insert(captureRankJob);
    }
}
