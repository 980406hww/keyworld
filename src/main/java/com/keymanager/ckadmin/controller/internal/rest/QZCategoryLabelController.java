package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.QZCategoryTagService;
import com.keymanager.ckadmin.criteria.QZCategoryTagCriteria;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/qzcategory")
public class QZCategoryLabelController {

    private static final Logger logger = LoggerFactory.getLogger(QZCategoryLabelController.class);

    @Resource(name = "qzCategoryTagService2")
    private QZCategoryTagService qzCategoryTagService;

    @PostMapping("/saveCategoryLabel")
    public ResultBean saveCategoryLabel(@RequestBody QZCategoryTagCriteria qzCategoryTagCriteria) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            qzCategoryTagService.saveCategoryTagNames(qzCategoryTagCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

}
