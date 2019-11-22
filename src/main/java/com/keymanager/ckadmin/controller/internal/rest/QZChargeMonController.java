package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.QZChargeMonCriteria;
import com.keymanager.ckadmin.entity.QzChargeMon;
import com.keymanager.ckadmin.service.QzChargeMonService;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author yaoqing
 */
@RestController
@RequestMapping("/internal/qzchargemon")
public class QZChargeMonController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(QZChargeMonController.class);

    @Resource(name = "qzChargeMonService2")
    private QzChargeMonService qzChargeMonService;

    @RequiresPermissions("/internal/qzchargemon/toQzChargeMon")
    @PostMapping(value = "/getQZChargeMonData")
    public ResultBean getQZChargeMonData(@RequestBody Map<String, Object> condition, HttpSession session) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String searchEngines = (String) condition.get("searchEngines");
            String time = (String) condition.get("time");
            String terminal = (String) condition.get("qzTerminal");
            Set<String> roles = getCurrentUser().getRoles();
            String loginName = null;
            if (!roles.contains("DepartmentManager")) {
                loginName = (String) session.getAttribute("username");
            }
            Map<String, Object> data = qzChargeMonService.getQZChargeMonData(searchEngines, terminal, time, loginName);
            if (null == data || data.isEmpty()) {
                resultBean.setCode(300);
            } else {
                resultBean.setData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/qzchargemon/toQzChargeMon")
    @GetMapping(value = "/toQzChargeMon")
    public ModelAndView toQzChargeMon() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzchargemon/qzChargeMon");
        return mv;
    }

    @RequiresPermissions("/internal/qzchargemon/toQzChargeMon")
    @PostMapping(value = "/getMonDataByCondition")
    public ResultBean getMonDataByCondition(@RequestBody QZChargeMonCriteria criteria, HttpSession session) {
        ResultBean resultBean = new ResultBean(0, "success");
        try {
            Set<String> roles = getCurrentUser().getRoles();
            if (!roles.contains("DepartmentManager")) {
                criteria.setLoginName((String) session.getAttribute("username"));
            }
            Page<QzChargeMon> page = new Page<>(criteria.getPage(), criteria.getLimit());
            page.setRecords(qzChargeMonService.getMonDateByCondition(page, criteria));
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/qzchargemon/toQzChargeMon")
    @GetMapping(value = {"/toQzChargeMonWithParam/{time}/{terminal}/{search}", "/toQzChargeMonWithParam/{time}/{search}", "/toQzChargeMonWithParam/{time}/{terminal}",
        "/toQzChargeMonWithParam/{time}"})
    public ModelAndView toQzChargeMonWithParam(@PathVariable(name = "time") String time, @PathVariable(name = "terminal", required = false) String terminal,
        @PathVariable(name = "search", required = false) String search) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzchargemon/qzChargeMon");
        mv.addObject("time", time);
        if (null == terminal) {
            mv.addObject("terminal", "");
        } else {
            mv.addObject("terminal", terminal);
        }
        if (null == search) {
            mv.addObject("search", "");
        } else {
            try {
                search = URLDecoder.decode(search, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
            mv.addObject("search", search);
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        mv.addObject("dateStart", sdf.format(date));
        return mv;
    }
}
