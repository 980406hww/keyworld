package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.NegativeListCriteria;
import com.keymanager.ckadmin.service.NegativeListService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/external/negativelist")
public class ExternalNegativeListController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalNegativeListController.class);

    @Resource(name = "negativeListService2")
    private NegativeListService negativeListService;

    /**
     * 保存 负面数据
     *
     * @param negativeListCriteria .negativeLists 数据主体
     * @return 成功状态
     */
    @RequestMapping(value = "/saveNegativeLists", method = RequestMethod.POST)
    public ResultBean saveNegativeLists(@RequestBody NegativeListCriteria negativeListCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(negativeListCriteria.getUserName(), negativeListCriteria.getPassword())) {
                negativeListService.saveNegativeLists(negativeListCriteria.getNegativeLists(), negativeListCriteria.getOperationType());
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalNegativeListController.saveNegativeLists()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
