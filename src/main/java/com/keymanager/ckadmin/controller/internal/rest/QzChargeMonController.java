package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.QzChargeMonService;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "/toQzChargeMon")
    public ModelAndView toQzChargeMon() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzchargemon/qzChargeMon");
        return mv;
    }
}
