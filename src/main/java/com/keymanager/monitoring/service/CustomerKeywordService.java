package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.enums.CollectMethod;
import com.keymanager.monitoring.common.email.ObserveOptimizationCountMailService;
import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.dao.CustomerKeywordDao;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.*;
import com.keymanager.monitoring.excel.operator.AbstractExcelReader;
import com.keymanager.monitoring.vo.*;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import com.keymanager.value.CustomerKeywordForCapturePosition;
import com.keymanager.value.CustomerKeywordForCaptureTitle;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

@Service
public class CustomerKeywordService extends ServiceImpl<CustomerKeywordDao, CustomerKeyword> {
    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordService.class);

    @Autowired
    private QZCaptureTitleLogService qzCaptureTitleLogService;

    @Autowired
    private QZSettingService qzSettingService;

    @Autowired
    private QZOperationTypeService qzOperationTypeService;

    @Autowired
    private CustomerChargeTypeService customerChargeTypeService;

    @Autowired
    private MachineInfoService machineInfoService;

    @Autowired
    private CustomerKeywordIPService customerKeywordIPService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private KeywordOptimizationCountService keywordOptimizationCountService;

    @Autowired
    private CustomerKeywordPositionSummaryService customerKeywordPositionSummaryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private CustomerKeywordInvalidCountLogService customerKeywordInvalidCountLogService;

    @Autowired
    private CaptureRealUrlService captureRealUrlService;

    @Autowired
    private NegativeListService negativeListService;

    @Autowired
    private CustomerKeywordDao customerKeywordDao;

    @Autowired
    private CaptureRankJobService captureRankJobService;

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private GroupSettingService groupSettingService;

    @Autowired
    private ObserveOptimizationCountMailService observeOptimizationCountMailService;

    @Autowired
    private DailyReportService dailyReportService;

    @Autowired
    private DailyReportItemService dailyReportItemService;

    @Autowired
    private NegativeListUpdateInfoService negativeListUpdateInfoService;

    @Autowired
    private ScreenedWebsiteService screenedWebsiteService;

    @Autowired
    private OperationCombineService operationCombineService;

    @Autowired
    private GroupService groupService;

    private final static Map<String, ArrayBlockingQueue> machineGroupQueueMap = new HashMap<String, ArrayBlockingQueue>();

    public void cacheCustomerKeywords() {
        List<String> machineGroups = customerKeywordDao.getMachineGroups();
        if (CollectionUtils.isNotEmpty(machineGroups)) {
            for (String machineGroup : machineGroups) {
                ArrayBlockingQueue blockingQueue = machineGroupQueueMap.get(machineGroup);
                if (blockingQueue == null) {
                    blockingQueue = new ArrayBlockingQueue<String>(10000);
                    machineGroupQueueMap.put(machineGroup, blockingQueue);
                }
                if (blockingQueue.size() < 2000) {
                    String[] terminalTypeAndMachineGroups = machineGroup.split("####");
                    List<OptimizationKeywordVO> optimizationKeywordVOS = null;
                    do {
                        optimizationKeywordVOS = customerKeywordDao.fetchCustomerKeywordsForCache(terminalTypeAndMachineGroups[0], terminalTypeAndMachineGroups[1]);
                        if (CollectionUtils.isNotEmpty(optimizationKeywordVOS)) {
                            List<Long> customerKeywordUuids = new ArrayList<Long>();
                            if(optimizationKeywordVOS.size() > 400) {
                                for (OptimizationKeywordVO optimizationKeywordVO : optimizationKeywordVOS) {
                                    blockingQueue.offer(optimizationKeywordVO);
                                    customerKeywordUuids.add(optimizationKeywordVO.getUuid());
                                }
                                updateOptimizationQueryTime(customerKeywordUuids);
                            }else{
                                int count = 0;
                                boolean hasNewElement = false;
                                Map<Long, Integer> customerKeywordUuidAndRepeatCount = new HashMap<>();
                                do {
                                    for (OptimizationKeywordVO optimizationKeywordVO : optimizationKeywordVOS) {
                                        if(optimizationKeywordVO.getQueryInterval() > 0) {
                                            int maxOptimizeCount = Math.round(System.currentTimeMillis() / (1000 * optimizationKeywordVO.getQueryInterval()));
                                            Integer repeatCount = customerKeywordUuidAndRepeatCount.get(optimizationKeywordVO.getUuid());
                                            if (repeatCount == null) {
                                                customerKeywordUuidAndRepeatCount.put(optimizationKeywordVO.getUuid(), 0);
                                            }
                                            if (maxOptimizeCount > optimizationKeywordVO.getOptimizedCount()) {
                                                blockingQueue.offer(optimizationKeywordVO);
                                                optimizationKeywordVO.setOptimizedCount(optimizationKeywordVO.getOptimizedCount() + 1);
                                                hasNewElement = true;
                                                customerKeywordUuidAndRepeatCount.put(optimizationKeywordVO.getUuid(), (customerKeywordUuidAndRepeatCount.get(optimizationKeywordVO.getUuid()) + 1));
                                                count++;
                                                if (count > 2000) {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }while(count < 2000 && hasNewElement);
                                do{
                                    customerKeywordUuids.addAll(customerKeywordUuidAndRepeatCount.keySet());
                                    updateOptimizationQueryTime(customerKeywordUuids);
                                    customerKeywordUuids.clear();
                                    for(Long customerKeywordUuid : customerKeywordUuidAndRepeatCount.keySet()){
                                        if(customerKeywordUuidAndRepeatCount.get(customerKeywordUuid) < 2){
                                            customerKeywordUuidAndRepeatCount.remove(customerKeywordUuid);
                                        }else{
                                            customerKeywordUuidAndRepeatCount.put(customerKeywordUuid, (customerKeywordUuidAndRepeatCount.get(customerKeywordUuid) - 1));
                                        }
                                    }
                                }while (customerKeywordUuidAndRepeatCount.size() > 0);
                            }
                        }
                    } while (blockingQueue.size() < 8000 && CollectionUtils.isNotEmpty(optimizationKeywordVOS));
                }
            }
        }
    }

    public OptimizationVO fetchCustoemrKeywordForOptimization(MachineInfo machineInfo) throws InterruptedException {
        if (!machineInfo.getValid()) {
            return null;
        }

        String key = machineInfo.getTerminalType() + "####" + (machineInfo.getMachineGroup() == null ? "Default" : machineInfo.getMachineGroup());
        ArrayBlockingQueue arrayBlockingQueue = machineGroupQueueMap.get(key);
        if (arrayBlockingQueue != null) {
            Object obj = arrayBlockingQueue.poll();
            if (obj != null) {
                OptimizationKeywordVO keywordVO = (OptimizationKeywordVO) obj;
                if(StringUtils.isNotBlank(keywordVO.getOptimizeGroup())) {
                    OperationCombine operationCombine = operationCombineService.getOperationCombine(keywordVO.getOptimizeGroup(), machineInfo.getTerminalType());
                    GroupSetting groupSetting = groupSettingService.getGroupSetting(operationCombine);
                    OptimizationMachineVO optimizationMachineVO = new OptimizationMachineVO();
                    optimizationMachineVO.setDisableStatistics(groupSetting.getDisableStatistics());
                    optimizationMachineVO.setDisableVisitWebsite(groupSetting.getDisableVisitWebsite());
                    optimizationMachineVO.setEntryPageMinCount(groupSetting.getEntryPageMinCount());
                    optimizationMachineVO.setEntryPageMaxCount(groupSetting.getEntryPageMaxCount());
                    optimizationMachineVO.setPageRemainMinTime(groupSetting.getPageRemainMinTime());
                    optimizationMachineVO.setPageRemainMaxTime(groupSetting.getPageRemainMaxTime());
                    optimizationMachineVO.setInputDelayMinTime(groupSetting.getInputDelayMinTime());
                    optimizationMachineVO.setInputDelayMaxTime(groupSetting.getInputDelayMaxTime());
                    optimizationMachineVO.setSlideDelayMinTime(groupSetting.getSlideDelayMinTime());
                    optimizationMachineVO.setSlideDelayMaxTime(groupSetting.getSlideDelayMaxTime());
                    optimizationMachineVO.setTitleRemainMinTime(groupSetting.getTitleRemainMinTime());
                    optimizationMachineVO.setTitleRemainMaxTime(groupSetting.getTitleRemainMaxTime());
                    optimizationMachineVO.setOptimizeKeywordCountPerIP(groupSetting.getOptimizeKeywordCountPerIP());
                    optimizationMachineVO.setRandomlyClickNoResult(groupSetting.getRandomlyClickNoResult());
                    optimizationMachineVO.setMaxUserCount(groupSetting.getMaxUserCount());
                    optimizationMachineVO.setPage(groupSetting.getPage());
                    optimizationMachineVO.setOperationType(groupSetting.getOperationType());
                    optimizationMachineVO.setClearCookie(groupSetting.getClearCookie());
                    OptimizationVO optimizationVO = new OptimizationVO();
                    optimizationVO.setKeywordVO(keywordVO);
                    optimizationVO.setMachineVO(optimizationMachineVO);
                    return optimizationVO;
                }
            }
        }
        return null;
    }

    public Page<CustomerKeyword> searchCustomerKeywords(Page<CustomerKeyword> page, CustomerKeywordCriteria customerKeywordCriteria) {
        page.setRecords(customerKeywordDao.searchCustomerKeywordsPageForCustomer(page, customerKeywordCriteria));
        return page;
    }

    public List<CustomerKeyword> searchCustomerKeywordInfo(CustomerKeywordCriteria customerKeywordCriteria) {
        return customerKeywordDao.searchCustomerKeywordInfo(customerKeywordCriteria);
    }

    public List<CustomerKeyword> searchCustomerKeywordsForDailyReport(CustomerKeywordCriteria customerKeywordCriteria) {
        return customerKeywordDao.searchCustomerKeywordsForDailyReport(customerKeywordCriteria);
    }

    // new
    public List<CustomerKeywordForCaptureTitle> searchCustomerKeywordsForCaptureTitle(String terminalType, String searchEngine, Integer batchCount) throws Exception {
        QZCaptureTitleLog qzCaptureTitleLog = qzCaptureTitleLogService.getAvailableQZSetting(QZCaptureTitleLogStatusEnum.Processing.getValue(), terminalType);
        if (qzCaptureTitleLog == null) {
            qzCaptureTitleLog = qzCaptureTitleLogService.getAvailableQZSetting(QZCaptureTitleLogStatusEnum.New.getValue(), terminalType);
            if (qzCaptureTitleLog != null) {
                qzCaptureTitleLogService.startQZCaptureTitleLog(qzCaptureTitleLog.getUuid());
            }
        }
        if (qzCaptureTitleLog == null) {
            return null;
        }
        List<Long> customerKeywordUuids = customerKeywordDao.searchCustomerKeywordsUuidForCaptureTitle(qzCaptureTitleLog, searchEngine, batchCount);
        if (CollectionUtils.isEmpty(customerKeywordUuids)) {
            qzCaptureTitleLogService.completeQZCaptureTitleLog(qzCaptureTitleLog.getUuid());
            customerKeywordDao.deleteEmptyTitleCustomerKeyword(qzCaptureTitleLog, searchEngine);
            logger.info("deleteEmptyTitleCustomerKeyword:" + qzCaptureTitleLog.getCustomerUuid() + "-" + qzCaptureTitleLog.getGroup());
            return null;
        } else {
            List<CustomerKeywordForCaptureTitle> customerKeywordForCaptureTitles = new ArrayList<CustomerKeywordForCaptureTitle>();
            QZOperationType qzOperationType = qzOperationTypeService.selectById(qzCaptureTitleLog.getQzOperationTypeUuid());
            QZSetting qzSetting = qzSettingService.selectById(qzOperationType.getQzSettingUuid());
            String subDomainName = qzOperationType.getSubDomainName();
            for (Long customerKeywordUuid : customerKeywordUuids) {
                CustomerKeywordForCaptureTitle captureTitle = customerKeywordDao.searchCustomerKeywordForCaptureTitle(customerKeywordUuid);
                if (StringUtils.isNotEmpty(subDomainName)) {
                    captureTitle.setWholeUrl(subDomainName);
                } else {
                    captureTitle.setWholeUrl(qzSetting.getDomain());
                }
                updateCaptureTitleQueryTime((long) captureTitle.getUuid());
                customerKeywordForCaptureTitles.add(captureTitle);
            }
            return customerKeywordForCaptureTitles;
        }
    }

    // new
    public List<CustomerKeywordForCaptureTitle> searchCustomerKeywordsForCaptureTitle(String groupName, String terminalType, String searchEngine, Integer batchCount) throws Exception {
        QZCaptureTitleLog qzCaptureTitleLog = new QZCaptureTitleLog();
        qzCaptureTitleLog.setGroup(groupName);
        qzCaptureTitleLog.setTerminalType(terminalType);
        List<Long> customerKeywordUuids = customerKeywordDao.searchCustomerKeywordsUuidForCaptureTitle(qzCaptureTitleLog, searchEngine, batchCount);
        List<CustomerKeywordForCaptureTitle> captureTitles = new ArrayList<CustomerKeywordForCaptureTitle>();
        if (CollectionUtils.isNotEmpty(customerKeywordUuids)) {
            for (Long customerKeywordUuid : customerKeywordUuids) {
                CustomerKeywordForCaptureTitle captureTitle = customerKeywordDao.searchCustomerKeywordForCaptureTitle(customerKeywordUuid);
                updateCaptureTitleQueryTime((long) captureTitle.getUuid());
                captureTitles.add(captureTitle);
            }
        }
        return captureTitles;
    }

    private void updateCaptureTitleQueryTime(Long uuid) {
        CustomerKeyword customerKeyword = customerKeywordDao.selectById(uuid);
        customerKeyword.setCaptureTitleQueryTime(new Date());
        customerKeywordDao.updateById(customerKeyword);
    }

    public void cleanTitle(CustomerKeywordCleanCriteria customerKeywordCleanCriteria) {
        if (CustomerKeywordCleanTypeEnum.CustomerTitle.name().equals(customerKeywordCleanCriteria.getCleanType())) {
            customerKeywordDao.cleanCustomerTitle(customerKeywordCleanCriteria.getTerminalType(), customerKeywordCleanCriteria.getEntryType(), customerKeywordCleanCriteria.getCustomerUuid());
        } else if (CustomerKeywordCleanTypeEnum.SelectedCustomerKeywordTitle.name().equals(customerKeywordCleanCriteria.getCleanType())) {
            customerKeywordDao.cleanSelectedCustomerKeywordTitle(customerKeywordCleanCriteria.getCustomerKeywordUuids());
        } else if (CustomerKeywordCleanTypeEnum.CaptureTitleBySelected.name().equals(customerKeywordCleanCriteria.getCleanType())) {
            customerKeywordDao.cleanCaptureTitleBySelected(customerKeywordCleanCriteria.getCustomerKeywordUuids());
        } else {
            customerKeywordDao.cleanCaptureTitleFlag(customerKeywordCleanCriteria.getTerminalType(), customerKeywordCleanCriteria.getEntryType(), customerKeywordCleanCriteria.getCustomerUuid());
        }
    }

    public int getMaxSequence(String terminalType, String entryType, Long customerUuid) {
        int maxSequence = 0;
        try {
            maxSequence = customerKeywordDao.getMaxSequence(terminalType, entryType, customerUuid);
        } catch (Exception ex) {
        }
        return maxSequence;
    }

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

   /* public void addCustomerKeywords(List<CustomerKeyword> customerKeywords) {
        for (CustomerKeyword customerKeyword : customerKeywords) {
            addCustomerKeyword(customerKeyword);
        }
    }*/


    public void addCustomerKeyword(CustomerKeyword customerKeyword, String userName) {
        customerKeyword = checkCustomerKeyword(customerKeyword, userName);
        if (customerKeyword != null) {
            customerKeywordDao.insert(customerKeyword);
        }
    }

    public void addCustomerKeyword(List<CustomerKeyword> customerKeywords, String userName) {
        List<CustomerKeyword> addCustomerKeywords = new ArrayList<>();
        for (CustomerKeyword customerKeyword : customerKeywords) {
            CustomerKeyword tmpCustomerKeyword = checkCustomerKeyword(customerKeyword, userName);
            if (tmpCustomerKeyword != null) {
                addCustomerKeywords.add(customerKeyword);
            }
        }
        if (CollectionUtils.isNotEmpty(addCustomerKeywords)) {
            customerKeywordDao.addCustomerKeywords(addCustomerKeywords);
        }
    }

    public CustomerKeyword checkCustomerKeyword(CustomerKeyword customerKeyword, String userName) {
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
            Integer sameCustomerKeywordCount = customerKeywordDao.getSameCustomerKeywordCount(customerKeyword.getTerminalType(), customerKeyword.getCustomerUuid(), customerKeyword.getKeyword(), customerKeyword.getUrl(), customerKeyword.getTitle());
            if (sameCustomerKeywordCount != null && sameCustomerKeywordCount > 0) {
                if (!CustomerKeywordSourceEnum.Capture.name().equals(customerKeyword.getCustomerKeywordSource())) {
                    customerKeywordDao.updateSameCustomerKeywordSource(customerKeyword.getTerminalType(), customerKeyword.getCustomerUuid(), customerKeyword.getKeyword(), customerKeyword.getUrl(), customerKeyword.getTitle(), customerKeyword.getCustomerKeywordSource());
                }
                return null;
            }
        }
        if (!EntryTypeEnum.fm.name().equals(customerKeyword.getType()) && haveDuplicatedCustomerKeyword(customerKeyword.getTerminalType(),
                customerKeyword.getCustomerUuid(), customerKeyword.getKeyword(), originalUrl, customerKeyword.getTitle())) {
            if (!CustomerKeywordSourceEnum.Capture.name().equals(customerKeyword.getCustomerKeywordSource())) {
                customerKeywordDao.updateSimilarCustomerKeywordSource(customerKeyword.getTerminalType(), customerKeyword.getCustomerUuid(), customerKeyword.getKeyword(), customerKeyword.getUrl(), customerKeyword.getTitle(), customerKeyword.getCustomerKeywordSource());
            }
            return null;
        }

        customerKeyword.setKeyword(customerKeyword.getKeyword().trim());
        customerKeyword.setUrl(customerKeyword.getUrl() != null ? customerKeyword.getUrl().trim() : null);
        customerKeyword.setTitle(customerKeyword.getTitle() != null ? customerKeyword.getTitle().trim() : null);
        customerKeyword.setOriginalUrl(customerKeyword.getOriginalUrl() != null ? customerKeyword.getOriginalUrl().trim() : null);
        customerKeyword.setOrderNumber(customerKeyword.getOrderNumber() != null ? customerKeyword.getOrderNumber().trim() : null);
        customerKeyword.setOptimizeRemainingCount(customerKeyword.getOptimizePlanCount() != null ? customerKeyword.getOptimizePlanCount() : 0);

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

        if (customerKeyword.getCurrentPosition() == null) {
            customerKeyword.setCurrentPosition(10);
        }
        if (Utils.isNullOrEmpty(customerKeyword.getMachineGroup())) {
            customerKeyword.setMachineGroup("Default");
        }
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

    public void updateCustomerKeywordFromUI(CustomerKeyword customerKeyword, String userName) {
        boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
        if (isDepartmentManager) {
            customerKeyword.setStatus(1);
        } else {
            customerKeyword.setStatus(2);
        }
        customerKeyword.setUpdateTime(new Date());
        customerKeywordDao.updateById(customerKeyword);
    }

    public boolean haveDuplicatedCustomerKeyword(String terminalType, long customerUuid, String keyword, String originalUrl, String title) {
        int customerKeywordCount = 0;
        try {
            customerKeywordCount = customerKeywordDao.getSimilarCustomerKeywordCount(terminalType, customerUuid, keyword, originalUrl, title);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
        return customerKeywordCount > 0;
    }

    public List<Map> searchAllKeywordAndUrl(Long customerUuid, String terminalType) {
        return customerKeywordDao.selectAllKeywordAndUrl(customerUuid, terminalType);
    }

    public int getCustomerKeywordCount(String terminalType, String entryType, long customerUuid) {
        return customerKeywordDao.getCustomerKeywordCount(terminalType, entryType, customerUuid);
    }

    public void deleteCustomerKeywordsByUuid(List<String> customerKeywordUuids) {
        logger.info("deleteCustomerKeywordsByUuid:" + customerKeywordUuids.toString());
        customerKeywordDao.deleteCustomerKeywordsByUuid(customerKeywordUuids);
    }

    public void deleteCustomerKeywordsWhenEmptyTitleAndUrl(String terminalType, String entryType, String customerUuid) {
        logger.info("deleteCustomerKeywordsWhenEmptyTitleAndUrl:" + customerUuid);
        customerKeywordDao.deleteCustomerKeywordsWhenEmptyTitleAndUrl(terminalType, entryType, customerUuid);
    }

    public void deleteCustomerKeywordsWhenEmptyTitle(String terminalType, String entryType, String customerUuid) {
        logger.info("deleteCustomerKeywordsWhenEmptyTitle:" + customerUuid);
        customerKeywordDao.deleteCustomerKeywordsWhenEmptyTitle(terminalType, entryType, customerUuid);
    }

    public void deleteCustomerKeywords(long customerUuid) {
        logger.info("deleteCustomerKeywords:" + customerUuid);
        customerKeywordDao.deleteCustomerKeywordsByCustomerUuid(customerUuid);
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

    public void supplementIndexAndPriceFromExisting(CustomerKeyword customerKeyword) {
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

    public CustomerKeyword getCustomerKeywordsForCaptureIndex() {
        CustomerKeyword customerKeyword = customerKeywordDao.getCustomerKeywordsForCaptureIndex();
        if (customerKeyword != null) {
            customerKeywordDao.updateCaptureIndexQueryTime(customerKeyword.getKeyword());
            return customerKeyword;
        }
        return null;
    }

    public void updateCustomerKeywordIndex(BaiduIndexCriteria baiduIndexCriteria) {
        List<CustomerKeyword> customerKeywords = customerKeywordDao.searchCustomerKeywordsForUpdateIndex(baiduIndexCriteria.getKeyword());
        if (CollectionUtils.isNotEmpty(customerKeywords)) {
            for (CustomerKeyword customerKeyword : customerKeywords) {
                if (TerminalTypeEnum.PC.name().equals(customerKeyword.getTerminalType())) {
                    customerKeyword.setInitialIndexCount(baiduIndexCriteria.getPcIndex());
                    customerKeyword.setCurrentIndexCount(baiduIndexCriteria.getPcIndex());
                    if (EntryTypeEnum.qz.name().equals(customerKeyword.getType())) {
                        customerKeyword.setOptimizePlanCount(baiduIndexCriteria.getPcIndex() == 0 ? 5 : baiduIndexCriteria.getPcIndex());
                        customerKeyword.setOptimizeRemainingCount(baiduIndexCriteria.getPcIndex() == 0 ? 5 : baiduIndexCriteria.getPcIndex());
                    } else {
                        customerKeyword.setOptimizePlanCount(baiduIndexCriteria.getPcIndex() < 100 ? 100 : baiduIndexCriteria.getPcIndex());
                        customerKeyword.setOptimizeRemainingCount(baiduIndexCriteria.getPcIndex() < 100 ? 100 : baiduIndexCriteria.getPcIndex());
                    }
                } else {
                    customerKeyword.setInitialIndexCount(baiduIndexCriteria.getPhoneIndex());
                    customerKeyword.setCurrentIndexCount(baiduIndexCriteria.getPhoneIndex());
                    if (EntryTypeEnum.qz.name().equals(customerKeyword.getType())) {
                        customerKeyword.setOptimizePlanCount(baiduIndexCriteria.getPhoneIndex() == 0 ? 5 : baiduIndexCriteria.getPhoneIndex());
                        customerKeyword.setOptimizeRemainingCount(baiduIndexCriteria.getPhoneIndex() == 0 ? 5 : baiduIndexCriteria.getPhoneIndex());
                    } else {
                        customerKeyword.setOptimizePlanCount(baiduIndexCriteria.getPhoneIndex() < 100 ? 100 : baiduIndexCriteria.getPhoneIndex());
                        customerKeyword.setOptimizeRemainingCount(baiduIndexCriteria.getPhoneIndex() < 100 ? 100 : baiduIndexCriteria.getPhoneIndex());
                    }
                }
                calculatePrice(customerKeyword);
                customerKeyword.setUpdateTime(new Date());
                customerKeywordDao.updateCustomerKeywordIndex(customerKeyword);
            }
        }
    }

    private void calculatePrice(CustomerKeyword customerKeyword) {
        CustomerChargeType customerChargeType = customerChargeTypeService.getCustomerChargeType(customerKeyword.getCustomerUuid());
        if (customerChargeType != null) {
            if (ChargeTypeEnum.Percentage.name().equals(customerChargeType.getChargeType())) {
                CustomerChargeTypeCalculation percentageCustomerChargeTypeCalculation = null;
                CustomerChargeTypeCalculation fixedPriceCustomerChargeTypeCalculation = null;
                for (CustomerChargeTypeCalculation tmpCustomerChargeTypeCalculation : customerChargeType.getCustomerChargeTypeCalculations()) {
                    if (tmpCustomerChargeTypeCalculation.getOperationType().equals(customerKeyword.getTerminalType())) {
                        if (ChargeDataTypeEnum.LessThanHundred.name().equals(tmpCustomerChargeTypeCalculation.getChargeDataType())) {
                            fixedPriceCustomerChargeTypeCalculation = tmpCustomerChargeTypeCalculation;
                        }
                        if (ChargeDataTypeEnum.Percentage.name().equals(tmpCustomerChargeTypeCalculation.getChargeDataType())) {
                            percentageCustomerChargeTypeCalculation = tmpCustomerChargeTypeCalculation;
                        }
                    }
                }

                if (percentageCustomerChargeTypeCalculation != null) {
                    if (percentageCustomerChargeTypeCalculation.getChargesOfFirst() != null) {
                        double positionFirstFee = calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeTypeCalculation != null ? fixedPriceCustomerChargeTypeCalculation.getChargesOfFirst() :
                                        null), percentageCustomerChargeTypeCalculation.getChargesOfFirst());
                        if (percentageCustomerChargeTypeCalculation.getMaxPrice() != null) {
                            double maxPrice = percentageCustomerChargeTypeCalculation.getMaxPrice();
                            positionFirstFee = positionFirstFee > maxPrice ? maxPrice : positionFirstFee;
                        }
                        customerKeyword.setPositionFirstFee(positionFirstFee);
                    }
                    if (percentageCustomerChargeTypeCalculation.getChargesOfSecond() != null) {
                        double positionSecondFee = calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeTypeCalculation != null ? fixedPriceCustomerChargeTypeCalculation.getChargesOfSecond() :
                                        null), percentageCustomerChargeTypeCalculation.getChargesOfSecond());
                        if (percentageCustomerChargeTypeCalculation.getMaxPrice() != null) {
                            double maxPrice = percentageCustomerChargeTypeCalculation.getMaxPrice();
                            positionSecondFee = positionSecondFee > maxPrice ? maxPrice : positionSecondFee;
                        }
                        customerKeyword.setPositionSecondFee(positionSecondFee);
                    }
                    if (percentageCustomerChargeTypeCalculation.getChargesOfThird() != null) {
                        double positionThirdFee = calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeTypeCalculation != null ? fixedPriceCustomerChargeTypeCalculation.getChargesOfThird() :
                                        null), percentageCustomerChargeTypeCalculation.getChargesOfThird());
                        if (percentageCustomerChargeTypeCalculation.getMaxPrice() != null) {
                            double maxPrice = percentageCustomerChargeTypeCalculation.getMaxPrice();
                            positionThirdFee = positionThirdFee > maxPrice ? maxPrice : positionThirdFee;
                        }
                        customerKeyword.setPositionThirdFee(positionThirdFee);
                    }
                    if (percentageCustomerChargeTypeCalculation.getChargesOfFourth() != null) {
                        double positionForthFee = calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeTypeCalculation != null ? fixedPriceCustomerChargeTypeCalculation.getChargesOfFourth() :
                                        null), percentageCustomerChargeTypeCalculation.getChargesOfFourth());
                        if (percentageCustomerChargeTypeCalculation.getMaxPrice() != null) {
                            double maxPrice = percentageCustomerChargeTypeCalculation.getMaxPrice();
                            positionForthFee = positionForthFee > maxPrice ? maxPrice : positionForthFee;
                        }
                        customerKeyword.setPositionForthFee(positionForthFee);
                    }
                    if (percentageCustomerChargeTypeCalculation.getChargesOfFifth() != null) {
                        double positionFifthFee = calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeTypeCalculation != null ? fixedPriceCustomerChargeTypeCalculation.getChargesOfFifth() :
                                        null), percentageCustomerChargeTypeCalculation.getChargesOfFifth());
                        if (percentageCustomerChargeTypeCalculation.getMaxPrice() != null) {
                            double maxPrice = percentageCustomerChargeTypeCalculation.getMaxPrice();
                            positionFifthFee = positionFifthFee > maxPrice ? maxPrice : positionFifthFee;
                        }
                        customerKeyword.setPositionFifthFee(positionFifthFee);
                    }
                    if (percentageCustomerChargeTypeCalculation.getChargesOfFirstPage() != null) {
                        double positionFirstPageFee = calculatePrice(customerKeyword.getCurrentIndexCount(),
                                fixedPriceCustomerChargeTypeCalculation.getChargesOfFirstPage(), percentageCustomerChargeTypeCalculation
                                        .getChargesOfFirstPage());
                        if (percentageCustomerChargeTypeCalculation.getMaxPrice() != null) {
                            double maxPrice = percentageCustomerChargeTypeCalculation.getMaxPrice();
                            positionFirstPageFee = positionFirstPageFee > maxPrice ? maxPrice : positionFirstPageFee;
                        }
                        customerKeyword.setPositionFirstPageFee(positionFirstPageFee);
                    }
                }
            } else {
                for (CustomerChargeTypeInterval tmpCustomerChargeTypeInterval : customerChargeType.getCustomerChargeTypeIntervals()) {
                    if (tmpCustomerChargeTypeInterval.getOperationType().equals(customerKeyword.getTerminalType())) {
                        if (tmpCustomerChargeTypeInterval.getStartIndex() <= customerKeyword
                                .getCurrentIndexCount() && (tmpCustomerChargeTypeInterval.getEndIndex() == null || customerKeyword
                                .getCurrentIndexCount() <= tmpCustomerChargeTypeInterval.getEndIndex())) {
                            for (CustomerChargeTypePercentage customerChargeTypePercentage : customerChargeType.getCustomerChargeTypePercentages()) {
                                if (customerChargeTypePercentage.getOperationType().equals(customerKeyword.getTerminalType())) {
                                    double positionFirstFee = Math.round(tmpCustomerChargeTypeInterval.getPrice().doubleValue() * customerChargeTypePercentage.getFirstChargePercentage() * 0.01);
                                    customerKeyword.setPositionFirstFee(positionFirstFee);

                                    double positionSecondFee = Math.round(tmpCustomerChargeTypeInterval.getPrice().doubleValue() * customerChargeTypePercentage.getSecondChargePercentage() * 0.01);
                                    customerKeyword.setPositionSecondFee(positionSecondFee);

                                    double positionThirdFee = Math.round(tmpCustomerChargeTypeInterval.getPrice().doubleValue() * customerChargeTypePercentage.getThirdChargePercentage() * 0.01);
                                    customerKeyword.setPositionThirdFee(positionThirdFee);

                                    double positionForthFee = Math.round(tmpCustomerChargeTypeInterval.getPrice().doubleValue() * customerChargeTypePercentage.getFourthChargePercentage() * 0.01);
                                    customerKeyword.setPositionForthFee(positionForthFee);

                                    double positionFifthFee = Math.round(tmpCustomerChargeTypeInterval.getPrice().doubleValue() * customerChargeTypePercentage.getFifthChargePercentage() * 0.01);
                                    customerKeyword.setPositionFifthFee(positionFifthFee);

                                    double positionFirstPageFee = Math.round(tmpCustomerChargeTypeInterval.getPrice().doubleValue() * customerChargeTypePercentage.getFirstPageChargePercentage() * 0.01);
                                    customerKeyword.setPositionFirstPageFee(positionFirstPageFee);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private double calculatePrice(int currentIndexCount, BigDecimal fixedPrice, BigDecimal pricePercentage) {
        if (fixedPrice != null && currentIndexCount <= 100) {
            return fixedPrice.doubleValue();
        }
        double price = Math.round((currentIndexCount * pricePercentage.doubleValue()) / 1000.0) * 10;
        return price < fixedPrice.doubleValue() ? fixedPrice.doubleValue() : price;
    }

    public List<Map> getCustomerKeywordsCount(List<Long> customerUuids, String terminalType, String entryType) {
        return customerKeywordDao.getCustomerKeywordsCount(customerUuids, terminalType, entryType);
    }

    public void updateCustomerKeywordGroupName(CustomerKeywordUpdateCriteria customerKeywordUpdateCriteria) {
        customerKeywordDao.updateCustomerKeywordGroupName(customerKeywordUpdateCriteria);
    }

    public void updateCustomerKeywordSearchEngine(CustomerKeywordUpdateCriteria customerKeywordUpdateCriteria) {
        customerKeywordDao.updateCustomerKeywordSearchEngine(customerKeywordUpdateCriteria);
    }

    public void updateCustomerKeywordGroupNameByRank(Map<String, Object> resultMap) {
        List<Long> customerKeywordUuids = customerKeywordDao.searchCustomerKeywordUuidByRank(resultMap);
        List<Long> customerKeyowrdUuidsTmp = new ArrayList<Long>();
        if (customerKeywordUuids != null) {
            for (Long customerKeywordUuid : customerKeywordUuids) {
                customerKeyowrdUuidsTmp.add(customerKeywordUuid);
                if (customerKeyowrdUuidsTmp.size() == 200) {
                    customerKeywordDao.updateCustomerKeywordGroupNameByRank(customerKeyowrdUuidsTmp, resultMap.get("targetGroupName").toString());
                    customerKeyowrdUuidsTmp.clear();
                }
            }
            if (customerKeyowrdUuidsTmp.size() > 0) {
                customerKeywordDao.updateCustomerKeywordGroupNameByRank(customerKeyowrdUuidsTmp, resultMap.get("targetGroupName").toString());
            }
        }
    }

    public CustomerKeyword getCustomerKeyword(Long CustomerKeywordUuid) {
        return customerKeywordDao.selectById(CustomerKeywordUuid);
    }

    //简化版Excel文件导入
    public boolean handleExcel(InputStream inputStream, String excelType, int customerUuid, String type, String terminalType, String userName)
            throws Exception {
        AbstractExcelReader operator = AbstractExcelReader.createExcelOperator(inputStream, excelType);
        List<CustomerKeyword> customerKeywords = operator.readDataFromExcel();
        supplementInfo(customerKeywords, customerUuid, type, terminalType);
        addCustomerKeywords(customerKeywords, userName);
        return true;
    }

    public void supplementInfo(List<CustomerKeyword> customerKeywords, int customerUuid, String type, String terminalType) {
        for (CustomerKeyword customerKeyword : customerKeywords) {
            customerKeyword.setCustomerUuid(customerUuid);
            customerKeyword.setType(type);
            customerKeyword.setCreateTime(Utils.getCurrentTimestamp());
            customerKeyword.setUpdateTime(Utils.getCurrentTimestamp());
            customerKeyword.setTerminalType(terminalType);
            customerKeyword.setCustomerKeywordSource(CustomerKeywordSourceEnum.Excel.name());
        }
    }

    public void addCustomerKeywords(List<CustomerKeyword> customerKeywords, String loginName) throws Exception {
        for (CustomerKeyword customerKeyword : customerKeywords) {
            if (customerKeyword.getKeywordEffect() == null || customerKeyword.getKeywordEffect().equals("")) {
                customerKeyword.setKeywordEffect(KeywordEffectEnum.Common.name());
            } else {
                if (customerKeyword.getKeywordEffect().trim().equals("曲线词")) {
                    customerKeyword.setKeywordEffect(KeywordEffectEnum.Curve.name());
                } else if (customerKeyword.getKeywordEffect().trim().equals("指定词")) {
                    customerKeyword.setKeywordEffect(KeywordEffectEnum.Appointment.name());
                } else if (customerKeyword.getKeywordEffect().trim().equals("赠送词")) {
                    customerKeyword.setKeywordEffect(KeywordEffectEnum.Present.name());
                } else {
                    customerKeyword.setKeywordEffect(KeywordEffectEnum.Common.name());
                }
            }
            addCustomerKeyword(customerKeyword, loginName);
        }
    }

    public void deleteCustomerKeywords(String deleteType, String terminalType, String entryType, String customerUuid, List<String> customerKeywordUuids) {
        if (CustomerKeywordDeletionTypeEnum.ByUuid.name().equals(deleteType)) {
            deleteCustomerKeywordsByUuid(customerKeywordUuids);
        } else if (CustomerKeywordDeletionTypeEnum.EmptyTitle.name().equals(deleteType)) {
            deleteCustomerKeywordsWhenEmptyTitle(terminalType, entryType, customerUuid);
        } else {
            deleteCustomerKeywordsWhenEmptyTitleAndUrl(terminalType, entryType, customerUuid);
        }
    }

    public List<String> getGroups(List<Long> customerUuids) {
        return customerKeywordDao.getGroups(customerUuids);
    }

    public CustomerKeywordForOptimization searchCustomerKeywordsForOptimization(String terminalType, String clientID, String version, boolean updateQueryInfo) {
        MachineInfo machineInfo = machineInfoService.selectById(clientID);
        if (machineInfo == null) {
            return null;
        }

        if (!machineInfo.getValid() || StringUtils.isEmpty(machineInfo.getGroup())) {
            return null;
        }

        if (keywordOptimizationCountService.resetBigKeywordIndicator(machineInfo.getGroup())) {
            keywordOptimizationCountService.init(machineInfo.getGroup());
        }

        Long customerKeywordUuid = null;
        CustomerKeyword customerKeyword = null;

        int retryCount = 0;
        int noPositionMaxInvalidCount = 2;
        OperationCombine operationCombine = operationCombineService.getOperationCombine(machineInfo.getGroup(), machineInfo.getTerminalType());
        GroupSetting groupSetting = groupSettingService.getGroupSetting(operationCombine);
        Integer maxInvalidCount = operationCombine.getMaxInvalidCount();

        if (groupSetting.getOperationType().contains(Constants.CONFIG_TYPE_ZHANNEI_SOGOU)) {
            Config configInvalidRefreshCount = configService.getConfig(Constants.CONFIG_TYPE_ZHANNEI_SOGOU, Constants.CONFIG_KEY_NOPOSITION_MAX_INVALID_COUNT);
            noPositionMaxInvalidCount = Integer.parseInt(configInvalidRefreshCount.getValue());
        }
        do {
            boolean isNormalKeyword = keywordOptimizationCountService.optimizeNormalKeyword(machineInfo.getGroup());
            customerKeywordUuid = customerKeywordDao.getCustomerKeywordUuidForOptimization(terminalType, machineInfo.getGroup(), !isNormalKeyword);

            if (customerKeywordUuid == null) {
                if (keywordOptimizationCountService.resetBigKeywordIndicator(machineInfo.getGroup())) {
                    keywordOptimizationCountService.init(machineInfo.getGroup());
                }
                customerKeywordUuid = customerKeywordDao.getCustomerKeywordUuidForOptimization(terminalType, machineInfo.getGroup(), false);
            }
            if (keywordOptimizationCountService.allowResetBigKeywordIndicator(machineInfo.getGroup())) {
                keywordOptimizationCountService.setLastVisitTime(machineInfo.getGroup());
                resetBigKeywordIndicator(machineInfo.getGroup(), maxInvalidCount, noPositionMaxInvalidCount);
            }
            retryCount++;
            if (customerKeywordUuid != null && updateQueryInfo) {
                customerKeywordDao.updateOptimizationQueryTimeSingle(customerKeywordUuid, maxInvalidCount);
            }
        } while (customerKeywordUuid == null && retryCount < 2);

        if (customerKeywordUuid != null) {
            customerKeyword = customerKeywordDao.getCustomerKeywordForOptimization(customerKeywordUuid);
            machineInfoService.updatePageNo(clientID, 0);
            CustomerKeywordForOptimization customerKeywordForOptimization = new CustomerKeywordForOptimization();
            customerKeywordForOptimization.setUuid(customerKeyword.getUuid());
            customerKeywordForOptimization.setKeyword(customerKeyword.getKeyword());
            customerKeywordForOptimization.setUrl(customerKeyword.getUrl());
            customerKeywordForOptimization.setEntryType(customerKeyword.getType());
            customerKeywordForOptimization.setBearPawNumber(customerKeyword.getBearPawNumber());

            customerKeywordForOptimization.setCurrentPosition(customerKeyword.getCurrentPosition());
            customerKeywordForOptimization.setOriginalUrl(customerKeyword.getOriginalUrl());
            customerKeywordForOptimization.setTitle(customerKeyword.getTitle());

            customerKeywordForOptimization.setGroup(machineInfo.getGroup());
            customerKeywordForOptimization.setOperationType(groupSetting.getOperationType());
            customerKeywordForOptimization.setBroadbandAccount(machineInfo.getBroadbandAccount());
            customerKeywordForOptimization.setBroadbandPassword(machineInfo.getBroadbandPassword());
            customerKeywordForOptimization.setSwitchGroupName(machineInfo.getSwitchGroupName());

            NegativeListUpdateInfo negativeListUpdateInfo = negativeListUpdateInfoService.getNegativeListUpdateInfo(customerKeyword.getKeyword());
            if (negativeListUpdateInfo != null) {
                customerKeywordForOptimization.setNegativeListUpdateTime(negativeListUpdateInfo.getNegativeListUpdateTime());
            }

            Set<String> specialGruupNames = new HashSet<String>();
            specialGruupNames.add("pc_pm_xiaowu");
            specialGruupNames.add("pc_pm_learner");
            specialGruupNames.add("pc_pm_51yza");
            specialGruupNames.add("pc_pm_yilufa");

            if (specialGruupNames.contains(customerKeyword.getOptimizeGroupName()) && (customerKeyword.getCurrentPosition() == 0 ||
                    customerKeyword.getCurrentPosition() > 20)) {
                customerKeywordForOptimization.setPage(2);
            } else {
                customerKeywordForOptimization.setPage(groupSetting.getPage());
            }

            if (groupSetting.getPageSize() != null) {
                if (specialGruupNames.contains(customerKeyword.getOptimizeGroupName()) && (customerKeyword.getCurrentPosition() == 0 ||
                        customerKeyword.getCurrentPosition() > 20)) {
                    customerKeywordForOptimization.setPageSize(50);
                } else {
                    customerKeywordForOptimization.setPageSize(groupSetting.getPageSize());
                }
            }
            if (groupSetting.getZhanneiPercent() != null) {
                customerKeywordForOptimization.setZhanneiPercent(groupSetting.getZhanneiPercent());
            }
            if (groupSetting.getZhanwaiPercent() != null) {
                customerKeywordForOptimization.setZhanwaiPercent(groupSetting.getZhanwaiPercent());
            }
            if (groupSetting.getSpecialCharPercent() != null) {
                customerKeywordForOptimization.setSpecialCharPercent(groupSetting.getSpecialCharPercent());
            }
            if (groupSetting.getBaiduSemPercent() != null) {
                customerKeywordForOptimization.setBaiduSemPercent(groupSetting.getBaiduSemPercent());
            }
            customerKeywordForOptimization.setClearCookie(groupSetting.getClearCookie());
            if (groupSetting.getDragPercent() != null) {
                customerKeywordForOptimization.setDragPercent(groupSetting.getDragPercent());
            }
            if (groupSetting.getKuaizhaoPercent() != null) {
                customerKeywordForOptimization.setKuaizhaoPercent(groupSetting.getKuaizhaoPercent());
            }
            if (groupSetting.getMultiBrowser() != null) {
                customerKeywordForOptimization.setMultiBrowser(groupSetting.getMultiBrowser());
            }
            customerKeywordForOptimization.setOpenStatistics(groupSetting.getDisableStatistics() == 1 ? 0 : 1);
            customerKeywordForOptimization.setDisableStatistics(groupSetting.getDisableStatistics());
            customerKeywordForOptimization.setCurrentTime(Utils.formatDate(new Date(), Utils.TIME_FORMAT));

            customerKeywordForOptimization.setEntryPageMinCount(groupSetting.getEntryPageMinCount());
            customerKeywordForOptimization.setEntryPageMaxCount(groupSetting.getEntryPageMaxCount());
            customerKeywordForOptimization.setDisableVisitWebsite(groupSetting.getDisableVisitWebsite());
            customerKeywordForOptimization.setPageRemainMinTime(groupSetting.getPageRemainMinTime());
            customerKeywordForOptimization.setPageRemainMaxTime(groupSetting.getPageRemainMaxTime());
            customerKeywordForOptimization.setInputDelayMinTime(groupSetting.getInputDelayMinTime());
            customerKeywordForOptimization.setInputDelayMaxTime(groupSetting.getInputDelayMaxTime());
            customerKeywordForOptimization.setSlideDelayMinTime(groupSetting.getSlideDelayMinTime());
            customerKeywordForOptimization.setSlideDelayMaxTime(groupSetting.getSlideDelayMaxTime());
            customerKeywordForOptimization.setTitleRemainMinTime(groupSetting.getTitleRemainMinTime());
            customerKeywordForOptimization.setTitleRemainMaxTime(groupSetting.getTitleRemainMaxTime());
            customerKeywordForOptimization.setOptimizeKeywordCountPerIP(groupSetting.getOptimizeKeywordCountPerIP());
            customerKeywordForOptimization.setOneIPOneUser(groupSetting.getOneIPOneUser());
            customerKeywordForOptimization.setRandomlyClickNoResult(groupSetting.getRandomlyClickNoResult());
            customerKeywordForOptimization.setJustVisitSelfPage(groupSetting.getJustVisitSelfPage());
            customerKeywordForOptimization.setSleepPer2Words(groupSetting.getSleepPer2Words());
            customerKeywordForOptimization.setSupportPaste(groupSetting.getSupportPaste());
            customerKeywordForOptimization.setMoveRandomly(groupSetting.getMoveRandomly());
            customerKeywordForOptimization.setParentSearchEntry(groupSetting.getParentSearchEntry());
            customerKeywordForOptimization.setClearLocalStorage(groupSetting.getClearLocalStorage());
            customerKeywordForOptimization.setLessClickAtNight(groupSetting.getLessClickAtNight());
            customerKeywordForOptimization.setSameCityUser(groupSetting.getSameCityUser());
            customerKeywordForOptimization.setLocateTitlePosition(groupSetting.getLocateTitlePosition());
            customerKeywordForOptimization.setBaiduAllianceEntry(groupSetting.getBaiduAllianceEntry());
            customerKeywordForOptimization.setJustClickSpecifiedTitle(groupSetting.getJustClickSpecifiedTitle());
            customerKeywordForOptimization.setRandomlyClickMoreLink(groupSetting.getRandomlyClickMoreLink());
            customerKeywordForOptimization.setMoveUp20(groupSetting.getMoveUp20());
            customerKeywordForOptimization.setWaitTimeAfterOpenBaidu(groupSetting.getWaitTimeAfterOpenBaidu());
            customerKeywordForOptimization.setWaitTimeBeforeClick(groupSetting.getWaitTimeBeforeClick());
            customerKeywordForOptimization.setWaitTimeAfterClick(groupSetting.getWaitTimeAfterClick());
            customerKeywordForOptimization.setMaxUserCount(groupSetting.getMaxUserCount());
            customerKeywordForOptimization.setSearchEngine(customerKeyword.getSearchEngine());
            customerKeywordForOptimization.setTerminalType(customerKeyword.getTerminalType());
            customerKeywordForOptimization.setRemarks(customerKeyword.getRemarks());
            if (StringUtils.isNotBlank(customerKeywordForOptimization.getOperationType())) {
                if (customerKeywordForOptimization.getOperationType().contains(Constants.CONFIG_TYPE_TJ_XG)) {
                    customerKeywordForOptimization.setNegativeKeywords(new ArrayList<String>());
                    Config configNegativeKeywords = configService.getConfig(Constants.CONFIG_TYPE_TJ_XG, Constants.CONFIG_KEY_NEGATIVE_KEYWORDS);
                    Set<String> negativeKeywords = new HashSet<String>();
                    if (StringUtils.isNotBlank(configNegativeKeywords.getValue())) {
                        negativeKeywords.addAll(convertToSets(configNegativeKeywords.getValue()));
                    }
                    if (StringUtils.isNotBlank(customerKeyword.getNegativeKeywords())) {
                        negativeKeywords.addAll(convertToSets(customerKeyword.getNegativeKeywords()));
                    }
                    customerKeywordForOptimization.getNegativeKeywords().addAll(negativeKeywords);

                    if (StringUtils.isNotBlank(customerKeyword.getExcludeKeywords())) {
                        customerKeywordForOptimization.setExcludeKeywords(new ArrayList<String>(convertToSets(customerKeyword.getExcludeKeywords())));
                    }
                    if (StringUtils.isNotBlank(customerKeyword.getRecommendKeywords())) {
                        customerKeywordForOptimization.setRecommendedKeywords(new ArrayList<String>(convertToSets(customerKeyword.getRecommendKeywords())));
                    }

                    customerKeywordForOptimization.setOperateSelectKeyword(customerKeyword.getOperateSelectKeyword());
                    customerKeywordForOptimization.setOperateRelatedKeyword(customerKeyword.getOperateRelatedKeyword());
                    customerKeywordForOptimization.setOperateRecommendKeyword(customerKeyword.getOperateRecommendKeyword());
                    customerKeywordForOptimization.setOperateSearchAfterSelectKeyword(customerKeyword.getOperateSearchAfterSelectKeyword());
                    customerKeywordForOptimization.setClickUrl(customerKeyword.getClickUrl());
                    customerKeywordForOptimization.setShowPage(customerKeyword.getShowPage());
                    customerKeywordForOptimization.setRelatedKeywordPercentage(customerKeyword.getRelatedKeywordPercentage());
                }

                if (customerKeywordForOptimization.getOperationType().indexOf("verify_ct") == 0) {
                    if (customerKeyword.getOptimizeGroupName().indexOf("verify_ct") == 0) {
                        if (StringUtils.isNotBlank(customerKeyword.getCt())) {
                            customerKeywordForOptimization.setCt(customerKeyword.getCt());
                        } else {
                            settingCustomerKeywordCt(customerKeywordUuid, customerKeywordForOptimization);
                        }
                    } else {
                        Config configCt = configService.getConfig(Constants.CONFIG_TYPE_CT, customerKeywordForOptimization.getGroup());
                        if (configCt != null) {
                            customerKeywordForOptimization.setCt(configCt.getValue());
                        }
                    }
                }
                if ("verify_src".equals(customerKeywordForOptimization.getOperationType())) {
                    if (customerKeyword.getOptimizeGroupName().indexOf("verify_src") == 0) {
                        if (StringUtils.isNotBlank(customerKeyword.getFromSource())) {
                            customerKeywordForOptimization.setFromSource(customerKeyword.getFromSource());
                        } else {
                            settingCustomerKeywordFromSource(customerKeywordUuid, customerKeywordForOptimization);
                        }
                    } else {
                        Config configFromSource = configService.getConfig(Constants.CONFIG_TYPE_FROM_SOURCE, customerKeywordForOptimization.getGroup());
                        if (configFromSource != null) {
                            customerKeywordForOptimization.setFromSource(configFromSource.getValue());
                        }
                    }
                }
                if (customerKeywordForOptimization.getEntryType().equals(EntryTypeEnum.qz.name()) &&
                        (customerKeywordForOptimization.getOperationType().contains("qz_zhannei") || customerKeywordForOptimization.getOperationType().contains("pc_zhannei_360"))) {
                    List<KeywordSimpleVO> qzKeywords = customerKeywordDao.getQZCustomerKeywordSummaryInfos(terminalType, customerKeyword.getOptimizeGroupName());
                    customerKeywordForOptimization.setRelatedQZKeywords(qzKeywords);
                }
                String screenedWebsite = screenedWebsiteService.getScreenedWebsiteByOptimizeGroupName(customerKeywordForOptimization.getGroup());
                if (screenedWebsite != null) {
                    customerKeywordForOptimization.setDisableVisitUrl(screenedWebsite);
                }
            }
            return customerKeywordForOptimization;
        }
        return null;
    }

    public CustomerKeywordForOptimizationSimple searchCustomerKeywordsForOptimizationZip(String terminalType, String clientID, String version, boolean updateQueryInfo) {
        MachineInfo machineInfo = machineInfoService.selectById(clientID);
        if (machineInfo == null) {
            return null;
        }

        if (!machineInfo.getValid() || StringUtils.isEmpty(machineInfo.getGroup())) {
            return null;
        }

        String usingOperationType = machineInfo.getUsingOperationType();
        if (usingOperationType == null || machineInfo.getPageSize() == null) {
            GroupSetting groupSetting = groupSettingService.getGroupSettingViaPercentage(machineInfo.getGroup(), machineInfo.getTerminalType());
            usingOperationType = groupSetting.getOperationType();
            machineInfo.setUsingOperationType(usingOperationType);
            machineInfo.setPage(groupSetting.getPage());
            machineInfo.setPageSize(groupSetting.getPageSize());
            machineInfoService.updateById(machineInfo);
            machineInfo = machineInfoService.selectById(clientID);
        }

        if (keywordOptimizationCountService.resetBigKeywordIndicator(machineInfo.getGroup())) {
            keywordOptimizationCountService.init(machineInfo.getGroup());
        }

        Long customerKeywordUuid = null;
        CustomerKeyword customerKeyword = null;

        int retryCount = 0;
        int noPositionMaxInvalidCount = 2;

        OperationCombine operationCombine = operationCombineService.getOperationCombine(machineInfo.getGroup(), machineInfo.getTerminalType());
        Integer maxInvalidCount = operationCombine.getMaxInvalidCount();
        if (usingOperationType.contains(Constants.CONFIG_TYPE_ZHANNEI_SOGOU)) {
            Config configInvalidRefreshCount = configService.getConfig(Constants.CONFIG_TYPE_ZHANNEI_SOGOU, Constants.CONFIG_KEY_NOPOSITION_MAX_INVALID_COUNT);
            noPositionMaxInvalidCount = Integer.parseInt(configInvalidRefreshCount.getValue());
        }
        do {
            boolean isNormalKeyword = keywordOptimizationCountService.optimizeNormalKeyword(machineInfo.getGroup());
            customerKeywordUuid = customerKeywordDao.getCustomerKeywordUuidForOptimization(terminalType, machineInfo.getGroup(), !isNormalKeyword);

            if (customerKeywordUuid == null) {
                if (keywordOptimizationCountService.resetBigKeywordIndicator(machineInfo.getGroup())) {
                    keywordOptimizationCountService.init(machineInfo.getGroup());
                }
                customerKeywordUuid = customerKeywordDao.getCustomerKeywordUuidForOptimization(terminalType, machineInfo.getGroup(), false);
            }
            if (keywordOptimizationCountService.allowResetBigKeywordIndicator(machineInfo.getGroup())) {
                keywordOptimizationCountService.setLastVisitTime(machineInfo.getGroup());
                resetBigKeywordIndicator(machineInfo.getGroup(), maxInvalidCount, noPositionMaxInvalidCount);
            }
            retryCount++;
            if (customerKeywordUuid != null && updateQueryInfo) {
                customerKeywordDao.updateOptimizationQueryTimeSingle(customerKeywordUuid, maxInvalidCount);
            }
        } while (customerKeywordUuid == null && retryCount < 2);

        if (customerKeywordUuid != null) {
            customerKeyword = customerKeywordDao.getCustomerKeywordForOptimization(customerKeywordUuid);
            machineInfoService.updatePageNo(clientID, 0);
            CustomerKeywordForOptimizationSimple customerKeywordForOptimization = new CustomerKeywordForOptimizationSimple();
            customerKeywordForOptimization.setUuid(customerKeyword.getUuid());
            customerKeywordForOptimization.setKeyword(customerKeyword.getKeyword());
            customerKeywordForOptimization.setUrl(customerKeyword.getUrl());
            customerKeywordForOptimization.setEntryType(customerKeyword.getType());
            customerKeywordForOptimization.setBearPawNumber(customerKeyword.getBearPawNumber());
            customerKeywordForOptimization.setSearchEngine(customerKeyword.getSearchEngine());
            customerKeywordForOptimization.setTerminalType(customerKeyword.getTerminalType());

            customerKeywordForOptimization.setCurrentPosition(customerKeyword.getCurrentPosition());
            customerKeywordForOptimization.setOriginalUrl(customerKeyword.getOriginalUrl());
            customerKeywordForOptimization.setTitle(customerKeyword.getTitle());

            customerKeywordForOptimization.setGroup(machineInfo.getGroup());
            customerKeywordForOptimization.setOperationType(usingOperationType);
            customerKeywordForOptimization.setUpdateSettingTime((machineInfo.getUpdateSettingTime().getTime() > operationCombine.getUpdateTime().getTime()) ? machineInfo.getUpdateSettingTime() : new Timestamp(operationCombine.getUpdateTime().getTime()));

            NegativeListUpdateInfo negativeListUpdateInfo = negativeListUpdateInfoService.getNegativeListUpdateInfo(customerKeyword.getKeyword());
            if (negativeListUpdateInfo != null) {
                customerKeywordForOptimization.setNegativeListUpdateTime(negativeListUpdateInfo.getNegativeListUpdateTime());
            }

            customerKeywordForOptimization.setPage(machineInfo.getPage());
            customerKeywordForOptimization.setPageSize(machineInfo.getPageSize());


            customerKeywordForOptimization.setRemarks(customerKeyword.getRemarks());
            if (StringUtils.isNotBlank(customerKeywordForOptimization.getOperationType())) {
                if (customerKeywordForOptimization.getOperationType().contains(Constants.CONFIG_TYPE_TJ_XG)) {
                    customerKeywordForOptimization.setNegativeKeywords(new ArrayList<String>());
                    Config configNegativeKeywords = configService.getConfig(Constants.CONFIG_TYPE_TJ_XG, Constants.CONFIG_KEY_NEGATIVE_KEYWORDS);
                    Set<String> negativeKeywords = new HashSet<String>();
                    if (StringUtils.isNotBlank(configNegativeKeywords.getValue())) {
                        negativeKeywords.addAll(convertToSets(configNegativeKeywords.getValue()));
                    }
                    if (StringUtils.isNotBlank(customerKeyword.getNegativeKeywords())) {
                        negativeKeywords.addAll(convertToSets(customerKeyword.getNegativeKeywords()));
                    }
                    customerKeywordForOptimization.getNegativeKeywords().addAll(negativeKeywords);

                    if (StringUtils.isNotBlank(customerKeyword.getExcludeKeywords())) {
                        customerKeywordForOptimization.setExcludeKeywords(new ArrayList<String>(convertToSets(customerKeyword.getExcludeKeywords())));
                    }
                    if (StringUtils.isNotBlank(customerKeyword.getRecommendKeywords())) {
                        customerKeywordForOptimization.setRecommendedKeywords(new ArrayList<String>(convertToSets(customerKeyword.getRecommendKeywords())));
                    }

                    customerKeywordForOptimization.setOperateSelectKeyword(customerKeyword.getOperateSelectKeyword());
                    customerKeywordForOptimization.setOperateRelatedKeyword(customerKeyword.getOperateRelatedKeyword());
                    customerKeywordForOptimization.setOperateRecommendKeyword(customerKeyword.getOperateRecommendKeyword());
                    customerKeywordForOptimization.setOperateSearchAfterSelectKeyword(customerKeyword.getOperateSearchAfterSelectKeyword());
                    customerKeywordForOptimization.setClickUrl(customerKeyword.getClickUrl());
                    customerKeywordForOptimization.setShowPage(customerKeyword.getShowPage());
                    customerKeywordForOptimization.setRelatedKeywordPercentage(customerKeyword.getRelatedKeywordPercentage());
                }

                if (customerKeywordForOptimization.getOperationType().indexOf("verify_ct") == 0) {
                    if (customerKeyword.getOptimizeGroupName().indexOf("verify_ct") == 0) {
                        if (StringUtils.isNotBlank(customerKeyword.getCt())) {
                            customerKeywordForOptimization.setCt(customerKeyword.getCt());
                        } else {
                            settingCustomerKeywordCt(customerKeywordUuid, customerKeywordForOptimization);
                        }
                    } else {
                        Config configCt = configService.getConfig(Constants.CONFIG_TYPE_CT, customerKeywordForOptimization.getGroup());
                        if (configCt != null) {
                            customerKeywordForOptimization.setCt(configCt.getValue());
                        }
                    }
                }
                if ("verify_src".equals(customerKeywordForOptimization.getOperationType())) {
                    if (customerKeyword.getOptimizeGroupName().indexOf("verify_src") == 0) {
                        if (StringUtils.isNotBlank(customerKeyword.getFromSource())) {
                            customerKeywordForOptimization.setFromSource(customerKeyword.getFromSource());
                        } else {
                            settingCustomerKeywordFromSource(customerKeywordUuid, customerKeywordForOptimization);
                        }
                    } else {
                        Config configFromSource = configService.getConfig(Constants.CONFIG_TYPE_FROM_SOURCE, customerKeywordForOptimization.getGroup());
                        if (configFromSource != null) {
                            customerKeywordForOptimization.setFromSource(configFromSource.getValue());
                        }
                    }
                }
                if (customerKeywordForOptimization.getEntryType().equals(EntryTypeEnum.qz.name()) &&
                        (customerKeywordForOptimization.getOperationType().contains("qz_zhannei") || customerKeywordForOptimization.getOperationType().contains("pc_zhannei_360"))) {
                    List<KeywordSimpleVO> qzKeywords = customerKeywordDao.getQZCustomerKeywordSummaryInfos(terminalType, customerKeyword.getOptimizeGroupName());
                    customerKeywordForOptimization.setRelatedQZKeywords(qzKeywords);
                }
                String screenedWebsite = screenedWebsiteService.getScreenedWebsiteByOptimizeGroupName(customerKeywordForOptimization.getGroup());
                if (screenedWebsite != null) {
                    customerKeywordForOptimization.setDisableVisitUrl(screenedWebsite);
                }
            }
            return customerKeywordForOptimization;
        }
        return null;
    }

    private synchronized void settingCustomerKeywordCt(Long customerKeywordUuid, CustomerKeywordForOptimizationSimple customerKeywordForOptimization) {
        Config configCt = configService.getConfig(Constants.CONFIG_TYPE_CT, customerKeywordForOptimization.getGroup());
        Config configCountPerElement = configService.getConfig(Constants.CONFIG_TYPE_COUNT_PER_ELEMENT, customerKeywordForOptimization.getGroup());
        String configValue = assignConfigValue(configCt, "_", Integer.parseInt(configCountPerElement.getValue()));
        customerKeywordForOptimization.setCt(configValue);
        customerKeywordDao.updateCustomerKeywordCt(customerKeywordUuid, configValue);
    }

    private synchronized void settingCustomerKeywordFromSource(Long customerKeywordUuid, CustomerKeywordForOptimizationSimple customerKeywordForOptimization) {
        Config configFromSource = configService.getConfig(Constants.CONFIG_TYPE_FROM_SOURCE, customerKeywordForOptimization.getGroup());
        Config configCountPerElement = configService.getConfig(Constants.CONFIG_TYPE_COUNT_PER_ELEMENT, customerKeywordForOptimization.getGroup());
        String configValue = assignConfigValue(configFromSource, "_count_", Integer.parseInt(configCountPerElement.getValue()));
        customerKeywordForOptimization.setFromSource(configValue);
        customerKeywordDao.updateCustomerKeywordFromSource(customerKeywordUuid, configValue);
    }

    private String assignConfigValue(Config configCt, String splitStr, int countPerElement) {
        String configValue = null;
        if (configCt != null) {
            String ctValue = configCt.getValue();
            int splitIndex = ctValue.indexOf(splitStr);
            if (splitIndex == -1) { // 未开始分配，分配第一个ct
                int index = ctValue.indexOf(",");
                String ct = ctValue;
                if (index > 0) {
                    ct = ctValue.substring(0, index);
                    configCt.setValue(ct + splitStr + "1" + ctValue.substring(index));
                }
                configValue = ct;
            } else {
                String afterSplitStr = ctValue.substring(splitIndex + splitStr.length());
                if (afterSplitStr.indexOf(",") == -1) { // 分配到最后一个ct
                    int count = Integer.parseInt(ctValue.substring(splitIndex + splitStr.length()));
                    int countLength = (count + "").length();
                    if (count == countPerElement) {
                        configValue = ctValue.substring(0, ctValue.length() - splitStr.length() - countLength);
                        configCt.setValue(configValue);
                        configValue = configValue.substring(configValue.lastIndexOf(",") + countLength);
                    } else {
                        count = count + 1;
                        configCt.setValue(ctValue.substring(0, splitIndex) + splitStr + count);
                        configValue = ctValue.substring(ctValue.lastIndexOf(",") + countLength, splitIndex);
                    }
                } else { // 分配到中间的ct
                    int count = Integer.parseInt(ctValue.substring(splitIndex + splitStr.length(), splitIndex + splitStr.length() + afterSplitStr.indexOf(",")));
                    int countLength = (count + "").length();
                    if (count == countPerElement) {
                        String beginCt = ctValue.substring(0, splitIndex);
                        String endCt = ctValue.substring(splitIndex);
                        endCt = endCt.substring(endCt.indexOf(",") + countLength);
                        if (endCt.indexOf(",") > -1) {
                            String ct = endCt.substring(0, endCt.indexOf(","));
                            configValue = ct;
                            configCt.setValue(beginCt + "," + ct + splitStr + "1" + endCt.substring(endCt.indexOf(",")));
                        } else {
                            configValue = endCt;
                            configCt.setValue(beginCt + "," + endCt + splitStr + "1");
                        }
                    } else {
                        count = count + 1;
                        String beginCt = ctValue.substring(0, splitIndex);
                        String endCt = ctValue.substring(splitIndex + countLength + splitStr.length());
                        configValue = beginCt.substring(beginCt.lastIndexOf(",") + countLength);
                        configCt.setValue(beginCt + splitStr + count + endCt);
                    }
                }
            }
            configService.updateConfig(configCt);
        }
        return configValue;
    }

    private Set<String> convertToSets(String str) {
        String[] firstElements = str.split(",");
        Set<String> elements = new HashSet<String>();
        for (String firstElement : firstElements) {
            for (String subElement : firstElement.split("，")) {
                elements.add(subElement);
            }
        }
        return elements;
    }

    public void resetOptimizationInfo() {
        customerKeywordDao.resetOptimizationInfo();
    }

    public void resetOptimizationInfoForNoOptimizeDate() {
        customerKeywordDao.resetOptimizationInfoForNoOptimizeDate();
    }

    public void updateOptimizationQueryTime(List<Long> customerKeywordUuids) {
        customerKeywordDao.updateOptimizationQueryTime(customerKeywordUuids);
    }

    public boolean haveCustomerKeywordForOptimization(String terminalType, String clientID) {
        CustomerKeywordForOptimization customerKeywordForOptimization = searchCustomerKeywordsForOptimization(terminalType, clientID, null, false);
        return customerKeywordForOptimization != null;
    }

    public void resetBigKeywordIndicator(String groupName, int maxInvalidCount, int noPositionMaxInvalidCount) {
        List<Map> remainingOptimizationCountMap = customerKeywordDao.searchRemainingOptimizationCount(groupName, maxInvalidCount, noPositionMaxInvalidCount);
        if (CollectionUtils.isNotEmpty(remainingOptimizationCountMap)) {
            customerKeywordDao.cleanBigKeywordIndicator(groupName);
            List<Long> customerKeywordUuids = new ArrayList<Long>();
            for (Map map : remainingOptimizationCountMap) {
                customerKeywordUuids.add(Long.parseLong(map.get("uuid").toString()));
                if ((1.0 * customerKeywordUuids.size()) / remainingOptimizationCountMap.size() > 0.2) {
                    break;
                }
            }
            customerKeywordDao.setBigKeywordIndicator(customerKeywordUuids);
//            machineInfoService.updateRemainingKeywordIndicator(groupName, 1);
        } else {
//            machineInfoService.updateRemainingKeywordIndicator(groupName, 0);
        }
    }

    public void updateOptimizationResult(String terminalType, Long customerKeywordUuid, int count, String ip, String city, String clientID, String status, String freeSpace, String version, String runningProgramType) {
        customerKeywordDao.updateOptimizationResult(customerKeywordUuid, count);
        machineInfoService.logMachineInfoTime(terminalType, clientID, status, freeSpace, version, city, count, runningProgramType);
//        customerKeywordIPService.addCustomerKeywordIP(customerKeywordUuid, city, ip);
    }

    public void batchUpdateOptimizedCount(List<Long> customerKeywordUuids) {
        customerKeywordDao.batchUpdateOptimizedCount(customerKeywordUuids);
    }

    public void adjustOptimizationCount() {
        List<String> groupNames = new ArrayList<String>();
        List<Map> bcCustomerKeywordSummaries = customerKeywordDao.searchKeywordsForAdjustingOptimizationCount("bc");
//        List<Map> ptCustomerKeywordSummaries = customerKeywordDao.searchKeywordsForAdjustingOptimizationCount("pt");
//        bcCustomerKeywordSummaries.addAll(ptCustomerKeywordSummaries);
        if (CollectionUtils.isNotEmpty(bcCustomerKeywordSummaries)) {
            for (Map customerKeywordSummaryMap : bcCustomerKeywordSummaries) {
                Long uuid = Long.parseLong(customerKeywordSummaryMap.get("uuid").toString());
                int currentIndexCount = (Integer) customerKeywordSummaryMap.get("currentIndexCount");
                Integer positionFirstFee = (Integer) customerKeywordSummaryMap.get("positionFirstFee");
                String positionStr = (String) customerKeywordSummaryMap.get("positions");
                String[] positionArray = positionStr.split(",");
                int optimizationPlanCount = currentIndexCount < 100 ? 150 : currentIndexCount;
                if (positionFirstFee != null && positionFirstFee > 0) {
                    optimizationPlanCount = (optimizationPlanCount + positionFirstFee * 4) / 2;
                }
                if (positionArray.length == 3) {
                    boolean lessFifth = true;
                    for (String position : positionArray) {
                        int iPosition = Integer.parseInt(position);
                        if (iPosition == 0 || iPosition >= 3) {
                            lessFifth = false;
                            break;
                        }
                    }
                    if (lessFifth) {
                        optimizationPlanCount = 20 + currentIndexCount / 50;
                    }
                }
                int queryInterval = (23 * 60) / optimizationPlanCount;

                customerKeywordDao.adjustOptimizePlanCount(uuid, optimizationPlanCount, queryInterval);
            }
        }
        customerKeywordDao.updateOptimizePlanCountForBaiduMap();
    }

    public void updateCustomerKeywordPosition(Long customerKeywordUuid, int position, Date capturePositionQueryTime, String ip, String city) {
        Double todayFee = null;
        if (position > 0 && position <= 10) {
            CustomerKeyword customerKeyword = customerKeywordDao.getCustomerKeywordFee(customerKeywordUuid);
            if (customerKeyword.getPositionFirstFee() != null && customerKeyword.getPositionFirstFee() > 0 && position == 1) {
                todayFee = customerKeyword.getPositionFirstFee();
            } else if (customerKeyword.getPositionSecondFee() != null && customerKeyword.getPositionSecondFee() > 0 && position == 2) {
                todayFee = customerKeyword.getPositionSecondFee();
            } else if (customerKeyword.getPositionThirdFee() != null && customerKeyword.getPositionThirdFee() > 0 && position == 3) {
                todayFee = customerKeyword.getPositionThirdFee();
            } else if (customerKeyword.getPositionForthFee() != null && customerKeyword.getPositionForthFee() > 0 && position == 4) {
                todayFee = customerKeyword.getPositionForthFee();
            } else if (customerKeyword.getPositionFifthFee() != null && customerKeyword.getPositionFifthFee() > 0 && position == 5) {
                todayFee = customerKeyword.getPositionFifthFee();
            } else if (customerKeyword.getPositionFirstPageFee() != null && customerKeyword.getPositionFirstPageFee() > 0) {
                todayFee = customerKeyword.getPositionFirstPageFee();
            }
        }
        customerKeywordDao.updatePosition(customerKeywordUuid, position, capturePositionQueryTime, todayFee, ip, city);
        if (capturePositionQueryTime != null) {
            customerKeywordPositionSummaryService.savePositionSummary(customerKeywordUuid, position);
        }
    }

    public CustomerKeywordForCapturePosition getCustomerKeywordForCapturePosition(String terminalType, List<String> groupNames, Long customerUuid,
                                                                                  Date startTime, Long captureRankJobUuid) {
        CustomerKeywordForCapturePosition customerKeywordForCapturePosition = new CustomerKeywordForCapturePosition();
        Boolean captureRankJobStatus = captureRankJobService.getCaptureRankJobStatus(captureRankJobUuid);
        if (captureRankJobStatus) {
            customerKeywordForCapturePosition.setCaptureRankJobStatus(captureRankJobStatus);
            Long customerKeywordUuid = customerKeywordDao.getCustomerKeywordUuidForCapturePosition(terminalType, groupNames, customerUuid, startTime, 0);
            if (null == customerKeywordUuid) {
                customerKeywordUuid = customerKeywordDao.getCustomerKeywordUuidForCapturePosition(terminalType, groupNames, customerUuid, startTime, 1);
            }
            if (null != customerKeywordUuid) {
                CustomerKeyword customerKeyword = customerKeywordDao.getCustomerKeywordForCapturePosition(customerKeywordUuid);
                customerKeywordForCapturePosition.setUuid(customerKeyword.getUuid());
                customerKeywordForCapturePosition.setKeyword(customerKeyword.getKeyword());
                customerKeywordForCapturePosition.setUrl(customerKeyword.getUrl());
                customerKeywordForCapturePosition.setTitle(customerKeyword.getTitle());
                customerKeywordForCapturePosition.setSearchEngine(customerKeyword.getSearchEngine());
                customerKeywordForCapturePosition.setTerminalType(customerKeyword.getTerminalType());
                customerKeywordForCapturePosition.setBearPawNumber(customerKeyword.getBearPawNumber());
                customerKeywordDao.updateCapturePositionQueryTimeAndCaptureStatus(customerKeyword.getUuid());
                return customerKeywordForCapturePosition;
            }
            return null;
        }
        return customerKeywordForCapturePosition;
    }

    public List<CustomerKeywordForCapturePosition> getCustomerKeywordForCapturePositionTemp(Long qzSettingUuid, String terminalType, String groupName, Long customerUuid, Date startTime, Long captureRankJobUuid) {

        List<CustomerKeywordForCapturePosition> customerKeywordForCapturePositions = new ArrayList<>();

        synchronized (CustomerKeywordService.class) {
            Boolean captureRankJobStatus = captureRankJobService.getCaptureRankJobStatus(captureRankJobUuid);
            if (captureRankJobStatus) {
                List<Long> customerKeywordUuids = customerKeywordDao.getCustomerKeywordUuidForCapturePositionTemp(qzSettingUuid, terminalType, groupName, customerUuid, startTime, 0);
                if (null == customerKeywordUuids || customerKeywordUuids.size() == 0) {
                    customerKeywordUuids = customerKeywordDao.getCustomerKeywordUuidForCapturePositionTemp(qzSettingUuid, terminalType, groupName, customerUuid, startTime, 1);
                }
                if (null != customerKeywordUuids && customerKeywordUuids.size() != 0) {
                    customerKeywordForCapturePositions = customerKeywordDao.getCustomerKeywordForCapturePositionTemp(customerKeywordUuids);
                    customerKeywordDao.updateCapturePositionQueryTimeAndCaptureStatusTemp(customerKeywordUuids);
                }
            }
        }
        return customerKeywordForCapturePositions;
    }

    public void resetInvalidRefreshCount(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) throws Exception {
        customerKeywordDao.resetInvalidRefreshCount(customerKeywordRefreshStatInfoCriteria);
    }

    public Page<CustomerKeyword> searchCustomerKeywordLists(Page<CustomerKeyword> page, CustomerKeywordCriteria customerKeywordCriteria) {
        long startMilleSeconds = System.currentTimeMillis();
        List<CustomerKeyword> customerKeywords = customerKeywordDao.searchCustomerKeywordsPage(page, customerKeywordCriteria);
        performanceService.addPerformanceLog(this.getClass() + ":searchCustomerKeywordLists", System.currentTimeMillis() - startMilleSeconds, null);
        page.setRecords(customerKeywords);
        return page;
    }

    public void updateCustomerKeywordTitle(SearchEngineResultItemVO searchEngineResultItemVO) {
        CustomerKeyword customerKeyword = customerKeywordDao.selectById(searchEngineResultItemVO.getUuid());
        if (customerKeyword != null) {
            if (searchEngineResultItemVO.getUrl() != null) {
                customerKeyword.setTitle(searchEngineResultItemVO.getTitle());
                customerKeyword.setUrl(searchEngineResultItemVO.getUrl());
                customerKeyword.setInitialPosition(searchEngineResultItemVO.getOrder());
                customerKeyword.setCurrentPosition(searchEngineResultItemVO.getOrder());
            }
            customerKeyword.setCapturedTitle(1);
            customerKeywordDao.updateById(customerKeyword);
        }
    }

    // new
    public void updateCustomerKeywordsTitle(List<SearchEngineResultItemVO> searchEngineResultItemVOs) {
        List<SearchEngineResultItemVO> havingUrlSearchEngineResultItemVOs = new ArrayList<SearchEngineResultItemVO>();
        for (SearchEngineResultItemVO searchEngineResultItemVO : searchEngineResultItemVOs) {
            if (searchEngineResultItemVO.getUrl() != null) {
                havingUrlSearchEngineResultItemVOs.add(searchEngineResultItemVO);
            }
        }
        customerKeywordDao.updateCustomerKeywordsTitle(havingUrlSearchEngineResultItemVOs);
    }

    public void addCustomerKeywords(SearchEngineResultVO searchEngineResultVO, String terminalType, String userName) throws Exception {
        if (searchEngineResultVO != null) {
            String searchEngine = searchEngineResultVO.getSearchEngine();
            List<CustomerKeyword> customerKeywords = new ArrayList<CustomerKeyword>();
            for (SearchEngineResultItemVO searchEngineResultItemVO : searchEngineResultVO.getSearchEngineResultItemVOs()) {
                CustomerKeyword customerKeyword = convert(searchEngineResultItemVO, terminalType, searchEngineResultVO.getGroup(), searchEngineResultVO.getMachineGroup(), searchEngineResultVO.getCustomerUuid(), searchEngine);
                customerKeywords.add(customerKeyword);
            }
            this.addCustomerKeyword(customerKeywords, userName);
        }
    }

    private CustomerKeyword convert(SearchEngineResultItemVO searchEngineResultItemVO, String terminalType, String groupName, String machineGroupName, long customerUuid, String searchEngine) {
        CustomerKeyword customerKeyword = new CustomerKeyword();
        customerKeyword.setCurrentPosition(searchEngineResultItemVO.getOrder());
        customerKeyword.setInitialPosition(searchEngineResultItemVO.getOrder());
        customerKeyword.setOptimizeGroupName(groupName);
        customerKeyword.setMachineGroup(machineGroupName);
        customerKeyword.setOptimizePlanCount(searchEngineResultItemVO.getClickCount());
        customerKeyword.setOptimizeRemainingCount(searchEngineResultItemVO.getClickCount());
        customerKeyword.setKeyword(searchEngineResultItemVO.getKeyword());
        customerKeyword.setTitle(searchEngineResultItemVO.getTitle());
        customerKeyword.setBearPawNumber(searchEngineResultItemVO.getBearPawNumber());
        customerKeyword.setType(searchEngineResultItemVO.getType());
        customerKeyword.setTerminalType(terminalType);
        customerKeyword.setOriginalUrl(searchEngineResultItemVO.getHref());
        customerKeyword.setServiceProvider("baidutop123");
        if (searchEngine.equals(Constants.SEARCH_ENGINE_BAIDU)) {
            customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
        }
        if (searchEngine.equals(Constants.SEARCH_ENGINE_SOGOU)) {
            customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_SOGOU);
        }
        if (searchEngine.equals(Constants.SEARCH_ENGINE_360)) {
            customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_360);
        }
        if (searchEngine.equals(Constants.SEARCH_ENGINE_SM)) {
            customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_SM);
        }
        customerKeyword.setUrl(searchEngineResultItemVO.getUrl());
        customerKeyword.setStartOptimizedTime(Utils.getCurrentTimestamp());
        customerKeyword.setCollectMethod(CollectMethod.PerMonth.getCode());
        customerKeyword.setCurrentIndexCount(20);
        customerKeyword.setCustomerUuid(customerUuid);
        customerKeyword.setAutoUpdateNegativeTime(Utils.getCurrentTimestamp());
        customerKeyword.setCreateTime(Utils.getCurrentTimestamp());
        customerKeyword.setUpdateTime(Utils.getCurrentTimestamp());
        customerKeyword.setCustomerKeywordSource(CustomerKeywordSourceEnum.Plugin.name());
        return customerKeyword;
    }

    public void updateCustomerKeywordStatus(List<Long> customerKeywordUuids, Integer status) {
        customerKeywordDao.updateCustomerKeywordStatus(customerKeywordUuids, status);
    }

    public void autoUpdateNegativeCustomerKeywords(SearchEngineResultVO searchEngineResultVO, String terminalType, String loginName) throws Exception {
        if (searchEngineResultVO != null) {
            List<NegativeList> negativeLists = negativeListService.getSpecifiedKeywordNegativeLists(searchEngineResultVO.getKeyword());
            List<CustomerKeyword> customerKeywords = new ArrayList<CustomerKeyword>();
            String searchEngine = searchEngineResultVO.getSearchEngine();
            for (SearchEngineResultItemVO searchEngineResultItemVO : searchEngineResultVO.getSearchEngineResultItemVOs()) {
                SearchEngineResultItemVO tmpSearchEngineResultItemVO = searchEngineResultItemVO;
                if (CollectionUtils.isNotEmpty(negativeLists)) {
                    for (NegativeList negativeList : negativeLists) {
                        if (Utils.isSameStr(searchEngineResultItemVO.getTitle(), negativeList.getTitle()) && Utils.isSameStr(searchEngineResultItemVO.getUrl(), negativeList.getUrl())) {
                            tmpSearchEngineResultItemVO = null;
                            break;
                        }
                    }
                }
                if (tmpSearchEngineResultItemVO != null) {
                    CustomerKeyword customerKeyword = convert(searchEngineResultItemVO, terminalType, searchEngineResultVO.getGroup(), searchEngineResultVO.getMachineGroup(), searchEngineResultVO.getCustomerUuid(), searchEngine);
                    if (!Utils.isNullOrEmpty(customerKeyword.getOriginalUrl())) {
                        String targetUrl = captureRealUrlService.fetchSingleRealUrl(customerKeyword.getOriginalUrl());
                        if (!Utils.isNullOrEmpty(targetUrl)) {
                            customerKeyword.setOriginalUrl(targetUrl);
                        }
                    }
                    customerKeywords.add(customerKeyword);
                }
            }
            customerKeywordDao.deleteCustomerKeywords(terminalType, searchEngineResultVO.getGroup(), searchEngineResultVO.getKeyword());
            logger.info("autoUpdateNegativeCustomerKeywords:" + terminalType + "-" + searchEngineResultVO.getGroup() + "-" + searchEngineResultVO.getKeyword());
            this.addCustomerKeyword(customerKeywords, loginName);
        }
    }

    public SearchEngineResultVO getCustomerKeywordForAutoUpdateNegative(String terminalType, String group) throws Exception {
        SearchEngineResultVO searchEngineResultVO = customerKeywordDao.getCustomerKeywordForAutoUpdateNegative(terminalType, group);
        if (searchEngineResultVO != null) {
            customerKeywordDao.updateAutoUpdateNegativeTime(terminalType, group, searchEngineResultVO.getKeyword());
        }
        return searchEngineResultVO;
    }

    public void updateAutoUpdateNegativeTimeAs4MinutesAgo(String terminalType, String group) {
        customerKeywordDao.updateAutoUpdateNegativeTimeAs4MinutesAgo(terminalType, group);
    }

    public void controlCustomerKeywordStatus() {
        List<Long> invalidCustomerKeywords = customerKeywordInvalidCountLogService.findInvalidCustomerKeyword();
        do {
            List<Long> subCustomerKeywordUuids = invalidCustomerKeywords.subList(0, (invalidCustomerKeywords.size() > 500) ? 500 : invalidCustomerKeywords.size());
            if (CollectionUtils.isNotEmpty(subCustomerKeywordUuids)) {
                customerKeywordDao.deleteBatchIds(subCustomerKeywordUuids);
                logger.info("controlCustomerKeywordStatus:" + subCustomerKeywordUuids.toString());
                invalidCustomerKeywords.removeAll(subCustomerKeywordUuids);
            }
        } while (invalidCustomerKeywords.size() > 0);
    }

    public List<CodeNameVo> searchGroups() {
        return customerKeywordDao.searchGroups();
    }

    public List<CodeNameVo> searchGroupsByTerminalType(String terminalType) {
        return customerKeywordDao.searchGroupsByTerminalType(terminalType);
    }

    public List<CustomerKeywordSummaryInfoVO> searchCustomerKeywordSummaryInfo(String entryType, Long customerUuid) {
        return customerKeywordDao.searchCustomerKeywordSummaryInfo(entryType, customerUuid);
    }

    public String[] searchCustomerNegativeKeywords(long customerUuid) {
        String[] customerNegativeKeyword = customerKeywordDao.searchCustomerNegativeKeywords(customerUuid);
        if (customerNegativeKeyword != null) {
            return customerNegativeKeyword;
        } else {
            return null;
        }
    }

    public List<customerSourceVO> getCustomerSource() {
        return customerService.findCustomerKeywordSource();
    }

    public void observeOptimizationCount() throws Exception {
        List<OptimizationCountVO> optimizationCountVOs = customerKeywordDao.takeOptimizationCountExceptionUsers();
        for (OptimizationCountVO optimizationCountVO : optimizationCountVOs) {
            List<OptimizationCountVO> groupOptimizationCountInfo = customerKeywordDao.observeGroupOptimizationCount(optimizationCountVO.getLoginName());
            List<OptimizationCountVO> keywordOptimizationCountInfo = customerKeywordDao.observeKeywordOptimizationCount(optimizationCountVO.getLoginName());
            observeOptimizationCountMailService.sendObserveOptimizationCountMail(optimizationCountVO.getEmail(), groupOptimizationCountInfo, keywordOptimizationCountInfo);
        }
    }

    public void controlRemainingKeywordIndicator() {
        Config fmMaxInvalidCountConfig = configService.getConfig(Constants.CONFIG_KEY_MAX_INVALID_COUNT, "fm");
        machineInfoService.updateAllRemainingKeywordIndicator(1);
        List<String> groupNames = customerKeywordDao.fetchOptimizationCompletedGroupNames("'fm'", Integer.parseInt(fmMaxInvalidCountConfig.getValue()));
        if (CollectionUtils.isNotEmpty(groupNames)) {
            updateRemainingKeywordIndicator(groupNames);
        }

        Config otherMaxInvalidCountConfig = configService.getConfig(Constants.CONFIG_KEY_MAX_INVALID_COUNT, "all");
        groupNames = customerKeywordDao.fetchOptimizationCompletedGroupNames("'pt','qz', 'bc'", Integer.parseInt(otherMaxInvalidCountConfig.getValue()));
        if (CollectionUtils.isNotEmpty(groupNames)) {
            updateRemainingKeywordIndicator(groupNames);
        }
    }

    private void updateRemainingKeywordIndicator(List<String> groupNames) {
        do {
            List<String> subGroupNames = groupNames.subList(0, (groupNames.size() > 500) ? 500 : groupNames.size());
            machineInfoService.updateRemainingKeywordIndicator(subGroupNames, 0);
            groupNames.removeAll(subGroupNames);
        } while (groupNames.size() > 0);
    }

    public void editCustomerOptimizePlanCount(Integer optimizePlanCount, String settingType, List<String> uuids) {
        customerKeywordDao.editCustomerOptimizePlanCount(optimizePlanCount, settingType, uuids);
    }

    public void editOptimizePlanCountByCustomerUuid(String terminalType, String entryType, Long customerUuid, Integer optimizePlanCount, String settingType) {
        customerKeywordDao.editOptimizePlanCountByCustomerUuid(terminalType, entryType, customerUuid, optimizePlanCount, settingType);
    }

    public void changeCustomerKeywordStatus(String terminalType, String entryType, Long customerUuid, Integer status) {
        customerKeywordDao.changeCustomerKeywordStatus(terminalType, entryType, customerUuid, status);
    }

    public void batchChangeCustomerKeywordStatus(String entryType, List<Long> customerUuids, Integer status) {
        customerKeywordDao.batchChangeCustomerKeywordStatus(entryType, customerUuids, status);
    }

    public void cleanCKLogFromAWeekAgo() {
        customerKeywordInvalidCountLogService.deleteInvalidCountLogFromAWeekAgo();
        customerKeywordPositionSummaryService.deletePositionSummaryFromAWeekAgo();
        dailyReportService.deleteDailyReportFromAWeekAgo();
        dailyReportItemService.deleteDailyReportItemFromAWeekAgo();
    }

    public void cleanCKLogFromAMonthAgo() {
        customerKeywordIPService.deleteCustomerKeywordIPFromAMonthAgo();
    }

    public void updateOptimizeGroupName(CustomerKeywordCriteria customerKeywordCriteria) {
        customerKeywordDao.updateOptimizeGroupName(customerKeywordCriteria);
    }

    public void updateBearPawNumber(CustomerKeywordCriteria customerKeywordCriteria) {
        customerKeywordDao.updateBearPawNumber(customerKeywordCriteria);
    }

    public void searchCustomerKeywordForNoReachStandard(CustomerKeywordCriteria customerKeywordCriteria) {
        Integer noReachStandardDays = customerKeywordCriteria.getNoReachStandardDays();
        customerKeywordCriteria.setNoReachStandardDays(7);
        int sevenDaysNoReachStandard = customerKeywordDao.searchCustomerKeywordForNoReachStandard(customerKeywordCriteria);
        customerKeywordCriteria.setNoReachStandardDays(15);
        int fifteenDaysNoReachStandard = customerKeywordDao.searchCustomerKeywordForNoReachStandard(customerKeywordCriteria);
        customerKeywordCriteria.setNoReachStandardDays(30);
        int thirtyDaysNoReachStandard = customerKeywordDao.searchCustomerKeywordForNoReachStandard(customerKeywordCriteria);
        customerKeywordCriteria.setNoReachStandardDays(noReachStandardDays);
        customerKeywordCriteria.setSevenDaysNoReachStandard(sevenDaysNoReachStandard);
        customerKeywordCriteria.setFifteenDaysNoReachStandard(fifteenDaysNoReachStandard);
        customerKeywordCriteria.setThirtyDaysNoReachStandard(thirtyDaysNoReachStandard);
    }

    public String findAllNegativeCustomerKeyword(String searchEngine) {
        Config config = configService.getConfig(Constants.CONFIG_TYPE_NEGATIVE_KEYWORD, searchEngine);
        return config.getValue();
    }

    public List<NegativeList> getCustomerKeywordSummaryInfos(String terminalType, String keyword) {
        return customerKeywordDao.getCustomerKeywordSummaryInfos(terminalType, keyword);
    }

    public void batchUpdateRequireDalete(List<RequireDeleteKeywordVO> requireDeleteKeywordVOs) {
        customerKeywordDao.batchUpdateRequireDalete(requireDeleteKeywordVOs);
    }

    public void updateCustomerKeywordQueryTime(Long customerKeywordUuid, Date date) {
        customerKeywordDao.updateCustomerKeywordQueryTime(customerKeywordUuid, DateUtils.addMinutes(date, -3));
    }

    public void updateKeywordCustomerUuid(List<String> keywordUuids, String customerUuid, String terminalType) {
        customerKeywordDao.updateKeywordCustomerUuid(keywordUuids, customerUuid, terminalType);
    }

    public void changeOptimizeGroupName() {
        // 移出noRankingOptimizeGroupName有刷量有排名关键字
        List<Config> noRankConfigs = configService.findConfigs(Constants.CONFIG_TYPE_NORANK_OPTIMIZE_GROUPNAME);
        customerKeywordDao.moveOutDefaultCustomerKeyword(noRankConfigs, Constants.CONFIG_TYPE_DEFAULT_OPTIMIZE_GROUPNAME);
        // 移出monitoringOptimizeGroupName没刷量没排名关键字
        List<String> monitorConfigs = configService.getMonitorOptimizeGroupName(Constants.CONFIG_TYPE_MONITOR_OPTIMIZE_GROUPNAME);
        customerKeywordDao.moveOutNoRankingCustomerKeyword(monitorConfigs, Constants.CONFIG_TYPE_NORANK_OPTIMIZE_GROUPNAME);
        // 排序关键字（优先排名，其次第一报价）
        List<CustomerKeywordSortVO> customerKeywordSortVOList = customerKeywordDao.sortCustomerKeywordForOptimize(monitorConfigs);
        // 限制分组下相同关键字个数
        List<String> needMoveUuids = new ArrayList<String>();
        Map<String, Integer> sameCustomerKeywordCountMap = configService.getSameCustomerKeywordCount();
        for (CustomerKeywordSortVO customerKeywordSortVO : customerKeywordSortVOList) {
            List<String> uuids = Arrays.asList(customerKeywordSortVO.getUuids().split(","));
            String key = customerKeywordSortVO.getSearchEngine() + "_" + customerKeywordSortVO.getTerminalType();
            Integer maxCount = sameCustomerKeywordCountMap.get(key);
            if (maxCount != null && uuids.size() > maxCount) {
                int split = 0;
                for (String uuid : uuids) {
                    if (uuid.indexOf("0") == 0) {
                        split++;
                    } else {
                        int count = uuids.size();
                        int hasPositionKeywordCount = count - split;
                        if (hasPositionKeywordCount >= maxCount) {
                            needMoveUuids.addAll(uuids.subList(0, split));
                            if (hasPositionKeywordCount > maxCount) {
                                needMoveUuids.addAll(uuids.subList(split + maxCount, count));
                            }
                        } else {
                            needMoveUuids.addAll(uuids.subList(maxCount - hasPositionKeywordCount, split));
                        }
                        break;
                    }
                    if (split == uuids.size() && CollectionUtils.isEmpty(needMoveUuids)) {
                        needMoveUuids.addAll(uuids.subList(maxCount, uuids.size()));
                    }
                }
            }
        }
        // 移出超出个数的关键字到noRankingOptimizeGroupName
        if (CollectionUtils.isNotEmpty(needMoveUuids)) {
            List<String> uuidList = new ArrayList<String>();
            for (String needMoveUuid : needMoveUuids) {
                uuidList.add(needMoveUuid.substring(needMoveUuid.indexOf("_") + 1));
            }
            customerKeywordDao.setNoRankingCustomerKeyword(uuidList, Constants.CONFIG_TYPE_NORANK_OPTIMIZE_GROUPNAME);
        }
    }

    public List<String> getCustomerKeywordInfo(CustomerKeywordCriteria customerKeywordCriteria) {
        return customerKeywordDao.getCustomerKeywordInfo(customerKeywordCriteria);
    }

    public void deleteDuplicateKeywords(Long customerUuid, String terminalType, String entryType) {
        CustomerKeywordCriteria customerKeywordCriteria = new CustomerKeywordCriteria();
        customerKeywordCriteria.setCustomerUuid(customerUuid);
        customerKeywordCriteria.setEntryType(entryType);
        customerKeywordCriteria.setTerminalType(terminalType);
        List<String> uuidsList = customerKeywordDao.searchDuplicateKeywords(customerKeywordCriteria);
        List<Long> customerKeywordUuids = new ArrayList<Long>();
        for (String uuids : uuidsList) {
            ArrayList<Long> listIds = new ArrayList<Long>(Arrays.asList((Long[]) ConvertUtils.convert(uuids.split(","), Long.class)));
            listIds.remove(0);
            customerKeywordUuids.addAll(listIds);
        }
        while (customerKeywordUuids.size() > 0) {
            List<Long> subCustomerKeywordUuids = customerKeywordUuids.subList(0, (customerKeywordUuids.size() > 500) ? 500 : customerKeywordUuids.size());
            customerKeywordDao.deleteBatchIds(subCustomerKeywordUuids);
            logger.info("controlCustomerKeywordStatus:" + subCustomerKeywordUuids.toString());
            customerKeywordUuids.removeAll(subCustomerKeywordUuids);
        }
    }

    //客户关键字批量设置
    public void batchUpdateKeywordStatus(KeywordStatusBatchUpdateVO keywordStatusBatchUpdateVO) {
        String[] keywordIDs = keywordStatusBatchUpdateVO.getCustomerUuids().split(",");
        customerKeywordDao.batchUpdateKeywordStatus(keywordIDs, keywordStatusBatchUpdateVO.getKeywordChecks(), keywordStatusBatchUpdateVO.getKeywordStatus());
    }

    public List<Long> getCustomerUuids(String entryType, String terminalType) {
        return customerKeywordDao.getCustomerUuids(entryType, terminalType);
    }

    public void excludeCustomerKeyword(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        customerKeywordDao.excludeCustomerKeyword(qzSettingExcludeCustomerKeywordsCriteria);
    }

    public void updateNoEnteredKeywordGroupName() {
        do {
            customerKeywordDao.updateNoEnteredKeywordGroupName();
        } while (customerKeywordDao.updateNoEnteredKeywordGroupName() == 10000);
    }

    public synchronized List<CustomerKeywordEnteredVO> getNoEnteredKeywords(String searchEngine) {
        List<CustomerKeywordEnteredVO> noEnteredKeywords = customerKeywordDao.getNoEnteredKeywords(searchEngine);
        if (CollectionUtils.isNotEmpty(noEnteredKeywords)) {
            List<Long> uuids = new ArrayList<>();
            for (CustomerKeywordEnteredVO customerKeywordEnteredVo : noEnteredKeywords) {
                uuids.add(customerKeywordEnteredVo.getUuid());
            }
            customerKeywordDao.updateVerifyEnteredKeywordTimeByUuids(uuids);
        }
        return noEnteredKeywords;
    }

    public void updateNoEnteredKeywords(List<CustomerKeywordEnteredVO> customerKeywordEnteredVos) {
        if (!customerKeywordEnteredVos.isEmpty()) {
            List<CustomerKeyword> customerKeywords = new ArrayList<CustomerKeyword>();
            Config configCustomerKeywordRemarks = configService.getConfig(Constants.CONFIG_TYPE_NO_ENTERED_KEYWORD, Constants.CONFIG_KEY_NO_ENTERED_KEYWORD_REMARKS);
            if (null != configCustomerKeywordRemarks) {
                for (CustomerKeywordEnteredVO customerKeywordEnteredVo : customerKeywordEnteredVos) {
                    CustomerKeyword customerKeyword = new CustomerKeyword();
                    if (configCustomerKeywordRemarks.getValue().equals(customerKeywordEnteredVo.getEnteredKeywordRemarks())) {
                        customerKeyword.setOptimizeGroupName(customerKeywordEnteredVo.getOptimizeGroupName().substring(3));
                        customerKeyword.setCapturedTitle(1);
                    }
                    customerKeyword.setUuid(customerKeywordEnteredVo.getUuid());
                    customerKeyword.setEnteredKeywordRemarks(customerKeywordEnteredVo.getEnteredKeywordRemarks());
                    customerKeywords.add(customerKeyword);
                }
            }
            if (!customerKeywords.isEmpty()) {
                customerKeywordDao.updateNoEnteredKeywords(customerKeywords);
            }
        }
    }

    public List<String> getAvailableOptimizationGroups(GroupSettingCriteria groupSettingCriteria) {
        return customerKeywordDao.getAvailableOptimizationGroups(groupSettingCriteria);
    }

    public void checkOptimizeGroupName(CustomerKeyword customerKeyword) {
        QZSetting qzSetting = qzSettingService.searchGroupMaxCustomerKeywordCount(customerKeyword.getCustomerUuid(), customerKeyword.getTerminalType(), customerKeyword.getUrl());
        String optimizeGroupName = TerminalTypeEnum.PC.name().equals(customerKeyword.getTerminalType()) ? qzSetting.getPcGroup() : qzSetting.getPhoneGroup();
        List<CustomerKeywordOptimizeGroupCriteria> customerKeywordOptimizeGroupCriteriaList = searchOptimizeGroupNameAndCount(optimizeGroupName);
        int maxGroupNameSuffix = 1;
        String tmpOptimizeGroupName = optimizeGroupName;
        if (customerKeywordOptimizeGroupCriteriaList.size() > 0) {
            for (CustomerKeywordOptimizeGroupCriteria customerKeywordOptimizeGroupCriteria : customerKeywordOptimizeGroupCriteriaList) {
                if (customerKeywordOptimizeGroupCriteria.getSameGroupCustomerKeywordCount() < qzSetting.getGroupMaxCustomerKeywordCount() && (customerKeywordOptimizeGroupCriteria.getOptimizeGroupName().equalsIgnoreCase(optimizeGroupName))) {
                    customerKeyword.setOptimizeGroupName(customerKeywordOptimizeGroupCriteria.getOptimizeGroupName());
                    return;
                }
                String optimizeGroupNameSuffix = customerKeywordOptimizeGroupCriteria.getOptimizeGroupName().replaceAll(optimizeGroupName + "_", "");
                if (StringUtils.isNumeric(optimizeGroupNameSuffix)) {
                    maxGroupNameSuffix = maxGroupNameSuffix < Integer.parseInt(optimizeGroupNameSuffix) ? Integer.parseInt(optimizeGroupNameSuffix) : maxGroupNameSuffix;
                }
            }
            tmpOptimizeGroupName = optimizeGroupName + "_" + (maxGroupNameSuffix + 1);
            groupService.determineAddingGroupInfo(tmpOptimizeGroupName);
        }
        customerKeyword.setOptimizeGroupName(tmpOptimizeGroupName);
    }

    public List<CustomerKeywordOptimizeGroupCriteria> searchOptimizeGroupNameAndCount(String optimizeGroupName) {
        List<CustomerKeywordOptimizeGroupCriteria> customerKeywordOptimizeGroupCriterias = customerKeywordDao.searchOptimizeGroupNameAndCount(optimizeGroupName);
        List<CustomerKeywordOptimizeGroupCriteria> tmpGroupCriterias = new ArrayList<CustomerKeywordOptimizeGroupCriteria>();
        for (CustomerKeywordOptimizeGroupCriteria customerKeywordOptimizeGroupCriteria : customerKeywordOptimizeGroupCriterias) {
            if (customerKeywordOptimizeGroupCriteria.getOptimizeGroupName().equalsIgnoreCase(optimizeGroupName) || customerKeywordOptimizeGroupCriteria.getOptimizeGroupName().matches(optimizeGroupName + "_\\d{1,}")) {
                tmpGroupCriterias.add(customerKeywordOptimizeGroupCriteria);
            }
        }
        return tmpGroupCriterias;
    }

    public CustomerKeywordOptimizeGroupCriteria matchOptimizeGroupName(List<CustomerKeywordOptimizeGroupCriteria> customerKeywordOptimizeGroupCriteriaList, String optimizeGroupName, int groupMaxCustomerKeywordCount) {
        int maxGroupNameSuffix = 1;
        String tmpOptimizeGroupName = optimizeGroupName;
        if (customerKeywordOptimizeGroupCriteriaList.size() > 0) {
            for (CustomerKeywordOptimizeGroupCriteria tempCustomerKeywordOptimizeGroupCriteria : customerKeywordOptimizeGroupCriteriaList) {
                if (tempCustomerKeywordOptimizeGroupCriteria.getSameGroupCustomerKeywordCount() < groupMaxCustomerKeywordCount) {
                    tempCustomerKeywordOptimizeGroupCriteria.setSameGroupCustomerKeywordCount(tempCustomerKeywordOptimizeGroupCriteria.getSameGroupCustomerKeywordCount() + 1);
                    return tempCustomerKeywordOptimizeGroupCriteria;
                }
                String optimizeGroupNameSuffix = tempCustomerKeywordOptimizeGroupCriteria.getOptimizeGroupName().replaceAll(optimizeGroupName + "_", "");
                if (StringUtils.isNumeric(optimizeGroupNameSuffix)) {
                    maxGroupNameSuffix = maxGroupNameSuffix < Integer.parseInt(optimizeGroupNameSuffix) ? Integer.parseInt(optimizeGroupNameSuffix) : maxGroupNameSuffix;
                }
                tmpOptimizeGroupName = optimizeGroupName + "_" + (maxGroupNameSuffix + 1);
                groupService.determineAddingGroupInfo(tmpOptimizeGroupName);
            }
        }
        CustomerKeywordOptimizeGroupCriteria customerKeywordOptimizeGroupCriteria = new CustomerKeywordOptimizeGroupCriteria();
        customerKeywordOptimizeGroupCriteria.setOptimizeGroupName(tmpOptimizeGroupName);
        customerKeywordOptimizeGroupCriteria.setSameGroupCustomerKeywordCount(1);
        customerKeywordOptimizeGroupCriteriaList.add(customerKeywordOptimizeGroupCriteria);
        return customerKeywordOptimizeGroupCriteria;
    }

    public void updateCustomerKeywordEffect(long customerUuid, String terminalType, String optimizeGroupName) {
        customerKeywordDao.updateCustomerKeywordEffect(customerUuid, terminalType, optimizeGroupName);
    }

    public void updateMachineGroup(CustomerKeywordCriteria customerKeywordCriteria) {
        customerKeywordDao.updateMachineGroup(customerKeywordCriteria);
    }

    public List<machineGroupQueueVO> getMachineGroupAndSize(){
        List<machineGroupQueueVO> machineGroupQueueVOS = new ArrayList<>();
        for (Map.Entry<String, ArrayBlockingQueue> entry : machineGroupQueueMap.entrySet()) {
            machineGroupQueueVOS.add(new machineGroupQueueVO(entry.getKey(),entry.getValue().size()));
        }
        return machineGroupQueueVOS;
    }
}