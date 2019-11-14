package com.keymanager.ckadmin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.CaptureRankJobSearchCriteria;
import com.keymanager.ckadmin.dao.CaptureRankJobDao;
import com.keymanager.ckadmin.entity.CaptureRankJob;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.enums.CaptureRankExectionStatus;
import com.keymanager.ckadmin.service.CaptureRankJobService;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.util.Utils;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("captureRankJobService2")
public class CaptureRankJobServiceImpl extends ServiceImpl<CaptureRankJobDao, CaptureRankJob> implements CaptureRankJobService {

    @Resource(name = "captureRankJobDao2")
    private CaptureRankJobDao captureRankJobDao;

    @Resource(name = "customerService2")
    private CustomerService customerService;

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
        Wrapper<CaptureRankJob> wrapper = new EntityWrapper<>();
        if (null != criteria.getInit() && "init".equals(criteria.getInit())) {
            wrapper.where("1 != 1", "");
        } else {
            if (null != criteria.getGroupNames() && !"".equals(criteria.getGroupNames())) {
                wrapper.where("fGroupNames LIKE CONCAT({0},'%')", criteria.getGroupNames());
            }
            if (null != criteria.getCustomerUuid() && !"".equals(criteria.getCustomerUuid())) {
                wrapper.where("fcustomerUuid = {0}", criteria.getCustomerUuid());
            }
            if (null != criteria.getOperationType() && !"".equals(criteria.getOperationType())) {
                wrapper.where("fOperationType = {0}", criteria.getOperationType());
            }
            if (null != criteria.getExectionType() && !"".equals(criteria.getExectionType())) {
                wrapper.where("fExectionType = {0}", criteria.getExectionType());
            }
            if (null != criteria.getExectionStatus() && !"".equals(criteria.getExectionStatus())) {
                wrapper.where("fExectionStatus = {0}", criteria.getExectionStatus());
            }
            if ("Common".equals(criteria.getRankJobType())) {
                wrapper.where("fRankJobType = {0}", "Common");
                wrapper.isNull("fQZSettingUuid");
            } else if ("Station".equals(criteria.getRankJobType())) {
                wrapper.isNotNull("fQZSettingUuid");
            } else {
                wrapper.where("fRankJobType = {0}", criteria.getRankJobType());
            }
            if (null != criteria.getRankJobArea() && !"".equals(criteria.getRankJobArea())) {
                wrapper.where("fRankJobArea = {0}", criteria.getRankJobArea());
            }
            if (null != criteria.getRankJobCity() && !"".equals(criteria.getRankJobCity())) {
                wrapper.where("fRankJobCity = {0}", criteria.getRankJobCity());
            }
        }
        page = selectPage(page, wrapper);
        Map<Long, Customer> customerMap = new HashMap<>();
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
}
