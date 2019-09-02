package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.UserInfo;
import java.util.List;

/**
 *
 * User 表数据服务层接口
 *
 */
public interface UserInfoInterface extends IService<UserInfo> {

    List<UserInfo> findActiveUsers();
}