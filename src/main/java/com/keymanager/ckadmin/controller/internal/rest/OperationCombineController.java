package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.OperationCombineCriteria;
import com.keymanager.ckadmin.criteria.UpdateGroupSettingCriteria;
import com.keymanager.ckadmin.entity.OperationCombine;
import com.keymanager.ckadmin.service.OperationCombineService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    /**
     * 根据终端获取组合的用户名以及id
     * @param terminalType
     * @return
     */
    @GetMapping("/getCombineUser/{terminalType}")
    public ResultBean getCombineUser(@PathVariable(name="terminalType") String terminalType){
        ResultBean result =new ResultBean(200,"success");
        try {
            List<Map<String,Object>> ops=operationCombineService.getCombinesUser(terminalType);
            result.setData(ops);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setCode(400);
            result.setMsg(e.getMessage());
            return result;
        }
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
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

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
            resultBean.setMsg(ex.getMessage());
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/operationCombine/saveOperationCombine")
    @PostMapping("/saveOperationCombine2")
    public ResultBean saveOperationCombine(@RequestBody OperationCombineCriteria operationCombineCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            operationCombineCriteria.setCreator((String) request.getSession().getAttribute("username"));
            operationCombineService.saveOperationCombine(operationCombineCriteria);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    /**
     * 更新搜索引擎
     * @param resultMap
     * @return
     */
    @PostMapping("/updateSearchEngine")
    public ResultBean updateSearchEngine(@RequestBody Map resultMap){
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            long uuid=Long.valueOf((String) resultMap.get("uuid"));
            OperationCombine oc=new OperationCombine();
            oc.setUuid(uuid);
            oc.setDefaultEngine((Integer) resultMap.get("defaultEngine"));
            oc.setTerminalType((String) resultMap.get("terminalType"));
            oc.setSearchEngine((String)resultMap.get("searchEngine"));
            operationCombineService.updateSearchEngine(oc);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/operationCombine/updateOperationCombine")
    @PostMapping("/updateOperationCombine2/{operationCombineUuid}")
    public ResultBean updateOperationCombine(@PathVariable("operationCombineUuid") long operationCombineUuid,
        @RequestBody UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            operationCombineService.updateOperationCombine(operationCombineUuid, updateGroupSettingCriteria);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/operationCombine/delOperationCombine")
    @PostMapping("/delOperationCombine2/{uuid}")
    public ResultBean deleteOperationCombine(@PathVariable("uuid") long uuid) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            operationCombineService.deleteOperationCombine(uuid);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }


}
