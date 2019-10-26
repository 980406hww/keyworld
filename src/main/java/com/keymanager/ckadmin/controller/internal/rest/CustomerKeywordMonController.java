package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.CustomerKeywordMonService;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/customerkeywordmon")
public class CustomerKeywordMonController {

    @Resource(name = "customerKeywordMonService2")
    private CustomerKeywordMonService customerKeywordMonService;

    @PostMapping(value = "/getMonDataByCondition")
    public ResultBean getMonDataByCondition(@RequestBody Map<String, Object> condition) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String time = (String) condition.get("time");
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
            Map<String, Object> data = customerKeywordMonService.selectByCondition(condition, num, type);
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
}
