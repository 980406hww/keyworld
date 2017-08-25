package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.enums.CollectMethod;
import com.keymanager.manager.CustomerKeywordManager;
import com.keymanager.monitoring.criteria.BaiduIndexCriteria;
import com.keymanager.monitoring.dao.CustomerKeywordDao;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.*;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import com.keymanager.value.CustomerKeywordForCaptureTitle;
import com.sun.xml.internal.xsom.impl.Ref;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    private CustomerChargeRuleTypeService customerChargeRuleTypeService;

    @Autowired
    private CustomerKeywordDao customerKeywordDao;

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
                ex.printStackTrace();
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

    private void addCustomerKeyword(CustomerKeyword customerKeyword) {
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

//	public List<CustomerKeyword> searchCustomerKeywords(String terminalType, long customerUuid, String keyword, String originalUrl){
//		CustomerKeyword customerKeyword = new CustomerKeyword();
//		customerKeyword.setCustomerUuid(customerUuid);
//		customerKeyword.setType(terminalType);
//		customerKeyword.setKeyword(keyword);
//		customerKeyword.setOriginalUrl(originalUrl);
//		Wrapper wrapper = new EntityWrapper(customerKeyword);
//	}

    public boolean haveDuplicatedCustomerKeyword(String terminalType, long customerUuid, String keyword, String originalUrl) {
        int customerKeywordCount = 0;
        try {
            customerKeywordCount = customerKeywordDao.getSimilarCustomerKeywordCount(terminalType, customerUuid, keyword, originalUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return customerKeywordCount > 0;
    }

    public int getCustomerKeywordCount(long customerUuid) {
        return customerKeywordDao.getCustomerKeywordCount(customerUuid);
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
        List<CustomerKeyword> customerKeywords = customerKeywordDao.getCustomerKeywordsForCaptureIndex(null);
        if (CollectionUtils.isNotEmpty(customerKeywords)) {
            CustomerKeyword customerKeyword = customerKeywords.get(0);
            customerKeywordDao.updateCaptureIndexQueryTime(customerKeyword.getKeyword());
            return customerKeyword;
        }
        return null;
    }

    public void updateCustomerKeywordIndex(BaiduIndexCriteria baiduIndexCriteria) {
        List<CustomerKeyword> customerKeywords = customerKeywordDao.getCustomerKeywordsForCaptureIndex(baiduIndexCriteria.getKeyword());
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
        CustomerChargeType customerChargeType = customerChargeRuleTypeService.getCustomerChargeRule(customerKeyword.getCustomerUuid());
        if (customerChargeType != null) {
            if (ChargeTypeEnum.Percentage.name().equals(customerChargeType.getChargeType())) {
                CustomerChargeRuleCalculation percentageCustomerChargeRuleCalculation = null;
                CustomerChargeRuleCalculation fixedPriceCustomerChargeRuleCalculation = null;
                for (CustomerChargeRuleCalculation tmpCustomerChargeRuleCalculation : customerChargeType.getCustomerChargeRuleCalculations()) {
                    if (tmpCustomerChargeRuleCalculation.getOperationType().equals(customerKeyword.getTerminalType())) {
                        if (ChargeDataTypeEnum.ZeroIndex.equals(tmpCustomerChargeRuleCalculation.getChargeDataType())) {
                            fixedPriceCustomerChargeRuleCalculation = tmpCustomerChargeRuleCalculation;
                        }
                        if (ChargeDataTypeEnum.Percentage.equals(tmpCustomerChargeRuleCalculation.getChargeDataType())) {
                            percentageCustomerChargeRuleCalculation = tmpCustomerChargeRuleCalculation;
                        }
                    }
                }

                if (percentageCustomerChargeRuleCalculation != null) {
                    if (percentageCustomerChargeRuleCalculation.getChargesOfFirst() != null) {
                        customerKeyword.setPositionFirstFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeRuleCalculation != null ? fixedPriceCustomerChargeRuleCalculation.getChargesOfFirst() :
                                        null), percentageCustomerChargeRuleCalculation.getChargesOfFirst()));
                    }
                    if (percentageCustomerChargeRuleCalculation.getChargesOfSecond() != null) {
                        customerKeyword.setPositionSecondFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeRuleCalculation != null ? fixedPriceCustomerChargeRuleCalculation.getChargesOfSecond() :
                                        null), percentageCustomerChargeRuleCalculation.getChargesOfSecond()));
                    }
                    if (percentageCustomerChargeRuleCalculation.getChargesOfThird() != null) {
                        customerKeyword.setPositionThirdFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeRuleCalculation != null ? fixedPriceCustomerChargeRuleCalculation.getChargesOfThird() :
                                        null), percentageCustomerChargeRuleCalculation.getChargesOfThird()));
                    }
                    if (percentageCustomerChargeRuleCalculation.getChargesOfFourth() != null) {
                        customerKeyword.setPositionForthFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeRuleCalculation != null ? fixedPriceCustomerChargeRuleCalculation.getChargesOfFourth() :
                                        null), percentageCustomerChargeRuleCalculation.getChargesOfFourth()));
                    }
                    if (percentageCustomerChargeRuleCalculation.getChargesOfFifth() != null) {
                        customerKeyword.setPositionFirstFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeRuleCalculation != null ? fixedPriceCustomerChargeRuleCalculation.getChargesOfFifth() :
                                        null), percentageCustomerChargeRuleCalculation.getChargesOfFifth()));
                    }
                    if (percentageCustomerChargeRuleCalculation.getChargesOfFirstPage() != null) {
                        customerKeyword.setPositionFirstPageFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                fixedPriceCustomerChargeRuleCalculation.getChargesOfFirstPage(), percentageCustomerChargeRuleCalculation
                                        .getChargesOfFirstPage()));
                        customerKeyword.setPositionFirstPageFee(calculatePrice(customerKeyword.getCurrentIndexCount(),
                                (fixedPriceCustomerChargeRuleCalculation != null ? fixedPriceCustomerChargeRuleCalculation.getChargesOfFirstPage() :
                                        null), percentageCustomerChargeRuleCalculation.getChargesOfFirstPage()));
                    }
                }
            } else {
                for (CustomerChargeRuleInterval tmpCustomerChargeRuleInterval : customerChargeType.getCustomerChargeRuleIntervals()) {
                    if (tmpCustomerChargeRuleInterval.getOperationType().equals(customerKeyword.getTerminalType())) {
                        if (tmpCustomerChargeRuleInterval.getStartIndex() <= customerKeyword
                                .getCurrentIndexCount() && (tmpCustomerChargeRuleInterval.getEndIndex() == null || customerKeyword
                                .getCurrentIndexCount() < tmpCustomerChargeRuleInterval.getEndIndex())) {
                            customerKeyword.setPositionFirstFee(tmpCustomerChargeRuleInterval.getPrice().doubleValue());
                            customerKeyword.setPositionSecondFee(tmpCustomerChargeRuleInterval.getPrice().doubleValue());
                            customerKeyword.setPositionThirdFee(tmpCustomerChargeRuleInterval.getPrice().doubleValue());
                            customerKeyword.setPositionForthFee(tmpCustomerChargeRuleInterval.getPrice().doubleValue() / 2);
                            customerKeyword.setPositionFifthFee(tmpCustomerChargeRuleInterval.getPrice().doubleValue() / 2);
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
        return Math.round((currentIndexCount * pricePercentage.doubleValue()) / 1000) * 10;
    }
}
