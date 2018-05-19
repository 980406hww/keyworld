package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.RoleDao;
import com.keymanager.monitoring.entity.Role;
import com.keymanager.monitoring.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService extends ServiceImpl<RoleDao, Role> {
	
	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserRoleService userRoleService;

	public Long selectUuidByRoleName(String roleName) {
		return roleDao.selectUuidByRoleName(roleName);
	}

	public List<String> selectRoleNames(Long userId) {
		List<UserRole> userRoles = userRoleService.selectByUserId(userId);
		List<Long> roleIds = new ArrayList<Long>();
		for (UserRole userRole : userRoles) {
			roleIds.add(userRole.getRoleID());
		}
		return roleDao.selectRoleNames(roleIds);
	}
}
