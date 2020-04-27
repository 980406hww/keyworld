package com.keymanager.ckadmin.controller.internal.shiro;

import com.keymanager.monitoring.common.base.BaseController;
import com.keymanager.monitoring.common.result.PageInfo;
import com.keymanager.monitoring.entity.Role;
import com.keymanager.monitoring.service.IRoleService;
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
import java.util.List;

/**
 * @description：权限管理
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private IRoleService roleService;

    /**
     * 权限管理页
     *
     * @return
     */
    @RequiresPermissions("/role/manager")
    @GetMapping("/manager")
    public String manager(HttpServletRequest request, String resource) {
        String requestURI=request.getRequestURI();
        if(null==resource || (!resource.equals("/login") && !resource.equals("/index")))
        {
            request.getSession().setAttribute("requestURI",requestURI);
            return "redirect:/index";
        }
        return "/views/admin/role/role";
    }

    /**
     * 权限列表
     *
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @RequiresPermissions("/role/manager")
    @PostMapping("/dataGrid")
    @ResponseBody
    public Object dataGrid(Integer page, Integer rows, String sort, String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        roleService.selectDataGrid(pageInfo);
        return pageInfo;
    }

    /**
     * 权限树
     *
     * @return
     */
    @RequiresPermissions("/role/manager")
    @PostMapping("/tree")
    @ResponseBody
    public Object tree() {
        return roleService.selectTree();
    }

    /**
     * 添加权限页
     *
     * @return
     */
    @RequiresPermissions("/role/manager")
    @GetMapping("/addPage")
    public String addPage() {
        return "/views/admin/role/roleAdd";
    }

    /**
     * 添加权限
     *
     * @param role
     * @return
     */
    @RequiresPermissions("/role/manager")
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid Role role) {
        roleService.insert(role);
        return renderSuccess("添加成功！");
    }

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    @RequiresPermissions("/role/manager")
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Long id) {
        roleService.deleteById(id);
        return renderSuccess("删除成功！");
    }

    /**
     * 编辑权限页
     *
     * @param model
     * @param id
     * @return
     */
    @RequiresPermissions("/role/manager")
    @RequestMapping("/editPage")
    public String editPage(Model model, Long id) {
        Role role = roleService.selectById(id);
        model.addAttribute("role", role);
        return "/views/admin/role/roleEdit";
    }

    /**
     * 删除权限
     *
     * @param role
     * @return
     */
    @RequiresPermissions("/role/manager")
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Role role) {
        roleService.updateById(role);
        return renderSuccess("编辑成功！");
    }

    /**
     * 授权页面
     *
     * @param id
     * @param model
     * @return
     */
    @RequiresPermissions("/role/manager")
    @GetMapping("/grantPage")
    public String grantPage(Model model, Long id) {
        model.addAttribute("id", id);
        return "/views/admin/role/roleGrant";
    }

    /**
     * 授权页面页面根据角色查询资源
     *
     * @param id
     * @return
     */
    @RequiresPermissions("/role/manager")
    @RequestMapping("/findResourceIdListByRoleId")
    @ResponseBody
    public Object findResourceByRoleId(Long id) {
        List<Long> resources = roleService.selectResourceIdListByRoleId(id);
        return renderSuccess(resources);
    }

    /**
     * 授权
     *
     * @param id
     * @param resourceIds
     * @return
     */
    @RequiresPermissions("/role/manager")
    @RequiresRoles("admin")
    @RequestMapping("/grant")
    @ResponseBody
    public Object grant(Long id, String resourceIds) {
        roleService.updateRoleResource(id, resourceIds);
        return renderSuccess("授权成功！");
    }

}
