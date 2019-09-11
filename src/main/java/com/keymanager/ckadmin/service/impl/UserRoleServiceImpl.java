package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.UserRoleDao;
import com.keymanager.ckadmin.entity.UserRole;
import com.keymanager.ckadmin.service.RoleService;
import com.keymanager.ckadmin.service.UserRoleService;
import com.keymanager.monitoring.service.IUserRoleService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * UserRole 表数据服务层接口实现类
 *
 */
@Service("userRoleService2")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements
    UserRoleService {

    @Resource(name = "userRoleDao2")
    private UserRoleDao userRoleDao;

    @Resource(name = "roleService2")
    private RoleService roleService;

    @Override
    public Boolean isDepartmentManager(Long userId) {
        Long roleUuid = roleService.selectUuidByRoleName("DepartmentManager");
        List<UserRole> userRoles = userRoleDao.selectByUserId(userId);
        for (UserRole userRole : userRoles) {
            if(userRole.getRoleID().equals(roleUuid)) {
                return true;
            }
        }
        return false;
    }
}