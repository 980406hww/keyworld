package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.CustomerKeywordCleanTitleCriteria;
import com.keymanager.ckadmin.criteria.CustomerKeywordUpdateStatusCriteria;
import com.keymanager.ckadmin.criteria.GroupSettingCriteria;
import com.keymanager.ckadmin.criteria.KeywordCriteria;
import com.keymanager.ckadmin.criteria.KeywordStandardCriteria;
import com.keymanager.ckadmin.criteria.PTKeywordCountCriteria;
import com.keymanager.ckadmin.criteria.QZRateKewordCountCriteria;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.dao.CustomerKeywordDao;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.enums.CollectMethod;
import com.keymanager.ckadmin.enums.CustomerKeywordSourceEnum;
import com.keymanager.ckadmin.enums.EntryTypeEnum;
import com.keymanager.ckadmin.enums.KeywordEffectEnum;
import com.keymanager.ckadmin.excel.operator.AbstractExcelReader;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerExcludeKeywordService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.QZRateStatisticsService;
import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;
import com.keymanager.ckadmin.util.Constants;
import com.keymanager.ckadmin.util.StringUtil;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.ckadmin.vo.CodeNameVo;
import com.keymanager.ckadmin.vo.CustomerKeywordSummaryInfoVO;
import com.keymanager.ckadmin.vo.GroupVO;
import com.keymanager.ckadmin.vo.KeywordCountVO;
import com.keymanager.ckadmin.vo.KeywordStandardVO;
import com.keymanager.ckadmin.vo.KeywordStatusBatchUpdateVO;
import com.keymanager.ckadmin.vo.MachineGroupQueueVO;
import com.keymanager.ckadmin.vo.OptimizationKeywordVO;
import com.keymanager.ckadmin.vo.PTkeywordCountVO;
import com.keymanager.ckadmin.vo.QZRateKeywordCountVO;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.Resource;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("customerKeywordService2")
public class CustomerKeywordServiceImpl extends ServiceImpl<CustomerKeywordDao, CustomerKeyword> implements CustomerKeywordService {

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

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    @Resource(name = "qzRateStatisticsService2")
    private QZRateStatisticsService qzRateStatisticsService;

    private final static Map<String, LinkedBlockingQueue> machineGroupQueueMap = new HashMap<>();

