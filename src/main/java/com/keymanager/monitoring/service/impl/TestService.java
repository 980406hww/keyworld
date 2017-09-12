package com.keymanager.monitoring.service.impl;

import com.keymanager.monitoring.dao.UserDao;
import com.keymanager.monitoring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class TestService {
    @Autowired
    private UserDao userDao;
	
    @Cacheable(value = "hour", key = "#id")
	public User selectById(Serializable id) {
		return userDao.selectById(id);
	}
}
