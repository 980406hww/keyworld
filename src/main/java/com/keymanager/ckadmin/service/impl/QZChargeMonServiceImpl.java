package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.QzChargeMonDao;
import com.keymanager.ckadmin.entity.QzChargeMon;
import com.keymanager.ckadmin.service.QzChargeMonService;
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

@Service("qzChargeMonService2")
public class QZChargeMonServiceImpl extends ServiceImpl<QzChargeMonDao, QzChargeMon> implements QzChargeMonService {

    @Resource(name = "qzChargeMonDao2")
    private QzChargeMonDao qzChargeMonDao;

    @Override
    public Map<String, Object> getQZChargeMonData(String searchEngines, String terminal, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, Integer.parseInt(time));
        Map<String, Object> condition = new HashMap<>(4);
        condition.put("ltDate", sdf.format(calendar.getTime()));
        condition.put("gtDate", sdf.format(new Date()));
        condition.put("searchEngine", searchEngines);
        condition.put("terminal", terminal);
        List<Map<String, Object>> maps = qzChargeMonDao.getQZChargeMonData(condition);
        if (CollectionUtils.isNotEmpty(maps)) {
            List<String> date = new ArrayList<>();
            List<Long> addQzData = new ArrayList<>();
            List<Long> renewalQzData = new ArrayList<>();
            List<Long> stopQzData = new ArrayList<>();
            List<Long> obtainedQzData = new ArrayList<>();
            List<Long> deleteQzData = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                date.add((String) map.get("monthDate"));
                addQzData.add((Long) map.get("addQzData"));
                renewalQzData.add((Long) map.get("renewalQzData"));
                stopQzData.add((Long) map.get("stopQzData"));
                obtainedQzData.add((Long) map.get("obtainedQzData"));
                deleteQzData.add((Long) map.get("monthDate"));
            }
            Map<String, Object> data = new HashMap<>(6);
            data.put("date", date);
            data.put("addQzDataCount", addQzData);
            data.put("renewalQzDataCount", renewalQzData);
            data.put("stopQzDataCount", stopQzData);
            data.put("obtainedQzDataCount", obtainedQzData);
            data.put("deleteQzDataCount", deleteQzData);
            return data;
        }
        return null;
    }
}
