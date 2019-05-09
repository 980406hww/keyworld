package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.OperationType;
import com.keymanager.monitoring.service.OperationTypeService;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wjianwu 2019/4/22 17:15
 */
@RestController
@RequestMapping(value = "/internal/operationType")
public class OperationTypeController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ScreenedWebsiteController.class);

    @Autowired
    private OperationTypeService operationTypeService;


    @RequestMapping(value = "/searchOperationTypeLists", method = RequestMethod.GET)
    public ModelAndView searchOperationTypeLists(HttpServletRequest request, @RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize) {
        OperationType operationType = new OperationType();
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        operationType.setTerminalType(terminalType);
        return constructSearchOperationTypeListsModelAndView(currentPageNumber, pageSize, operationType);
    }

    @RequestMapping(value = "/searchOperationTypeLists", method = RequestMethod.POST)
    public ModelAndView searchOperationTypeLists(OperationType operationType, HttpServletRequest request) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        operationType.setTerminalType(terminalType);
        if (StringUtils.isEmpty(currentPageNumber)) {
            currentPageNumber = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "50";
        }
        return constructSearchOperationTypeListsModelAndView(Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize), operationType);
    }

    private ModelAndView constructSearchOperationTypeListsModelAndView(int currentPageNumber, int pageSize, OperationType operationType) {
        ModelAndView modelAndView = new ModelAndView("operationType/operationType");
        Page<OperationType> page = new Page<>(currentPageNumber, pageSize);
        List<OperationType> list = operationTypeService.getOperationTypes(operationType, page);
        page.setRecords(list);
        modelAndView.addObject("operationType", operationType);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequestMapping(value = "/getOperationType/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getOperationType(@PathVariable("uuid") Long uuid) {
        try {
            return new ResponseEntity<Object>(operationTypeService.getOperationType(uuid), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/saveOperationType", method = RequestMethod.POST)
    public ResponseEntity<?> saveOrUpdateOperationType(@RequestBody OperationType operationType) {
        try {
            if (operationType.getUuid() == null) {
                operationType.setCreateTime(new Date());
            }
            operationType.setUpdateTime(new Date());
            operationTypeService.clearOperationTypeCache(operationType.getTerminalType());
            return new ResponseEntity<Object>(operationTypeService.insertOrUpdate(operationType), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/deleteOperationType", method = RequestMethod.POST)
    public ResponseEntity<?> deleteOperationType(@RequestBody Map<String, Object> requestMap) {
        try {
            return new ResponseEntity<Object>(operationTypeService.deleteOperationType(requestMap), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
