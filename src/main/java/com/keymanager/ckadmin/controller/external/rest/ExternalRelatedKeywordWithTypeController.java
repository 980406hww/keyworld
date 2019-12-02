package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.RelatedKeywordWithTypeCriteria;
import com.keymanager.ckadmin.service.RelatedKeywordWithTypeService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/RelatedKeywordWithType")
public class ExternalRelatedKeywordWithTypeController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalRelatedKeywordWithTypeController.class);

    @Resource(name = "relatedKeywordWithTypeService2")
    private RelatedKeywordWithTypeService relatedKeywordWithTypeService;

    /**
     * 查询插件主要关键词
     *
     * @param criteria 条件
     * @return 数据主体
     */
    @RequestMapping(value = "/findRelatedKeywordWithType2", method = RequestMethod.POST)
    public ResultBean findRelatedKeywordWithType(@RequestBody RelatedKeywordWithTypeCriteria criteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(criteria.getUserName(), criteria.getPassword())) {
                resultBean.setData(relatedKeywordWithTypeService.findRelatedKeywordWithType(criteria.getMainKeyword()));
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalRelatedKeywordWithTypeController.findRelatedKeywordWithType()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 保存相关关键词
     *
     * @param criteria 数据主体
     * @return 成功状态 200
     */
    @RequestMapping(value = "/saveRelatedKeywordWithType2", method = RequestMethod.POST)
    public ResultBean saveRelatedKeywordWithType(@RequestBody RelatedKeywordWithTypeCriteria criteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(criteria.getUserName(), criteria.getPassword())) {
                relatedKeywordWithTypeService.saveRelatedKeywordWithType(criteria);
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalRelatedKeywordWithTypeController.saveRelatedKeywordWithType()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 移除相关关键词
     *
     * @param criteria 数据主体
     * @return 成功状态 200
     */
    @RequestMapping(value = "/deleteRelatedKeywordWithType2", method = RequestMethod.POST)
    public ResultBean deleteRelatedKeywordWithType(@RequestBody RelatedKeywordWithTypeCriteria criteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(criteria.getUserName(), criteria.getPassword())) {
                relatedKeywordWithTypeService.deleteRelatedKeywordWithType(criteria.getMainKeyword(), criteria.getRelatedKeyword());
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalRelatedKeywordWithTypeController.saveRelatedKeywordWithType()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
