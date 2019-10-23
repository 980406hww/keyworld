package com.keymanager.ckadmin.controller.internal.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.ckadmin.annotation.QzStatusMon;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.entity.QZChargeLog;
import com.keymanager.ckadmin.service.QZChargeLogService;
import com.keymanager.ckadmin.service.QZSettingService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/qzchargelog")
public class QZChargeLogController {

    @Resource(name = "qzChargeLogService2")
    private QZChargeLogService qzChargeLogService;

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    //点击收费按钮触发的方法
    @RequestMapping(value = "/getQZChargeLogs/{uuid}", method = RequestMethod.GET)
    public ResultBean getQZChargeLogs(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            resultBean.setData(qzChargeLogService.getQZChargeLog(uuid));
        } catch (Exception e) {
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    //插入一条收费流水表
    @RequiresPermissions("/internal/qzchargelog/save")
    @RequestMapping(value = "/saveQZChargeLogs", method = RequestMethod.POST)
    @QzStatusMon(type = 2)
    public ResultBean saveQZChargeLogs(@RequestBody Map<String, Object> data, HttpSession session) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            Map msg = (Map) data.get("msg");
            Long uuid = Long.parseLong((String) msg.get("uuid"));
            int renewalStatus = Integer.parseInt((String) msg.get("renewalStatus"));
            qzSettingService.updRenewalStatus(uuid, renewalStatus);
            if (renewalStatus == 2) {
                String loginName = (String) session.getAttribute("username");
                List<QZChargeLog> qzChargeLogs = new ObjectMapper()
                    .convertValue(data.get("data"), new TypeReference<List<QZChargeLog>>() {
                    });
                qzChargeLogService.saveQZChargeLog(qzChargeLogs, loginName);
            }
        } catch (Exception e) {
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
}
