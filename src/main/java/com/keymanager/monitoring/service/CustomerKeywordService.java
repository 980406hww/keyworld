package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.enums.CollectMethod;
import com.keymanager.excel.operator.AbstractExcelReader;
import com.keymanager.manager.CustomerKeywordManager;
import com.keymanager.monitoring.criteria.BaiduIndexCriteria;
import com.keymanager.monitoring.criteria.CustomerKeywordCrilteria;
import com.keymanager.monitoring.dao.CustomerKeywordDao;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.*;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import com.keymanager.value.CustomerKeywordForCaptureTitle;
import com.keymanager.value.CustomerKeywordVO;
import com.keymanager.value.CustomerVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private CustomerKeywordDao customerKeywordDao;

    public Page<CustomerKeyword>  searchCustomerKeywords(Page<CustomerKeyword> page, CustomerKeywordCrilteria customerKeywordCrilteria){
        page.setRecords(customerKeywordDao.searchCustomerKeywords(page, customerKeywordCrilteria));
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

    public void clearTitle(String uuids, String customerUuid, String terminalType) {
        if (StringUtils.isEmpty(uuids)) {
            customerKeywordDao.clearTitleByCustomerUuidAndTerminalType(terminalType, customerUuid);
        } else {
            customerKeywordDao.clearTitleByUuids(uuids.split(","));
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

    public void addCustomerKeywords(List<CustomerKeyword> customerKeywords) {
        for (CustomerKeyword customerKeyword : customerKeywords) {
            addCustomerKeyword(customerKeyword);
        }
    }

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

    public void deleteCustomerKeyword(long customerKeywordUuid,String entry) {
        CustomerKeyword customerKeyword = new CustomerKeyword();
        customerKeyword.setUuid(customerKeywordUuid);
        customerKeyword.setType(entry);
//        Wrapper wrapper = new EntityWrapper(customerKeyword);
//        this.delete(wrapper);
        customerKeywordDao.deleteCustomerKeyword(customerKeyword,null);
    }

    public void deleteCustomerKeywords(List<String> customerKeywordUuids, String entry, String deleteType, String terminalType,String customerUuid) {
        if (customerKeywordUuids != null) {
            for (String customerKeywordUuid : customerKeywordUuids) {
                CustomerKeyword customerKeyword = new CustomerKeyword();
                customerKeyword.setUuid(Long.parseLong(customerKeywordUuid));
                customerKeyword.setType(entry);
                customerKeywordDao.deleteCustomerKeyword(customerKeyword, deleteType);
            }
        }else {
            CustomerKeyword customerKeyword = new CustomerKeyword();
            customerKeyword.setCustomerUuid(Long.parseLong(customerUuid));
            customerKeyword.setTerminalType(terminalType);
            customerKeywordDao.deleteCustomerKeyword(customerKeyword, deleteType);
        }
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

    public void updateCustomerKeywordGroupName(CustomerKeyword customerKeyword) {
        customerKeywordDao.updateCustomerKeywordGroupName(customerKeyword);
    }

    public void changeOptimizationGroup(CustomerKeyword customerKeyword) {
        customerKeywordDao.changeOptimizationGroup(customerKeyword);
    }

    //重采标题
    public void resetTitle (CustomerKeyword customerKeyword,String resetType) {
        customerKeywordDao.resetTitle(customerKeyword,resetType);
    }
    //
    public CustomerKeyword getCustomerKeyword(Long CustomerKeywordUuid) {
        return customerKeywordDao.selectById(CustomerKeywordUuid);
    }
    //
    public CustomerVO getCustomerByUuid(String uuid) throws Exception {
        return null;
    }

    //简化版Excel文件导入
    public boolean handleExcel(InputStream inputStream, String excelType, int customerUuid, String type, String terminalType)
            throws Exception {
        AbstractExcelReader operator = AbstractExcelReader.createExcelOperator(inputStream, excelType);
        List customerKeywordVOs = operator.readDataFromExcel();
        supplementInfo(customerKeywordVOs, customerUuid, type, terminalType);
        addCustomerKeywordVOs(customerKeywordVOs);
        return true;
    }

    public void supplementInfo(List<CustomerKeywordVO> customerKeywordVOs, int customerUuid, String type, String terminalType) {
        for (CustomerKeywordVO customerKeywordVO : customerKeywordVOs) {
            customerKeywordVO.setCustomerUuid(customerUuid);
            customerKeywordVO.setType(type);
            customerKeywordVO.setCreateTime(Utils.getCurrentTimestamp());
            customerKeywordVO.setUpdateTime(Utils.getCurrentTimestamp());
            customerKeywordVO.setStatus(1);
            customerKeywordVO.setTerminalType(terminalType);
        }
    }
    public void addCustomerKeywordVOs(List<CustomerKeywordVO> customerKeywords) throws Exception {
        for (CustomerKeywordVO customerKeywordVO : customerKeywords) {
            if(StringUtil.isNullOrEmpty(customerKeywordVO.getOriginalUrl())){
                customerKeywordVO.setOriginalUrl(customerKeywordVO.getUrl());
            }
            String originalUrl = customerKeywordVO.getOriginalUrl();
            if(!StringUtil.isNullOrEmpty(originalUrl)){
                if(originalUrl.indexOf("www.") == 0){
                    originalUrl = originalUrl.substring(4);
                }else if(originalUrl.indexOf("m.") == 0){
                    originalUrl = originalUrl.substring(2);
                }
            }else{
                originalUrl = null;
            }
            if(!"fm".equals(customerKeywordVO.getType()) && haveDuplicatedCustomerKeyword(customerKeywordVO.getTerminalType(), customerKeywordVO
                    .getCustomerUuid(), customerKeywordVO.getKeyword(), originalUrl)){
                return ;
            }
            CustomerKeyword customerKeyword = new CustomerKeyword();
            customerKeyword.setCustomerUuid(customerKeywordVO.getCustomerUuid());
            customerKeyword.setType(customerKeywordVO.getType().trim());
            customerKeyword.setKeyword(customerKeywordVO.getKeyword().trim());
            customerKeyword.setUrl(customerKeywordVO.getUrl() != null ? customerKeywordVO.getUrl().trim() : "");
            customerKeyword.setTitle(customerKeywordVO.getTitle() != null ? customerKeywordVO.getTitle().trim() : "");
            customerKeyword.setSnapshotDateTime(customerKeywordVO.getSnapshotDateTime());
            customerKeyword.setSearchEngine(customerKeywordVO.getSearchEngine());
            customerKeyword.setInitialIndexCount(customerKeywordVO.getInitialIndexCount());


            customerKeyword.setTerminalType(customerKeywordVO.getTerminalType());
            customerKeyword.setOriginalUrl(customerKeywordVO.getOriginalUrl() != null ? customerKeywordVO.getOriginalUrl().trim() : "");
            customerKeyword.setPaymentStatus(customerKeywordVO.getPaymentStatus() != null ? customerKeywordVO.getPaymentStatus().trim() : "");
            customerKeyword.setOrderNumber(customerKeywordVO.getOrderNumber() != null ? customerKeywordVO.getOrderNumber().trim() : "");

            customerKeyword.setInitialPosition(customerKeywordVO.getInitialPosition());
            customerKeyword.setCurrentIndexCount(customerKeywordVO.getCurrentIndexCount());
            customerKeyword.setCurrentPosition(10);

            customerKeyword.setQueryTime(customerKeywordVO.getQueryTime());
            customerKeyword.setServiceProvider(customerKeywordVO.getServiceProvider());
            customerKeyword.setOptimizeGroupName(customerKeywordVO.getOptimizeGroupName());

            customerKeyword.setOptimizePlanCount(customerKeywordVO.getOptimizePlanCount());
            customerKeyword.setOptimizedCount(customerKeywordVO.getOptimizedCount());
            customerKeyword.setSequence(customerKeywordVO.getSequence());

            customerKeyword.setRelatedKeywords(customerKeywordVO.getRelatedKeywords());
            customerKeyword.setPositionFirstCost(customerKeywordVO.getPositionFirstCost());
            customerKeyword.setPositionSecondCost(customerKeywordVO.getPositionSecondCost());
            customerKeyword.setPositionThirdCost(customerKeywordVO.getPositionThirdCost());
            customerKeyword.setPositionForthCost(customerKeywordVO.getPositionForthCost());
            customerKeyword.setPositionFifthCost(customerKeywordVO.getPositionFifthCost());

            customerKeyword.setPositionFirstFee(customerKeywordVO.getPositionFirstFee());
            customerKeyword.setPositionSecondFee(customerKeywordVO.getPositionSecondFee());
            customerKeyword.setPositionThirdFee(customerKeywordVO.getPositionThirdFee());
            customerKeyword.setPositionForthFee(customerKeywordVO.getPositionForthFee());
            customerKeyword.setPositionFifthFee(customerKeywordVO.getPositionFifthFee());
            customerKeyword.setPositionFirstPageFee(customerKeywordVO.getPositionFirstPageFee());

            customerKeyword.setCollectMethod(customerKeywordVO.getCollectMethod());
            customerKeyword.setStartOptimizedTime(customerKeywordVO.getStartOptimizedTime());
            customerKeyword.setEffectiveFromTime(customerKeywordVO.getEffectiveFromTime());
            customerKeyword.setEffectiveToTime(customerKeywordVO.getEffectiveToTime());

            customerKeyword.setStatus(customerKeywordVO.getStatus());
            customerKeyword.setRemarks(customerKeywordVO.getRemarks());
            customerKeyword.setAutoUpdateNegativeDateTime(new Date());
            customerKeyword.setUpdateTime(new Date());
            customerKeywordDao.insert(customerKeyword);

//            Keyword keyword = new Keyword();
//            keywordService.addKeywordVOs(customerKeyword.getRelatedKeywords(), customerKeyword.getSearchEngine(),
//                    KeywordType.RelatedKeyword.name());
//            keywordService.addKeywordVOs(customerKeyword.getKeyword(), customerKeyword.getSearchEngine(),
//                    KeywordType.CustomerKeyword.name());
        }
    }


}
