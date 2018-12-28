package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.QZOperationType;
import com.keymanager.monitoring.service.QZKeywordRankInfoService;
import com.keymanager.monitoring.service.QZOperationTypeService;
import com.keymanager.monitoring.vo.ExternalQzSettingVO;
import com.keymanager.monitoring.vo.QZOperationTypeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/external/qzOperationType")
public class ExternalQZOperationTypeController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ExternalQZSettingRestController.class);
    @Autowired
    private QZOperationTypeService qzOperationTypeService;

    @RequestMapping(value = "/findQZOperationType", method = RequestMethod.POST)
    public ResponseEntity<?> findQZOperationType(HttpServletRequest request) {
        String userName = (String) request.getParameter("userName");
        String password = (String) request.getParameter("password");
        String qzSettingUuid = request.getParameter("uuid");
        String operationType = request.getParameter("operationType");
        String group = request.getParameter("group");
        try {
            if(StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password) && validUser(userName, password)) {
                List<QZOperationTypeVO> qzOperationTypes = qzOperationTypeService.findQZOperationTypes(qzSettingUuid,operationType,group);
                return new ResponseEntity<Object>(qzOperationTypes, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(false, HttpStatus.OK);
    }
}
