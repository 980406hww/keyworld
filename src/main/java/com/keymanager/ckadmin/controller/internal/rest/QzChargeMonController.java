package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.QZChargeMonCriteria;
import com.keymanager.ckadmin.entity.QzChargeMon;
import com.keymanager.ckadmin.service.QzChargeMonService;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/internal/qzchargemon")
public class QzChargeMonController {

    @Resource(name = "qzChargeMonService2")
    private QzChargeMonService qzChargeMonService;

    @PostMapping(value = "/getMonDataBySe")
    public ResultBean getMonDataBySe(@RequestBody Map<String, Object> condition) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String searchEngines = (String) condition.get("searchEngines");
            String time = (String) condition.get("time");
            String terminal = (String) condition.get("qzTerminal");
            int num = 12, type = 1;
            if (null != time) {
                switch (time) {
                    case "2":
                        num = 3;
                        break;
                    case "1":
                        type = 2;
                        num = 30;
                        break;
                    default:
                        num = 12;
                        break;
                }
            }
            Map<String, Object> data = qzChargeMonService.selectBySe(searchEngines, terminal, num, type);
            if (null == data || data.isEmpty()) {
                resultBean.setCode(300);
            } else {
                resultBean.setData(data);
            }
        } catch (Exception e) {
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

    @PostMapping(value = "/getMonDataByCondition")
    public ResultBean getMonDataByCondition(@RequestBody QZChargeMonCriteria criteria) {
        ResultBean resultBean = new ResultBean(0, "success");
        try {
            Page<QzChargeMon> page = new Page<>(criteria.getPage(), criteria.getLimit());
            page.setOrderByField("fOperationDate");
            page.setAsc(false);
            Wrapper<QzChargeMon> wrapper = new EntityWrapper<>();
            wrapper.like("fTerminalType", criteria.getTerminal());
            if (null != criteria.getSearchEngine() && !"".equals(criteria.getSearchEngine())) {
                wrapper.eq("fSearchEngine", criteria.getSearchEngine());
            }
            if (null != criteria.getOperationType()) {
                wrapper.eq("fOperationType", criteria.getOperationType());
            }
            if (null != criteria.getDateStart() && !"".equals(criteria.getDateStart())) {
                wrapper.where("fOperationDate >= {0}", criteria.getDateStart());
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH, -30);
                wrapper.where("fOperationDate >= {0}", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
            }
            if (null != criteria.getDateEnd() && !"".equals(criteria.getDateEnd())) {
                wrapper.where("fOperationDate <= {0}", criteria.getDateEnd());
            }
            page = qzChargeMonService.selectPage(page, wrapper);
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
        } catch (Exception e) {
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/qzchargemon/toQzChargeMon")
    @GetMapping(value = "/toQzChargeMonWithParam/{terminal}/{search}/{time}")
    public ModelAndView toQzChargeMonWithParam(@PathVariable String terminal, @PathVariable String search, @PathVariable String time) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzchargemon/qzChargeMon");
        if ("null".equals(terminal)) {
            mv.addObject("terminal", "");
        } else {
            mv.addObject("terminal", terminal);
        }
        if ("null".equals(search)) {
            mv.addObject("search", "");
        } else {
            try {
                search = URLDecoder.decode(search, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mv.addObject("search", search);
        }

        mv.addObject("time", time);
        return mv;
    }
}
