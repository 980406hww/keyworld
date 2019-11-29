package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.NegativeListCriteria;
import com.keymanager.ckadmin.entity.NegativeList;
import com.keymanager.ckadmin.service.NegativeListService;
import com.keymanager.ckadmin.service.PerformanceService;
import com.keymanager.ckadmin.util.Constants;
import com.keymanager.ckadmin.util.StringUtils;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/negativelist")
public class ExternalNegativeListController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalNegativeListController.class);

    @Resource(name = "negativeListService2")
    private NegativeListService negativeListService;

    @Resource(name = "performanceService2")
    private PerformanceService performanceService;

    /**
     * 保存 负面数据
     *
     * @param negativeListCriteria .negativeLists 数据主体
     * @return 成功状态 200
     */
    @RequestMapping(value = "/saveNegativeLists2", method = RequestMethod.POST)
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

    /**
     * 查询明确关键字舆情列表
     *
     * @param negativeListCriteria 参数主体
     * @return 数据主体
     */
    @RequestMapping(value = "/getSpecifiedKeywordNegativeLists2", method = RequestMethod.POST)
    public ResultBean getSpecifiedKeywordNegativeLists(@RequestBody NegativeListCriteria negativeListCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(negativeListCriteria.getUserName(), negativeListCriteria.getPassword())) {
                long startMilleSeconds = System.currentTimeMillis();
                if (null != negativeListCriteria.getKeyword() && !"".equals(negativeListCriteria.getKeyword())) {
                    resultBean.setData(negativeListService.getSpecifiedKeywordNegativeLists(negativeListCriteria.getKeyword()));
                }
                performanceService.addPerformanceLog("getSpecifiedKeywordNegativeLists", System.currentTimeMillis() - startMilleSeconds, "");
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalNegativeListController.getSpecifiedKeywordNegativeLists():Post" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 查询明确关键字舆情列表
     *
     * @param request 参数主体
     * @return 数据主体
     */
    @RequestMapping(value = "/getSpecifiedKeywordNegativeLists2", method = RequestMethod.GET)
    public ResultBean getSpecifiedKeywordNegativeLists(HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String keyword = request.getParameter("keyword");
        try {
            if (validUser(userName, password)) {
                long startMilleSeconds = System.currentTimeMillis();
                List<NegativeList> negativeLists = negativeListService.getSpecifiedKeywordNegativeLists(keyword);
                StringBuilder sb = new StringBuilder(Constants.COLUMN_SPLITTOR);
                if (CollectionUtils.isNotEmpty(negativeLists)) {
                    for (NegativeList negativeList : negativeLists) {
                        sb.append(negativeList.getTitle());
                        sb.append(Constants.COLUMN_SPLITTOR);
                        sb.append(negativeList.getTitle());
                        sb.append(Constants.COLUMN_SPLITTOR);
                    }
                    performanceService.addPerformanceLog("All:getSpecifiedKeywordNegativeLists", System.currentTimeMillis() - startMilleSeconds, "Record Count: " + negativeLists.size());
                }
                resultBean.setData(sb.toString());
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalNegativeListController.getSpecifiedKeywordNegativeLists():Get" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
