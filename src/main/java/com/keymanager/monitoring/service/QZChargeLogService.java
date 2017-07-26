package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.QZChargeLogDao;
import com.keymanager.monitoring.service.QZChargeRuleService;
import com.keymanager.monitoring.service.QZOperationTypeService;
import com.keymanager.monitoring.service.QZSettingService;
import com.keymanager.monitoring.entity.QZChargeLog;
import com.keymanager.monitoring.entity.QZChargeRule;
import com.keymanager.monitoring.entity.QZOperationType;
import com.keymanager.monitoring.entity.QZSetting;
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
    public String getQZChargeLog(Long uuid) {
        Map<String, String> resultMap = new HashMap<String, String>();
        String pcInitialKeywordCount = null;
        String pcCurrentKeywordCount = null; //当前词量(通过爬虫抓取)
        String pcReceivableAmount = "0";
        String pcPlanChargeDate = null;
        String pcQzOperationTypeUuid = null;
        //手机规则信息
        String phoneInitialKeywordCount = null;
        String phoneCurrentKeywordCount = "1";
        String phoneReceivableAmount = "0";
        String phonePlanChargeDate = null;
        String phoneQzOperationTypeUuid = null;

        List<QZOperationType> qzOperationTypes = qzOperationTypeService.searchQZOperationTypesByQZSettingUuid(uuid);
        //通过operationTypeUuid主键去查询规则表（多条数据）
        for (QZOperationType qzOperationType : qzOperationTypes) {
            if (qzOperationType.getOperationtype().equals("PC")) {
                //判断是否达标
                if (null == qzOperationType.getReachTargetDate() && null == qzOperationType.getNextChargeDate()) {
                    pcReceivableAmount = "0";
                }
                pcCurrentKeywordCount = qzOperationType.getCurrentKeywordCount().toString();
                pcQzOperationTypeUuid = qzOperationType.getUuid().toString();
                pcInitialKeywordCount = qzOperationType.getInitialKeywordCount().toString();//初始词量
                //计划缴费日期
                pcPlanChargeDate = qzOperationType.getNextChargeDate() == null ? sdf.format(new Date()) : sdf.format(qzOperationType.getNextChargeDate());

                List<QZChargeRule> qzChargeRules = qzChargeRuleService.searchQZChargeRuleByqzOperationTypeUuids(qzOperationType.getUuid());
                for (QZChargeRule qzChargeRule : qzChargeRules) {
                    //如果当前值在规则中的起始值-----终止值  区间内 或者比最后一条规则的初始值还大
                    if (Long.valueOf(pcCurrentKeywordCount) >= qzChargeRule.getStartKeywordCount() && null != qzChargeRule.getEndKeywordCount() && Long.valueOf(pcCurrentKeywordCount) <= qzChargeRule.getEndKeywordCount()) {
                        pcReceivableAmount = qzChargeRule.getAmount().toString();
                        break;
                    }
                    if (Long.valueOf(pcCurrentKeywordCount) >= qzChargeRules.get(qzChargeRules.size() - 1).getStartKeywordCount()) {
                        pcReceivableAmount = qzChargeRules.get(qzChargeRules.size() - 1).getAmount().toString();
                        break;
                    }
                }
            } else {
                //判断是否达标
                if (null == qzOperationType.getReachTargetDate() && null == qzOperationType.getNextChargeDate()) {
                    phoneReceivableAmount = "0";
                }
                phoneCurrentKeywordCount = qzOperationType.getCurrentKeywordCount().toString();
                phoneQzOperationTypeUuid = qzOperationType.getUuid().toString();
                phoneInitialKeywordCount = qzOperationType.getInitialKeywordCount().toString();//初始词量
                //计划缴费日期
                phonePlanChargeDate = qzOperationType.getNextChargeDate() == null ? sdf.format(new Date()) : sdf.format(qzOperationType.getNextChargeDate());
                List<QZChargeRule> qzChargeRules = qzChargeRuleService.searchQZChargeRuleByqzOperationTypeUuids(qzOperationType.getUuid());
                for (QZChargeRule qzChargeRule : qzChargeRules) {
                    if (Long.valueOf(phoneCurrentKeywordCount) >= qzChargeRule.getStartKeywordCount() && Long.valueOf(phoneCurrentKeywordCount) <= qzChargeRule.getEndKeywordCount()) {
                        phoneReceivableAmount = qzChargeRule.getAmount().toString();
                        break;
                    }
                    if (Long.valueOf(phoneCurrentKeywordCount) >= qzChargeRules.get(qzChargeRules.size() - 1).getStartKeywordCount()) {
                        phoneReceivableAmount = qzChargeRules.get(qzChargeRules.size() - 1).getAmount().toString();
                        break;
                    }
                }
            }
        }

        //向前台传一个map
        resultMap.put("pcInitialKeywordCount", pcInitialKeywordCount);//初始词量
        resultMap.put("pcCurrentKeywordCount", pcCurrentKeywordCount); //当前词量
        resultMap.put("pcReceivableAmount", pcReceivableAmount);//计划收费金额
        resultMap.put("pcPlanChargeDate", pcPlanChargeDate);//计划收费日期
        resultMap.put("pcQzOperationTypeUuid", pcQzOperationTypeUuid);//uuid

        resultMap.put("phoneInitialKeywordCount", phoneInitialKeywordCount);//初始词量
        resultMap.put("phoneCurrentKeywordCount", phoneCurrentKeywordCount); //当前词量
        resultMap.put("phoneReceivableAmount", phoneReceivableAmount);//计划收费金额
        resultMap.put("phonePlanChargeDate", phonePlanChargeDate);//计划收费日期
        resultMap.put("phoneQzOperationTypeUuid", phoneQzOperationTypeUuid);//uuid

        JSONObject jsonResultMap = JSONObject.fromObject(resultMap);
        return jsonResultMap.toString();
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
