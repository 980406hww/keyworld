package com.keymanager.ckadmin.controller.internal.shiro;

import com.keymanager.monitoring.common.base.BaseController;
import com.keymanager.monitoring.entity.Organization;
import com.keymanager.monitoring.service.IOrganizationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @description：部门管理
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/organization")
public class OrganizationController extends BaseController {

    @Autowired
    private IOrganizationService organizationService;

    /**
     * 部门管理主页
     *
     * @return
     */
    @RequiresPermissions("/organization/manager")
    @GetMapping(value = "/manager")
    public String manager(HttpServletRequest request, String resource) {
        String requestURI=request.getRequestURI();
        if(null==resource || (!resource.equals("/login") && !resource.equals("/index")))
        {
            request.getSession().setAttribute("requestURI",requestURI);
            return "redirect:/index";
        }
        return "/views/admin/organization/organization";
    }

    /**
     * 部门资源树
     *
     * @return
     */
    @RequiresPermissions("/organization/manager")
    @PostMapping(value = "/tree")
    @ResponseBody
    public Object tree() {
        return organizationService.selectTree();
    }

    /**
     * 部门列表
     *
     * @return
     */
    @RequiresPermissions("/organization/manager")
    @RequestMapping("/treeGrid")
    @ResponseBody
    public Object treeGrid() {
        return organizationService.selectTreeGrid();
    }

    /**
     * 添加部门页
     *
     * @return
     */
    @RequiresPermissions("/organization/manager")
    @RequestMapping("/addPage")
    public String addPage() {
        return "/views/admin/organization/organizationAdd";
    }

    /**
     * 添加部门
     *
     * @param organization
     * @return
     */
    @RequiresPermissions("/organization/manager")
    @RequestMapping("/add")
    @ResponseBody
    public Object add(@Valid Organization organization) {
        organizationService.insert(organization);
        return renderSuccess("添加成功！");
    }

    /**
     * 编辑部门页
     *
     * @param request
     * @param id
     * @return
     */
    @RequiresPermissions("/organization/manager")
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
        Organization organization = organizationService.selectById(id);
        model.addAttribute("organization", organization);
        return "/views/admin/organization/organizationEdit";
    }

    /**
     * 编辑部门
     *
     * @param organization
     * @return
     */
    @RequiresPermissions("/organization/manager")
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Organization organization) {
        organizationService.updateById(organization);
        return renderSuccess("编辑成功！");
    }

    /**
     * 删除部门
     *
     * @param id
     * @return
     */
    @RequiresPermissions("/organization/manager")
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Long id) {
        organizationService.deleteById(id);
        return renderSuccess("删除成功！");
    }
}