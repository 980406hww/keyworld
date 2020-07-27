package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.common.shiro.ShiroUser;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.UserRefreshStatisticCriteria;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.entity.UserRefreshStatisticInfo;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRefreshStatisticService;
import com.keymanager.ckadmin.util.StringUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/internal/userRefreshStatistic")
public class UserRefreshStatisticInfoController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(UserRefreshStatisticInfoController.class);

    @Resource(name = "userRefreshStatisticService")
    private UserRefreshStatisticService userRefreshStatisticService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    /**
     * 跳转用户刷量统计
     */
    @RequiresPermissions("/internal/userRefreshStatistic/toUserRefreshStatistic")
    @GetMapping(value = "/toUserRefreshStatistic")
    public ModelAndView toQzChargeStatus() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("userRefreshStatistic/userRefreshStatistics");
        return mv;
    }

    /**
     * 获取用户刷量信息
     */
    @RequiresPermissions("/internal/userRefreshStatistic/toUserRefreshStatistic")
    @PostMapping(value = "/getUserRefreshInfo")
    public ResultBean getRefreshDataByCondition(@RequestBody UserRefreshStatisticCriteria criteria) {
        ResultBean resultBean = new ResultBean(0, "success");
        if ("init".equals(criteria.getInit())) {
            return resultBean;
        }
        try {
//            ShiroUser shiroUser = getCurrentUser();
//            Set<String> roles = shiroUser.getRoles();
//            if (!roles.contains("DepartmentManager")) {
//                criteria.setUserName(shiroUser.getLoginName());
//            }
            UserInfo userInfo = userInfoService.getUserInfo(getCurrentUser().getLoginName());
            if (userInfo.getDataAuthority().equals("self")){
                criteria.setUserName(userInfo.getLoginName());
            }else if (userInfo.getDataAuthority().equals("department")){
                criteria.setOrganizationID(userInfo.getOrganizationID());
            }
            List<UserRefreshStatisticInfo> userRefreshStatisticInfos;
            String nowDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
            if (StringUtil.isNotNullNorEmpty(criteria.getHistoryDate()) && !nowDate.equals(criteria.getHistoryDate())) {
                userRefreshStatisticInfos= userRefreshStatisticService.getHistoryUserRefreshStatisticInfo(criteria);
            } else {
                userRefreshStatisticInfos = userRefreshStatisticService.generateUserRefreshStatisticInfo(criteria);
            }
            if (null != userRefreshStatisticInfos && !userRefreshStatisticInfos.isEmpty()) {
                resultBean.setData(userRefreshStatisticInfos);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setData(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
