package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.QZChargeLogDao;
import com.keymanager.monitoring.entity.QZChargeLog;
import com.keymanager.monitoring.entity.QZChargeRule;
import com.keymanager.monitoring.entity.QZOperationType;
import com.keymanager.monitoring.vo.QZChargeInfoVO;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj01 on 2017/7/17.
 */
@Service
public class QZChargeLogService extends ServiceImpl<QZChargeLogDao, QZChargeLog> {

    @Autowired
    private QZChargeLogDao qzChargeLogDao;

    @Autowired
    private QZSettingService qzSettingService;

    @Autowired
    private QZOperationTypeService qzOperationTypeService;

    @Autowired
    private QZChargeRuleService qzChargeRuleService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //通过QZSettingUuid去
    public List<QZChargeInfoVO> getQZChargeLog(Long uuid) {
        List<QZOperationType> qzOperationTypes = qzOperationTypeService.searchQZOperationTypesByQZSettingUuid(uuid);
        //通过operationTypeUuid主键去查询规则表（多条数据）
        List<QZChargeInfoVO> qzChargeInfoVOs = new ArrayList<QZChargeInfoVO>();

        for (QZOperationType qzOperationType : qzOperationTypes) {
            //判断是否达标
            QZChargeInfoVO qzChargeInfoVO = new QZChargeInfoVO();
            if (null == qzOperationType.getReachTargetDate() && null == qzOperationType
                .getNextChargeDate()) {
                qzChargeInfoVO.setPcReceivableAmount("0");
            }
            qzChargeInfoVO
                .setPcCurrentKeywordCount(qzOperationType.getCurrentKeywordCount().toString());
            qzChargeInfoVO.setPcQzOperationTypeUuid(qzOperationType.getUuid().toString());
            qzChargeInfoVO.setPcInitialKeywordCount(
                qzOperationType.getInitialKeywordCount().toString());//初始词量
            //计划缴费日期
            qzChargeInfoVO.setPcPlanChargeDate(qzOperationType.getNextChargeDate() == null ? null
                : sdf.format(qzOperationType.getNextChargeDate()));

            List<QZChargeRule> qzChargeRules = qzChargeRuleService
                .searchQZChargeRuleByqzOperationTypeUuids(qzOperationType.getUuid());
            for (QZChargeRule qzChargeRule : qzChargeRules) {
                //如果当前值在规则中的起始值-----终止值  区间内 或者比最后一条规则的初始值还大
                if (qzOperationType.getCurrentKeywordCount() >= qzChargeRule.getStartKeywordCount()
                    &&
                    (null == qzChargeRule.getEndKeywordCount()
                        || qzOperationType.getCurrentKeywordCount() <= qzChargeRule
                        .getEndKeywordCount())) {
                    qzChargeInfoVO.setPcReceivableAmount(qzChargeRule.getAmount().toString());
                    break;
                }
            }
            qzChargeInfoVOs.add(qzChargeInfoVO);
        }

        return qzChargeInfoVOs;
    }

