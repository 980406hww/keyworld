package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.CustomerKeywordMonDao;
import com.keymanager.ckadmin.entity.CustomerKeywordMon;
import com.keymanager.ckadmin.service.CustomerKeywordMonService;
import com.keymanager.ckadmin.vo.CustomerKeywordMonCountVO;
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

@Service("customerKeywordMonService2")
public class CustomerKeywordMonServiceImpl extends ServiceImpl<CustomerKeywordMonDao, CustomerKeywordMon> implements CustomerKeywordMonService {

    @Resource(name = "customerKeywordMonDao2")
    private CustomerKeywordMonDao customerKeywordMonDao;

    @Override
    public Map<String, Object> getCustomerKeywordMonData(Map<String, Object> condition) {
        handleCondition(condition);
        List<CustomerKeywordMonCountVO> customerKeywordMonCountVos = customerKeywordMonDao.getCustomerKeywordMonData(condition);
        if (CollectionUtils.isNotEmpty(customerKeywordMonCountVos)) {
            List<String> dates = new ArrayList<>();
            List<Long> topThreeData = new ArrayList<>();
            List<Long> topFiveData = new ArrayList<>();
            List<Long> topTenData = new ArrayList<>();
            List<Long> topFifthData = new ArrayList<>();
            for (CustomerKeywordMonCountVO customerKeywordMonCountVo : customerKeywordMonCountVos) {
                dates.add(customerKeywordMonCountVo.getDate());
                topThreeData.add(customerKeywordMonCountVo.getTopThreeCount());
                topFiveData.add(customerKeywordMonCountVo.getTopFiveCount());
                topTenData.add(customerKeywordMonCountVo.getTopTenCount());
                topFifthData.add(customerKeywordMonCountVo.getTopFifthCount());
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
    public Page<Map<String, Object>> selectTableByCondition(Map<String, Object> condition) {
        Integer cur = (Integer) condition.get("page");
        Integer limit = (Integer) condition.get("limit");
        if (null == cur || null == limit || cur.equals(0) || limit.equals(0)) {
            return null;
        }
        Page<Map<String, Object>> page = new Page<>();
        Integer total = customerKeywordMonDao.selectCountByCondition(condition);
        if (null == total || total == 0) {
            return null;
        }
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        condition.put("today", sdf.format(date) + " 23:59:59");
        calendar.add(Calendar.DATE, -14);
        String fifteenDayAgo = sdf.format(calendar.getTime());
        condition.put("fifteenDayAgo", fifteenDayAgo);
        condition.put("start", (cur - 1) * limit);
        page.setSearchCount(false);
        page.setTotal(total);
        List<Map<String, Object>> data = customerKeywordMonDao.selectTableByCondition(condition);
        page.setRecords(data);
        return page;
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
