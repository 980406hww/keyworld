package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.UserPageSetupDao;
import com.keymanager.ckadmin.entity.UserPageSetup;
import com.keymanager.ckadmin.service.UserPageSetupService;
import com.keymanager.util.Utils;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("userPageSetupService2")
public class UserPageSetupServiceImpl extends ServiceImpl<UserPageSetupDao, UserPageSetup> implements UserPageSetupService {

    @Resource(name = "userPageSetupDao2")
    private UserPageSetupDao userPageSetupDao;

    @Override
    public UserPageSetup searchUserPageSetup(String loginName,String pageUrl ){
        return userPageSetupDao.searchUserPageSetup(loginName,pageUrl);
    }

    @Override
    public void updateUserPageSetup(String loginName,String pageUrl ,String hiddenField){
        userPageSetupDao.updateUserPageSetup(loginName,pageUrl ,hiddenField);
    }

    @Override
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
