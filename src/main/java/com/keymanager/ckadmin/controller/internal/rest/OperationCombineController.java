package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.OperationCombineService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhoukai
 * @Date 2019/9/10 14:10
 */
@RestController
@RequestMapping(value = "/internal/operationcombine")
public class OperationCombineController {

    private static Logger logger = LoggerFactory.getLogger(OperationCombineController.class);

    @Resource(name = "operationCombineService2")
    private OperationCombineService operationCombineService;

    /**
     * 根据终端类型获取操作组合列表
     * @param requestMap
     * @return
     */
    @PostMapping("/getOperationCombines")
    public ResultBean getOperationCombines(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            String terminalType = (String) requestMap.get("terminalType");
            List<Map<String, Object>> operationCombines = operationCombineService.getOperationCombineNames(terminalType);
            resultBean.setCode(200);
            resultBean.setMsg("success");
            resultBean.setData(operationCombines);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

}