    @Override
    public void cacheCustomerKeywords() {
        List<String> machineGroups = customerKeywordDao.getMachineGroups();
        if (CollectionUtils.isNotEmpty(machineGroups)) {
            for (String machineGroup : machineGroups) {
                String[] terminalTypeAndMachineGroups = machineGroup.split("####");
                LinkedBlockingQueue blockingQueue = machineGroupQueueMap.get(terminalTypeAndMachineGroups[0] + "####" + terminalTypeAndMachineGroups[1]);
                if (blockingQueue == null) {
                    blockingQueue = new LinkedBlockingQueue<String>(100000);
                    machineGroupQueueMap.put(terminalTypeAndMachineGroups[0] + "####" + terminalTypeAndMachineGroups[1], blockingQueue);
                }
                int machineCount = Integer.parseInt(terminalTypeAndMachineGroups[2]);
                int currentSize = blockingQueue.size();
                int offerCount = 0;
                if (currentSize < (machineCount * 4)) {
                    List<OptimizationKeywordVO> optimizationKeywordVOS = null;
                    do {
                        optimizationKeywordVOS = customerKeywordDao
                            .fetchCustomerKeywordsForCache(terminalTypeAndMachineGroups[0], terminalTypeAndMachineGroups[1],
                                ((machineCount * 10) > 5000 ? 5000 : (machineCount * 10)));
                        if (CollectionUtils.isNotEmpty(optimizationKeywordVOS)) {
                            List<Long> customerKeywordUuids = new ArrayList<>();
                            if (optimizationKeywordVOS.size() > machineCount) {
                                for (OptimizationKeywordVO optimizationKeywordVO : optimizationKeywordVOS) {
                                    if (blockingQueue.offer(optimizationKeywordVO)) {
                                        offerCount++;
                                        customerKeywordUuids.add(optimizationKeywordVO.getUuid());
                                    } else {
                                        break;
                                    }
                                }
                                updateOptimizationQueryTime(customerKeywordUuids);
                            } else {
                                int count = 0;
                                int repeatTimes = 0;
                                boolean hasNewElement = false;
                                Map<Long, Integer> customerKeywordUuidAndRepeatCount = new HashMap<>();
                                do {
                                    hasNewElement = false;
                                    repeatTimes++;
                                    Iterator<OptimizationKeywordVO> it = optimizationKeywordVOS.iterator();
                                    while (it.hasNext()) {
                                        OptimizationKeywordVO optimizationKeywordVO = it.next();
                                        if (optimizationKeywordVO.getQueryInterval() == 0) {
                                            optimizationKeywordVO.setQueryInterval(5000);
                                        }
                                        int maxOptimizeCount = Math.round(
                                            DateUtils.getFragmentInSeconds(Calendar.getInstance(), Calendar.DATE) / (optimizationKeywordVO.getQueryInterval()));
                                        Integer repeatCount = customerKeywordUuidAndRepeatCount.get(optimizationKeywordVO.getUuid());
                                        if (repeatCount == null) {
                                            customerKeywordUuidAndRepeatCount.put(optimizationKeywordVO.getUuid(), 0);
                                        }
                                        if (maxOptimizeCount - optimizationKeywordVO.getOptimizedCount() > 0) {
                                            offerCount++;
                                            blockingQueue.offer(optimizationKeywordVO);
                                            optimizationKeywordVO.setOptimizedCount(optimizationKeywordVO.getOptimizedCount() + 1);
                                            hasNewElement = true;
                                            customerKeywordUuidAndRepeatCount.put(optimizationKeywordVO.getUuid(),
                                                (customerKeywordUuidAndRepeatCount.get(optimizationKeywordVO.getUuid()) + 1));
                                            count++;
                                            if (count > (machineCount * 4)) {
                                                break;
                                            }
                                        } else {
                                            it.remove();
                                        }
                                    }
                                }
                                while (count < (machineCount * 4) && hasNewElement && repeatTimes < 50 && CollectionUtils.isNotEmpty(optimizationKeywordVOS));

                                if (customerKeywordUuidAndRepeatCount.size() == 0) {
                                    break;
                                }
                                while (customerKeywordUuidAndRepeatCount.size() > 0) {
                                    customerKeywordUuids.addAll(customerKeywordUuidAndRepeatCount.keySet());
                                    updateOptimizationQueryTime(customerKeywordUuids);
                                    customerKeywordUuids.clear();
                                    for (Long customerKeywordUuid : customerKeywordUuidAndRepeatCount.keySet()) {
                                        if (customerKeywordUuidAndRepeatCount.get(customerKeywordUuid) < 2) {
                                            customerKeywordUuids.add(customerKeywordUuid);
                                        } else {
                                            customerKeywordUuidAndRepeatCount
                                                .put(customerKeywordUuid, (customerKeywordUuidAndRepeatCount.get(customerKeywordUuid) - 1));
                                        }
                                    }
                                    for (Long customerKeywordUuid : customerKeywordUuids) {
                                        customerKeywordUuidAndRepeatCount.remove(customerKeywordUuid);
                                    }
                                    customerKeywordUuids.clear();
                                }
                                if (!CollectionUtils.isNotEmpty(optimizationKeywordVOS)) {
                                    break;
                                }
                            }
                        }
                    }
                    while (currentSize + offerCount < (machineCount * 10) && CollectionUtils.isNotEmpty(optimizationKeywordVOS));
                }
            }
        }
    }

    @Override
    public void updateOptimizationQueryTime(List<Long> customerKeywordUuids) {
        customerKeywordDao.updateOptimizationQueryTime(customerKeywordUuids);
    }

