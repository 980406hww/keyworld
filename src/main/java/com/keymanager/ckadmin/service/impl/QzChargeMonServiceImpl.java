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
import org.springframework.stereotype.Service;

@Service("qzChargeMonService2")
public class QzChargeMonServiceImpl extends ServiceImpl<QzChargeMonDao, QzChargeMon> implements QzChargeMonService {

    @Resource(name = "qzChargeMonDao2")
    private QzChargeMonDao qzChargeMonDao;

    @Override
    public Map<String, Object> selectBySe(String searchEngines, String terminal, Integer num, Integer type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(type == 1 ? Calendar.MONTH : Calendar.DAY_OF_MONTH, -(--num));
        String pattern = "yyyy-MM";
        if (type == 2) {
            pattern += "-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Map<String, Object> condition = new HashMap<>(4);
        condition.put("operationDate", sdf.format(calendar.getTime()));
        condition.put("searchEngine", searchEngines);
        condition.put("terminal", terminal);
        if (type == 2) {
            condition.put("pattern", "%Y-%m-%d");
        } else {
            condition.put("pattern", "%Y-%m");
        }
        List<Map<String, Object>> maps = qzChargeMonDao.selectBySe(condition);
        if (null == maps || maps.isEmpty()) {
            return null;
        }
        List<String> date = new ArrayList<>(num);
        List<Long> oneData = new ArrayList<>(num);
        List<Long> twoData = new ArrayList<>(num);
        List<Long> threeData = new ArrayList<>(num);
        List<Long> fourData = new ArrayList<>(num);
        List<Long> fiveData = new ArrayList<>(num);
        String lastMonth = "";
        for (Map<String, Object> map : maps) {
            String month = (String) map.get("monthDate");
            if (!lastMonth.equals(month)) {
                date.add(month);
                oneData.add(0L);
                twoData.add(0L);
                threeData.add(0L);
                fourData.add(0L);
                fiveData.add(0L);
            }
            Integer operationType = (Integer) map.get("operationType");
            Long number = (Long) map.get("number");
            int index = date.size() - 1;
            switch (operationType) {
                case 2:
                    oneData.remove(index);
                    oneData.add(index, number);
                    break;
                case 1:
                    twoData.remove(index);
                    twoData.add(index, number);
                    break;
                case 0:
                    threeData.remove(index);
                    threeData.add(index, number);
                    break;
                case 3:
                    fourData.remove(index);
                    fourData.add(index, number);
                    break;
                case 4:
                    fiveData.remove(index);
                    fiveData.add(index, number);
                    break;
                default:
                    break;
            }
            lastMonth = month;
        }
        Map<String, Object> data = new HashMap<>(5);
        data.put("date", date);
        data.put("one", oneData);
        data.put("two", twoData);
        data.put("three", threeData);
        data.put("four", fourData);
        data.put("five", fiveData);
        return data;
    }
}
