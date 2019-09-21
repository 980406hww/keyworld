package com.keymanager.ckadmin.controller.internal.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.entity.QZCategoryTag;
import com.keymanager.ckadmin.service.QZCategoryTagService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public ResultBean saveCategoryLabel(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            String userName = (String) request.getSession().getAttribute("username");
            long qzSettingUuid = Long.parseLong((String) requestMap.get("qzSettingUuid"));
            List<QZCategoryTag> targetQZCategoryTags = new ObjectMapper().convertValue(requestMap.get("qzCategoryTags"),
                new TypeReference<List<QZCategoryTag>>() {});
            qzCategoryTagService.saveCategoryTagNames(qzSettingUuid, targetQZCategoryTags, userName);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

}
