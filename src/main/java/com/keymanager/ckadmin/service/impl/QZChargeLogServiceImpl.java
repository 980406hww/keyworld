package com.keymanager.ckadmin.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.ckadmin.dao.QZChargeLogDao;
import com.keymanager.ckadmin.entity.QZChargeLog;
import com.keymanager.ckadmin.entity.QZChargeRule;
import com.keymanager.ckadmin.entity.QZOperationType;
import com.keymanager.ckadmin.service.QZChargeLogService;
import com.keymanager.ckadmin.service.QZChargeRuleService;
import com.keymanager.ckadmin.service.QZOperationTypeService;
import com.keymanager.ckadmin.vo.QZChargeInfoVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("qzChargeLogService2")
public class QZChargeLogServiceImpl extends ServiceImpl<QZChargeLogDao, QZChargeLog> implements
    QZChargeLogService {

    @Resource(name = "qzChargeLogDao2")
    private QZChargeLogDao qzChargeLogDao;
    @Resource(name = "qzOperationTypeService2")
    private QZOperationTypeService qzOperationTypeService;
    @Resource(name = "qzChargeRuleService2")
    private QZChargeRuleService qzChargeRuleService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
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
            if (qzOperationType.getInitialKeywordCount() != null) {
                qzChargeInfoVO.setInitialKeywordCount(
                    qzOperationType.getInitialKeywordCount().toString());//初始词量
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

    @Override
    public void saveQZChargeLog(List<QZChargeLog> qzChargeLogs, String loginName) {
        for (QZChargeLog qzChargeLog : qzChargeLogs) {
            if (null != qzChargeLog && null != qzChargeLog.getQzOperationTypeUuid()) {
                qzChargeLog.setLoginName(loginName);
                qzChargeLogDao.insert(qzChargeLog);
                //插入时 更新 下一次的收费日期
                QZOperationType qzOperationType = qzOperationTypeService
                    .selectById(qzChargeLog.getQzOperationTypeUuid());
                qzOperationType.setNextChargeDate(qzChargeLog.getNextChargeDate());
                qzOperationTypeService.updateById(qzOperationType);
            }
        }
    }

    @Override
    public List<QZChargeLog> chargesList(Long uuid) {
        return qzChargeLogDao.chargesList(uuid);
    }
}
