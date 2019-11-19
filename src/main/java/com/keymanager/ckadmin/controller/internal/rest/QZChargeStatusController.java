package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.QZChargeStatusCriteria;
import com.keymanager.ckadmin.entity.QZChargeStatus;
import com.keymanager.ckadmin.service.QZChargeStatusService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/internal/qzchargestatus")
public class QZChargeStatusController {

    private static final Logger logger = LoggerFactory.getLogger(QZChargeStatusController.class);

    @Resource(name = "qzChargeStatusService2")
    QZChargeStatusService qzChargeStatusService;

    /**
     * 跳转收费状态记录页面
     */
    @GetMapping(value = "/toQzChargeStatus")
    public ModelAndView toQzChargeStatus() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/qzChargeStatus");
        return mv;
    }

    /**
     * 收费状态
     *
     * @param dataMap 数据列表
     * @param session 获得登录名
     * @return 成功或失败信息
     */
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
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @GetMapping(value = "/getOneQzChargeStatus/{uuid}")
    public ResultBean getOneQzChargeStatus(@PathVariable Long uuid) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            resultBean.setData(qzChargeStatusService.selectById(uuid));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @PostMapping(value = "/getQzChargeStatus")
    public ResultBean getQzChargeStatus(@RequestBody QZChargeStatusCriteria criteria) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        try {
            Page<QZChargeStatus> page = new Page<>(criteria.getPage(), criteria.getLimit());
            page = qzChargeStatusService.getQzChargeStatus(page, criteria.getQzSettingUuid());
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
