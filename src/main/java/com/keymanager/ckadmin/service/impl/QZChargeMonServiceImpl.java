package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.QZChargeMonCriteria;
import com.keymanager.ckadmin.dao.QzChargeMonDao;
import com.keymanager.ckadmin.entity.QzChargeMon;
import com.keymanager.ckadmin.service.QzChargeMonService;
import com.keymanager.ckadmin.vo.QZChargeMonCountVO;
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int countTime = Integer.parseInt(time);
        String dateFormat = "%Y-%m";
        switch (countTime) {
            case 1:
                calendar.add(Calendar.YEAR, -1);
                break;
            case 2:
                calendar.add(Calendar.YEAR, -3);
                break;
            default:
                calendar.add(Calendar.DATE, countTime);
                dateFormat = "%Y-%m-%d";
                break;
        }
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Map<String, Object> condition = new HashMap<>(4);
        condition.put("ltDate", sdf.format(calendar.getTime()));
        condition.put("gtDate", sdf.format(new Date()) + " 23:59:59");
        condition.put("searchEngine", searchEngines);
        condition.put("terminal", terminal);
        condition.put("dateFormat", dateFormat);
        List<QZChargeMonCountVO> chargeMonCountVos = qzChargeMonDao.getQZChargeMonData(condition);
        if (CollectionUtils.isNotEmpty(chargeMonCountVos)) {
            List<String> date = new ArrayList<>();
            List<Long> addQzData = new ArrayList<>();
            List<Long> renewalQzData = new ArrayList<>();
            List<Long> stopQzData = new ArrayList<>();
            List<Long> obtainedQzData = new ArrayList<>();
            List<Long> deleteQzData = new ArrayList<>();
            for (QZChargeMonCountVO qzChargeMonCountVo : chargeMonCountVos) {
                date.add(qzChargeMonCountVo.getMonthDate());
                addQzData.add(qzChargeMonCountVo.getAddQzData());
                renewalQzData.add(qzChargeMonCountVo.getRenewalQzData());
                stopQzData.add(qzChargeMonCountVo.getStopQzData());
                obtainedQzData.add(qzChargeMonCountVo.getObtainedQzData());
                deleteQzData.add(qzChargeMonCountVo.getDeleteQzData());
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

    @Override
    public List<QzChargeMon> getMonDateByCondition(Page<QzChargeMon> page, QZChargeMonCriteria criteria) {
        return qzChargeMonDao.getMonDateByCondition(page, criteria);
    }
}
