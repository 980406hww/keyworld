package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.criteria.QZCategoryTagCriteria;
import com.keymanager.monitoring.service.QZCategoryTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhoukai
 * @Date 2019/1/7 17:39
 **/
@RestController
@RequestMapping(value = "/internal/qzcategorytag")
public class QZCategoryTagRestController {
    private static Logger logger = LoggerFactory.getLogger(QZSettingRestController.class);

    @Autowired
    private QZCategoryTagService qzCategoryTagService;

    @RequestMapping(value = "/save" , method = RequestMethod.POST)
    public ResponseEntity<?> saveCategoryTagNames(@RequestBody QZCategoryTagCriteria qzCategoryTagCriteria) {
        try {
            qzCategoryTagService.saveCategoryTagNames(qzCategoryTagCriteria);
            return  new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }
}