    @Override
    public List<MachineGroupQueueVO> getMachineGroupAndSize() {
        List<MachineGroupQueueVO> machineGroupQueueVos = new ArrayList<>();
        for (Map.Entry<String, LinkedBlockingQueue> entry : machineGroupQueueMap.entrySet()) {
            machineGroupQueueVos.add(new MachineGroupQueueVO(entry.getKey(), entry.getValue().size()));
        }
        return machineGroupQueueVos;
    }

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
    public void excludeCustomerKeyword(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        customerKeywordDao.excludeCustomerKeyword(qzSettingExcludeCustomerKeywordsCriteria);
    }

    @Override
    public void addCustomerKeyword(List<CustomerKeyword> customerKeywords, String userName) {
        List<CustomerKeyword> addCustomerKeywords = new ArrayList<>();
        for (CustomerKeyword customerKeyword : customerKeywords) {
            CustomerKeyword tmpCustomerKeyword = checkCustomerKeyword(customerKeyword, userName);
            if (null != tmpCustomerKeyword) {
                addCustomerKeywords.add(tmpCustomerKeyword);
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
            CustomerKeyword customerKeyword1 = customerKeywordDao.getOneSameCustomerKeyword(customerKeyword.getTerminalType(), customerKeyword.getCustomerUuid()
                , customerKeyword.getKeyword(), customerKeyword.getUrl(), customerKeyword.getTitle());
            if (customerKeyword1 != null) {
                detectCustomerKeywordEffect(customerKeyword, customerKeyword1);
                return null;
            }
        }

        if (!EntryTypeEnum.fm.name().equals(customerKeyword.getType())) {
            CustomerKeyword customerKeyword1 = customerKeywordDao.getOneSimilarCustomerKeyword(customerKeyword.getTerminalType(), customerKeyword.getCustomerUuid()
                , customerKeyword.getKeyword(), originalUrl, customerKeyword.getTitle());
            if (customerKeyword1 != null) {
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
            int optimizeTodayCount = (int) Math.floor(Utils.getRoundValue(customerKeyword.getOptimizePlanCount() * (Math.random() * 0.7 + 0.5), 1));
            queryInterval = queryInterval / optimizeTodayCount;
            customerKeyword.setOptimizeTodayCount(optimizeTodayCount);
            customerKeyword.setOptimizeRemainingCount(optimizeTodayCount);
        } else {
            if ("Important".equals(customerKeyword.getKeywordEffect())) {
                Integer optimizePlanCount = Integer.valueOf(configService.getConfig("KeywordEffectOptimizePlanCount", "ImportantKeyword").getValue());
                customerKeyword.setOptimizePlanCount(optimizePlanCount);
            } else {
                customerKeyword.setOptimizePlanCount(10);
            }
        }
        customerKeyword.setQueryInterval(queryInterval);
        customerKeyword.setAutoUpdateNegativeDateTime(Utils.getCurrentTimestamp());
        customerKeyword.setCapturePositionQueryTime(Utils.addDay(Utils.getCurrentTimestamp(), -2));
        customerKeyword.setCaptureIndexQueryTime(Utils.addDay(Utils.getCurrentTimestamp(), -2));
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
    public List<CustomerKeywordSummaryInfoVO> searchCustomerKeywordSummaryInfo(String entryType, long customerUuid) {
        return customerKeywordDao.searchCustomerKeywordSummaryInfo(entryType, customerUuid);
    }

    private void detectCustomerKeywordEffect(CustomerKeyword customerKeyword, CustomerKeyword customerKeyword1) {
        if (!CustomerKeywordSourceEnum.Capture.name().equals(customerKeyword.getCustomerKeywordSource())) {
            String oldKeywordEffect = customerKeyword1.getKeywordEffect();
            String keywordEffect = customerKeyword.getKeywordEffect();
            if (StringUtil.isNotNullNorEmpty(oldKeywordEffect)) {
                if (!oldKeywordEffect.equals(keywordEffect)) {
                    if ("Common".equals(oldKeywordEffect)) {
                        customerKeyword.setKeywordEffect(keywordEffect);
                    } else {
                        Map<String, Integer> levelMap = KeywordEffectEnum.toLevelMap();
                        Integer levelValue = levelMap.get(keywordEffect);
                        String newKeywordEffect = levelMap.get(oldKeywordEffect) < (levelValue == null ? 4 : levelValue) ? oldKeywordEffect : keywordEffect;
                        customerKeyword.setKeywordEffect(newKeywordEffect);
                    }
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
    public Map<String, Object> getCustomerKeywordsCountByCustomerUuid(Long customerUuid, String type) {
        Map<String, Object> map = null;
        List<KeywordCountVO> keywordCountVos = customerKeywordDao.getCustomerKeywordsCountByCustomerUuid(customerUuid, type);
        if (CollectionUtils.isNotEmpty(keywordCountVos)) {
            map = new HashMap<>(3);
            int totalCount = 0;
            for (KeywordCountVO keywordCountVo : keywordCountVos) {
                totalCount += keywordCountVo.getTotalCount();
                map.put(keywordCountVo.getTerminalType(), keywordCountVo);
            }
            map.put("totalCount", totalCount);
        }
        return map;
    }

    @Override
    public void resetInvalidRefreshCount(RefreshStatisticsCriteria criteria) {
        customerKeywordDao.resetInvalidRefreshCount(criteria);
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
                    customerKeyword.setStatus(3);
                }
            }
        }
        customerKeyword.setCustomerKeywordSource(CustomerKeywordSourceEnum.UI.name());
        customerKeyword.setKeywordEffect(KeywordEffectEnum.Important.name());
        customerKeyword.setManualCleanTitle(true);
        customerKeyword.setServiceProvider("baidutop123");
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
            customerKeyword.setOptimizeRemainingCount(optimizeTodayCount);
        }
        customerKeyword.setQueryInterval(queryInterval);
        customerKeyword.setUpdateTime(new Date());
        customerKeywordDao.updateById(customerKeyword);
    }

    @Override
    public void updateOptimizePlanCount(KeywordCriteria keywordCriteria) {
        customerKeywordDao.updateOptimizePlanCount(keywordCriteria);
    }


    /**
     * 简化版Excel文件导入
     */
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
            if (StringUtil.isNullOrEmpty(customerKeyword.getKeywordEffect())) {
                customerKeyword.setKeywordEffect("Common");
            } else {
                switch (customerKeyword.getKeywordEffect().trim()) {
                    case "曲线词":
                        customerKeyword.setKeywordEffect(KeywordEffectEnum.Curve.name());
                        break;
                    case "指定词":
                        customerKeyword.setKeywordEffect(KeywordEffectEnum.Appointment.name());
                        break;
                    case "赠送词":
                        customerKeyword.setKeywordEffect("Present");
                        break;
                    default:
                        customerKeyword.setKeywordEffect("Common");
                        break;
                }
            }
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
    public void changeCustomerKeywordStatusInCKPage(CustomerKeywordUpdateStatusCriteria customerKeywordUpdateStatusCriteria) {
        customerKeywordDao.changeCustomerKeywordStatusInCKPage(customerKeywordUpdateStatusCriteria);
    }

    @Override
    public void cleanTitle(CustomerKeywordCleanTitleCriteria customerKeywordCleanTitleCriteria) {
        switch (customerKeywordCleanTitleCriteria.getCleanType()) {
            case "recollectAll":
                customerKeywordDao.cleanCaptureTitleFlag(customerKeywordCleanTitleCriteria.getTerminalType(), customerKeywordCleanTitleCriteria.getType(),
                    customerKeywordCleanTitleCriteria.getCustomerUuid());
                break;
            case "recollectSelect":
                customerKeywordDao.cleanCaptureTitleBySelected(customerKeywordCleanTitleCriteria.getUuids());
                break;
            case "cleanAll":
                customerKeywordDao.cleanCustomerTitle(customerKeywordCleanTitleCriteria.getTerminalType(), customerKeywordCleanTitleCriteria.getType(),
                    customerKeywordCleanTitleCriteria.getCustomerUuid());
                break;
            case "cleanSelect":
                customerKeywordDao.cleanSelectedCustomerKeywordTitle(customerKeywordCleanTitleCriteria.getUuids());
                break;
            default:
        }
    }

    @Override
    public void deleteDuplicateKeywords(CustomerKeywordUpdateStatusCriteria customerKeywordUpdateStatusCriteria) {
        List<String> uuidsList = customerKeywordDao.searchDuplicateKeywords(customerKeywordUpdateStatusCriteria);
        List<Long> customerKeywordUuids = new ArrayList<>();
        for (String uuids : uuidsList) {
            ArrayList<Long> listIds = new ArrayList<>(Arrays.asList((Long[]) ConvertUtils.convert(uuids.split(","), Long.class)));
            listIds.remove(0);
            customerKeywordUuids.addAll(listIds);
        }
        while (customerKeywordUuids.size() > 0) {
            List<Long> subCustomerKeywordUuids = customerKeywordUuids.subList(0, Math.min(customerKeywordUuids.size(), 500));
            customerKeywordDao.deleteBatchIds(subCustomerKeywordUuids);
            logger.info("controlCustomerKeywordStatus:" + subCustomerKeywordUuids.toString());
            customerKeywordUuids.removeAll(subCustomerKeywordUuids);
        }
    }

    @Override
    public CustomerKeyword getKeywordInfoByUuid(Long uuid) {
        return customerKeywordDao.selectById(uuid);
    }

    //客户关键字批量设置
    @Override
    public void batchUpdateKeywords(KeywordStatusBatchUpdateVO keywordStatusBatchUpdateVO) {
        customerKeywordDao.batchUpdateKeywords(keywordStatusBatchUpdateVO.getUuids(), keywordStatusBatchUpdateVO.getKeywordChecks(),
            keywordStatusBatchUpdateVO.getKeywordValues());
    }

    @Override
    public List<CodeNameVo> searchGroupsByTerminalType(String terminalType) {
        return customerKeywordDao.searchGroupsByTerminalType(terminalType);
    }

    @Override
    public KeywordStandardVO searchCustomerKeywordForNoReachStandard(KeywordStandardCriteria KeywordStandardCriteria) {
        KeywordStandardVO keywordStandardVO = new KeywordStandardVO();
        KeywordStandardCriteria.setNoReachStandardDays(7);
        int sevenDaysNoReachStandard = customerKeywordDao.searchCustomerKeywordForNoReachStandard(KeywordStandardCriteria);
        KeywordStandardCriteria.setNoReachStandardDays(15);
        int fifteenDaysNoReachStandard = customerKeywordDao.searchCustomerKeywordForNoReachStandard(KeywordStandardCriteria);
        KeywordStandardCriteria.setNoReachStandardDays(30);
        int thirtyDaysNoReachStandard = customerKeywordDao.searchCustomerKeywordForNoReachStandard(KeywordStandardCriteria);
        keywordStandardVO.setSevenDaysNoReachStandard(sevenDaysNoReachStandard);
        keywordStandardVO.setFifteenDaysNoReachStandard(fifteenDaysNoReachStandard);
        keywordStandardVO.setThirtyDaysNoReachStandard(thirtyDaysNoReachStandard);
        return keywordStandardVO;
    }

    @Override
    public List<GroupVO> getAvailableOptimizationGroups(GroupSettingCriteria groupSettingCriteria) {
        return customerKeywordDao.getAvailableOptimizationGroups(groupSettingCriteria);
    }

    @Override
    public Page<PTkeywordCountVO> searchPTKeywordCount(Page<PTkeywordCountVO> page, PTKeywordCountCriteria keywordCriteria) {
        page.setRecords(customerKeywordDao.searchPTKeywordCount(page, keywordCriteria));
        return page;
    }

    @Override
    public void updateKeywordCustomerUuid(List<String> keywordUuids, String customerUuid, String terminalType) {
        customerKeywordDao.updateKeywordCustomerUuid(keywordUuids, customerUuid, terminalType);
    }

    @Override
    public void updateCustomerUuidByQzUuids(Long customerUuid, List<Long> qzUuids) {
        customerKeywordDao.updateCustomerUuidByQzUuids(customerUuid, qzUuids);
    }

    @Override
    public void editOptimizePlanCountByCustomerUuid(String terminalType, String entryType, Long customerUuid, Integer optimizePlanCount, String settingType) {
        customerKeywordDao.editOptimizePlanCountByCustomerUuid(terminalType, entryType, customerUuid, optimizePlanCount, settingType);
    }

    @Override
    public void editCustomerOptimizePlanCount(Integer optimizePlanCount, String settingType, List<String> uuids) {
        customerKeywordDao.editCustomerOptimizePlanCount(optimizePlanCount, settingType, uuids);
    }

    @Override
    public Page<QZRateKeywordCountVO> getQZRateKeywordCountList(Page<QZRateKeywordCountVO> page, QZRateKewordCountCriteria qzRateKewordCountCriteria) throws CloneNotSupportedException {
        List<Long> qzUuids = qzSettingService.getQZUuidsByUserID(qzRateKewordCountCriteria.getUserID(), qzRateKewordCountCriteria.getSearchEngine(), qzRateKewordCountCriteria.getTerminalType());
        if (null == qzUuids || qzUuids.isEmpty()) {
            return page;
        }
        qzRateKewordCountCriteria.setQzUuids(qzUuids);
        // 不使用mp自带分页查询，解决count慢问题
        page.setSearchCount(false);
        page.setTotal(customerKeywordDao.getQZRateKeywordCountByCriteria(qzRateKewordCountCriteria));
        List<QZRateKeywordCountVO> qzRateKeywordCountVOS = customerKeywordDao.getQZRateKeywordCount(page, qzRateKewordCountCriteria);
        if (CollectionUtils.isNotEmpty(qzRateKeywordCountVOS)) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            String todayDate = f.format(new Date());
            // 当不传入终端类型时，需要判断当前站点是否包含pc、phone终端类型
            if (("").equals(qzRateKewordCountCriteria.getTerminalType())) {
                List<QZRateKeywordCountVO> qzRateKeywordCountVONewList = new ArrayList<>();
                for (QZRateKeywordCountVO qzRateKeywordCountVO : qzRateKeywordCountVOS) {
                    if (qzRateKeywordCountVO.getHasPC() == 1) {
                        QZRateKeywordCountVO qzRateKeywordCountVOClone1 = (QZRateKeywordCountVO) qzRateKeywordCountVO.clone();
                        qzRateKeywordCountVOClone1.setRate(qzRateStatisticsService.getRate(qzRateKeywordCountVO.getQzUuid(), "PC", todayDate));
                        qzRateKeywordCountVOClone1.setTerminalType("PC");
                        qzRateKeywordCountVONewList.add(qzRateKeywordCountVOClone1);
                    }
                    if (qzRateKeywordCountVO.getHasPhone() == 1) {
                        QZRateKeywordCountVO qzRateKeywordCountVOClone2 = (QZRateKeywordCountVO) qzRateKeywordCountVO.clone();
                        qzRateKeywordCountVOClone2.setRate(qzRateStatisticsService.getRate(qzRateKeywordCountVO.getQzUuid(), "Phone", todayDate));
                        qzRateKeywordCountVOClone2.setTerminalType("Phone");
                        qzRateKeywordCountVONewList.add(qzRateKeywordCountVOClone2);
                    }

                }
                qzRateKeywordCountVOS = qzRateKeywordCountVONewList;
            } else {
                for (QZRateKeywordCountVO qzRateKeywordCountVO : qzRateKeywordCountVOS) {
                    qzRateKeywordCountVO.setRate(qzRateStatisticsService.getRate(qzRateKeywordCountVO.getQzUuid(), qzRateKewordCountCriteria.getTerminalType(), todayDate));
                    qzRateKeywordCountVO.setTerminalType(qzRateKewordCountCriteria.getTerminalType());
                }
            }
        }
        page.setRecords(qzRateKeywordCountVOS);
        return page;
    }
    
    @Override
    public void updateSelectFailReason(KeywordCriteria keywordCriteria) {
        customerKeywordDao.updateSelectFailReason(keywordCriteria);
    }

    @Override
    public void addCustomerKeywordsFromSimpleUI(List<CustomerKeyword> customerKeywords, String terminalType, String entryType, String userName) {
        if (CollectionUtils.isNotEmpty(customerKeywords)) {
            long customerUuid = customerKeywords.get(0).getCustomerUuid();
            int maxSequence = getMaxSequence(terminalType, entryType, customerUuid);
            for (CustomerKeyword customerKeyword : customerKeywords) {
                supplementInfoFromSimpleUI(customerKeyword, terminalType, entryType, ++maxSequence);
                supplementIndexAndPriceFromExisting(customerKeyword);
                addCustomerKeyword(customerKeyword, userName);
            }
        }
    }

    private int getMaxSequence(String terminalType, String entryType, Long customerUuid) {
        Integer maxSequence;
        try {
            maxSequence = customerKeywordDao.getMaxSequence(terminalType, entryType, customerUuid);
        } catch (Exception ex) {
            throw new RuntimeException("customerKeywordDao.getMaxSequence() is error");
        }
        return maxSequence == null ? 0 : maxSequence;
    }

    private void supplementInfoFromSimpleUI(CustomerKeyword customerKeyword, String terminalType, String entryType, int maxSequence) {
        customerKeyword.setType(entryType);
        customerKeyword.setTerminalType(terminalType);
        customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
        customerKeyword.setStartOptimizedTime(Utils.getCurrentTimestamp());
        customerKeyword.setCollectMethod(CollectMethod.PerDay.getCode());
        customerKeyword.setServiceProvider("baidutop123");
        customerKeyword.setSequence(maxSequence);
        customerKeyword.setCreateTime(Utils.getCurrentTimestamp());
        customerKeyword.setUpdateTime(Utils.getCurrentTimestamp());
        customerKeyword.setCustomerKeywordSource(CustomerKeywordSourceEnum.UI.name());
    }

    private void supplementIndexAndPriceFromExisting(CustomerKeyword customerKeyword) {
        List<CustomerKeyword> existingCustomerKeywords = customerKeywordDao.searchSameCustomerKeywords(customerKeyword.getTerminalType(),
            customerKeyword.getCustomerUuid(), customerKeyword.getKeyword().trim(), customerKeyword.getSearchEngine());
        if (CollectionUtils.isNotEmpty(existingCustomerKeywords)) {
            CustomerKeyword existingCustomerKeyword = existingCustomerKeywords.get(0);
            customerKeyword.setInitialIndexCount(existingCustomerKeyword.getInitialIndexCount());
            customerKeyword.setCurrentIndexCount(existingCustomerKeyword.getCurrentIndexCount());
            customerKeyword.setOptimizePlanCount(existingCustomerKeyword.getOptimizePlanCount());
            customerKeyword.setOptimizeRemainingCount(existingCustomerKeyword.getOptimizePlanCount());
            customerKeyword.setPositionFirstFee(existingCustomerKeyword.getPositionFirstFee());
            customerKeyword.setPositionSecondFee(existingCustomerKeyword.getPositionSecondFee());
            customerKeyword.setPositionThirdFee(existingCustomerKeyword.getPositionThirdFee());
            customerKeyword.setPositionForthFee(existingCustomerKeyword.getPositionForthFee());
            customerKeyword.setPositionFifthFee(existingCustomerKeyword.getPositionFifthFee());
            customerKeyword.setPositionFirstPageFee(existingCustomerKeyword.getPositionFirstPageFee());
        }
    }

    @Override
    public Integer getMaxInvalidCountByMachineGroup(String machineGroup) {
        return customerKeywordDao.getMaxInvalidCountByMachineGroup(machineGroup);
    }

    @Override
    public Map<String, Object> getCustomerKeywordStatusCount(String loginName) {
        return customerKeywordDao.getCustomerKeywordStatusCount(loginName);
    }

    @Override
    public Map<String, Object> getUseMachineProportion(String username) {
        return customerKeywordDao.getUseMachineProportion(username);
    }
}


