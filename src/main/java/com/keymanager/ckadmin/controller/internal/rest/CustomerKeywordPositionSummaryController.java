package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.service.CustomerKeywordPositionSummaryService;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import javafx.beans.binding.ObjectExpression;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
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
@RequestMapping("/internal/ckpositionsummary")
public class CustomerKeywordPositionSummaryController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerKeywordPositionSummaryController.class);

    @Resource(name = "ckPositionSummaryService2")
    private CustomerKeywordPositionSummaryService customerKeywordPositionSummaryService;

    @RequiresPermissions("/internal/ckpositionsummary/toCustomerKeywordPositionSummary")
    @PostMapping(value = "/getCustomerKeywordPositionSummaryData")
    public ResultBean getCustomerKeywordPositionSummaryData(@RequestBody Map<String, Object> condition, HttpSession session) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (!getCurrentUser().getRoles().contains("DepartmentManager")) {
                condition.put("loginName", session.getAttribute("username"));
            }
            Map<String, Object> data = customerKeywordPositionSummaryService.getCustomerKeywordPositionSummaryData(condition);
            if (null == data || data.isEmpty()) {
                resultBean.setCode(300);
            } else {
                resultBean.setData(data);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/ckpositionsummary/toCustomerKeywordPositionSummary")
    @PostMapping(value = "/getCKPositionSummaryDataInitTable")
    public ResultBean getCKPositionSummaryDataInitTable(@RequestBody Map<String, Object> condition, HttpSession session) {
        ResultBean resultBean = new ResultBean(0, "success");
        try {
            if (!getCurrentUser().getRoles().contains("DepartmentManager")) {
                condition.put("loginName", session.getAttribute("username"));
            }
            Page<Map<String, Object>> page = customerKeywordPositionSummaryService.getCKPositionSummaryDataInitTable(condition);
            if (CollectionUtils.isEmpty(page.getRecords())) {
                resultBean.setCode(300);
                resultBean.setMsg("暂无数据");
            } else {
                resultBean.setCount(page.getTotal());
                resultBean.setData(page.getRecords());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/ckpositionsummary/toCustomerKeywordPositionSummary")
    @GetMapping(value = "/getOneCKPositionSummaryData/{uuid}")
    public ResultBean getOneCKPositionSummaryData(@PathVariable(name = "uuid") Long uuid) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            Map<String, Object> data = customerKeywordPositionSummaryService.getOneCKPositionSummaryData(uuid);
            if (null == data || data.isEmpty()){
                resultBean.setCode(300);
                resultBean.setMsg("暂无数据");
            }else {
                resultBean.setData(data);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/ckpositionsummary/toCustomerKeywordPositionSummary")
    @GetMapping(value = "/toCustomerKeywordPositionSummary")
    public ModelAndView toCustomerKeywordPositionSummary() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("ckPositionSummary/ckPositionSummary");
        return mv;
    }

    @RequiresPermissions("/internal/ckpositionsummary/toCustomerKeywordPositionSummary")
    @GetMapping(value = {"/toCustomerKeywordPositionSummary/{time}/{terminal}/{search}", "/toCustomerKeywordPositionSummary/{time}/{terminal}",
        "/toCustomerKeywordPositionSummary/{time}/{search}", "/toCustomerKeywordPositionSummary/{time}"})
    public ModelAndView toCustomerKeywordPositionSummary(@PathVariable(name = "time") String time, @PathVariable(name = "terminal", required = false) String terminal,
        @PathVariable(name = "search", required = false) String search) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("ckPositionSummary/ckPositionSummary");
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
                logger.error(e.getMessage());
            }
            mv.addObject("search", search);
        }
        return mv;
    }
}
