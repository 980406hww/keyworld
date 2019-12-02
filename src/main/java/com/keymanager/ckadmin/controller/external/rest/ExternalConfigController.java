package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.base.ExternalBaseCriteria;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.util.Constants;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/config")
public class ExternalConfigController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalConfigController.class);

    @Resource(name = "configService2")
    private ConfigService configService;

    /**
     * 获取 配置信息
     */
    @RequestMapping(value = "/getPositiveListNewsSource2", method = RequestMethod.POST)
    public ResultBean getPositiveListNewsSource(@RequestBody ExternalBaseCriteria criteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(criteria.getUserName(), criteria.getPassword())) {
                resultBean.setData(configService.findConfigs(Constants.CONFIG_TYPE_OPTIMIZE_METHOD_GROUPNAME));
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
