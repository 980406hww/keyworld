package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.KeywordCriteria;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.dao.CustomerKeywordDao;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.enums.KeywordEffectEnum;
import com.keymanager.ckadmin.excel.operator.AbstractExcelReader;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerExcludeKeywordService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;
import com.keymanager.ckadmin.enums.CustomerKeywordSourceEnum;
import com.keymanager.ckadmin.enums.EntryTypeEnum;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.ckadmin.vo.CustomerKeywordSummaryInfoVO;
import com.keymanager.ckadmin.vo.KeywordCountVO;

import com.keymanager.ckadmin.util.StringUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("customerKeywordService2")
public class CustomerKeywordServiceImpl extends ServiceImpl<CustomerKeywordDao, CustomerKeyword> implements
        CustomerKeywordService {
    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordServiceImpl.class);

    @Resource(name = "customerKeywordDao2")
    private CustomerKeywordDao customerKeywordDao;

    @Resource(name = "userRoleService2")
    private UserRoleService userRoleService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @Resource(name = "customerExcludeKeywordService2")
    private CustomerExcludeKeywordService customerExcludeKeywordService;

    @Resource(name = "configService2")
    private ConfigService configService;

    @Override
    public List<Map> getCustomerKeywordsCount(List<Long> customerUuids, String terminalType, String entryType) {
        return customerKeywordDao.getCustomerKeywordsCount(customerUuids, terminalType, entryType);
    }

    @Override
    public void changeCustomerKeywordStatus(String terminalType, String entryType, Long customerUuid, Integer status) {
        customerKeywordDao.changeCustomerKeywordStatus(terminalType, entryType, customerUuid, status);
    }

    @Override
    public void deleteCustomerKeywords(long customerUuid) {
        logger.info("deleteCustomerKeywords:" + customerUuid);
        customerKeywordDao.deleteCustomerKeywordsByCustomerUuid(customerUuid);
    }

    @Override
    public int getCustomerKeywordCount(String terminalType, String entryType, long customerUuid) {
        return customerKeywordDao.getCustomerKeywordCount(terminalType, entryType, customerUuid);
    }

    @Override
    public void excludeCustomerKeyword(
        QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        customerKeywordDao.excludeCustomerKeyword(qzSettingExcludeCustomerKeywordsCriteria);
    }

    @Override
    public void addCustomerKeyword(List<CustomerKeyword> customerKeywords, String userName) {
        List<CustomerKeyword> addCustomerKeywords = new ArrayList<>();
        for (CustomerKeyword customerKeyword : customerKeywords) {
            CustomerKeyword tmpCustomerKeyword = checkCustomerKeyword(customerKeyword, userName);
            if (tmpCustomerKeyword != null) {
                addCustomerKeywords.add(customerKeyword);
            }
        }
        if (CollectionUtils.isNotEmpty(addCustomerKeywords)) {
            int fromIndex = 0, toIndex = 1000;
            do {
                customerKeywordDao.addCustomerKeywords(new ArrayList<>(addCustomerKeywords.subList(fromIndex, Math.min(toIndex, addCustomerKeywords.size()))));
                fromIndex += 1000;
                toIndex += 1000;
            } while (addCustomerKeywords.size() > fromIndex);
        }
    }

    private CustomerKeyword checkCustomerKeyword(CustomerKeyword customerKeyword, String userName) {
        if (StringUtil.isNullOrEmpty(customerKeyword.getOriginalUrl())) {
            customerKeyword.setOriginalUrl(customerKeyword.getUrl());
        }
        String originalUrl = customerKeyword.getOriginalUrl();
        if (!StringUtil.isNullOrEmpty(originalUrl)) {
            if (originalUrl.indexOf("www.") == 0) {
                originalUrl = originalUrl.substring(4);
            } else if (originalUrl.indexOf("m.") == 0) {
                originalUrl = originalUrl.substring(2);
            }
        } else {
            originalUrl = null;
        }
        customerKeyword.setKeyword(customerKeyword.getKeyword().trim());

        if (!EntryTypeEnum.fm.name().equals(customerKeyword.getType())) {
            CustomerKeyword customerKeyword1 = customerKeywordDao.getOneSameCustomerKeyword(customerKeyword.getTerminalType(), customerKeyword.getCustomerUuid(), customerKeyword.getKeyword(), customerKeyword.getUrl(), customerKeyword.getTitle());
            if (customerKeyword1 != null ) {
                detectCustomerKeywordEffect(customerKeyword, customerKeyword1);
                return null;
            }
        }

        if (!EntryTypeEnum.fm.name().equals(customerKeyword.getType()) ) {
            CustomerKeyword customerKeyword1 = customerKeywordDao
                .getOneSimilarCustomerKeyword(customerKeyword.getTerminalType(), customerKeyword.getCustomerUuid(), customerKeyword.getKeyword(), originalUrl,
                    customerKeyword.getTitle());
            if (customerKeyword1 != null ) {
                detectCustomerKeywordEffect(customerKeyword, customerKeyword1);
                return null;
            }
        }

        customerKeyword.setKeyword(customerKeyword.getKeyword().trim());
        customerKeyword.setUrl(customerKeyword.getUrl() != null ? customerKeyword.getUrl().trim() : null);
        customerKeyword.setTitle(customerKeyword.getTitle() != null ? customerKeyword.getTitle().trim() : null);
        customerKeyword.setOriginalUrl(customerKeyword.getOriginalUrl() != null ? customerKeyword.getOriginalUrl().trim() : null);
        customerKeyword.setOrderNumber(customerKeyword.getOrderNumber() != null ? customerKeyword.getOrderNumber().trim() : null);
        customerKeyword.setOptimizeRemainingCount(customerKeyword.getOptimizePlanCount() != null ? customerKeyword.getOptimizePlanCount() : 0);
        if (customerKeyword.getStatus() == null) {
            if (StringUtils.isNotBlank(userName)) {
                boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
                if (isDepartmentManager) {
                    customerKeyword.setStatus(1);
                } else {
                    customerKeyword.setStatus(2);
                }
            } else {
                customerKeyword.setStatus(1);
            }
        }

        if ("否".equals(customerKeyword.getRunImmediate())) {
            customerKeyword.setStatus(2);
        }

        if (customerKeyword.getCurrentPosition() == null) {
            customerKeyword.setCurrentPosition(100);
        }

        if (EntryTypeEnum.qz.name().equals(customerKeyword.getType())) {
            customerKeyword.setCurrentIndexCount(-1);
        }

        if (Utils.isNullOrEmpty(customerKeyword.getMachineGroup())) {
            customerKeyword.setMachineGroup(customerKeyword.getType());
        }
        int queryInterval = 24 * 60 * 60;
        if (null != customerKeyword.getOptimizePlanCount() && customerKeyword.getOptimizePlanCount() > 0) {
            int optimizeTodayCount = (int) Math.floor(Utils
                .getRoundValue(customerKeyword.getOptimizePlanCount() * (Math.random() * 0.7 + 0.5),
                    1));
            queryInterval = queryInterval / optimizeTodayCount;
            customerKeyword.setOptimizeTodayCount(optimizeTodayCount);
        } else {
            if ("Important".equals(customerKeyword.getKeywordEffect())) {
                Integer optimizePlanCount = Integer.valueOf(configService
                    .getConfig("KeywordEffectOptimizePlanCount", "ImportantKeyword")
                    .getValue());
                customerKeyword.setOptimizePlanCount(optimizePlanCount);
            } else {
                customerKeyword.setOptimizePlanCount(10);
            }
        }
        customerKeyword.setQueryInterval(queryInterval);
        customerKeyword.setAutoUpdateNegativeDateTime(Utils.getCurrentTimestamp());
        customerKeyword.setCapturePositionQueryTime(Utils.addDay(Utils.getCurrentTimestamp(), -2));
        customerKeyword.setStartOptimizedTime(new Date());
        customerKeyword.setLastReachStandardDate(Utils.yesterday());
        customerKeyword.setQueryTime(new Date());
        customerKeyword.setQueryDate(new Date());
        customerKeyword.setUpdateTime(new Date());
        customerKeyword.setCreateTime(new Date());
        return customerKeyword;
    }

    @Override
    public Integer getCustomerKeywordCountByOptimizeGroupName(String groupName) {
        return customerKeywordDao.getCustomerKeywordCountByOptimizeGroupName(groupName);
    }

    @Override
    public List<CustomerKeywordSummaryInfoVO> searchCustomerKeywordSummaryInfo(String entryType,
        long customerUuid) {
        return customerKeywordDao.searchCustomerKeywordSummaryInfo(entryType, customerUuid);
    }

    private void detectCustomerKeywordEffect(CustomerKeyword customerKeyword, CustomerKeyword customerKeyword1) {
        if (!CustomerKeywordSourceEnum.Capture.name().equals(customerKeyword.getCustomerKeywordSource())) {
            String oldKeywordEffect = customerKeyword1.getKeywordEffect();
            String keywordEffect = customerKeyword.getKeywordEffect();
            if (null != oldKeywordEffect) {
                if (!oldKeywordEffect.equals(keywordEffect)){
                    Map<String, Integer> levelMap = KeywordEffectEnum.toLevelMap();
                    String newKeywordEffect = levelMap.get(oldKeywordEffect) < levelMap.get(keywordEffect) ? oldKeywordEffect : keywordEffect;
                    customerKeyword.setKeywordEffect(newKeywordEffect);
                    customerKeyword.setUpdateTime(new Date());
                    customerKeywordDao.updateSameCustomerKeyword(customerKeyword);
                }
            } else {
                customerKeyword.setKeywordEffect(keywordEffect);
                customerKeyword.setUpdateTime(new Date());
                customerKeywordDao.updateSameCustomerKeyword(customerKeyword);
            }
        }
    }

    @Override
    public KeywordCountVO getCustomerKeywordsCountByCustomerUuid(Long customerUuid, String terminalType) {
        return customerKeywordDao.getCustomerKeywordsCountByCustomerUuid(customerUuid, terminalType);
    }

    @Override
    public Page<CustomerKeyword> searchKeywords(Page<CustomerKeyword> page, KeywordCriteria keywordCriteria) {
        // 不使用mp自带分页查询，解决count慢问题
        page.setSearchCount(false);
        page.setTotal(customerKeywordDao.getKeywordCountByKeywordCriteria(keywordCriteria));
        List<CustomerKeyword> customerKeywordList = customerKeywordDao.searchKeywords(page, keywordCriteria);
        page.setRecords(customerKeywordList);
        return page;
    }

    @Override
    public void updateCustomerKeywordStatus(List<Long> customerKeywordUuids, Integer status) {
        customerKeywordDao.updateCustomerKeywordStatus(customerKeywordUuids, status);
    }

    @Override
    public void updateOptimizeGroupName(KeywordCriteria keywordCriteria) {
        customerKeywordDao.updateOptimizeGroupName(keywordCriteria);
    }

    @Override
    public void updateMachineGroup(KeywordCriteria keywordCriteria) {
        customerKeywordDao.updateMachineGroup(keywordCriteria);
    }

    @Override
    public void updateBearPawNumber(KeywordCriteria keywordCriteria) {
        customerKeywordDao.updateBearPawNumber(keywordCriteria);
    }

    @Override
    public void deleteCustomerKeywordsByDeleteType(KeywordCriteria keywordCriteria) {
        switch (keywordCriteria.getDeleteType()) {
            case "byUuids":
                deleteCustomerKeywordsByUuids(keywordCriteria);
                break;
            case "byEmptyTitle":
                deleteCustomerKeywordsWhenEmptyTitle(keywordCriteria);
                break;
            case "byEmptyTitleAndUrl":
                deleteCustomerKeywordsWhenEmptyTitleAndUrl(keywordCriteria);
                break;
            default:

        }

    }

    private void deleteCustomerKeywordsByUuids(KeywordCriteria keywordCriteria) {
        customerKeywordDao.deleteCustomerKeywordsByUuids(keywordCriteria.getUuids());
    }

    private void deleteCustomerKeywordsWhenEmptyTitle(KeywordCriteria keywordCriteria) {
        customerKeywordDao.deleteCustomerKeywordsWhenEmptyTitle(keywordCriteria);
    }

    private void deleteCustomerKeywordsWhenEmptyTitleAndUrl(KeywordCriteria keywordCriteria) {
        customerKeywordDao.deleteCustomerKeywordsWhenEmptyTitleAndUrl(keywordCriteria);
    }

    @Override
    public Page<CustomerKeyword> searchCustomerKeywords(Page<CustomerKeyword> page, KeywordCriteria keywordCriteria) {
        // 不使用mp自带分页查询，解决count慢问题
        page.setSearchCount(false);
        page.setTotal(customerKeywordDao.getKeywordCountByKeywordCriteria(keywordCriteria));
        List<CustomerKeyword> customerKeywordList = customerKeywordDao.searchCustomerKeywords(page, keywordCriteria);
        page.setRecords(customerKeywordList);
        return page;
    }

    @Override
    public void saveCustomerKeyword(CustomerKeyword customerKeyword, String userName) {
        String customerExcludeKeywords = customerExcludeKeywordService.getCustomerExcludeKeyword(customerKeyword.getCustomerUuid(),
            customerKeyword.getQzSettingUuid(), customerKeyword.getTerminalType(), customerKeyword.getUrl());
        if (null != customerExcludeKeywords) {
            Set<String> excludeKeyword = new HashSet<>(Arrays.asList(customerExcludeKeywords.split(",")));
            if (!excludeKeyword.isEmpty()) {
                if (excludeKeyword.contains(customerKeyword.getKeyword())) {
                    customerKeyword.setStatus(0);
                }
            }
        }
        customerKeyword.setCustomerKeywordSource(CustomerKeywordSourceEnum.UI.name());
        addCustomerKeyword(customerKeyword, userName);
    }

    private void addCustomerKeyword(CustomerKeyword customerKeyword, String userName) {
        customerKeyword = checkCustomerKeyword(customerKeyword, userName);
        if (customerKeyword != null) {
            customerKeywordDao.insert(customerKeyword);
        }
    }

    @Override
    public void updateCustomerKeywordFromUI(CustomerKeyword customerKeyword, String userName) {
        boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
        if (isDepartmentManager) {
            customerKeyword.setStatus(1);
        } else {
            customerKeyword.setStatus(2);
        }
        int queryInterval = 24 * 60 * 60;
        if (null != customerKeyword.getOptimizePlanCount() && customerKeyword.getOptimizePlanCount() > 0) {
            int optimizeTodayCount = (int) Math.floor(Utils.getRoundValue(customerKeyword.getOptimizePlanCount() * (Math.random() * 0.7 + 0.5), 1));
            queryInterval = queryInterval / optimizeTodayCount;
            customerKeyword.setOptimizeTodayCount(optimizeTodayCount);
        }
        customerKeyword.setQueryInterval(queryInterval);
        customerKeyword.setUpdateTime(new Date());
        customerKeywordDao.updateById(customerKeyword);
    }

    @Override
    public void updateOptimizePlanCount(KeywordCriteria keywordCriteria) {
        customerKeywordDao.updateOptimizePlanCount(keywordCriteria);
    }


    //简化版Excel文件导入
    @Override
    public boolean handleExcel(InputStream inputStream, String excelType, int customerUuid, String type, String terminalType, String userName)
        throws Exception {
        AbstractExcelReader operator = AbstractExcelReader.createExcelOperator(inputStream, excelType);
        if (null != operator) {
            List<CustomerKeyword> customerKeywords = operator.readDataFromExcel();
            supplementInfo(customerKeywords, customerUuid, type, terminalType);
            addCustomerKeywords(customerKeywords, userName);
            return true;
        }
        return false;
    }

    private void supplementInfo(List<CustomerKeyword> customerKeywords, int customerUuid, String type, String terminalType) {
        for (CustomerKeyword customerKeyword : customerKeywords) {
            customerKeyword.setCustomerUuid(customerUuid);
            customerKeyword.setType(type);
            customerKeyword.setCreateTime(Utils.getCurrentTimestamp());
            customerKeyword.setUpdateTime(Utils.getCurrentTimestamp());
            customerKeyword.setTerminalType(terminalType);
            customerKeyword.setCustomerKeywordSource(CustomerKeywordSourceEnum.Excel.name());
        }
    }

    private void addCustomerKeywords(List<CustomerKeyword> customerKeywords, String loginName) throws Exception {
        for (CustomerKeyword customerKeyword : customerKeywords) {
            customerKeyword.setKeywordEffect("");
            addCustomerKeyword(customerKeyword, loginName);
        }
    }

    @Override
    public List<CustomerKeyword> searchCustomerKeywordsForDailyReport(KeywordCriteria keywordCriteria) {
        return customerKeywordDao.searchCustomerKeywordsForDailyReport(keywordCriteria);
    }

    @Override
    public List<String> getGroups(List<Long> customerUuids) {
        return customerKeywordDao.getGroups(customerUuids);
    }

    @Override
    public List<Long> getCustomerUuids(String entryType, String terminalType) {
        return customerKeywordDao.getCustomerUuids(entryType, terminalType);
    }

    @Override
    public List<CustomerKeyword> searchCustomerKeywordInfo(KeywordCriteria keywordCriteria) {
        return customerKeywordDao.searchCustomerKeywordInfo(keywordCriteria);
    }

    @Override
    public List<Map> searchAllKeywordAndUrl(Long customerUuid, String terminalType) {
        return customerKeywordDao.selectAllKeywordAndUrl(customerUuid, terminalType);
    }

    @Override
    public void updateSearchEngine(KeywordCriteria keywordCriteria) {
        customerKeywordDao.updateSearchEngine(keywordCriteria);
    }

    @Override
    public void changeCustomerKeywordStatusInCKPage(KeywordCriteria keywordCriteria) {
        customerKeywordDao.changeCustomerKeywordStatusInCKPage(keywordCriteria);
    }
}


