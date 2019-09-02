package com.keymanager.ckadmin.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.ConfigDao2;
import com.keymanager.ckadmin.dao.UserInfoDao2;
import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.service.UserInfoInterface;
import com.keymanager.ckadmin.util.Constants;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * User 表数据服务层接口实现类
 *
 */
@Service("userInfoSerice2")
public class UserInfoService2 extends ServiceImpl<UserInfoDao2, UserInfo> implements UserInfoInterface {

    @Resource(name = "userInfoDao2")
    private UserInfoDao2 userInfoDao2;

    @Resource(name = "configDao2")
    private ConfigDao2 configDao2;

    @Override
    public List<UserInfo> findActiveUsers() {
        Config config = configDao2.getConfig(Constants.CONFIG_TYPE_EXTERNALUSER, Constants.CONFIG_KEY_EXTERNALUSER);
        return userInfoDao2.findActiveUsers(config.getValue());
    }

}