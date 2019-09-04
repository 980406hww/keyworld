package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.UserRole;
import org.springframework.stereotype.Repository;

@Repository("userRoleDao2")
public interface UserRoleDao extends BaseMapper<UserRole> {
}
