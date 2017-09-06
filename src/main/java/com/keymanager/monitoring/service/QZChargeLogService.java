package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.QZChargeLogDao;
import com.keymanager.monitoring.entity.QZChargeLog;
import com.keymanager.monitoring.entity.QZChargeRule;
import com.keymanager.monitoring.entity.QZOperationType;
import com.keymanager.monitoring.vo.QZChargeInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
        List<QZOperationType> qzOperationTypes = qzOperationTypeService
            .searchQZOperationTypesByQZSettingUuid(uuid);
        //通过operationTypeUuid主键去查询规则表（多条数据）
        List<QZChargeInfoVO> qzChargeInfoVOs = new ArrayList<QZChargeInfoVO>();

        for (QZOperationType qzOperationType : qzOperationTypes) {
            //判断是否达标
            QZChargeInfoVO qzChargeInfoVO = new QZChargeInfoVO();
            if (null == qzOperationType.getReachTargetDate() && null == qzOperationType
                .getNextChargeDate()) {
                qzChargeInfoVO.setReceivableAmount("0");
            }
            qzChargeInfoVO.setOperationType(qzOperationType.getOperationType());
            qzChargeInfoVO.setQzOperationTypeUuid(qzOperationType.getUuid().toString());
            if(qzOperationType.getInitialKeywordCount() != null) {
                qzChargeInfoVO.setInitialKeywordCount(qzOperationType.getInitialKeywordCount().toString());//初始词量
            }
            //计划缴费日期
            qzChargeInfoVO.setPlanChargeDate(qzOperationType.getNextChargeDate() == null ? null
                : sdf.format(qzOperationType.getNextChargeDate()));

            List<QZChargeRule> qzChargeRules = qzChargeRuleService
                .searchQZChargeRuleByqzOperationTypeUuids(qzOperationType.getUuid());
            if (qzOperationType.getCurrentKeywordCount() != null) {
                qzChargeInfoVO
                    .setCurrentKeywordCount(qzOperationType.getCurrentKeywordCount().toString());
                for (QZChargeRule qzChargeRule : qzChargeRules) {
                    //如果当前值在规则中的起始值-----终止值  区间内 或者比最后一条规则的初始值还大
                    if (qzOperationType.getCurrentKeywordCount() >= qzChargeRule
                        .getStartKeywordCount()
                        &&
                        (null == qzChargeRule.getEndKeywordCount()
                            || qzOperationType.getCurrentKeywordCount() <= qzChargeRule
                            .getEndKeywordCount())) {
                        qzChargeInfoVO.setReceivableAmount(qzChargeRule.getAmount().toString());
                        break;
                    }
                }
            }
            qzChargeInfoVOs.add(qzChargeInfoVO);
        }
        return qzChargeInfoVOs;
    }

    //保存收费流水表----->可能是多个对象
    public String saveQZChargeLog(List<QZChargeLog> qzChargeLogs,HttpServletRequest request) {
        String userID = (String)request.getSession().getAttribute("username");
        for(QZChargeLog qzChargeLog : qzChargeLogs){
            if (null !=qzChargeLog  && null != qzChargeLog.getQzOperationTypeUuid()) {
                qzChargeLog.setUserID(userID);
                qzChargeLogDao.insert(qzChargeLog);
                //插入时 更新 下一次的收费日期
                QZOperationType qzOperationType = qzOperationTypeService.selectById(qzChargeLog.getQzOperationTypeUuid());
                qzOperationType.setNextChargeDate(qzChargeLog.getNextChargeDate());
                qzOperationTypeService.updateById(qzOperationType);
            }
        }
        return "{\"status\":true}";
    }

    //收费记录
    public List<QZChargeLog> chargesList(Long uuid){
        List<QZChargeLog> qzChargeLogs  = qzChargeLogDao.chargesList(uuid);
        if(qzChargeLogs.size()==0){
            return null;
        }
        return qzChargeLogs;
    }
}
