package com.keymanager.ckadmin.controller.internal.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.QZChargeLog;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.entity.QzChargeMon;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.QZChargeLogService;
import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.ckadmin.service.QzChargeMonService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/qzchargelog")
public class QZChargeLogController {

    private static Logger logger = LoggerFactory.getLogger(QZChargeLogController.class);

    @Resource(name = "qzChargeLogService2")
    private QZChargeLogService qzChargeLogService;

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    @Resource(name = "qzChargeMonService2")
    private QzChargeMonService qzChargeMonService;

    @Resource(name = "customerService2")
    private CustomerService customerService;

    //点击收费按钮触发的方法
    @RequestMapping(value = "/getQZChargeLogs/{uuid}", method = RequestMethod.GET)
    public ResultBean getQZChargeLogs(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            resultBean.setData(qzChargeLogService.getQZChargeLog(uuid));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    //插入一条收费流水表
    @RequiresPermissions("/internal/qzchargelog/save")
    @RequestMapping(value = "/saveQZChargeLogs", method = RequestMethod.POST)
    public ResultBean saveQZChargeLogs(@RequestBody Map<String, Object> data, HttpSession session) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            Map msg = (Map) data.get("msg");
            Long uuid = Long.parseLong((String) msg.get("uuid"));
            int renewalStatus = Integer.parseInt((String) msg.get("renewalStatus"));
            qzSettingService.updRenewalStatus(uuid, renewalStatus);
            List<QZChargeLog> qzChargeLogs = new ObjectMapper()
                .convertValue(data.get("data"), new TypeReference<List<QZChargeLog>>() {
                });
            if (renewalStatus == 1) {
                String loginName = (String) session.getAttribute("username");
                qzChargeLogService.saveQZChargeLog(qzChargeLogs, loginName);
            }
            //日志监控
            saveQzChargeMon(uuid, renewalStatus, (String) session.getAttribute("name"));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    //查看收费记录
    @RequiresPermissions("/internal/qzchargelog/chargesList")
    @RequestMapping(value = "/qzChargesLogsList/{uuid}", method = RequestMethod.GET)
    public ResultBean qzChargesLogsList(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        List<QZChargeLog> logs;
        try {
            logs = qzChargeLogService.chargesList(uuid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        if (null == logs || logs.isEmpty()) {
            resultBean.setCode(300);
        } else {
            resultBean.setData(logs);
        }
        return resultBean;
    }

    private void saveQzChargeMon(Long uuid, Integer operationType, String userName) {
        try {
            QZSetting qzSetting = qzSettingService.selectById(uuid);
            if (qzSetting.getRenewalStatus() == 4) {
                return;
            }
            Customer customer = customerService.selectById(qzSetting.getCustomerUuid());
            String terminal = null;
            if (null == qzSetting.getPcGroup() || "".equals(qzSetting.getPcGroup())) {
                if (null != qzSetting.getPhoneGroup() && !"".equals(qzSetting.getPhoneGroup())) {
                    terminal = "Phone";
                }
            } else {
                terminal = "PC";
                if (null != qzSetting.getPhoneGroup() && !"".equals(qzSetting.getPhoneGroup())) {
                    terminal += ",Phone";
                }
            }
            QzChargeMon qzChargeMon = new QzChargeMon();
            qzChargeMon.setOperationType(operationType);
            qzChargeMon.setQzDomain(qzSetting.getDomain());
            qzChargeMon.setQzCustomer(customer.getContactPerson());
            qzChargeMon.setSearchEngine(qzSetting.getSearchEngine());
            qzChargeMon.setTerminalType(terminal);
            qzChargeMon.setOperationUser(userName);
            qzChargeMon.setQzzSettingUuid(uuid);
            qzChargeMonService.insert(qzChargeMon);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
