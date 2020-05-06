package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.UserRefreshStatisticCriteria;
import com.keymanager.ckadmin.entity.RefreshStatRecord;
import com.keymanager.ckadmin.entity.UserRefreshStatisticInfo;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRefreshStatisticService;
import com.keymanager.ckadmin.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/internal/userRefreshStatistic")
public class UserRefreshStatisticInfoController {

    private static Logger logger = LoggerFactory.getLogger(UserRefreshStatisticInfoController.class);

    @Resource(name = "userRefreshStatisticService")
    private UserRefreshStatisticService userRefreshStatisticService;

    /**
     * 跳转用户刷量统计
     */
    @GetMapping(value = "/toUserRefreshStatistic")
    public ModelAndView toQzChargeStatus() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("userRefreshStatistic/userRefreshStatistics");
        return mv;
    }

    @PostMapping(value = "/getUserRefreshInfo")
    public ResultBean getRefreshDataByCondition(@RequestBody UserRefreshStatisticCriteria criteria) {
        ResultBean resultBean = new ResultBean(0, "success");
        if ("init".equals(criteria.getInit())) {
            return resultBean;
        }
        try {
            List<UserRefreshStatisticInfo> userRefreshStatisticInfos;
            if (criteria.getHistoryDate() != "" && criteria.getHistoryDate() != null) {
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
