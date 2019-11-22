package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerKeywordPositionSummaryDao;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.CustomerKeywordPositionSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerKeywordPositionSummaryService extends ServiceImpl<CustomerKeywordPositionSummaryDao, CustomerKeywordPositionSummary> {

    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordPositionSummaryService.class);

    @Autowired
    private CustomerKeywordPositionSummaryDao customerKeywordPositionSummaryDao;
    
    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void savePositionSummary(Long customerKeywordUuid, int position){
        try {
            CustomerKeywordPositionSummary positionSummary = customerKeywordPositionSummaryDao.getTodayPositionSummary(customerKeywordUuid);
            if (positionSummary != null) {
                boolean updFlag =
                    (positionSummary.getPosition() == null || positionSummary.getPosition() <= 0) || (position > 0 && positionSummary.getPosition() > position);
                if (updFlag) {
                    if (null == positionSummary.getSearchEngine()) {
                        fixFieldValueByCustomerKeyword(customerKeywordUuid, positionSummary);
                    }
                    positionSummary.setPosition(position);
                    customerKeywordPositionSummaryDao.updateById(positionSummary);
                } else {
                    if (null == positionSummary.getSearchEngine()) {
                        fixFieldValueByCustomerKeyword(customerKeywordUuid, positionSummary);
                        customerKeywordPositionSummaryDao.updateById(positionSummary);
                    }
                }
            } else {
                positionSummary = fixFieldValueByCustomerKeyword(customerKeywordUuid, new CustomerKeywordPositionSummary());
                positionSummary.setPosition(position);
                positionSummary.setCustomerKeywordUuid(customerKeywordUuid);
                customerKeywordPositionSummaryDao.addPositionSummary(positionSummary);
            }
        } catch (Exception ex) {
            logger.error("savePositionSummary error: " + ex.getMessage());
        }
    }

    public void deletePositionSummaryFromOneYearAgo() {
        customerKeywordPositionSummaryDao.deletePositionSummaryFromOneYearAgo();
    }

    private CustomerKeywordPositionSummary fixFieldValueByCustomerKeyword(Long customerKeywordUuid, CustomerKeywordPositionSummary positionSummary) {
        CustomerKeyword customerKeyword = customerKeywordService.selectById(customerKeywordUuid);
        positionSummary.setSearchEngine(customerKeyword.getSearchEngine());
        positionSummary.setTerminalType(customerKeyword.getTerminalType());
        positionSummary.setCustomerUuid(customerKeyword.getCustomerUuid());
        positionSummary.setType(customerKeyword.getType());
        return positionSummary;
    }
}