    //保存收费流水表----->可能是多个对象
    public String saveQZChargeLog(String chargeData,HttpServletRequest request) {
        Long qzOperationTypeUuidPC = null;
        Date planChargeDatePC = null;
        Date actualChargeDatePC = null;
        BigDecimal receivableAmountPC = null;
        BigDecimal actualAmountPC = null;

        Long qzOperationTypeUuidPhone = null;
        Date planChargeDatePhone = null;
        Date actualChargeDatePhone = null;
        BigDecimal receivableAmountPhone = null;
        BigDecimal actualAmountPhone = null;
        String userID = null;
        QZChargeLog qzChargeLogPC = null;
        QZChargeLog qzChargeLogPhone = null;

        JSONObject jsonStr = JSONObject.fromObject(chargeData);
        HttpSession user = request.getSession();
        userID = user.getAttribute("username").toString();
        //转换异常
        try {
            if (null != jsonStr.get("fQzOperationTypeUuidPC") && jsonStr.size() >= 3) {
                qzOperationTypeUuidPC = Long.valueOf(jsonStr.get("fQzOperationTypeUuidPC").toString());
                if(!StringUtils.isBlank(jsonStr.get("fPlanChargeDatePC").toString())){
                    planChargeDatePC = sdf.parse(jsonStr.get("fPlanChargeDatePC").toString());//计划收费日期
                }
                if(!StringUtils.isBlank(jsonStr.get("fReceivableAmountPC").toString())){
                    receivableAmountPC = new BigDecimal(jsonStr.get("fReceivableAmountPC").toString());
                }
                actualChargeDatePC = sdf.parse(jsonStr.get("fActualChargeDatePC").toString());//实际收费日期
                actualAmountPC = new BigDecimal(jsonStr.get("fActualAmountPC").toString());//实际收费金额
                qzChargeLogPC = new QZChargeLog(qzOperationTypeUuidPC, planChargeDatePC, actualChargeDatePC, receivableAmountPC, actualAmountPC, userID);
            }
            if (null != jsonStr.get("fQzOperationTypeUuidPhone") && jsonStr.size() >= 3) {
                qzOperationTypeUuidPhone = Long.valueOf(jsonStr.get("fQzOperationTypeUuidPhone").toString());
                if(!StringUtils.isBlank(jsonStr.get("fPlanChargeDatePhone").toString())){
                    planChargeDatePhone = sdf.parse(jsonStr.get("fPlanChargeDatePhone").toString());//计划收费日期
                }
                if(!StringUtils.isBlank(jsonStr.get("fReceivableAmountPhone").toString())){
                    receivableAmountPhone = new BigDecimal(jsonStr.get("fReceivableAmountPhone").toString());
                }
                actualChargeDatePhone = sdf.parse(jsonStr.get("fActualChargeDatePhone").toString());//实际收费日期
                actualAmountPhone = new BigDecimal(jsonStr.get("fActualAmountPhone").toString());//实际收费金额
                qzChargeLogPhone = new QZChargeLog(qzOperationTypeUuidPhone, planChargeDatePhone, actualChargeDatePhone, receivableAmountPhone, actualAmountPhone, userID);
            }
            //添加到数据库中
            saveQZChargeLog(qzChargeLogPC, qzChargeLogPhone,jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"status\":true}";
    }

    public void saveQZChargeLog(QZChargeLog qzChargeLogPC, QZChargeLog qzChargeLogPhone,JSONObject jsonStr) {
        if (null != qzChargeLogPC && null != qzChargeLogPC.getQzOperationTypeUuid()) {
            qzChargeLogDao.insert(qzChargeLogPC);
            //插入时 更新 下一次的收费日期
            QZOperationType qzOperationType = qzOperationTypeService.selectById(qzChargeLogPC.getQzOperationTypeUuid());
            try {
                qzOperationType.setNextChargeDate(sdf.parse(jsonStr.get("fNextChargeDatePC").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            qzOperationTypeService.updateById(qzOperationType);
        }
        if (null != qzChargeLogPhone && null != qzChargeLogPhone.getQzOperationTypeUuid()) {
            qzChargeLogDao.insert(qzChargeLogPhone);
            QZOperationType qzOperationType = qzOperationTypeService.selectById(qzChargeLogPhone.getQzOperationTypeUuid());
            try {
                qzOperationType.setNextChargeDate(sdf.parse(jsonStr.get("fNextChargeDatePhone").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            qzOperationTypeService.updateById(qzOperationType);
        }
    }
    
    //收费记录
    public String chargesList(Long uuid){
        List<QZChargeLog> qzChargeLogs  = qzChargeLogDao.chargesList(uuid);
        if(qzChargeLogs.size()==0){
            return null;
        }
        JSONArray jsonQZChargeLogs = JSONArray.fromObject(qzChargeLogs);
        return jsonQZChargeLogs.toString();
    }
}
