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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

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
    private ClientStatusService clientStatusService;

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
    private ObserveOptimizationCountMailService observeOptimizationCountMailService;

    @Autowired
    private DailyReportService dailyReportService;

    @Autowired
    private DailyReportItemService dailyReportItemService;

    public Page<CustomerKeyword> searchCustomerKeywords(Page<CustomerKeyword> page, CustomerKeywordCriteria customerKeywordCriteria){
        page.setRecords(customerKeywordDao.searchCustomerKeywordsPageForCustomer(page, customerKeywordCriteria));
        return page;
    }

    public List<CustomerKeyword> searchCustomerKeywordInfo(CustomerKeywordCriteria customerKeywordCriteria) {
        return customerKeywordDao.searchCustomerKeywordInfo(customerKeywordCriteria);
    }

    public List<CustomerKeyword> searchCustomerKeywordsForDailyReport(CustomerKeywordCriteria customerKeywordCriteria){
        return customerKeywordDao.searchCustomerKeywordsForDailyReport(customerKeywordCriteria);
    }

    public CustomerKeywordForCaptureTitle searchCustomerKeywordForCaptureTitle(String terminalType,String searchEngine) throws Exception {
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
        CustomerKeywordForCaptureTitle captureTitle = customerKeywordDao.searchCustomerKeywordForCaptureTitle(qzCaptureTitleLog,searchEngine);
        if (captureTitle == null) {
            qzCaptureTitleLogService.completeQZCaptureTitleLog(qzCaptureTitleLog.getUuid());
            customerKeywordDao.deleteEmptyTitleCustomerKeyword(qzCaptureTitleLog,searchEngine);
            logger.info("deleteEmptyTitleCustomerKeyword:" + qzCaptureTitleLog.getCustomerUuid() + "-" + qzCaptureTitleLog.getGroup());
            return null;
        } else {
            QZOperationType qzOperationType = qzOperationTypeService.selectById(qzCaptureTitleLog.getQzOperationTypeUuid());
            QZSetting qzSetting = qzSettingService.selectById(qzOperationType.getQzSettingUuid());
            String subDomainName = qzOperationType.getSubDomainName();
            if(StringUtils.isNotEmpty(subDomainName)){
                captureTitle.setWholeUrl(subDomainName);
            }else {
                captureTitle.setWholeUrl(qzSetting.getDomain());
            }
            updateCaptureTitleQueryTime((long)captureTitle.getUuid());
            return captureTitle;
        }
    }

    public CustomerKeywordForCaptureTitle searchCustomerKeywordForCaptureTitle(String groupName, String terminalType,String searchEngine) throws Exception {
        QZCaptureTitleLog qzCaptureTitleLog = new QZCaptureTitleLog();
        qzCaptureTitleLog.setGroup(groupName);
        qzCaptureTitleLog.setTerminalType(terminalType);
        CustomerKeywordForCaptureTitle captureTitle = customerKeywordDao.searchCustomerKeywordForCaptureTitle(qzCaptureTitleLog,searchEngine);
        if(null != captureTitle) {
            updateCaptureTitleQueryTime((long)captureTitle.getUuid());
        }
        return captureTitle;
    }

    private void updateCaptureTitleQueryTime(Long uuid) {
        CustomerKeyword customerKeyword = customerKeywordDao.selectById(uuid);
        customerKeyword.setCaptureTitleQueryTime(new Date());
        customerKeywordDao.updateById(customerKeyword);
    }

    public void cleanTitle(CustomerKeywordCleanCriteria customerKeywordCleanCriteria) {
        if (CustomerKeywordCleanTypeEnum.CustomerTitle.name().equals(customerKeywordCleanCriteria.getCleanType())) {
            customerKeywordDao.cleanCustomerTitle(customerKeywordCleanCriteria.getTerminalType(), customerKeywordCleanCriteria.getEntryType(), customerKeywordCleanCriteria.getCustomerUuid());
        } else if(CustomerKeywordCleanTypeEnum.SelectedCustomerKeywordTitle.name().equals(customerKeywordCleanCriteria.getCleanType())) {
            customerKeywordDao.cleanSelectedCustomerKeywordTitle(customerKeywordCleanCriteria.getCustomerKeywordUuids());
        } else if(CustomerKeywordCleanTypeEnum.CaptureTitleBySelected.name().equals(customerKeywordCleanCriteria.getCleanType())){
            customerKeywordDao.cleanCaptureTitleBySelected(customerKeywordCleanCriteria.getCustomerKeywordUuids());
        } else{
            customerKeywordDao.cleanCaptureTitleFlag(customerKeywordCleanCriteria.getTerminalType(), customerKeywordCleanCriteria.getEntryType(), customerKeywordCleanCriteria.getCustomerUuid());
        }
    }

    public void addCustomerKeywordsFromSimpleUI(List<CustomerKeyword> customerKeywords, String terminalType, String entryType, String userName) {
        if (CollectionUtils.isNotEmpty(customerKeywords)) {
            long customerUuid = customerKeywords.get(0).getCustomerUuid();
            int maxSequence = 0;
            try {
                maxSequence = customerKeywordDao.getMaxSequence(terminalType, entryType, customerUuid);
            } catch (Exception ex) {
//                ex.printStackTrace();
            }
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
        if (!EntryTypeEnum.fm.name().equals(customerKeyword.getType()) && haveDuplicatedCustomerKeyword(customerKeyword.getTerminalType(),
                customerKeyword.getCustomerUuid(), customerKeyword.getKeyword(), originalUrl)) {
            return;
        }
        customerKeyword.setKeyword(customerKeyword.getKeyword().trim());
        customerKeyword.setUrl(customerKeyword.getUrl() != null ? customerKeyword.getUrl().trim() : null);
        customerKeyword.setTitle(customerKeyword.getTitle() != null ? customerKeyword.getTitle().trim() : null);
        customerKeyword.setOriginalUrl(customerKeyword.getOriginalUrl() != null ? customerKeyword.getOriginalUrl().trim() : null);
        customerKeyword.setOrderNumber(customerKeyword.getOrderNumber() != null ? customerKeyword.getOrderNumber().trim() : null);

        boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
        if(isDepartmentManager) {
            customerKeyword.setStatus(1);
        } else {
            customerKeyword.setStatus(2);
        }

        if(customerKeyword.getCurrentPosition() == null){
            customerKeyword.setCurrentPosition(10);
        }
        customerKeyword.setAutoUpdateNegativeDateTime(Utils.getCurrentTimestamp());
        customerKeyword.setCapturePositionQueryTime(Utils.addDay(Utils.getCurrentTimestamp(), -2));
        customerKeyword.setStartOptimizedTime(new Date());
        customerKeyword.setLastReachStandardDate(Utils.yesterday());
        customerKeyword.setQueryTime(new Date());
        customerKeyword.setQueryDate(new Date());
        customerKeyword.setUpdateTime(new Date());
        customerKeyword.setCreateTime(new Date());
        customerKeywordDao.insert(customerKeyword);
    }

    public void updateCustomerKeywordFromUI(CustomerKeyword customerKeyword, String userName){
        boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
        if(isDepartmentManager) {
            customerKeyword.setStatus(1);
        } else {
            customerKeyword.setStatus(2);
        }
        customerKeyword.setUpdateTime(new Date());
        customerKeywordDao.updateById(customerKeyword);
    }

    public boolean haveDuplicatedCustomerKeyword(String terminalType, long customerUuid, String keyword, String originalUrl) {
        int customerKeywordCount = 0;
        try {
            customerKeywordCount = customerKeywordDao.getSimilarCustomerKeywordCount(terminalType, customerUuid, keyword, originalUrl);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
        return customerKeywordCount > 0;
    }

    public int getCustomerKeywordCount(String terminalType, String entryType, long customerUuid) {
        return customerKeywordDao.getCustomerKeywordCount(terminalType, entryType, customerUuid);
    }

    public void deleteCustomerKeywordsByUuid(List<String> customerKeywordUuids){
        logger.info("deleteCustomerKeywordsByUuid:" + customerKeywordUuids.toString());
        customerKeywordDao.deleteCustomerKeywordsByUuid(customerKeywordUuids);
    }

    public void deleteCustomerKeywordsWhenEmptyTitleAndUrl(String terminalType, String entryType,String customerUuid){
        logger.info("deleteCustomerKeywordsWhenEmptyTitleAndUrl:" + customerUuid);
        customerKeywordDao.deleteCustomerKeywordsWhenEmptyTitleAndUrl(terminalType, entryType,customerUuid);
    }

    public void deleteCustomerKeywordsWhenEmptyTitle(String terminalType, String entryType,String customerUuid){
        logger.info("deleteCustomerKeywordsWhenEmptyTitle:" + customerUuid);
        customerKeywordDao.deleteCustomerKeywordsWhenEmptyTitle(terminalType, entryType,customerUuid);
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
    }

    private void supplementIndexAndPriceFromExisting(CustomerKeyword customerKeyword) {
        List<CustomerKeyword> existingCustomerKeywords = customerKeywordDao.searchSameCustomerKeywords(customerKeyword.getTerminalType(),
                customerKeyword.getCustomerUuid(), customerKeyword.getKeyword());
        if (CollectionUtils.isNotEmpty(existingCustomerKeywords)) {
            CustomerKeyword existingCustomerKeyword = existingCustomerKeywords.get(0);
            customerKeyword.setInitialIndexCount(existingCustomerKeyword.getInitialIndexCount());
            customerKeyword.setCurrentIndexCount(existingCustomerKeyword.getCurrentIndexCount());
            customerKeyword.setOptimizePlanCount(existingCustomerKeyword.getOptimizePlanCount());

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
                    customerKeyword.setOptimizePlanCount(baiduIndexCriteria.getPcIndex() < 100 ? 100 : baiduIndexCriteria.getPcIndex());
                } else {
                    customerKeyword.setInitialIndexCount(baiduIndexCriteria.getPhoneIndex());
                    customerKeyword.setCurrentIndexCount(baiduIndexCriteria.getPhoneIndex());
                    customerKeyword.setOptimizePlanCount(baiduIndexCriteria.getPhoneIndex() < 100 ? 100 : baiduIndexCriteria.getPhoneIndex());
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
                        customerKeyword.setPositionFirstFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeTypeCalculation != null ? fixedPriceCustomerChargeTypeCalculation.getChargesOfFirst() :
                                        null), percentageCustomerChargeTypeCalculation.getChargesOfFirst()));
                    }
                    if (percentageCustomerChargeTypeCalculation.getChargesOfSecond() != null) {
                        customerKeyword.setPositionSecondFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeTypeCalculation != null ? fixedPriceCustomerChargeTypeCalculation.getChargesOfSecond() :
                                        null), percentageCustomerChargeTypeCalculation.getChargesOfSecond()));
                    }
                    if (percentageCustomerChargeTypeCalculation.getChargesOfThird() != null) {
                        customerKeyword.setPositionThirdFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeTypeCalculation != null ? fixedPriceCustomerChargeTypeCalculation.getChargesOfThird() :
                                        null), percentageCustomerChargeTypeCalculation.getChargesOfThird()));
                    }
                    if (percentageCustomerChargeTypeCalculation.getChargesOfFourth() != null) {
                        customerKeyword.setPositionForthFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeTypeCalculation != null ? fixedPriceCustomerChargeTypeCalculation.getChargesOfFourth() :
                                        null), percentageCustomerChargeTypeCalculation.getChargesOfFourth()));
                    }
                    if (percentageCustomerChargeTypeCalculation.getChargesOfFifth() != null) {
                        customerKeyword.setPositionFifthFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeTypeCalculation != null ? fixedPriceCustomerChargeTypeCalculation.getChargesOfFifth() :
                                        null), percentageCustomerChargeTypeCalculation.getChargesOfFifth()));
                    }
                    if (percentageCustomerChargeTypeCalculation.getChargesOfFirstPage() != null) {
                        customerKeyword.setPositionFirstPageFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                fixedPriceCustomerChargeTypeCalculation.getChargesOfFirstPage(), percentageCustomerChargeTypeCalculation
                                        .getChargesOfFirstPage()));
                    }
                }
            } else {
                for (CustomerChargeTypeInterval tmpCustomerChargeTypeInterval : customerChargeType.getCustomerChargeTypeIntervals()) {
                    if (tmpCustomerChargeTypeInterval.getOperationType().equals(customerKeyword.getTerminalType())) {
                        if (tmpCustomerChargeTypeInterval.getStartIndex() <= customerKeyword
                                .getCurrentIndexCount() && (tmpCustomerChargeTypeInterval.getEndIndex() == null || customerKeyword
                                .getCurrentIndexCount() <= tmpCustomerChargeTypeInterval.getEndIndex())) {
                            customerKeyword.setPositionFirstFee(tmpCustomerChargeTypeInterval.getPrice().doubleValue());
                            customerKeyword.setPositionSecondFee(tmpCustomerChargeTypeInterval.getPrice().doubleValue());
                            customerKeyword.setPositionThirdFee(tmpCustomerChargeTypeInterval.getPrice().doubleValue());
                            if(TerminalTypeEnum.PC.name().equals(customerKeyword.getTerminalType())) {
                                customerKeyword.setPositionForthFee(tmpCustomerChargeTypeInterval.getPrice().doubleValue() / 2);
                                customerKeyword.setPositionFifthFee(tmpCustomerChargeTypeInterval.getPrice().doubleValue() / 2);
                            }else{
                                customerKeyword.setPositionForthFee(null);
                                customerKeyword.setPositionFifthFee(null);
                            }
                            customerKeyword.setPositionFirstPageFee(null);
                            break;
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
        return Math.round((currentIndexCount * pricePercentage.doubleValue()) / 1000 - 0.5) * 10;
    }

    public List<Map> getCustomerKeywordsCount(List<Long> customerUuids, String terminalType, String entryType){
        return customerKeywordDao.getCustomerKeywordsCount(customerUuids, terminalType, entryType);
    }

    public void updateCustomerKeywordGroupName(CustomerKeywordUpdateCriteria customerKeywordUpdateCriteria) {
        customerKeywordDao.updateCustomerKeywordGroupName(customerKeywordUpdateCriteria);
    }

    public void updateCustomerKeywordSearchEngine(CustomerKeywordUpdateCriteria customerKeywordUpdateCriteria) {
        customerKeywordDao.updateCustomerKeywordSearchEngine(customerKeywordUpdateCriteria);
    }

    public void updateCustomerKeywordGroupNameByRank(Map<String,Object> resultMap) {
        List<Long>  customerKeywordUuids = customerKeywordDao.searchCustomerKeywordUuidByRank(resultMap);
        List<Long>  customerKeyowrdUuidsTmp = new ArrayList<Long>();
        if (customerKeywordUuids != null) {
            for(Long customerKeywordUuid : customerKeywordUuids){
                customerKeyowrdUuidsTmp.add(customerKeywordUuid);
                if(customerKeyowrdUuidsTmp.size()==200){
                    customerKeywordDao.updateCustomerKeywordGroupNameByRank(customerKeyowrdUuidsTmp,resultMap.get("targetGroupName").toString());
                    customerKeyowrdUuidsTmp.clear();
                }
            }
            if(customerKeyowrdUuidsTmp.size()>0){
                customerKeywordDao.updateCustomerKeywordGroupNameByRank(customerKeyowrdUuidsTmp,resultMap.get("targetGroupName").toString());
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
        String bearPawNumber = customerKeywordDao.getBearPawNumberByCustomerUuid(customerUuid, type, terminalType);
        for (CustomerKeyword customerKeyword : customerKeywords) {
            customerKeyword.setCustomerUuid(customerUuid);
            customerKeyword.setType(type);
            customerKeyword.setBearPawNumber(bearPawNumber);
            customerKeyword.setCreateTime(Utils.getCurrentTimestamp());
            customerKeyword.setUpdateTime(Utils.getCurrentTimestamp());
            customerKeyword.setTerminalType(terminalType);
        }
    }
    public void addCustomerKeywords(List<CustomerKeyword> customerKeywords, String loginName) throws Exception {
        for (CustomerKeyword customerKeyword : customerKeywords) {
            addCustomerKeyword(customerKeyword, loginName);
        }
    }

    public void deleteCustomerKeywords(String deleteType, String terminalType, String entryType, String customerUuid, List<String> customerKeywordUuids) {
        if(CustomerKeywordDeletionTypeEnum.ByUuid.name().equals(deleteType)){
            deleteCustomerKeywordsByUuid(customerKeywordUuids);
        }else if(CustomerKeywordDeletionTypeEnum.EmptyTitle.name().equals(deleteType)){
            deleteCustomerKeywordsWhenEmptyTitle(terminalType, entryType,customerUuid);
        }else{
            deleteCustomerKeywordsWhenEmptyTitleAndUrl(terminalType, entryType,customerUuid);
        }
    }

    public List<String> getGroups(){
        return customerKeywordDao.getGroups();
    }


    public CustomerKeywordForOptimization searchCustomerKeywordsForOptimization(String terminalType, String clientID, String version, boolean updateQueryInfo) {
        ClientStatus clientStatus = clientStatusService.selectById(clientID);
        if(clientStatus == null) {
            clientStatusService.addSummaryClientStatus(terminalType, clientID, 500 + "", version, null);
            return null;
        }

        if(!clientStatus.getValid() || StringUtils.isEmpty(clientStatus.getGroup())){
            return null;
        }

        String typeName = "all";
        List<String> entryTypes = customerKeywordDao.getEntryTypes(clientStatus.getGroup());
        if(!Utils.isEmpty(entryTypes) && entryTypes.size() == 1){
            typeName = entryTypes.get(0);
        }

        if(configService.optimizationDateChanged()) {
            customerKeywordInvalidCountLogService.addCustomerKeywordInvalidCountLog();
            configService.updateOptimizationDateAsToday();
            customerKeywordDao.resetOptimizationInfo();
            clientStatusService.updateAllRemainingKeywordIndicator(1);
        }

        Config maxInvalidCountConfig = configService.getConfig(Constants.CONFIG_KEY_MAX_INVALID_COUNT, typeName);
        if(keywordOptimizationCountService.resetBigKeywordIndicator(clientStatus.getGroup())) {
            keywordOptimizationCountService.init(clientStatus.getGroup());
//            resetBigKeywordIndicator(clientStatus.getGroup(), Integer.parseInt(maxInvalidCountConfig.getValue()));
        }

        CustomerKeyword customerKeyword = null;
        int retryCount = 0;
        int noPositionMaxInvalidCount = 2;
        if(clientStatus.getOperationType().contains(Constants.CONFIG_TYPE_ZHANNEI_SOGOU)) {
            Config configInvalidRefreshCount = configService.getConfig(Constants.CONFIG_TYPE_ZHANNEI_SOGOU, Constants.CONFIG_KEY_NOPOSITION_MAX_INVALID_COUNT);
            noPositionMaxInvalidCount = Integer.parseInt(configInvalidRefreshCount.getValue());
        }
        do{
            boolean isNormalKeyword = keywordOptimizationCountService.optimizeNormalKeyword(clientStatus.getGroup());
            customerKeyword = customerKeywordDao.getCustomerKeywordForOptimization(terminalType, clientStatus.getGroup(),
                    Integer.parseInt(maxInvalidCountConfig.getValue()), noPositionMaxInvalidCount, !isNormalKeyword);

            if(customerKeyword == null){
                customerKeyword = customerKeywordDao.getCustomerKeywordForOptimization(terminalType, clientStatus.getGroup(),
                        Integer.parseInt(maxInvalidCountConfig.getValue()), noPositionMaxInvalidCount, false);
            }
            retryCount++;
            if(customerKeyword == null){
                if(keywordOptimizationCountService.resetBigKeywordIndicator(clientStatus.getGroup())) {
                    keywordOptimizationCountService.init(clientStatus.getGroup());
                }
                if(keywordOptimizationCountService.allowResetBigKeywordIndicator(clientStatus.getGroup())){
                    keywordOptimizationCountService.setLastVisitTime(clientStatus.getGroup());
                    resetBigKeywordIndicator(clientStatus.getGroup(), Integer.parseInt(maxInvalidCountConfig.getValue()), noPositionMaxInvalidCount);
                }
            }else if(updateQueryInfo){
                updateOptimizationQueryTime((customerKeyword.getUuid()));
            }
        }while(customerKeyword == null && retryCount < 2);

        if(customerKeyword != null){
            clientStatusService.updatePageNo(clientID, 0);
            CustomerKeywordForOptimization customerKeywordForOptimization = new CustomerKeywordForOptimization();
            customerKeywordForOptimization.setUuid(customerKeyword.getUuid());
            customerKeywordForOptimization.setKeyword(customerKeyword.getKeyword());
            customerKeywordForOptimization.setUrl(customerKeyword.getUrl());
            customerKeywordForOptimization.setEntryType(customerKeyword.getType());
            customerKeywordForOptimization.setBearPawNumber(customerKeyword.getBearPawNumber());

            customerKeywordForOptimization.setCurrentPosition(customerKeyword.getCurrentPosition());
            customerKeywordForOptimization.setOriginalUrl(customerKeyword.getOriginalUrl());
            customerKeywordForOptimization.setTitle(customerKeyword.getTitle());

            customerKeywordForOptimization.setGroup(clientStatus.getGroup());
            customerKeywordForOptimization.setOperationType(clientStatus.getOperationType());
            customerKeywordForOptimization.setBroadbandAccount(clientStatus.getBroadbandAccount());
            customerKeywordForOptimization.setBroadbandPassword(clientStatus.getBroadbandPassword());

            Set<String> specialGruupNames = new HashSet<String>();
            specialGruupNames.add("pc_pm_xiaowu");
            specialGruupNames.add("pc_pm_learner");
            specialGruupNames.add("pc_pm_51yza");
            specialGruupNames.add("pc_pm_yilufa");

            if(specialGruupNames.contains(customerKeyword.getOptimizeGroupName()) && (customerKeyword.getCurrentPosition() == 0 ||
                    customerKeyword.getCurrentPosition() > 20)) {
                customerKeywordForOptimization.setPage(2);
            }else{
                customerKeywordForOptimization.setPage(clientStatus.getPage());
            }

            if(clientStatus.getPageSize() != null) {
                if(specialGruupNames.contains(customerKeyword.getOptimizeGroupName()) && (customerKeyword.getCurrentPosition() == 0 ||
                        customerKeyword.getCurrentPosition() > 20)){
                    customerKeywordForOptimization.setPageSize(50);
                }else {
                    customerKeywordForOptimization.setPageSize(clientStatus.getPageSize());
                }
            }
            if(clientStatus.getZhanneiPercent() != null) {
                customerKeywordForOptimization.setZhanneiPercent(clientStatus.getZhanneiPercent());
            }
            if(clientStatus.getZhanwaiPercent() != null) {
                customerKeywordForOptimization.setZhanwaiPercent(clientStatus.getZhanwaiPercent());
            }
            if(clientStatus.getSpecialCharPercent() != null) {
                customerKeywordForOptimization.setSpecialCharPercent(clientStatus.getSpecialCharPercent());
            }
            if(clientStatus.getBaiduSemPercent() != null) {
                customerKeywordForOptimization.setBaiduSemPercent(clientStatus.getBaiduSemPercent());
            }
            customerKeywordForOptimization.setClearCookie(clientStatus.getClearCookie());
            if(clientStatus.getDragPercent() != null) {
                customerKeywordForOptimization.setDragPercent(clientStatus.getDragPercent());
            }
            if(clientStatus.getKuaizhaoPercent() != null) {
                customerKeywordForOptimization.setKuaizhaoPercent(clientStatus.getKuaizhaoPercent());
            }
            if(clientStatus.getMultiBrowser() != null) {
                customerKeywordForOptimization.setMultiBrowser(clientStatus.getMultiBrowser());
            }
            customerKeywordForOptimization.setOpenStatistics(clientStatus.getDisableStatistics() == 1 ? 0 : 1);
            customerKeywordForOptimization.setDisableStatistics(clientStatus.getDisableStatistics());
            customerKeywordForOptimization.setCurrentTime(Utils.formatDate(new Date(), Utils.TIME_FORMAT));

            customerKeywordForOptimization.setEntryPageMinCount(clientStatus.getEntryPageMinCount());
            customerKeywordForOptimization.setEntryPageMaxCount(clientStatus.getEntryPageMaxCount());
            customerKeywordForOptimization.setDisableVisitWebsite(clientStatus.getDisableVisitWebsite());
            customerKeywordForOptimization.setPageRemainMinTime(clientStatus.getPageRemainMinTime());
            customerKeywordForOptimization.setPageRemainMaxTime(clientStatus.getPageRemainMaxTime());
            customerKeywordForOptimization.setInputDelayMinTime(clientStatus.getInputDelayMinTime());
            customerKeywordForOptimization.setInputDelayMaxTime(clientStatus.getInputDelayMaxTime());
            customerKeywordForOptimization.setSlideDelayMinTime(clientStatus.getSlideDelayMinTime());
            customerKeywordForOptimization.setSlideDelayMaxTime(clientStatus.getSlideDelayMaxTime());
            customerKeywordForOptimization.setTitleRemainMinTime(clientStatus.getTitleRemainMinTime());
            customerKeywordForOptimization.setTitleRemainMaxTime(clientStatus.getTitleRemainMaxTime());
            customerKeywordForOptimization.setOptimizeKeywordCountPerIP(clientStatus.getOptimizeKeywordCountPerIP());
            customerKeywordForOptimization.setOneIPOneUser(clientStatus.getOneIPOneUser());
            customerKeywordForOptimization.setRandomlyClickNoResult(clientStatus.getRandomlyClickNoResult());
            customerKeywordForOptimization.setJustVisitSelfPage(clientStatus.getJustVisitSelfPage());
            customerKeywordForOptimization.setSleepPer2Words(clientStatus.getSleepPer2Words());
            customerKeywordForOptimization.setSupportPaste(clientStatus.getSupportPaste());
            customerKeywordForOptimization.setMoveRandomly(clientStatus.getMoveRandomly());
            customerKeywordForOptimization.setParentSearchEntry(clientStatus.getParentSearchEntry());
            customerKeywordForOptimization.setClearLocalStorage(clientStatus.getClearLocalStorage());
            customerKeywordForOptimization.setLessClickAtNight(clientStatus.getLessClickAtNight());
            customerKeywordForOptimization.setSameCityUser(clientStatus.getSameCityUser());
            customerKeywordForOptimization.setLocateTitlePosition(clientStatus.getLocateTitlePosition());
            customerKeywordForOptimization.setBaiduAllianceEntry(clientStatus.getBaiduAllianceEntry());
            customerKeywordForOptimization.setJustClickSpecifiedTitle(clientStatus.getJustClickSpecifiedTitle());
            customerKeywordForOptimization.setRandomlyClickMoreLink(clientStatus.getRandomlyClickMoreLink());
            customerKeywordForOptimization.setMoveUp20(clientStatus.getMoveUp20());
            customerKeywordForOptimization.setWaitTimeAfterOpenBaidu(clientStatus.getWaitTimeAfterOpenBaidu());
            customerKeywordForOptimization.setWaitTimeBeforeClick(clientStatus.getWaitTimeBeforeClick());
            customerKeywordForOptimization.setWaitTimeAfterClick(clientStatus.getWaitTimeAfterClick());
            customerKeywordForOptimization.setMaxUserCount(clientStatus.getMaxUserCount());
            customerKeywordForOptimization.setSearchEngine(customerKeyword.getSearchEngine());
            customerKeywordForOptimization.setTerminalType(customerKeyword.getTerminalType());
            customerKeywordForOptimization.setRemarks(customerKeyword.getRemarks());
            if(StringUtils.isNotBlank(customerKeywordForOptimization.getOperationType())) {
                if(customerKeywordForOptimization.getOperationType().contains(Constants.CONFIG_TYPE_TJ_XG)) {
                    customerKeywordForOptimization.setNegativeKeywords(new ArrayList<String>());
                    Config configNegativeKeywords = configService.getConfig(Constants.CONFIG_TYPE_TJ_XG, Constants.CONFIG_KEY_NEGATIVE_KEYWORDS);
                    Set<String> negativeKeywords = new HashSet<String>();
                    if(StringUtils.isNotBlank(configNegativeKeywords.getValue())) {
                        negativeKeywords.addAll(convertToSets(configNegativeKeywords.getValue()));
                    }
                    if(StringUtils.isNotBlank(customerKeyword.getNegativeKeywords())) {
                        negativeKeywords.addAll(convertToSets(customerKeyword.getNegativeKeywords()));
                    }
                    customerKeywordForOptimization.getNegativeKeywords().addAll(negativeKeywords);

                    if(StringUtils.isNotBlank(customerKeyword.getExcludeKeywords())) {
                        customerKeywordForOptimization.setExcludeKeywords(new ArrayList<String>(convertToSets(customerKeyword.getExcludeKeywords())));
                    }
                    if(StringUtils.isNotBlank(customerKeyword.getRecommendKeywords())) {
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

                if(customerKeywordForOptimization.getEntryType().equals(EntryTypeEnum.qz.name()) && customerKeywordForOptimization.getOperationType().contains("qz_zhannei")) {
                    List<KeywordSimpleVO> qzKeywords = customerKeywordDao.getQZCustomerKeywordSummaryInfos(terminalType, customerKeyword.getOptimizeGroupName());
                    customerKeywordForOptimization.setRelatedQZKeywords(qzKeywords);
                }
            }
            return customerKeywordForOptimization;
        }
        return null;
    }

    private Set<String> convertToSets(String str){
        String[] firstElements = str.split(",");
        Set<String> elements = new HashSet<String>();
        for(String firstElement : firstElements) {
            for (String subElement : firstElement.split("，")) {
                elements.add(subElement);
            }
        }
        return elements;
    }

    public void updateOptimizationQueryTime(Long customerKeywordUuid){
        customerKeywordDao.updateOptimizationQueryTime(customerKeywordUuid);
    }

    public boolean haveCustomerKeywordForOptimization(String terminalType, String clientID){
        CustomerKeywordForOptimization customerKeywordForOptimization = searchCustomerKeywordsForOptimization(terminalType, clientID, null, false);
        return customerKeywordForOptimization != null;
    }

    public void resetBigKeywordIndicator(String groupName, int maxInvalidCount, int noPositionMaxInvalidCount) {
        List<Map> remainingOptimizationCountMap = customerKeywordDao.searchRemainingOptimizationCount(groupName, maxInvalidCount, noPositionMaxInvalidCount);
        if(CollectionUtils.isNotEmpty(remainingOptimizationCountMap)) {
            customerKeywordDao.cleanBigKeywordIndicator(groupName);
            List<Long> customerKeywordUuids = new ArrayList<Long>();
            for(Map map : remainingOptimizationCountMap) {
                customerKeywordUuids.add(Long.parseLong(map.get("uuid").toString()));
                if((1.0 * customerKeywordUuids.size()) / remainingOptimizationCountMap.size() > 0.2) {
                    break;
                }
            }
            customerKeywordDao.setBigKeywordIndicator(customerKeywordUuids);
//            clientStatusService.updateRemainingKeywordIndicator(groupName, 1);
        }else{
//            clientStatusService.updateRemainingKeywordIndicator(groupName, 0);
        }
    }

    public void updateOptimizationResult(String terminalType, Long customerKeywordUuid, int count, String ip, String city, String clientID, String status, String freeSpace, String version){
        if(configService.optimizationDateChanged()) {
            customerKeywordInvalidCountLogService.addCustomerKeywordInvalidCountLog();
            configService.updateOptimizationDateAsToday();
            customerKeywordDao.resetOptimizationInfo();
            clientStatusService.updateAllRemainingKeywordIndicator(1);
        }

        customerKeywordDao.updateOptimizationResult(customerKeywordUuid, count);
        clientStatusService.logClientStatusTime(terminalType, clientID, status, freeSpace, version, city, count);
        customerKeywordIPService.addCustomerKeywordIP(customerKeywordUuid, city, ip);
    }

    public void batchUpdateOptimizedCount(List<Long> customerKeywordUuids) {
        customerKeywordDao.batchUpdateOptimizedCount(customerKeywordUuids);
    }

    public void adjustOptimizationCount(){
        List<String> groupNames = new ArrayList<String>();
        groupNames.add("pc_pm_xiaowu");
        groupNames.add("pc_pm_learner");
        groupNames.add("pc_pm_51yza");
        groupNames.add("pc_pm_yilufa");
        groupNames.add("m_pm_tiantian");
        groupNames.add("m_pm_tianqi");
        groupNames.add("m_pm_learner");
        List<Map> customerKeywordSummaries = customerKeywordDao.searchCustomerKeywordsForAdjustingOptimizationCount(groupNames);
        List<Map> ptCustomerKeywordSummaries = customerKeywordDao.searchPTKeywordsForAdjustingOptimizationCount();
        customerKeywordSummaries.addAll(ptCustomerKeywordSummaries);
        if(CollectionUtils.isNotEmpty(customerKeywordSummaries)){
            for(Map customerKeywordSummaryMap : customerKeywordSummaries) {
                Long uuid = Long.parseLong(customerKeywordSummaryMap.get("uuid").toString());
                int currentIndexCount = (Integer) customerKeywordSummaryMap.get("currentIndexCount");
                Integer positionFirstFee = (Integer) customerKeywordSummaryMap.get("positionFirstFee");
                String positionStr = (String) customerKeywordSummaryMap.get("positions");
                String[] positionArray = positionStr.split(",");
                int optimizationPlanCount = currentIndexCount < 100 ? 150 : currentIndexCount;
                if(positionFirstFee != null && positionFirstFee > 0) {
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

    public void updateCustomerKeywordPosition(Long customerKeywordUuid, int position, Date capturePositionQueryTime){
        boolean reachStandardFlag = false;
        if(position > 0 && position <= 10) {
            CustomerKeyword customerKeyword = customerKeywordDao.selectById(customerKeywordUuid);
            if(customerKeyword.getPositionFifthFee() != null && customerKeyword.getPositionFifthFee() > 0 && position <= 5) {
                reachStandardFlag = true;
            } else if(customerKeyword.getPositionThirdFee() != null && customerKeyword.getPositionThirdFee() > 0 && position <= 3) {
                reachStandardFlag = true;
            } else if(customerKeyword.getPositionFirstPageFee() != null && customerKeyword.getPositionFirstPageFee() > 0 && position <= 10) {
                reachStandardFlag = true;
            } else if(customerKeyword.getPositionSecondFee() != null && customerKeyword.getPositionSecondFee() > 0 && position <= 2) {
                reachStandardFlag = true;
            } else if(customerKeyword.getPositionFirstFee() != null && customerKeyword.getPositionFirstFee() > 0 && position == 1) {
                reachStandardFlag = true;
            }
        }
        customerKeywordDao.updatePosition(customerKeywordUuid, position, capturePositionQueryTime, reachStandardFlag);
        if(capturePositionQueryTime != null) {
            customerKeywordPositionSummaryService.savePositionSummary(customerKeywordUuid, position);
        }
    }

    public CustomerKeywordForCapturePosition getCustomerKeywordForCapturePosition(String terminalType, List<String> groupNames, Long customerUuid,
                                                                                  Date startTime,Long captureRankJobUuid){
        CustomerKeywordForCapturePosition customerKeywordForCapturePosition = new CustomerKeywordForCapturePosition();
        Boolean captureRankJobStatus = captureRankJobService.getCaptureRankJobStatus(captureRankJobUuid);
        customerKeywordForCapturePosition.setCaptureRankJobStatus(captureRankJobStatus);
        if(captureRankJobStatus){
            CustomerKeyword customerKeyword = customerKeywordDao.getCustomerKeywordForCapturePosition(terminalType, groupNames, customerUuid, startTime);
            if(customerKeyword != null){
                customerKeywordForCapturePosition.setUuid(customerKeyword.getUuid());
                customerKeywordForCapturePosition.setKeyword(customerKeyword.getKeyword());
                customerKeywordForCapturePosition.setUrl(customerKeyword.getUrl());
                customerKeywordForCapturePosition.setTitle(customerKeyword.getTitle());
                customerKeywordForCapturePosition.setSearchEngine(customerKeyword.getSearchEngine());
                customerKeywordForCapturePosition.setTerminalType(customerKeyword.getTerminalType());
                customerKeywordForCapturePosition.setBearPawNumber(customerKeyword.getBearPawNumber());
                customerKeywordDao.updateCapturePositionQueryTime(customerKeyword.getUuid());
                return customerKeywordForCapturePosition;
            }
            return null;
        }
        return customerKeywordForCapturePosition;
    }

    public void resetInvalidRefreshCount(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) throws Exception {
        customerKeywordDao.resetInvalidRefreshCount(customerKeywordRefreshStatInfoCriteria);
    }

    public Page<CustomerKeyword> searchCustomerKeywordLists(Page<CustomerKeyword> page, CustomerKeywordCriteria customerKeywordCriteria) {
        long startMilleSeconds = System.currentTimeMillis();
        RowBounds rowBounds = new RowBounds(page.getOffset(),page.getLimit());
        List<CustomerKeyword> customerKeywords = customerKeywordDao.searchCustomerKeywordsPage(page, customerKeywordCriteria);
        performanceService.addPerformanceLog(this.getClass() + ":searchCustomerKeywordLists", System.currentTimeMillis() - startMilleSeconds, null);
        List<CustomerKeyword> customerKeywordList = new ArrayList<CustomerKeyword>();
        Map<Long,String> customerMap = new HashMap<Long, String>();
        for (CustomerKeyword customerKeyword : customerKeywords) {
            String contactPerson = customerMap.get(customerKeyword.getCustomerUuid());
            if(contactPerson == null) {
                Customer customer = customerService.selectById(customerKeyword.getCustomerUuid());
                contactPerson = customer.getContactPerson();
                customerMap.put(customer.getUuid(), customer.getContactPerson());
            }
            customerKeyword.setContactPerson(contactPerson);
            customerKeywordList.add(customerKeyword);
        }
        page.setRecords(customerKeywordList);
        return page;
    }

    public void updateCustomerKeywordTitle(SearchEngineResultItemVO searchEngineResultItemVO) {
        CustomerKeyword customerKeyword = customerKeywordDao.selectById(searchEngineResultItemVO.getUuid());
        if (customerKeyword != null) {
            if(searchEngineResultItemVO.getUrl() != null){
                customerKeyword.setTitle(searchEngineResultItemVO.getTitle());
                customerKeyword.setUrl(searchEngineResultItemVO.getUrl());
                customerKeyword.setInitialPosition(searchEngineResultItemVO.getOrder());
                customerKeyword.setCurrentPosition(searchEngineResultItemVO.getOrder());
            }
            customerKeyword.setCapturedTitle(1);
            customerKeywordDao.updateById(customerKeyword);
        }
    }

    public void addCustomerKeywords(SearchEngineResultVO searchEngineResultVO, String terminalType, String userName) throws Exception {
        if(searchEngineResultVO != null){
            String searchEngine = searchEngineResultVO.getSearchEngine();
            List<CustomerKeyword> customerKeywords = new ArrayList<CustomerKeyword>();
            for(SearchEngineResultItemVO searchEngineResultItemVO : searchEngineResultVO.getSearchEngineResultItemVOs()){
                CustomerKeyword customerKeyword = convert(searchEngineResultItemVO, terminalType, searchEngineResultVO.getGroup(), searchEngineResultVO.getCustomerUuid(),searchEngine);
                customerKeywords.add(customerKeyword);
            }
            this.addCustomerKeywords(customerKeywords, userName);
        }
    }

    private CustomerKeyword convert(SearchEngineResultItemVO searchEngineResultItemVO, String terminalType, String groupName, long customerUuid,String searchEngine) {
        CustomerKeyword customerKeyword = new CustomerKeyword();
        customerKeyword.setCurrentPosition(searchEngineResultItemVO.getOrder());
        customerKeyword.setInitialPosition(searchEngineResultItemVO.getOrder());
        customerKeyword.setOptimizeGroupName(groupName);
        customerKeyword.setOptimizePlanCount(searchEngineResultItemVO.getClickCount());
        customerKeyword.setKeyword(searchEngineResultItemVO.getKeyword());
        customerKeyword.setTitle(searchEngineResultItemVO.getTitle());
        customerKeyword.setType(searchEngineResultItemVO.getType());
        customerKeyword.setTerminalType(terminalType);
        customerKeyword.setOriginalUrl(searchEngineResultItemVO.getHref());
        customerKeyword.setServiceProvider("baidutop123");
        if(searchEngine.equals(Constants.SEARCH_ENGINE_BAIDU)){
            customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
        }
        if(searchEngine.equals(Constants.SEARCH_ENGINE_SOGOU)){
            customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_SOGOU);
        }
        if(searchEngine.equals(Constants.SEARCH_ENGINE_360)){
            customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_360);
        }
        if(searchEngine.equals(Constants.SEARCH_ENGINE_SM)){
            customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_SM);
        }
        //customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
        customerKeyword.setUrl(searchEngineResultItemVO.getUrl());
        customerKeyword.setStartOptimizedTime(Utils.getCurrentTimestamp());
        customerKeyword.setCollectMethod(CollectMethod.PerMonth.getCode());
//                customerKeyword.setPositionFirstFee(0);
//                customerKeyword.setPositionSecondFee(0);
//                customerKeyword.setPositionThirdFee(0);
        customerKeyword.setCurrentIndexCount(20);
        customerKeyword.setCustomerUuid(customerUuid);
        customerKeyword.setAutoUpdateNegativeTime(Utils.getCurrentTimestamp());
        customerKeyword.setCreateTime(Utils.getCurrentTimestamp());
        customerKeyword.setUpdateTime(Utils.getCurrentTimestamp());
        return customerKeyword;
    }

    public void updateCustomerKeywordStatus(List<Long> customerKeywordUuids, Integer status) {
        customerKeywordDao.updateCustomerKeywordStatus(customerKeywordUuids, status);
    }

    public void autoUpdateNegativeCustomerKeywords(SearchEngineResultVO searchEngineResultVO, String terminalType, String loginName) throws Exception {
        if(searchEngineResultVO != null) {
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
                    CustomerKeyword customerKeyword = convert(searchEngineResultItemVO, terminalType, searchEngineResultVO.getGroup(), searchEngineResultVO.getCustomerUuid(),searchEngine);
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
            this.addCustomerKeywords(customerKeywords, loginName);
        }
    }

    public SearchEngineResultVO getCustomerKeywordForAutoUpdateNegative(String terminalType, String group) throws Exception
    {
        SearchEngineResultVO searchEngineResultVO = customerKeywordDao.getCustomerKeywordForAutoUpdateNegative(terminalType, group);
        if(searchEngineResultVO != null) {
            customerKeywordDao.updateAutoUpdateNegativeTime(terminalType, group, searchEngineResultVO.getKeyword());
        }
        return searchEngineResultVO;
    }

    public void updateAutoUpdateNegativeTimeAs4MinutesAgo(String terminalType, String group){
        customerKeywordDao.updateAutoUpdateNegativeTimeAs4MinutesAgo(terminalType, group);
    }

    public void controlCustomerKeywordStatus() {
        List<Long> invalidCustomerKeywords = customerKeywordInvalidCountLogService.findInvalidCustomerKeyword();
        do {
            List<Long> subCustomerKeywordUuids = invalidCustomerKeywords.subList(0, (invalidCustomerKeywords.size() > 500) ? 500 : invalidCustomerKeywords.size());
            customerKeywordDao.deleteBatchIds(subCustomerKeywordUuids);
            logger.info("controlCustomerKeywordStatus:" + subCustomerKeywordUuids.toString());
            invalidCustomerKeywords.removeAll(subCustomerKeywordUuids);
        } while (invalidCustomerKeywords.size() > 0);
    }

    public List<CodeNameVo> searchGroups() {
        return customerKeywordDao.searchGroups();
    }

    public List<String> searchCustomerKeywordSummaryInfo(String entryType, Long customerUuid) {
        return customerKeywordDao.searchCustomerKeywordSummaryInfo(entryType, customerUuid);
    }

    public String[] searchCustomerNegativeKeywords(long customerUuid) {
        String [] customerNegativeKeyword = customerKeywordDao.searchCustomerNegativeKeywords(customerUuid);
        if(customerNegativeKeyword != null){
            return customerNegativeKeyword;
        }else {
            return null;
        }
    }

    public List<ZTreeVO> getCustomerSource() {
        List<ZTreeVO> zTreeList = new ArrayList<ZTreeVO>();
        List<Customer> customers = customerService.findNegativeCustomer();
        Long currentTime = Utils.getCurrentTimestamp().getTime();
        for (Customer customer : customers) {
            zTreeList.add(new ZTreeVO(customer.getUuid(), 0L, customer.getContactPerson()));
            String [] customerKeywords = customerKeywordDao.searchCustomerNegativeKeywords(customer.getUuid());
            for (int i = 0; i < customerKeywords.length; i++) {
                zTreeList.add(new ZTreeVO(currentTime, customer.getUuid(), customerKeywords[i]));
                Long keywordTime = currentTime;
                currentTime++;
                for (String searchStyle : Constants.SEARCH_STYLE_LIST) {
                    String searchStyleJson = "{id:customer" + currentTime + ", pId:" + keywordTime + ", name:" + searchStyle + "}";
                    zTreeList.add(new ZTreeVO(currentTime, keywordTime, searchStyle));
                    currentTime++;
                }
            }
        }
        return zTreeList;
    }

    public void observeOptimizationCount() throws Exception {
        List<OptimizationCountVO> optimizationCountVOs = customerKeywordDao.tabkeOptimizationCountExceptionUsers();
        for (OptimizationCountVO optimizationCountVO : optimizationCountVOs) {
            List<OptimizationCountVO> groupOptimizationCountInfo = customerKeywordDao.observeGroupOptimizationCount(optimizationCountVO.getLoginName());
            List<OptimizationCountVO> keywordOptimizationCountInfo = customerKeywordDao.observeKeywordOptimizationCount(optimizationCountVO.getLoginName());
            observeOptimizationCountMailService.sendObserveOptimizationCountMail(optimizationCountVO.getEmail(), groupOptimizationCountInfo, keywordOptimizationCountInfo);
        }
    }

    public void controlRemainingKeywordIndicator(){
        Config fmMaxInvalidCountConfig = configService.getConfig(Constants.CONFIG_KEY_MAX_INVALID_COUNT, "fm");
        clientStatusService.updateAllRemainingKeywordIndicator(1);
        List<String> groupNames = customerKeywordDao.fetchOptimizationCompletedGroupNames("'fm'", Integer.parseInt(fmMaxInvalidCountConfig.getValue()));
        if(CollectionUtils.isNotEmpty(groupNames)) {
            updateRemainingKeywordIndicator(groupNames);
        }

        Config otherMaxInvalidCountConfig = configService.getConfig(Constants.CONFIG_KEY_MAX_INVALID_COUNT, "all");
        groupNames = customerKeywordDao.fetchOptimizationCompletedGroupNames("'pt','qz', 'bc'", Integer.parseInt(otherMaxInvalidCountConfig.getValue()));
        if(CollectionUtils.isNotEmpty(groupNames)) {
            updateRemainingKeywordIndicator(groupNames);
        }
    }

    private void updateRemainingKeywordIndicator(List<String> groupNames) {
        do {
            List<String> subGroupNames = groupNames.subList(0, (groupNames.size() > 500) ? 500 : groupNames.size());
            clientStatusService.updateRemainingKeywordIndicator(subGroupNames, 0);
            groupNames.removeAll(subGroupNames);
        } while (groupNames.size() > 0);
    }

    public void editCustomerOptimizePlanCount(Integer optimizePlanCount, String settingType, List<String> uuids) {
        customerKeywordDao.editCustomerOptimizePlanCount(optimizePlanCount, settingType, uuids);
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
}