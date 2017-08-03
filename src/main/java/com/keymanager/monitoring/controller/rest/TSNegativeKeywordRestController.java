package com.keymanager.monitoring.controller.rest;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.service.TSMainKeywordService;
import com.keymanager.monitoring.service.TSNegativeKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shunshikj08 on 2017/8/2.
 */
@RestController
@RequestMapping(value = "/spring/tsnegativekeyword")
public class TSNegativeKeywordRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(TSNegativeKeywordRestController.class);

    @Autowired
    private TSMainKeywordService tsMainKeywordService;

    @Autowired
    private TSNegativeKeywordService tsNegativeKeywordService;
}
