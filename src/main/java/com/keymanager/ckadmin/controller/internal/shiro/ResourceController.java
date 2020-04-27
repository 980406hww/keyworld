package com.keymanager.ckadmin.controller.internal.shiro;

import com.keymanager.monitoring.common.base.BaseController;
import com.keymanager.ckadmin.common.shiro.ShiroUser;
import com.keymanager.monitoring.entity.Resource;
import com.keymanager.monitoring.service.IResourceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
 * @description：资源管理
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController {

    @Autowired
    private IResourceService resourceService;

    /**
     * 菜单树
     */
    @RequiresRoles("admin")
    @PostMapping("/tree")
    @ResponseBody
    public Object tree() {
        ShiroUser shiroUser = getShiroUser();
        return resourceService.selectTree(shiroUser);
    }

    /**
     * 资源管理页
     */
    @RequiresRoles("admin")
    @GetMapping("/manager")
    public String manager(HttpServletRequest request, String resource) {
        String requestURI = request.getRequestURI();
        if (null == resource || (!resource.equals("/login") && !resource.equals("/index"))) {
            request.getSession().setAttribute("requestURI", requestURI);
            return "redirect:/index";
        }
        return "/views/admin/resource/resource";
    }

    /**
     * 资源管理列表
     */
    @RequiresRoles("admin")
    @PostMapping("/treeGrid")
    @ResponseBody
    public Object treeGrid() {
        return resourceService.selectAll();
    }

    /**
     * 添加资源页
     */
    @RequiresRoles("admin")
    @GetMapping("/addPage")
    public String addPage() {
        return "/views/admin/resource/resourceAdd";
    }

    /**
     * 添加资源
     */
    @RequiresRoles("admin")
    @RequestMapping("/add")
    @ResponseBody
    public Object add(@Valid Resource resource) {
        // 选择菜单时将openMode设置为null
        Integer type = resource.getResourceType();
        if (null != type && type == 0) {
            resource.setOpenMode(null);
        }
        resourceService.insert(resource);
        return renderSuccess("添加成功！");
    }

    /**
     * 查询所有的菜单
     */
    @RequiresRoles("admin")
    @RequestMapping("/allTree")
    @ResponseBody
    public Object allMenu() {
        return resourceService.selectAllMenu();
    }

    /**
     * 查询所有的资源tree
     */
    @RequiresRoles("admin")
    @RequestMapping("/allTrees")
    @ResponseBody
    public Object allTree() {
        return resourceService.selectAllTree();
    }

    /**
     * 编辑资源页
     */
    @RequiresRoles("admin")
    @RequestMapping("/editPage")
    public String editPage(Model model, Long id) {
        Resource resource = resourceService.selectById(id);
        model.addAttribute("resource", resource);
        return "/views/admin/resource/resourceEdit";
    }

    /**
     * 编辑资源
     */
    @RequiresRoles("admin")
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Resource resource) {
        // 选择菜单时将openMode设置为null
        Integer type = resource.getResourceType();
        if (null != type && type == 0) {
            resource.setOpenMode(null);
        }
        resourceService.updResourceById(resource);
        return renderSuccess("编辑成功！");
    }

    /**
     * 删除资源
     */
    @RequiresRoles("admin")
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Long id) {
        resourceService.deleteById(id);
        return renderSuccess("删除成功！");
    }

}
