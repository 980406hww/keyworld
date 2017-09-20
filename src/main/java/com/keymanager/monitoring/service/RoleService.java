package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.RoleDao;
import com.keymanager.monitoring.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends ServiceImpl<RoleDao, Role> {
	
	@Autowired
	private RoleDao roleDao;

	public Long selectUuidByRoleName(String roleName) {
		return roleDao.selectUuidByRoleName(roleName);
	}
}
