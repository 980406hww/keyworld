package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.base.ExternalBaseCriteria;
import com.keymanager.ckadmin.service.NegativeExcludeKeywordService;
import com.keymanager.ckadmin.service.NegativeKeywordNameService;
import com.keymanager.ckadmin.service.NegativeKeywordService;
import com.keymanager.ckadmin.service.NegativeSiteContactKeywordService;
import com.keymanager.ckadmin.service.PerformanceService;
import com.keymanager.ckadmin.vo.NegativeSupportingDataVO;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/external/negativeKeywordName")
public class ExternalNegativeKeywordNameController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalNegativeKeywordNameController.class);

    @Resource(name = "negativeKeywordNameService2")
    private NegativeKeywordNameService negativeKeywordNameService;

    @Resource(name = "negativeSiteContactKeywordService2")
    private NegativeSiteContactKeywordService negativeSiteContactKeywordService;

    @Resource(name = "negativeKeywordService2")
    private NegativeKeywordService negativeKeywordService;

    @Resource(name = "negativeExcludeKeywordService2")
    private NegativeExcludeKeywordService negativeExcludeKeywordService;

    @Resource(name = "performanceService2")
    private PerformanceService performanceService;

    @RequestMapping(value = "/getNegativeSupportingData", method = RequestMethod.POST)
    public ResultBean getNegativeSupportingData(@RequestBody ExternalBaseCriteria criteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(criteria.getUserName(), criteria.getPassword())) {
                long startMilleSeconds = System.currentTimeMillis();
                NegativeSupportingDataVO negativeSupportingDataVO = new NegativeSupportingDataVO();
                negativeSupportingDataVO.setContactKeywords(negativeSiteContactKeywordService.getContactKeyword());
                negativeSupportingDataVO.setExcludeKeywords(negativeExcludeKeywordService.getNegativeExcludeKeyword());
                negativeSupportingDataVO.setNegativeGroups(negativeKeywordNameService.getNegativeGroup());
                negativeSupportingDataVO.setNegativeKeywords(negativeKeywordService.getNegativeKeyword());
                performanceService.addPerformanceLog("getNegativeSupportingData", System.currentTimeMillis() - startMilleSeconds, "");
                resultBean.setData(negativeSupportingDataVO);
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalConfigController.getPositiveListNewsSource()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
