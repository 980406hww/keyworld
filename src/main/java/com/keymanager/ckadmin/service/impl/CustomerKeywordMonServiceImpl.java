package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.CustomerKeywordMonDao;
import com.keymanager.ckadmin.entity.CustomerKeywordMon;
import com.keymanager.ckadmin.service.CustomerKeywordMonService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service("customerKeywordMonService2")
public class CustomerKeywordMonServiceImpl extends ServiceImpl<CustomerKeywordMonDao, CustomerKeywordMon> implements CustomerKeywordMonService {

    @Override
    public Map<String, Object> selectByCondition(Map<String, Object> condition, int num, int type) {
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
        return null;
    }
}
