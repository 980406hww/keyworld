package com.keymanager.monitoring.controller.rest.shiro;

import com.keymanager.monitoring.common.base.BaseController;
import com.keymanager.monitoring.common.result.PageInfo;
import com.keymanager.monitoring.common.shiro.PasswordHash;
import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.entity.Role;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.service.IUserInfoService;
import com.keymanager.monitoring.vo.UserVO;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description：用户管理
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private PasswordHash passwordHash;

    /**
     * 用户管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager(HttpServletRequest request, String resource) {
        String requestURI=request.getRequestURI();
        if(null==resource || (!resource.equals("/login") && !resource.equals("/index")))
        {
            request.getSession().setAttribute("requestURI",requestURI);
            return "redirect:/index";
        }
        return "views/admin/user/user";
    }

    /**
     * 用户管理列表
     *
     * @param userVo
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @PostMapping("/dataGrid")
    @ResponseBody
    public Object dataGrid(UserVO userVo, Integer page, Integer rows, String sort, String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(userVo.getUserName())) {
            condition.put("userName", userVo.getUserName());
        }
        if (userVo.getOrganizationID() != null) {
            condition.put("organizationID", userVo.getOrganizationID());
        }
        if (userVo.getCreatedateStart() != null) {
            condition.put("createTime", userVo.getCreatedateStart());
        }
        pageInfo.setCondition(condition);
        userInfoService.selectDataGrid(pageInfo);
        return pageInfo;
    }

    /**
     * 添加用户页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "/views/admin/user/userAdd";
    }

    /**
     * 添加用户
     *
     * @param userVo
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid UserVO userVo) {
        List<UserInfo> list = userInfoService.selectByLoginName(userVo);
        if (list != null && !list.isEmpty()) {
            return renderError("登录名已存在!");
        }
        String salt = StringUtils.getUUId();
        String pwd = passwordHash.toHex(userVo.getPassword(), salt);
        userVo.setSalt(salt);
        userVo.setPassword(pwd);
        userInfoService.insertByVo(userVo);
        return renderSuccess("添加成功");
    }

    /**
     * 编辑用户页
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
        UserVO userVo = userInfoService.selectVoById(id);
        List<Role> rolesList = userVo.getRolesList();
        List<Long> ids = new ArrayList<Long>();
        for (Role role : rolesList) {
            ids.add(role.getUuid());
        }
        model.addAttribute("roleIds", ids);
        model.addAttribute("user", userVo);
        return "/views/admin/user/userEdit";
    }

    /**
     * 编辑用户
     *
     * @param userVo
     * @return
     */
    @RequiresRoles("admin")
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid UserVO userVo) {
        List<UserInfo> list = userInfoService.selectByLoginName(userVo);
        if (list != null && !list.isEmpty()) {
            return renderError("登录名已存在!");
        }
        // 更新密码
        if (StringUtils.isNotBlank(userVo.getPassword())) {
            UserInfo user = userInfoService.selectById(userVo.getUserUuid());
            String salt = user.getSalt();
            String pwd = passwordHash.toHex(userVo.getPassword(), salt);
            userVo.setPassword(pwd);
        }
        userInfoService.updateByVo(userVo);
        return renderSuccess("修改成功！");
    }

    /**
     * 修改密码页
     *
     * @return
     */
    @GetMapping("/editPwdPage")
    public String editPwdPage() {
        return "/views/admin/user/userEditPwd";
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param pwd
     * @return
     */
    @PostMapping("/editUserPwd")
    @ResponseBody
    public Object editUserPwd(@RequestBody Map<String, Object> requestMap) {
        UserInfo user = userInfoService.selectById(getUserId());
        String oldPwd = (String) requestMap.get("oldPwd");
        String pwd = (String) requestMap.get("pwd");
        String salt = user.getSalt();
        if (!user.getPassword().equals(passwordHash.toHex(oldPwd, salt))) {
            return renderError("原始密码不正确!");
        }
        userInfoService.updatePwdByUserId(getUserId(), passwordHash.toHex(pwd, salt));
        return renderSuccess("密码修改成功,请重新登录!");
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequiresRoles("admin")
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Long id) {
        Long currentUserId = getUserId();
        if (id == currentUserId) {
            return renderError("不可以删除自己！");
        }
        userInfoService.deleteUserById(id);
        return renderSuccess("删除成功！");
    }
}