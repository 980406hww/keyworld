package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.service.QZOperationTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Created by shunshikj01 on 2017/7/10.
 */
@RestController
@RequestMapping("/internal/operationtype/")
public class QZOperationTypeRestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(QZOperationTypeRestController.class);

    @Autowired
    private QZOperationTypeService qzOperationTypeService;

}
