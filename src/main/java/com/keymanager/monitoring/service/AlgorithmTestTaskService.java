package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.ExternalAlgorithmTestTaskCriteria;
import com.keymanager.monitoring.criteria.GroupCriteria;
import com.keymanager.monitoring.dao.AlgorithmTestPlanDao;
import com.keymanager.monitoring.dao.AlgorithmTestTaskDao;
import com.keymanager.monitoring.dao.CustomerDao;
import com.keymanager.monitoring.dao.CustomerKeywordDao;
import com.keymanager.monitoring.dao.GroupDao;
import com.keymanager.monitoring.entity.AlgorithmTestPlan;
import com.keymanager.monitoring.entity.AlgorithmTestTask;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerKeyword;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 算法测试任务表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
@Service
public class AlgorithmTestTaskService extends ServiceImpl<AlgorithmTestTaskDao, AlgorithmTestTask> {

    @Autowired
    private AlgorithmTestTaskDao algorithmTestTaskDao;

    @Autowired
    private AlgorithmTestPlanDao algorithmTestPlanDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public Page<AlgorithmTestTask> selectAlgorithmTestTasksByAlgorithmTestPlanUuid(Page<AlgorithmTestTask> algorithmTestTaskPage, Long algorithmTestPlanUuid) {

        List<AlgorithmTestTask> algorithmTestTasks = algorithmTestTaskDao.selectAlgorithmTestTasksByAlgorithmTestPlanUuid(algorithmTestTaskPage, algorithmTestPlanUuid);
        algorithmTestTaskPage.setRecords(algorithmTestTasks);
        return algorithmTestTaskPage;
    }

    public void saveAlgorithmTestTask(ExternalAlgorithmTestTaskCriteria externalAlgorithmTestTaskCriteria) {

        Customer customer = externalAlgorithmTestTaskCriteria.getCustomer();
        customerDao.saveExternalCustomer(customer);
        GroupCriteria groupCriteria = externalAlgorithmTestTaskCriteria.getGroupCriteria();
        groupDao.saveExternalGroup(groupCriteria);
        List<CustomerKeyword> customerKeywords = externalAlgorithmTestTaskCriteria.getCustomerKeywords();
        for (CustomerKeyword customerKeyword : customerKeywords) {
            customerKeyword.setCustomerUuid(customer.getUuid());
            customerKeyword.setOptimizeGroupName(groupCriteria.getGroupName());
        }

        customerKeywordService.addCustomerKeyword(customerKeywords, groupCriteria.getUserName());
        AlgorithmTestPlan algorithmTestPlan = new AlgorithmTestPlan();
        algorithmTestPlan.setUuid(externalAlgorithmTestTaskCriteria.getAlgorithmTestTask().getAlgorithmTestPlanUuid());
        algorithmTestPlan.setExcuteStatus(0);
        algorithmTestPlanDao.updateById(algorithmTestPlan);
        algorithmTestTaskDao.saveAlgorithmTestTask(externalAlgorithmTestTaskCriteria.getAlgorithmTestTask());
    }
}
