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
import org.springframework.stereotype.Service;

@Service("customerKeywordMonService2")
public class CustomerKeywordMonServiceImpl extends ServiceImpl<CustomerKeywordMonDao, CustomerKeywordMon> implements CustomerKeywordMonService {

    @Resource(name = "customerKeywordMonDao2")
    private CustomerKeywordMonDao customerKeywordMonDao;

    @Override
    public Map<String, Object> getCustomerKeywordMonData(Map<String, Object> condition, int num, int type) {
        handleCondition(condition, num, type);
        List<Map<String, Object>> maps = customerKeywordMonDao.getCustomerKeywordMonData(condition);
        if (null == maps || maps.isEmpty()) {
            return null;
        }
        List<String> dates = new ArrayList<>();
        List<Long> topThreeData = new ArrayList<>();
        List<Long> topFiveData = new ArrayList<>();
        List<Long> topTenData = new ArrayList<>();
        List<Long> topFifthData = new ArrayList<>();
        String lastDate = "";
        for (Map<String, Object> map : maps) {
            String date = (String) map.get("date");
            if (!lastDate.equals(date)) {
                dates.add(date);
                topThreeData.add(0L);
                topFiveData.add(0L);
                topTenData.add(0L);
                topFifthData.add(0L);
            }
            Integer position = (Integer) map.get("position");
            Long number = (Long) map.get("number");
            int index = dates.size() - 1;
            long count;
            if (position <= 3) {
                count = topThreeData.get(index);
                topThreeData.remove(index);
                topThreeData.add(index, number + count);
            }
            if (position <= 5) {
                count = topFiveData.get(index);
                topFiveData.remove(index);
                topFiveData.add(index, number + count);
            }
            if (position <= 10) {
                count = topTenData.get(index);
                topTenData.remove(index);
                topTenData.add(index, number + count);
            }
            if (position <= 50) {
                count = topFifthData.get(index);
                topFifthData.remove(index);
                topFifthData.add(index, number + count);
            }
            lastDate = date;
        }
        Map<String, Object> data = new HashMap<>(5);
        data.put("date", dates);
        data.put("topThreeData", topThreeData);
        data.put("topFiveData", topFiveData);
        data.put("topTenData", topTenData);
        data.put("topFifthData", topFifthData);
        return data;
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

    private void handleCondition(Map<String, Object> condition, int num, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(type == 1 ? Calendar.MONTH : Calendar.DAY_OF_MONTH, type == 1 ? -(--num) : -num);
        String pattern = type == 2 ? "yyyy-MM-dd" : "yyyy-MM";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        condition.put("date", sdf.format(calendar.getTime()));
        condition.put("pattern", (type == 2 ? "%Y-%m-%d" : "%Y-%m"));
    }
}
