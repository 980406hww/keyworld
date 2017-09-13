package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.enums.CollectMethod;
import com.keymanager.manager.*;
import com.keymanager.monitoring.excel.operator.AbstractExcelReader;
import com.keymanager.monitoring.criteria.BaiduIndexCriteria;
import com.keymanager.monitoring.criteria.CustomerKeywordCleanCriteria;
import com.keymanager.monitoring.criteria.CustomerKeywordCrilteria;
import com.keymanager.monitoring.criteria.CustomerKeywordUpdateGroupCriteria;
import com.keymanager.monitoring.dao.CustomerKeywordDao;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.*;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import com.keymanager.value.*;
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
    private CustomerKeywordDao customerKeywordDao;

    public Page<CustomerKeyword>  searchCustomerKeywords(Page<CustomerKeyword> page, CustomerKeywordCrilteria customerKeywordCrilteria){
        page.setRecords(customerKeywordDao.searchCustomerKeywords(page, customerKeywordCrilteria));
        return page;
    }

    public List<CustomerKeyword>  searchCustomerKeywords(CustomerKeywordCrilteria customerKeywordCrilteria){
        return customerKeywordDao.searchCustomerKeywords(customerKeywordCrilteria);
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

    public void addCustomerKeywordsFromSimpleUI(List<CustomerKeyword> customerKeywords, String terminalType, String entryType) {
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
                addCustomerKeyword(customerKeyword);
            }
        }
    }

   /* public void addCustomerKeywords(List<CustomerKeyword> customerKeywords) {
        for (CustomerKeyword customerKeyword : customerKeywords) {
            addCustomerKeyword(customerKeyword);
        }
    }*/

    public void addCustomerKeyword(CustomerKeyword customerKeyword) {
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
        customerKeyword.setStatus(1);
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

    public CustomerKeyword getCustomerKeyword(Long CustomerKeywordUuid) {
        return customerKeywordDao.selectById(CustomerKeywordUuid);
    }

    //简化版Excel文件导入
    public boolean handleExcel(InputStream inputStream, String excelType, int customerUuid, String type, String terminalType)
            throws Exception {
        AbstractExcelReader operator = AbstractExcelReader.createExcelOperator(inputStream, excelType);
        List<CustomerKeyword> customerKeywords = operator.readDataFromExcel();
        supplementInfo(customerKeywords, customerUuid, type, terminalType);
        addCustomerKeywords(customerKeywords);
        return true;
    }

    public void supplementInfo(List<CustomerKeyword> customerKeywords, int customerUuid, String type, String terminalType) {
        for (CustomerKeyword customerKeyword : customerKeywords) {
            customerKeyword.setCustomerUuid(customerUuid);
            customerKeyword.setType(type);
            customerKeyword.setCreateTime(Utils.getCurrentTimestamp());
            customerKeyword.setUpdateTime(Utils.getCurrentTimestamp());
            customerKeyword.setStatus(1);
            customerKeyword.setTerminalType(terminalType);
        }
    }
    public void addCustomerKeywords(List<CustomerKeyword> customerKeywords) throws Exception {
        for (CustomerKeyword customerKeyword : customerKeywords) {
            if(StringUtil.isNullOrEmpty(customerKeyword.getOriginalUrl())){
                customerKeyword.setOriginalUrl(customerKeyword.getUrl());
            }
            String originalUrl = customerKeyword.getOriginalUrl();
            if(!StringUtil.isNullOrEmpty(originalUrl)){
                if(originalUrl.indexOf("www.") == 0){
                    originalUrl = originalUrl.substring(4);
                }else if(originalUrl.indexOf("m.") == 0){
                    originalUrl = originalUrl.substring(2);
                }
            }else{
                originalUrl = null;
            }
            if(!"fm".equals(customerKeyword.getType()) && haveDuplicatedCustomerKeyword(customerKeyword.getTerminalType(), customerKeyword
                    .getCustomerUuid(), customerKeyword.getKeyword(), originalUrl)){
                return ;
            }
            customerKeyword.setKeyword(customerKeyword.getKeyword().trim());
            customerKeyword.setUrl(customerKeyword.getUrl() != null ? customerKeyword.getUrl().trim() : "");
            customerKeyword.setTitle(customerKeyword.getTitle() != null ? customerKeyword.getTitle().trim() : "");

            customerKeyword.setOriginalUrl(customerKeyword.getOriginalUrl() != null ? customerKeyword.getOriginalUrl().trim() : "");
            customerKeyword.setOrderNumber(customerKeyword.getOrderNumber() != null ? customerKeyword.getOrderNumber().trim() : "");

            customerKeyword.setCurrentPosition(10);
            customerKeyword.setAutoUpdateNegativeDateTime(Utils.getCurrentTimestamp());
            customerKeyword.setUpdateTime(new Date());
            customerKeywordDao.insert(customerKeyword);
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


    public CustomerKeywordForOptimization searchCustomerKeywordsForOptimization(String terminalType, String clientID, String version) throws
            Exception {
        ClientStatus clientStatus = clientStatusService.selectById(clientID);
        if(clientStatus == null) {
            clientStatusService.addSummaryClientStatus(terminalType, clientID, 500 + "", version, null);
            return null;
        }
        clientStatusService.updatePageNo(clientID, 0);
        if(!clientStatus.isValid() || StringUtils.isEmpty(clientStatus.getGroup())){
            return null;
        }

        String typeName = "all";
        List<String> entryTypes = customerKeywordDao.getEntryTypes(clientStatus.getGroup());
        if(!Utils.isEmpty(entryTypes) && entryTypes.size() == 1){
            typeName = entryTypes.get(0);
        }

        if(configService.optimizationDateChanged()) {
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
                    Integer.parseInt(maxInvalidCountConfig.getValue()), isNormalKeyword == false ? 0 : 1);
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

//            String relatedKeyword = "";
//            Random rd = new Random();
//            if (!Utils.isNullOrEmpty(customerKeyword.getRelatedKeywords())) {
//                String[] keywords = customerKeyword.getRelatedKeywords().split(",");
//                relatedKeyword = keywords[rd.nextInt(keywords.length)];
//            }
//            if (Utils.isNullOrEmpty(relatedKeyword)) {
//                KeywordPositionUrlManager manager = new KeywordPositionUrlManager();
//
//                relatedKeyword = manager.getKeywordPositionUrlVOString(datasourceName, customerKeyword.getSearchEngine(), null);
//                customerKeywordForOptimization.setRelatedKeyword(relatedKeyword);
//            }

            customerKeywordForOptimization.setCurrentPosition(customerKeyword.getCurrentPosition());
            customerKeywordForOptimization.setOriginalUrl(customerKeyword.getOriginalUrl());
            customerKeywordForOptimization.setTitle(customerKeyword.getTitle());

//            KeywordExcludeTitleManager manager = new KeywordExcludeTitleManager();
//            List<KeywordExcludeTitleVO> keywordExcludeTitleVOs = manager.searchKeywordExcludeTitleVOs(conn, customerKeyword.getKeyword());
//            StringBuilder sbTitle = new StringBuilder();
//            if(keywordExcludeTitleVOs != null){
//                List<String> excludeTitles = new ArrayList<String>();
//                customerKeywordForOptimization.setExcludeTitles(excludeTitles);
//                for(KeywordExcludeTitleVO keywordExcludeTitleVO : keywordExcludeTitleVOs){
//                    excludeTitles.add(keywordExcludeTitleVO.getExcludeTitle());
//                }
//            }
//
//
//            BaiduAdUrlManager baiduAdUrlManager = new BaiduAdUrlManager();
//            String baiduAdUrl = baiduAdUrlManager.getBaiduAdUrl(conn);
//            customerKeywordForOptimization.setBaiduAdUrl(baiduAdUrl);

            customerKeywordForOptimization.setGroup(clientStatus.getGroup());
            customerKeywordForOptimization.setOperationType(clientStatus.getOperationType());

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

            customerKeywordDao.updateOptimizationQueryTime(customerKeyword.getUuid());
            return customerKeywordForOptimization;
        }
        return null;
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
            configService.updateOptimizationDateAsToday();
            customerKeywordDao.resetOptimizationInfo();
        }
        customerKeywordDao.updateOptimizationResult(customerKeywordUuid, count);
        clientStatusService.logClientStatusTime(terminalType, clientID, status, freeSpace, version, city, count);
        customerKeywordIPService.addCustomerKeywordIP(customerKeywordUuid, city, ip);
    }
}
