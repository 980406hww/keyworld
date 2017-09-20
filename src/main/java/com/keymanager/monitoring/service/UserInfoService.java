package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.UserInfoDao;
import com.keymanager.monitoring.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService extends ServiceImpl<UserInfoDao, UserInfo> {
	
	@Autowired
	private UserInfoDao userInfoDao;

	public Long getUuidByLoginName(String loginName) {
		return userInfoDao.getUuidByLoginName(loginName);
	}


}
