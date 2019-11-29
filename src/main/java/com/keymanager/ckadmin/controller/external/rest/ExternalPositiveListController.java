package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.PositiveListCriteria;
import com.keymanager.ckadmin.service.PositiveListService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/external/positivelist")
public class ExternalPositiveListController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalPositiveListController.class);

    @Resource(name = "positiveListService2")
    private PositiveListService positiveListService;

    /**
     * 保存优质接口
     *
     * @param positiveListCriteria 数据主体
     * @return 成功状态 200
     */
    @RequestMapping(value = "/savePositiveLists", method = RequestMethod.POST)
    public ResultBean savePositiveLists(@RequestBody PositiveListCriteria positiveListCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(positiveListCriteria.getUserName(), positiveListCriteria.getPassword())) {
                positiveListService.savePositiveLists(positiveListCriteria.getPositiveListVOs(), positiveListCriteria.getOperationType(), positiveListCriteria.getBtnType(), positiveListCriteria.getUserName());
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
}
