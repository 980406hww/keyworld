package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.enums.KeywordEffectEnum;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.monitoring.common.shiro.ShiroUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CommonController
 * @Description 通用数据请求类
 * @Author lhc
 * @Date 2019/9/5 15:37
 * @Version 1.0
 */
@RestController
@RequestMapping("/internal/common")
public class CommonController {

    private static Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Resource(name = "configService2")
    private ConfigService configService;

    @PostMapping("/getSearchEngines")
    public ResultBean getSearchEngineMap(@RequestBody Map<String, String> requestMap) {
        try {
            String terminalType = (String) requestMap.get("terminalType");
            String[] searchEngines = configService.getSearchEngines(terminalType);
            Arrays.sort(searchEngines);
            return new ResultBean(200, "获取搜索引擎列表成功", searchEngines);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400,"未知错误!");
        }
    }

    @GetMapping(value = "/getKeywordEffect")
    public ResultBean getKeywordEffect() {
        ResultBean resultBean = new ResultBean(200, "查询成功");
        try {
            resultBean.setData(KeywordEffectEnum.toList());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @GetMapping(value = "/getKeywordTypeByUserRole")
    public ResultBean getKeywordTypeByUserRole(HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            Set<String> roles = shiroUser.getRoles();
            List<String> businessType = new ArrayList<>();
            if (roles.contains("Operation")) {
                businessType = Arrays.asList(configService.getConfig("BusinessType", "All").getValue().split(","));
            } else if (roles.contains("SEOSales")) {
                businessType = Arrays.asList(configService.getConfig("BusinessType", "SEOSales").getValue().split(","));
            } else if (roles.contains("NegativeSales")) {
                businessType = Arrays.asList(configService.getConfig("BusinessType", "NegativeSales").getValue().split(","));
            }
            resultBean.setData(businessType);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

}
