package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CustomerKeywordPositionSummaryDao;
import com.keymanager.monitoring.entity.CustomerKeywordPositionSummary;
import com.keymanager.monitoring.vo.UpdateCustomerKeywordPositionVO;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerKeywordPositionSummaryService extends ServiceImpl<CustomerKeywordPositionSummaryDao, CustomerKeywordPositionSummary> {

    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordPositionSummaryService.class);

    @Autowired
    private CustomerKeywordPositionSummaryDao customerKeywordPositionSummaryDao;

    public void savePositionSummary(List<UpdateCustomerKeywordPositionVO> updateCustomerKeywordPositionVos){
        try {
            List<CustomerKeywordPositionSummary> ckPositionSummaryListForUpdate = new ArrayList<>();
            List<CustomerKeywordPositionSummary> ckPositionSummaryListForInsert = new ArrayList<>();
            for (UpdateCustomerKeywordPositionVO updateCustomerKeywordPositionVo : updateCustomerKeywordPositionVos) {
                Long customerKeywordUuid = updateCustomerKeywordPositionVo.getCustomerKeywordUuid();
                Integer position = updateCustomerKeywordPositionVo.getPosition();
                CustomerKeywordPositionSummary positionSummary = customerKeywordPositionSummaryDao.getTodayPositionSummary(customerKeywordUuid);
                if (positionSummary != null) {
                    boolean positionUpd =
                        (positionSummary.getPosition() == null || positionSummary.getPosition() <= 0) || (position > 0 && positionSummary.getPosition() > position);
                    if (positionUpd) {
                        positionSummary.setPosition(position);
                        ckPositionSummaryListForUpdate.add(positionSummary);
                    }
                } else {
                    positionSummary = new CustomerKeywordPositionSummary();
                    positionSummary.setPosition(position);
                    positionSummary.setCustomerKeywordUuid(customerKeywordUuid);
                    ckPositionSummaryListForInsert.add(positionSummary);
                }
            }
            if (CollectionUtils.isNotEmpty(ckPositionSummaryListForUpdate)) {
                customerKeywordPositionSummaryDao.updatePositionSummary(ckPositionSummaryListForUpdate);
            }
            if (CollectionUtils.isNotEmpty(ckPositionSummaryListForInsert)) {
                customerKeywordPositionSummaryDao.addPositionSummary(ckPositionSummaryListForInsert);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    public void deletePositionSummaryFromAWeekAgo() {
        customerKeywordPositionSummaryDao.deletePositionSummaryFromAWeekAgo();
    }

    public List<Integer> searchOneWeekPositionByCustomerUuid(Long customerKeywordUuid) {
        return customerKeywordPositionSummaryDao.searchOneWeekPositionByCustomerUuid(customerKeywordUuid);
    }
}
