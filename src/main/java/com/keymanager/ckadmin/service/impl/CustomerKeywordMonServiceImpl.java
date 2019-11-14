package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.CustomerKeywordMonDao;
import com.keymanager.ckadmin.entity.CustomerKeywordMon;
import com.keymanager.ckadmin.service.CustomerKeywordMonService;
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
        List<Map<String, Object>> maps = customerKeywordMonDao.getCustomerKeywordMonData(condition);
        if (CollectionUtils.isNotEmpty(maps)) {
            List<String> dates = new ArrayList<>();
            List<Long> topThreeData = new ArrayList<>();
            List<Long> topFiveData = new ArrayList<>();
            List<Long> topTenData = new ArrayList<>();
            List<Long> topFifthData = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                dates.add((String) map.get("date"));
                topThreeData.add((Long) map.get("topThreeCount"));
                topFiveData.add((Long) map.get("topFiveCount"));
                topTenData.add((Long) map.get("topTenCount"));
                topFifthData.add((Long) map.get("topFifthCount"));
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
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(date);
        String yesterday = sdf.format(calendar.getTime());
        condition.put("yesterday", yesterday);
        condition.put("day", day);
        Page<Map<String, Object>> page = new Page<>();
        Integer total = customerKeywordMonDao.selectCountByCondition(condition);
        if (null == total || total == 0) {
            return null;
        }
        calendar.add(Calendar.DATE, -6);
        String sevenDayAgo = sdf.format(calendar.getTime());
        condition.put("sevenDayAgo", sevenDayAgo);
        condition.put("start", (cur - 1) * limit);
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
        String gtDate = f.format(new Date());
        condition.put("ltDate", ltDate);
        condition.put("gtDate", gtDate);
    }
}
