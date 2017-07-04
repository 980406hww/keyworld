package com.keymanager.monitoring.service;

import com.keymanager.monitoring.dao.UserDao;
import com.keymanager.monitoring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserDao, User>{
	
	public static final String HASH_ALGORITHM = "SHA-1";
	
	public static final int HASH_INTERATIONS = 1024;
	
	private static final int SALT_SIZE = 8;
	
	@Autowired
	private UserDao userDao;

	public User getUser(String userID){
		return userDao.getUser(userID);
	}

	public List<User> findActiveUsers(){
		return userDao.findActiveUsers();
	}
}
