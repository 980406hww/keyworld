package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.UserPageSetup;

public interface UserPageSetupService extends IService<UserPageSetup> {

    public UserPageSetup searchUserPageSetup(String loginName,String pageUrl );

    public void updateUserPageSetup(String loginName,String pageUrl ,String hiddenField);

    public void addUserPageSetup(String loginName,String pageUri,String HiddenColumns);
}
