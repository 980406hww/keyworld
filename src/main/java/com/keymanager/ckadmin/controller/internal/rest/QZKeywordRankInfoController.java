package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.QZSettingSearchCriteria;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.service.QZKeywordRankInfoService;
import com.keymanager.ckadmin.service.UserInfoService;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/internal/qzkeywordrank")
public class QZKeywordRankInfoController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(QZKeywordRankInfoController.class);

    @Resource(name = "qzKeywordRankInfoService2")
    private QZKeywordRankInfoService qzKeywordRankInfoService;
    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @RequiresPermissions("/internal/qzsetting/searchQZSettings")
    @RequestMapping(value = "/searchCountNumOfQZKeywordRankInfoNew", method = RequestMethod.POST)
    public ResultBean searchCountNumOfQZKeywordRankInfoNew(
        @RequestBody QZSettingSearchCriteria qzSettingSearchCriteria, HttpSession session) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            Set<String> roles = getCurrentUser().getRoles();
            if (!roles.contains("DepartmentManager")) {
                String loginName = (String) session.getAttribute("username");
                qzSettingSearchCriteria.setLoginName(loginName);
            } else {
                if (qzSettingSearchCriteria.getUserInfoID() != null) {
                    UserInfo userInfo = userInfoService
                        .selectById(qzSettingSearchCriteria.getUserInfoID());
                    qzSettingSearchCriteria.setLoginName(userInfo.getLoginName());
                }
            }
            resultBean.setData(qzKeywordRankInfoService
                .searchCountNumOfQZKeywordRankInfo(qzSettingSearchCriteria));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
