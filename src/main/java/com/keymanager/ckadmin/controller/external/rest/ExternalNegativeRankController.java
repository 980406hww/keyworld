package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.NegativeRankCriteria;
import com.keymanager.ckadmin.service.NegativeRankService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/negativeRank")
public class ExternalNegativeRankController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalNegativeRankController.class);

    @Resource(name = "negativeRankService2")
    private NegativeRankService negativeRankService;

    /**
     * 查询 负面排名
     */
    @RequestMapping(value = "/findNegativeRanks2", method = RequestMethod.POST)
    public ResultBean findNegativeRanks(@RequestBody NegativeRankCriteria negativeRankCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(negativeRankCriteria.getUserName(), negativeRankCriteria.getPassword())) {
                resultBean.setData(negativeRankService.findNegativeRanks(negativeRankCriteria));
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalNegativeRankController.findNegativeRanks()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 查询 关键字初始輿情排名
     */
    @RequestMapping(value = "/findInitialNegativeRanks2", method = RequestMethod.POST)
    public ResultBean findInitialNegativeRanks(@RequestBody NegativeRankCriteria negativeRankCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(negativeRankCriteria.getUserName(), negativeRankCriteria.getPassword())) {
                resultBean.setData(negativeRankService.findInitialNegativeRanks(negativeRankCriteria));
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalNegativeRankController.findInitialNegativeRanks()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
