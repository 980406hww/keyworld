package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.WarnListCriteria;
import com.keymanager.ckadmin.service.WarnListService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/warnlist")
public class ExternalWarnListController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalWarnListController.class);

    @Resource(name = "warnListService2")
    private WarnListService warnListService;

    /**
     * 保存 预警清单
     *
     * @param warnListCriteria 数据主体
     * @return 成功状态 200
     */
    @RequestMapping(value = "/saveWarnLists2", method = RequestMethod.POST)
    public ResultBean savewWarnLists(@RequestBody WarnListCriteria warnListCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(warnListCriteria.getUserName(), warnListCriteria.getPassword())) {
                warnListService.saveWarnLists(warnListCriteria.getWarnLists(), warnListCriteria.getOperationType());
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalWarnListController.savewWarnLists()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 获取明确的预警列表
     *
     * @param warnListCriteria 参数主体
     * @return 数据主体
     */
    @RequestMapping(value = "/getSpecifiedKeywordWarnLists2", method = RequestMethod.POST)
    public ResultBean getSpecifiedKeywordWarnLists(@RequestBody WarnListCriteria warnListCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(warnListCriteria.getUserName(), warnListCriteria.getPassword())) {
                resultBean.setData(warnListService.getSpecifiedKeywordWarnLists(warnListCriteria.getKeyword()));
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalWarnListController.getSpecifiedKeywordWarnLists()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
