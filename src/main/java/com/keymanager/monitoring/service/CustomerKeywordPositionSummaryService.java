package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerKeywordPositionSummaryDao;
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

    public void savePositionSummary(Long customerKeywordUuid, int position){
        try {
            CustomerKeywordPositionSummary positionSummary = customerKeywordPositionSummaryDao.getTodayPositionSummary(customerKeywordUuid);
            if (positionSummary != null) {
                if ((positionSummary.getPosition() == null || positionSummary.getPosition() <= 0) || (position > 0 && positionSummary.getPosition() > position)) {
                    positionSummary.setPosition(position);
                    customerKeywordPositionSummaryDao.updateById(positionSummary);
                }
            } else {
                positionSummary = new CustomerKeywordPositionSummary();
                positionSummary.setPosition(position);
                positionSummary.setCustomerKeywordUuid(customerKeywordUuid);
                customerKeywordPositionSummaryDao.addPositionSummary(positionSummary);
            }
        }catch (Exception ex){

        }
    }

    public void deletePositionSummaryFromAWeekAgo() {
        customerKeywordPositionSummaryDao.deletePositionSummaryFromAWeekAgo();
    }
}
