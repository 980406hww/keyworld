package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.entity.QZChargeLog;
import com.keymanager.ckadmin.service.QZChargeLogService;
import java.util.List;
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
    public ResultBean saveQZChargeLogs(@RequestBody List<QZChargeLog> qzChargeLogs,
        HttpSession session) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            String loginName = (String) session.getAttribute("username");
            qzChargeLogService.saveQZChargeLog(qzChargeLogs, loginName);
        } catch (Exception e) {
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
//
//    //查看收费记录
//    @RequestMapping(value = "/chargesList/{uuid}", method = RequestMethod.GET)
//    public ResponseEntity<?> chargesList(@PathVariable("uuid") Long uuid) {
//        return new ResponseEntity<Object>(qzChargeLogService.chargesList(uuid), HttpStatus.OK);
//    }
}
