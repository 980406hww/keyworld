package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.User;

import java.util.List;

public interface UserDao extends BaseMapper<User> {

	User getUser(String userID);

	List<User> findActiveUsers();
}
