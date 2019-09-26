package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.entity.RefreshStatRecord;
import com.keymanager.ckadmin.service.CustomerKeywordRefreshStatInfoService;
import com.keymanager.ckadmin.service.CustomerKeywordTerminalRefreshStatRecordService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;
import com.keymanager.util.TerminalTypeMapping;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/internal/refreshstatistics")
public class RefreshStatisticsController {

    private static Logger logger = LoggerFactory.getLogger(RefreshStatisticsController.class);

    @Resource(name = "userRoleService2")
    private UserRoleService userRoleService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @Resource(name = "customerKeywordRefreshStatInfoService2")
    private CustomerKeywordRefreshStatInfoService refreshStatInfoService;

    @Resource(name = "customerKeywordTerminalRefreshStatRecordService2")
    private CustomerKeywordTerminalRefreshStatRecordService refreshStatRecordService;

    /**
     * 跳转刷量统计页面
     */
    @GetMapping(value = "/toRefreshStatistics")
    public ModelAndView toQzChargeStatus() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("refreshStatistics/refreshStatistics");
        return mv;
    }


    @PostMapping(value = "/getRefreshDataByCondition")
    public ResultBean getRefreshDataByCondition(@RequestBody RefreshStatisticsCriteria criteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            criteria.setTerminalType(terminalType);
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("username");
            boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
            if (!isDepartmentManager) {
                criteria.setUserName(userName);
            }
            List<RefreshStatRecord> refreshStatRecords;
            if (criteria.getDayNum() > 0) {
                refreshStatRecords = refreshStatRecordService.getHistoryTerminalRefreshStatRecord(criteria);
            } else {
                refreshStatRecords = refreshStatInfoService.generateCustomerKeywordStatInfo(criteria);
            }
            if (null != refreshStatRecords && !refreshStatRecords.isEmpty()) {
                resultBean.setData(refreshStatRecords);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setData(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
