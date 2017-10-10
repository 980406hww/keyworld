package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.enums.CollectMethod;
import com.keymanager.enums.CustomerKeywordStatus;
import com.keymanager.manager.CustomerKeywordManager;
import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.dao.CustomerKeywordDao;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.*;
import com.keymanager.monitoring.excel.operator.AbstractExcelReader;
import com.keymanager.monitoring.vo.SearchEngineResultItemVO;
import com.keymanager.monitoring.vo.SearchEngineResultVO;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import com.keymanager.value.CustomerKeywordForCapturePosition;
import com.keymanager.value.CustomerKeywordForCaptureTitle;
import com.keymanager.value.CustomerKeywordForOptimization;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
    private CustomerKeywordOptimizedCountLogService customerKeywordOptimizedCountLogService;

    @Autowired
    private CaptureRealUrlService captureRealUrlService;

    @Autowired
    private NegativeListService negativeListService;

    @Autowired
    private CustomerKeywordDao customerKeywordDao;

    public Page<CustomerKeyword> searchCustomerKeywords(Page<CustomerKeyword> page, CustomerKeywordCriteria customerKeywordCriteria){
        page.setRecords(customerKeywordDao.searchCustomerKeywords(page, customerKeywordCriteria));
        return page;
    }

    public String searchCustomerKeywordForCaptureTitle(String terminalType) throws Exception {
        QZCaptureTitleLog qzCaptureTitleLog = qzCaptureTitleLogService.getAvailableQZSetting(QZCaptureTitleLogStatusEnum.Processing.getValue(), terminalType);
        if (qzCaptureTitleLog == null) {
            qzCaptureTitleLog = qzCaptureTitleLogService.getAvailableQZSetting(QZCaptureTitleLogStatusEnum.New.getValue(), terminalType);
            if (qzCaptureTitleLog != null) {
                qzCaptureTitleLogService.startQZCaptureTitleLog(qzCaptureTitleLog.getUuid());
            }
        }
        if (qzCaptureTitleLog == null) {
            return "";
        }
        CustomerKeywordManager manager = new CustomerKeywordManager();
        CustomerKeywordForCaptureTitle captureTitle = manager.searchCustomerKeywordForCaptureTitle("keyword", qzCaptureTitleLog.getTerminalType(),
                qzCaptureTitleLog.getGroup(), qzCaptureTitleLog.getCustomerUuid());

        if (captureTitle == null) {
            qzCaptureTitleLogService.completeQZCaptureTitleLog(qzCaptureTitleLog.getUuid());
            manager.deleteEmptyTitleCustomerKeyword("keyword", qzCaptureTitleLog.getTerminalType(), qzCaptureTitleLog.getCustomerUuid(),
                    qzCaptureTitleLog.getType(), qzCaptureTitleLog.getGroup());
            return "";
        } else {
            QZOperationType qzOperationType = qzOperationTypeService.selectById(qzCaptureTitleLog.getQzOperationTypeUuid());
            QZSetting qzSetting = qzSettingService.selectById(qzOperationType.getQzSettingUuid());
            captureTitle.setWholeUrl(qzSetting.getDomain());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(captureTitle);
        }
    }

    public String searchCustomerKeywordForCaptureTitle(String groupName, String terminalType) throws Exception {
        CustomerKeywordManager manager = new CustomerKeywordManager();
        return manager.searchCustomerKeywordForCaptureTitle("keyword", terminalType, groupName);
    }

    public void cleanTitle(CustomerKeywordCleanCriteria customerKeywordCleanCriteria) {
        if (CustomerKeywordCleanTypeEnum.CustomerTitle.name().equals(customerKeywordCleanCriteria.getCleanType())) {
            customerKeywordDao.cleanCustomerTitle(customerKeywordCleanCriteria.getTerminalType(), customerKeywordCleanCriteria.getEntryType(), customerKeywordCleanCriteria.getCustomerUuid());
        } else if(CustomerKeywordCleanTypeEnum.SelectedCustomerKeywordTitle.name().equals(customerKeywordCleanCriteria.getCleanType())) {
            customerKeywordDao.cleanSelectedCustomerKeywordTitle(customerKeywordCleanCriteria.getCustomerKeywordUuids());
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
        customerKeyword.setUpdateTime(new Date());
        customerKeyword.setCreateTime(new Date());
        customerKeywordDao.insert(customerKeyword);
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

    public int getCustomerKeywordCount(long customerUuid) {
        return customerKeywordDao.getCustomerKeywordCount(customerUuid);
    }

    public void deleteCustomerKeywordsByUuid(List<String> customerKeywordUuids){
        customerKeywordDao.deleteCustomerKeywordsByUuid(customerKeywordUuids);
    }

    public void deleteCustomerKeywordsWhenEmptyTitleAndUrl(String terminalType, String entryType,String customerUuid){
        customerKeywordDao.deleteCustomerKeywordsWhenEmptyTitleAndUrl(terminalType, entryType,customerUuid);
    }

    public void deleteCustomerKeywordsWhenEmptyTitle(String terminalType, String entryType,String customerUuid){
        customerKeywordDao.deleteCustomerKeywordsWhenEmptyTitle(terminalType, entryType,customerUuid);
    }

    public void deleteCustomerKeywords(long customerUuid) {
        CustomerKeyword customerKeyword = new CustomerKeyword();
        customerKeyword.setCustomerUuid(customerUuid);
        Wrapper wrapper = new EntityWrapper(customerKeyword);
        this.delete(wrapper);
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
                } else {
                    customerKeyword.setInitialIndexCount(baiduIndexCriteria.getPhoneIndex());
                    customerKeyword.setCurrentIndexCount(baiduIndexCriteria.getPhoneIndex());
                }
                calculatePrice(customerKeyword);
                customerKeyword.setUpdateTime(new Date());
                customerKeywordDao.updateById(customerKeyword);
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

    public void updateCustomerKeywordGroupName(CustomerKeywordUpdateGroupCriteria customerKeywordUpdateGroupCriteria) {
        customerKeywordDao.updateCustomerKeywordGroupName(customerKeywordUpdateGroupCriteria);
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
        for (CustomerKeyword customerKeyword : customerKeywords) {
            customerKeyword.setCustomerUuid(customerUuid);
            customerKeyword.setType(type);
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


    public CustomerKeywordForOptimization searchCustomerKeywordsForOptimization(String terminalType, String clientID, String version) {
        ClientStatus clientStatus = clientStatusService.selectById(clientID);
        if(clientStatus == null) {
            clientStatusService.addSummaryClientStatus(terminalType, clientID, 500 + "", version, null);
            return null;
        }
        clientStatusService.updatePageNo(clientID, 0);
        if(!clientStatus.getValid() || StringUtils.isEmpty(clientStatus.getGroup())){
            return null;
        }

        String typeName = "all";
        List<String> entryTypes = customerKeywordDao.getEntryTypes(clientStatus.getGroup());
        if(!Utils.isEmpty(entryTypes) && entryTypes.size() == 1){
            typeName = entryTypes.get(0);
        }

        if(configService.optimizationDateChanged()) {
            customerKeywordOptimizedCountLogService.addCustomerKeywordOptimizedCountLog();
            configService.updateOptimizationDateAsToday();
            customerKeywordDao.resetOptimizationInfo();
        }

        Config maxInvalidCountConfig = configService.getConfig(Constants.CONFIG_KEY_MAX_INVALID_COUNT, typeName);
        if(keywordOptimizationCountService.resetBigKeywordIndicator(clientStatus.getGroup())) {
            keywordOptimizationCountService.init(clientStatus.getGroup());
            resetBigKeywordIndicator(clientStatus.getGroup(), Integer.parseInt(maxInvalidCountConfig.getValue()));
        }

        CustomerKeyword customerKeyword = null;
        int retryCount = 0;
        do{
            boolean isNormalKeyword = keywordOptimizationCountService.optimizeNormalKeyword(clientStatus.getGroup());
            customerKeyword = customerKeywordDao.getCustomerKeywordForOptimization(terminalType, clientStatus.getGroup(),
                    Integer.parseInt(maxInvalidCountConfig.getValue()), isNormalKeyword ? 0 : 1);
            retryCount++;
            if(customerKeyword == null){
                keywordOptimizationCountService.init(clientStatus.getGroup());
                resetBigKeywordIndicator(clientStatus.getGroup(), Integer.parseInt(maxInvalidCountConfig.getValue()));
            }
        }while(customerKeyword == null && retryCount < 3);

        if(customerKeyword != null){
            CustomerKeywordForOptimization customerKeywordForOptimization = new CustomerKeywordForOptimization();
            customerKeywordForOptimization.setUuid(customerKeyword.getUuid());
            customerKeywordForOptimization.setKeyword(customerKeyword.getKeyword());
            customerKeywordForOptimization.setUrl(customerKeyword.getUrl());
            customerKeywordForOptimization.setEntryType(customerKeyword.getType());

            customerKeywordForOptimization.setCurrentPosition(customerKeyword.getCurrentPosition());
            customerKeywordForOptimization.setOriginalUrl(customerKeyword.getOriginalUrl());
            customerKeywordForOptimization.setTitle(customerKeyword.getTitle());

            customerKeywordForOptimization.setGroup(clientStatus.getGroup());
            customerKeywordForOptimization.setOperationType(clientStatus.getOperationType());
            customerKeywordForOptimization.setBroadbandAccount(clientStatus.getBroadbandAccount());
            customerKeywordForOptimization.setBroadbandPassword(clientStatus.getBroadbandPassword());

            if("pc_pm_xiaowu".equals(customerKeyword.getOptimizeGroupName()) && (customerKeyword.getCurrentPosition() == 0 ||
                    customerKeyword.getCurrentPosition() > 20)) {
                customerKeywordForOptimization.setPage(2);
            }else{
                customerKeywordForOptimization.setPage(clientStatus.getPage());
            }

            if(clientStatus.getPageSize() != null) {
                if("pc_pm_xiaowu".equals(customerKeyword.getOptimizeGroupName()) && (customerKeyword.getCurrentPosition() == 0 ||
                        customerKeyword.getCurrentPosition() > 20)){
                    customerKeywordForOptimization.setPageSize(50);
                }else {
                    customerKeywordForOptimization.setPageSize(clientStatus.getPageSize());
                }
            }
            if(clientStatus.getZhanneiPercent() != null) {
                customerKeywordForOptimization.setZhanneiPercent(clientStatus.getZhanneiPercent());
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
            return customerKeywordForOptimization;
        }
        return null;
    }

    public void updateOptimizationQueryTime(Long customerKeywordUuid){
        customerKeywordDao.updateOptimizationQueryTime(customerKeywordUuid);
    }

    public boolean haveCustomerKeywordForOptimization(String terminalType, String clientID){
        CustomerKeywordForOptimization customerKeywordForOptimization = searchCustomerKeywordsForOptimization(terminalType, clientID, null);
        return customerKeywordForOptimization != null;
    }

    public void resetBigKeywordIndicator(String groupName, int maxInvalidCount) {
        customerKeywordDao.cleanBigKeywordIndicator(groupName);
        List<Map> remainingOptimizationCountMap = customerKeywordDao.searchRemainingOptimizationCount(groupName, maxInvalidCount);
        if(CollectionUtils.isNotEmpty(remainingOptimizationCountMap)) {
            List<Long> customerKeywordUuids = new ArrayList<Long>();
            for(Map map : remainingOptimizationCountMap) {
                customerKeywordUuids.add(Long.parseLong(map.get("uuid").toString()));
                if((1.0 * customerKeywordUuids.size()) / remainingOptimizationCountMap.size() > 0.2) {
                    break;
                }
            }
            customerKeywordDao.setBigKeywordIndicator(customerKeywordUuids);
        }
    }

    public void updateOptimizationResult(String terminalType, Long customerKeywordUuid, int count, String ip, String city, String clientID, String status, String freeSpace, String version){
        if(configService.optimizationDateChanged()) {
            customerKeywordOptimizedCountLogService.addCustomerKeywordOptimizedCountLog();
            configService.updateOptimizationDateAsToday();
            customerKeywordDao.resetOptimizationInfo();
        }

        customerKeywordDao.updateOptimizationResult(customerKeywordUuid, count);
        clientStatusService.logClientStatusTime(terminalType, clientID, status, freeSpace, version, city, count);
        customerKeywordIPService.addCustomerKeywordIP(customerKeywordUuid, city, ip);
    }

    public void adjustOptimizationCount(){
        List<Map> customerKeywordSummaries = customerKeywordDao.searchCustomerKeywordsForAdjustingOptimizationCount("pc_pm_xiaowu");
        if(CollectionUtils.isNotEmpty(customerKeywordSummaries)){
            for(Map customerKeywordSummaryMap : customerKeywordSummaries) {
                Long uuid = Long.parseLong(customerKeywordSummaryMap.get("uuid").toString());
                int currentIndexCount = (Integer) customerKeywordSummaryMap.get("currentIndexCount");
                String positionStr = (String) customerKeywordSummaryMap.get("positions");
                String[] positionArray = positionStr.split(",");
                int optimizationPlanCount = currentIndexCount < 100 ? 150 : currentIndexCount;
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
    }

    public void updateCustomerKeywordPosition(Long customerKeywordUuid, int position){
        CustomerKeyword customerKeyword = customerKeywordDao.selectById(customerKeywordUuid);
        if(customerKeyword != null){
            customerKeyword.setCurrentPosition(position);
            if(customerKeyword.getInitialPosition() == null){
                customerKeyword.setInitialPosition(position);
            }
            customerKeyword.setCapturePositionQueryTime(Utils.getCurrentTimestamp());
            customerKeyword.setUpdateTime(new Date());
            customerKeywordDao.updateById(customerKeyword);

            customerKeywordPositionSummaryService.savePositionSummary(customerKeywordUuid, position);
        }
    }

    public CustomerKeywordForCapturePosition getCustomerKeywordForCapturePosition(String terminalType, List<String> groupNames, Long customerUuid,
                                                                                  int minutes){
        CustomerKeyword customerKeyword = customerKeywordDao.getCustomerKeywordForCapturePosition(terminalType, groupNames, customerUuid, minutes);
        if(customerKeyword != null){
            CustomerKeywordForCapturePosition customerKeywordForCapturePosition = new CustomerKeywordForCapturePosition();
            customerKeywordForCapturePosition.setUuid(customerKeyword.getUuid());
            customerKeywordForCapturePosition.setKeyword(customerKeyword.getKeyword());
            customerKeywordForCapturePosition.setUrl(customerKeyword.getUrl());
            customerKeywordForCapturePosition.setTitle(customerKeyword.getTitle());

            customerKeyword.setCapturePositionQueryTime(new Date());
            customerKeywordDao.updateById(customerKeyword);
            return customerKeywordForCapturePosition;
        }
        return null;
    }

    public void resetInvalidRefreshCount(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) throws Exception {
        customerKeywordDao.resetInvalidRefreshCount(customerKeywordRefreshStatInfoCriteria);
    }

    public Page<CustomerKeyword> searchCustomerKeywordLists(Page<CustomerKeyword> page, CustomerKeywordCriteria customerKeywordCriteria) {
        List<CustomerKeyword> customerKeywords = customerKeywordDao.searchCustomerKeywords(page, customerKeywordCriteria);
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
            List<CustomerKeyword> customerKeywords = new ArrayList<CustomerKeyword>();
            for(SearchEngineResultItemVO searchEngineResultItemVO : searchEngineResultVO.getSearchEngineResultItemVOs()){
                CustomerKeyword customerKeyword = convert(searchEngineResultItemVO, terminalType, searchEngineResultVO.getGroup(), searchEngineResultVO.getCustomerUuid());
                customerKeywords.add(customerKeyword);
            }
            this.addCustomerKeywords(customerKeywords, userName);
        }
    }

    private CustomerKeyword convert(SearchEngineResultItemVO searchEngineResultItemVO, String terminalType, String groupName, long customerUuid) {
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
        customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
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
            List<NegativeList> negativeLists = negativeListService.getSpecifiedKeywordNegativeLists(terminalType, searchEngineResultVO.getKeyword());
            List<CustomerKeyword> customerKeywords = new ArrayList<CustomerKeyword>();
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
                    CustomerKeyword customerKeyword = convert(searchEngineResultItemVO, terminalType, searchEngineResultVO.getGroup(), searchEngineResultVO.getCustomerUuid());
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
        List<Long> invalidCustomerKeywords = customerKeywordOptimizedCountLogService.findInvalidCustomerKeyword();
        do {
            List<Long> subCustomerKeywordUuids = invalidCustomerKeywords.subList(0, (invalidCustomerKeywords.size() > 500) ? 500 : invalidCustomerKeywords.size());
            customerKeywordDao.updateCustomerKeywordStatus(subCustomerKeywordUuids, CustomerKeywordStatus.Inactive.getCode());
            invalidCustomerKeywords.removeAll(subCustomerKeywordUuids);
        } while (invalidCustomerKeywords.size() > 0);
    }
}

