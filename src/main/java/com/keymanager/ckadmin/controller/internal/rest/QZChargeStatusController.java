package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.QZChargeStatusService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/qzchargestatus")
public class QZChargeStatusController {

    @Resource(name = "qzChargeStatusService2")
    QZChargeStatusService qzChargeStatusService;

    //点击收费按钮触发的方法
    @RequestMapping(value = "/saveQZChargeStatus", method = RequestMethod.POST)
    public ResultBean saveQZChargeStatus(@RequestBody Map<String, Object> dataMap, HttpSession session) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            List<Integer> uuids = (List<Integer>) dataMap.get("qzSettingUUids");
            Object strMoney = dataMap.get("money");
            BigDecimal money = null;
            if (null != strMoney && !"".equals(strMoney)) {
                money = new BigDecimal((String) dataMap.get("money"));
            }
            Integer status = (Integer) dataMap.get("chargeStatus");
            Integer satisfaction = (Integer) dataMap.get("satisfaction");
            String msg = null == dataMap.get("customerMsg") ? "" : (String) dataMap.get("customerMsg");
            String loginName = (String) session.getAttribute("username");
            qzChargeStatusService.saveQZChargeStatus(uuids, money, status, satisfaction, msg, loginName);
        } catch (Exception e) {
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
