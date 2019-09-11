package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.monitoring.common.result.Tree;

import java.util.List;

/**
 *
 * User 表数据服务层接口
 *
 */
public interface UserInfoService extends IService<UserInfo> {

    List<UserInfo> findActiveUsers();

    List<Tree> selectUserInfoTrees ();

    Object selectUserInfoTrees2();
}