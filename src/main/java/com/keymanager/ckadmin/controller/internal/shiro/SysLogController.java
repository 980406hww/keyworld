package com.keymanager.ckadmin.controller.internal.shiro;

import com.keymanager.monitoring.common.result.PageInfo;
import com.keymanager.monitoring.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @description：日志管理
 * @author：zhixuan.wang
 * @date：2015/10/30 18:06
 */
@Controller
@RequestMapping("/sysLog")
public class SysLogController {
    @Autowired
    private ISysLogService sysLogService;

    @GetMapping("/manager")
    public String manager(HttpServletRequest request, String resource) {
        String requestURI=request.getRequestURI();
        if(null==resource || (!resource.equals("/login") && !resource.equals("/index")))
        {
            request.getSession().setAttribute("requestURI",requestURI);
            return "redirect:/index";
        }
        return "/views/admin/syslog";
    }

    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(Integer page, Integer rows, 
            @RequestParam(value = "sort", defaultValue = "fCreateTime") String sort,
            @RequestParam(value = "order", defaultValue = "DESC") String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        sysLogService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    @GetMapping("/icons")
    public String toIcons() {
        return "/views/icons";
    }
}
