package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.QZChargeMonCriteria;
import com.keymanager.ckadmin.dao.QzChargeMonDao;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.entity.QzChargeMon;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.QzChargeMonService;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.ckadmin.vo.QZChargeMonCountVO;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @Override
    public Map<String, Object> getQZChargeMonData(String searchEngines, String terminal, String time, String loginName) {
        // 前台时间控件传的�
        String[] times = time.replaceAll(" ", "").split("~");
        Timestamp ltDate = Utils.parseDate(times[0], "yyyy-MM");
        Timestamp gtDate = Utils.parseDate(times[1], "yyyy-MM");
        Date firstDayOfMonth = Utils.getFirstDayOfMonth(ltDate);
        Date lastDayOfMonth = Utils.getLastDayOfMonth(gtDate);
        Map<String, Object> condition = new HashMap<>(6);
        condition.put("ltDate", firstDayOfMonth);
        condition.put("gtDate", lastDayOfMonth);
        condition.put("searchEngine", searchEngines);
        condition.put("terminal", terminal);
        condition.put("loginName", loginName);

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

    @Override
    public void deleteByQZSettingUuid(Long qzSettingUuid) {
        qzChargeMonDao.deleteByQZSettingUuid(qzSettingUuid);
    }

    @Override
    public void insertQzChargeMonInfo(QZSetting qzSetting, int operationType, String terminalType, String loginName) {
        QzChargeMon qzChargeMon = new QzChargeMon();
        qzChargeMon.setOperationType(operationType);
        qzChargeMon.setQzDomain(qzSetting.getDomain());
        qzChargeMon.setQzCustomer(customerService.selectById(qzSetting.getCustomerUuid()).getContactPerson());
        qzChargeMon.setSearchEngine(qzSetting.getSearchEngine());
        qzChargeMon.setTerminalType(terminalType);
        qzChargeMon.setOperationUser(loginName);
        qzChargeMon.setQzSettingUuid(qzSetting.getUuid());
        qzChargeMonDao.insert(qzChargeMon);
        if (operationType == 4) {
            qzChargeMonDao.deleteByQZSettingUuid(qzChargeMon.getQzSettingUuid());
        }
    }
}
