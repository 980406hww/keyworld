package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.OperationCombineService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * @param terminalType
     * @return
     */
    @GetMapping("/getOperationCombines/{terminalType}")
    public ResultBean getOperationCombines(@PathVariable String terminalType) {
        ResultBean resultBean = new ResultBean();
        try {
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

    @RequiresPermissions("/internal/operationCombine/updateMaxInvalidCount")
    @PostMapping("/updateMaxInvalidCount2")
    public ResultBean updateMaxInvalidCount(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            long uuid = Long.parseLong((String) requestMap.get("uuid"));
            int maxInvalidCount = Integer.parseInt((String) requestMap.get("maxInvalidCount"));
            operationCombineService.updateMaxInvalidCount(uuid, maxInvalidCount);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/groupsetting/searchGroupSettings")
    @PostMapping("/getGroupNames2/{uuid}")
    public ResultBean getGroupNames (@PathVariable(name = "uuid") long uuid) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            List<String> groupNames = operationCombineService.getGroupNames(uuid);
            resultBean.setData(groupNames);
            return resultBean;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
    }

}
