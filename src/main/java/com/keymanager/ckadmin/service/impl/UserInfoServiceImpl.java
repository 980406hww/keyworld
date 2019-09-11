package com.keymanager.ckadmin.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.ConfigDao;
import com.keymanager.ckadmin.dao.UserInfoDao;
import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.util.Constants;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * User 表数据服务层接口实现类
 */
@Service("userInfoService2")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

    @Resource(name = "userInfoDao2")
    private UserInfoDao userInfoDao;

    @Resource(name = "configDao2")
    private ConfigDao configDao;

    @Override
    public List<UserInfo> findActiveUsers() {
        Config config = configDao.getConfig(Constants.CONFIG_TYPE_EXTERNALUSER, Constants.CONFIG_KEY_EXTERNALUSER);
        return userInfoDao.findActiveUsers(config.getValue());
    }

    @Override
    public Long getUuidByLoginName(String loginName) {
        return userInfoDao.getUuidByLoginName(loginName);
    }

}