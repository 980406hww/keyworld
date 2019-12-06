package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.CustomerKeywordPositionSummaryDao;
import com.keymanager.ckadmin.entity.CustomerKeywordPositionSummary;
import com.keymanager.ckadmin.service.CustomerKeywordPositionSummaryService;
import com.keymanager.ckadmin.vo.CustomerKeywordPositionSummaryCountVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @author shunshikj40
 */
@Service("ckPositionSummaryService2")
public class CustomerKeywordPositionSummaryServiceImpl extends ServiceImpl<CustomerKeywordPositionSummaryDao, CustomerKeywordPositionSummary> implements
    CustomerKeywordPositionSummaryService {

    @Resource(name = "customerKeywordPositionSummaryDao2")
    private CustomerKeywordPositionSummaryDao customerKeywordPositionSummaryDao;

    @Override
    public Map<String, Object> getCustomerKeywordPositionSummaryData(Map<String, Object> condition) {
        handleCondition(condition);
        List<CustomerKeywordPositionSummaryCountVO> customerKeywordPositionSummaryCountVos = customerKeywordPositionSummaryDao.getCustomerKeywordPositionSummaryData(condition);
        if (CollectionUtils.isNotEmpty(customerKeywordPositionSummaryCountVos)) {
            List<String> dates = new ArrayList<>();
            List<Long> topThreeData = new ArrayList<>();
            List<Long> topFiveData = new ArrayList<>();
            List<Long> topTenData = new ArrayList<>();
            List<Long> topFifthData = new ArrayList<>();
            for (CustomerKeywordPositionSummaryCountVO customerKeywordPositionSummaryCountVo : customerKeywordPositionSummaryCountVos) {
                dates.add(customerKeywordPositionSummaryCountVo.getDate());
                topThreeData.add(customerKeywordPositionSummaryCountVo.getTopThreeCount());
                topFiveData.add(customerKeywordPositionSummaryCountVo.getTopFiveCount());
                topTenData.add(customerKeywordPositionSummaryCountVo.getTopTenCount());
                topFifthData.add(customerKeywordPositionSummaryCountVo.getTopFifthCount());
            }
            Map<String, Object> data = new HashMap<>(5);
            data.put("date", dates);
            data.put("topThreeData", topThreeData);
            data.put("topFiveData", topFiveData);
            data.put("topTenData", topTenData);
            data.put("topFifthData", topFifthData);
            return data;
        }
        return null;
    }

    @Override
    public Page<Map<String, Object>> getCKPositionSummaryDataInitTable(Map<String, Object> condition) {
        Page<Map<String, Object>> page = new Page<>();
        Integer cur = (Integer) condition.get("page");
        Integer limit = (Integer) condition.get("limit");
        if (null == cur || null == limit || cur.equals(0) || limit.equals(0)) {
            return null;
        }
        int totalCount = customerKeywordPositionSummaryDao.getCKPositionSummaryDataInitCount(condition);
        if (totalCount > 0) {
            condition.put("start", (cur - 1) * limit);
            condition.put("limit", limit);
            page.setRecords(customerKeywordPositionSummaryDao.getCKPositionSummaryDataInitTable(condition));
        }
        page.setTotal(totalCount);
        return page;
    }

    @Override
    public Map<String, Object> getOneCKPositionSummaryData(Long uuid) {
        Map<String, Object> condition = new HashMap<>(3);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        condition.put("today", sdf.format(date) + " 23:59:59");
        calendar.add(Calendar.DATE, -7);
        String oneWeekAgo = sdf.format(calendar.getTime());
        condition.put("oneWeekAgo", oneWeekAgo);
        condition.put("uuid", uuid);
        return customerKeywordPositionSummaryDao.getOneCKPositionSummaryData(condition);
    }

    @Override
    public void savePositionSummary(Long customerKeywordUuid, String searchEngine, String terminalType, Long customerUuid, String type, int position) {
        try {
            CustomerKeywordPositionSummary positionSummary = customerKeywordPositionSummaryDao.getTodayPositionSummary(customerKeywordUuid);
            if (positionSummary != null) {
                boolean updFlag = (positionSummary.getPosition() == null || positionSummary.getPosition() <= 0) || (position > 0 && positionSummary.getPosition() > position);
                if (updFlag) {
                    positionSummary.setPosition(position);
                    customerKeywordPositionSummaryDao.updateById(positionSummary);
                }
            } else {
                positionSummary = new CustomerKeywordPositionSummary();
                positionSummary.setPosition(position);
                positionSummary.setCustomerKeywordUuid(customerKeywordUuid);
                positionSummary.setSearchEngine(searchEngine);
                positionSummary.setTerminalType(terminalType);
                positionSummary.setCustomerUuid(customerUuid);
                positionSummary.setType(type);
                customerKeywordPositionSummaryDao.addPositionSummary(positionSummary);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleCondition(Map<String, Object> condition) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, Integer.parseInt((String) condition.get("time")));
        String ltDate = f.format(calendar.getTime());
        condition.put("ltDate", ltDate);
        condition.put("gtDate", f.format(new Date()) + " 23:59:59");
    }
}
