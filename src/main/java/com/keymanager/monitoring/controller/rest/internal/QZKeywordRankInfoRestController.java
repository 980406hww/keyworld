package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.service.IUserInfoService;
import com.keymanager.monitoring.service.QZKeywordRankInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
@RequestMapping(value = "/internal/qzkeywordrank")
public class QZKeywordRankInfoRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(QZKeywordRankInfoRestController.class);

    @Autowired
    private QZKeywordRankInfoService qzKeywordRankInfoService;

    @Autowired
    private IUserInfoService userInfoService;
    @RequiresPermissions("/internal/qzsetting/searchQZSettings")
    @RequestMapping(value = "/searchCountNumOfQZKeywordRankInfo", method = RequestMethod.POST)
    public ResponseEntity<?> searchCountNumOfQZKeywordRankInfo(@RequestBody QZSettingSearchCriteria qzSettingSearchCriteria, HttpServletRequest request) {
        try {
            Set<String> roles = getCurrentUser().getRoles();
            if(!roles.contains("DepartmentManager")) {
                String loginName = (String) request.getSession().getAttribute("username");
                qzSettingSearchCriteria.setLoginName(loginName);
            } else {
                if (qzSettingSearchCriteria.getUserInfoID() != null) {
                    UserInfo userInfo = userInfoService.selectById(qzSettingSearchCriteria.getUserInfoID());
                    qzSettingSearchCriteria.setLoginName(userInfo.getLoginName());
                }
            }
            return new ResponseEntity<Object>(qzKeywordRankInfoService.searchCountNumOfQZKeywordRankInfo(qzSettingSearchCriteria), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
    }
}
