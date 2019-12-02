package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.PositiveListCriteria;
import com.keymanager.ckadmin.service.PositiveListService;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/positivelist")
public class ExternalPositiveListController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalPositiveListController.class);

    @Resource(name = "positiveListService2")
    private PositiveListService positiveListService;

    /**
     * 保存 优质接口
     */
    @RequestMapping(value = "/savePositiveLists2", method = RequestMethod.POST)
    public ResultBean savePositiveLists(@RequestBody PositiveListCriteria positiveListCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(positiveListCriteria.getUserName(), positiveListCriteria.getPassword())) {
                positiveListService.savePositiveLists(positiveListCriteria.getPositiveListVos(), positiveListCriteria.getOperationType(),
                    positiveListCriteria.getBtnType(), positiveListCriteria.getUserName());
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalPositiveListController.savePositiveLists()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 获取的 优质数据
     */
    @RequestMapping(value = "/getSpecifiedKeywordPositiveLists2", method = RequestMethod.POST)
    public ResultBean getSpecifiedKeywordPositiveLists(@RequestBody PositiveListCriteria positiveListCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(positiveListCriteria.getUserName(), positiveListCriteria.getPassword())) {
                if (StringUtils.isNotEmpty(positiveListCriteria.getKeyword())) {
                    resultBean.setData(positiveListService.getSpecifiedKeywordPositiveLists(positiveListCriteria.getKeyword(), positiveListCriteria.getTerminalType()));
                }
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalPositiveListController.getSpecifiedKeywordPositiveLists()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
