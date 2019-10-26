package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.UserRoleDao;
import com.keymanager.monitoring.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService extends ServiceImpl<UserRoleDao, UserRole> {
	
	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private RoleService roleService;

	public Boolean isDepartmentManager(Long userId) {
		Long roleUuid = roleService.selectUuidByRoleName("DepartmentManager");
		List<UserRole> userRoles = userRoleDao.selectByUserId(userId);
		for (UserRole userRole : userRoles) {
			if(roleUuid.equals(userRole.getRoleID())) {
				return true;
			}
		}
		return false;
	}
}
