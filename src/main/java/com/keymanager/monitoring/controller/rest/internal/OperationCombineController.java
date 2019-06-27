package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.criteria.OperationCombineCriteria;
import com.keymanager.monitoring.criteria.UpdateGroupSettingCriteria;
import com.keymanager.monitoring.service.OperationCombineService;
import com.keymanager.monitoring.vo.OperationCombineVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * @Author zhoukai
 * @Date 2019/6/24 15:52
 **/
@RestController
@RequestMapping(value = "/internal/operationCombine")
public class OperationCombineController {

    private static Logger logger = LoggerFactory.getLogger(GroupSettingRestController.class);

    @Autowired
    private OperationCombineService operationCombineService;

    @PostMapping("/getGroupNames/{uuid}")
    public ResponseEntity<?> getGroupNames (@PathVariable Long uuid) {
        try {
            String groupNames = operationCombineService.getGroupNames(uuid);
            return new ResponseEntity<Object>(groupNames, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/searchGroupsBelowOperationCombine")
    public ResponseEntity<?> searchGroupsBelowOperationCombine(@RequestBody Map<String, Object> requestMap) {
        try {
            long uuid = Long.valueOf((String) requestMap.get("uuid"));
            String groupName = (String) requestMap.get("groupName");
            List<OperationCombineVO> operationCombineVos = operationCombineService.searchGroupsBelowOperationCombine(uuid, groupName);
            return new ResponseEntity<Object>(operationCombineVos, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOperationCombine")
    public ResponseEntity<?> saveOperationCombine(@RequestBody OperationCombineCriteria operationCombineCriteria,
                                                  HttpServletRequest request) {
        try {
            operationCombineCriteria.setCreator((String) request.getSession().getAttribute("username"));
            operationCombineService.saveOperationCombine(operationCombineCriteria);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delOperationCombine/{uuid}")
    public ResponseEntity<?> deleteOperationCombine(@PathVariable("uuid") long uuid) {
        try {
            operationCombineService.deleteOperationCombine(uuid);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateOperationCombine/{operationCombineUuid}")
    public ResponseEntity<?> updateOperationCombine(@PathVariable("operationCombineUuid") long operationCombineUuid,
                                                    @RequestBody UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        try {
            operationCombineService.updateOperationCombine(operationCombineUuid, updateGroupSettingCriteria);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
