package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.CustomerKeywordMonService;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import javax.annotation.Resource;
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
@RequestMapping("/internal/customerkeywordmon")
public class CustomerKeywordMonController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerKeywordMonController.class);

    @Resource(name = "customerKeywordMonService2")
    private CustomerKeywordMonService customerKeywordMonService;

    @PostMapping(value = "/getCustomerKeywordMonData")
    public ResultBean getCustomerKeywordMonData(@RequestBody Map<String, Object> condition) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            Map<String, Object> data = customerKeywordMonService.getCustomerKeywordMonData(condition);
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

    @PostMapping(value = "/getMonTableDataByCondition")
    public ResultBean getMonTableDataByCondition(@RequestBody Map<String, Object> condition) {
        ResultBean resultBean = new ResultBean(0, "success");
        try {
            Page<Map<String, Object>> page = customerKeywordMonService.selectTableByCondition(condition);
            if (null == page || page.getRecords().isEmpty()) {
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

//    @RequiresPermissions("/internal/customerkeywordmon/toCustomerKeywordMon")
    @GetMapping(value = "/toCustomerKeywordMon")
    public ModelAndView toCustomerKeywordMon() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customerKeywordsMon/customerKeywordsMon");
        return mv;
    }

//    @RequiresPermissions("/internal/customerkeywordmon/toCustomerKeywordMon")
    @GetMapping(value = {"/toCustomerKeywordMon/{time}/{terminal}/{search}", "/toCustomerKeywordMon/{time}/{terminal}", "/toCustomerKeywordMon/{time}/{search}",
        "/toCustomerKeywordMon/{time}"})
    public ModelAndView toCustomerKeywordMon(@PathVariable(name = "time") String time, @PathVariable(name = "terminal", required = false) String terminal,
        @PathVariable(name = "search", required = false) String search) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customerKeywordsMon/customerKeywordsMon");
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
        return mv;
    }
}
