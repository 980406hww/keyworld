package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.UserPageSetupDao;
import com.keymanager.monitoring.entity.UserPageSetup;
import com.keymanager.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPageSetupService extends ServiceImpl<UserPageSetupDao,UserPageSetup>{
	
	@Autowired
	private UserPageSetupDao userPageSetupDao;

	public UserPageSetup searchUserPageSetup(String loginName,String pageUrl ){
		return userPageSetupDao.searchUserPageSetup(loginName,pageUrl);
	}

	public void updateUserPageSetup(String loginName,String pageUrl ,String hiddenField){
		userPageSetupDao.updateUserPageSetup(loginName,pageUrl ,hiddenField);
	}

	public void addUserPageSetup(String loginName,String pageUri,String HiddenColumns){
		UserPageSetup userPageSetup = new UserPageSetup();
		userPageSetup.setLoginName(loginName);
		userPageSetup.setPageUrl(pageUri);
		userPageSetup.setHiddenField(HiddenColumns);
		userPageSetup.setUpdateTime(Utils.getCurrentTimestamp());
		userPageSetup.setCreateTime(Utils.getCurrentTimestamp());
		userPageSetupDao.insert(userPageSetup);
	}
}
