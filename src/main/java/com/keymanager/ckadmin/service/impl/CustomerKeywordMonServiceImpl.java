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
    public Map<String, Object> selectByCondition(Map<String, Object> condition, int num, int type) {
        handleCondition(condition, num, type);
        List<Map<String, Object>> maps = customerKeywordMonDao.selectByCondition(condition);
        if (null == maps || maps.isEmpty()) {
            return null;
        }
        List<String> dates = new ArrayList<>();
        List<Long> oneData = new ArrayList<>();
        List<Long> twoData = new ArrayList<>();
        List<Long> threeData = new ArrayList<>();
        List<Long> fourData = new ArrayList<>();
        String lastDate = "";
        for (Map<String, Object> map : maps) {
            String date = (String) map.get("date");
            if (!lastDate.equals(date)) {
                dates.add(date);
                oneData.add(0L);
                twoData.add(0L);
                threeData.add(0L);
                fourData.add(0L);
            }
            Integer position = (Integer) map.get("position");
            Long number = (Long) map.get("number");
            int index = dates.size() - 1;
            long count;
            if (position <= 3) {
                count = oneData.get(index);
                oneData.remove(index);
                oneData.add(index, number + count);
            }
            if (position <= 5) {
                count = twoData.get(index);
                twoData.remove(index);
                twoData.add(index, number + count);
            }
            if (position <= 10) {
                count = threeData.get(index);
                threeData.remove(index);
                threeData.add(index, number + count);
            }
            if (position <= 50) {
                count = fourData.get(index);
                fourData.remove(index);
                fourData.add(index, number + count);
            }
            lastDate = date;
        }
        Map<String, Object> data = new HashMap<>(5);
        data.put("date", dates);
        data.put("one", oneData);
        data.put("two", twoData);
        data.put("three", threeData);
        data.put("four", fourData);
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
        if (null == total || !(total > 0)) {
            return null;
        }
        calendar.add(Calendar.DATE, -6);
        String sevenDayAgo = sdf.format(calendar.getTime());
        condition.put("sevenDayAgo", sevenDayAgo);
        condition.put("start", (cur - 1) * limit);
        page.setTotal(total);
        List<Map<String, Object>> data = customerKeywordMonDao.selectTableByCondition(condition);
//        for (Map<String, Object> map : data) {
//            if (null == map.get("hData") || null == map.get("hDate")) {
//                continue;
//            }
//            String[] positions = ((String) map.get("hData")).split(",");
//            String[] dates = ((String) map.get("hDate")).split(",");
//            map.put("positions", positions);
//            map.put("dates", dates);
//        }
        page.setRecords(data);
        return page;
    }

    private void handleCondition(Map<String, Object> condition, int num, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(type == 1 ? Calendar.MONTH : Calendar.DAY_OF_MONTH, type == 1 ? -(--num) : -num);
        String pattern = "yyyy-MM";
        if (type == 2) {
            pattern += "-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        condition.put("date", sdf.format(calendar.getTime()));
        if (type == 2) {
            condition.put("pattern", "%Y-%m-%d");
        } else {
            condition.put("pattern", "%Y-%m");
        }
    }
}
