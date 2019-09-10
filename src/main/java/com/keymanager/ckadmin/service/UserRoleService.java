package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.UserRole;

/**
 *
 * UserRole 表数据服务层接口
 *
 */
public interface UserRoleService extends IService<UserRole> {


    Boolean isDepartmentManager(Long uuidByLoginName);
}