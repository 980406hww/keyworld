package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.ExternalAlgorithmTestTaskCriteria;
import com.keymanager.ckadmin.dao.AlgorithmTestPlanDao;
import com.keymanager.ckadmin.dao.AlgorithmTestTaskDao;
import com.keymanager.ckadmin.dao.CustomerDao;
import com.keymanager.ckadmin.dao.GroupDao;
import com.keymanager.ckadmin.entity.AlgorithmTestPlan;
import com.keymanager.ckadmin.entity.AlgorithmTestTask;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.service.AlgorithmTestTaskService;

import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.criteria.GroupCriteria;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 算法测试任务表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
@Service("algorithmTestTaskService2")
public class AlgorithmTestTaskServiceImpl extends ServiceImpl<AlgorithmTestTaskDao, AlgorithmTestTask> implements AlgorithmTestTaskService {

    @Resource(name = "algorithmTestTaskDao2")
    private AlgorithmTestTaskDao algorithmTestTaskDao;

    @Resource(name = "algorithmTestPlanDao2")
    private AlgorithmTestPlanDao algorithmTestPlanDao;

    @Resource(name = "customerDao2")
    private CustomerDao customerDao;

    @Resource(name = "groupDao2")
    private GroupDao groupDao;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Override
    public Page<AlgorithmTestTask> selectAlgorithmTestTasksByAlgorithmTestPlanUuid(Page<AlgorithmTestTask> algorithmTestTaskPage, Long algorithmTestPlanUuid) {

        List<AlgorithmTestTask> algorithmTestTasks = algorithmTestTaskDao.selectAlgorithmTestTasksByAlgorithmTestPlanUuid(algorithmTestTaskPage, algorithmTestPlanUuid);
        algorithmTestTaskPage.setRecords(algorithmTestTasks);
        return algorithmTestTaskPage;
    }

    @Override
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
