package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.OperationTypeCriteria;
import com.keymanager.ckadmin.criteria.base.BaseCriteria;
import com.keymanager.ckadmin.entity.OperationType;
import com.keymanager.ckadmin.service.OperationTypeService;
import com.keymanager.util.TerminalTypeMapping;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/operationTypes")
public class OperationTypeListController {

    private static Logger logger = LoggerFactory.getLogger(OperationType.class);

    @Resource(name = "operationTypeService2")
    private OperationTypeService operationTypeService;

    @RequestMapping(value = "/toSearchOperationTypeLists", method = RequestMethod.GET)
    public ModelAndView toSearchOperationTypeLists() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("operationTypeList/OperationTypeList");
        return mv;
    }

    @RequestMapping(value = "/toSaveOperationType", method = RequestMethod.GET)
    public ModelAndView toSaveOperationType() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("operationTypeList/AddOperationType");
        return mv;
    }

    @RequestMapping(value = "/saveOperationType", method = RequestMethod.POST)
    public ResultBean saveOperationType(@RequestBody OperationType operationType) {
        ResultBean resultBean = new ResultBean();
        try {
            if (operationType.getUuid() == null) {
                operationType.setCreateTime(new Date());
            }
            operationType.setUpdateTime(new Date());
            operationTypeService.clearOperationTypeCache(operationType.getTerminalType());
            operationTypeService.insertOrUpdate(operationType);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/getOperationType/{uuid}", method = RequestMethod.GET)
    public ResultBean getOperationType(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            OperationType operationType = operationTypeService.getOperationType(uuid);
            resultBean.setCode(200);
            resultBean.setData(operationType);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/searchOperationTypeLists", method = RequestMethod.POST)
    public ResultBean searchOperationTypeLists(@RequestBody OperationTypeCriteria operationTypeCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<OperationType> page = new Page<>(operationTypeCriteria.getPage(), operationTypeCriteria.getLimit());
            OperationType operationType = new OperationType();
            operationType.setTerminalType(operationTypeCriteria.getTerminalType());
            operationType.setOperationTypeName(operationTypeCriteria.getOperationTypeName());
            List<OperationType> operationTypeList = operationTypeService.getOperationTypes(operationType, page, operationTypeCriteria.getInit());
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(operationTypeList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/deleteOperationType", method = RequestMethod.POST)
    public ResultBean deleteOperationType(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            operationTypeService.deleteOperationType(requestMap);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

}
