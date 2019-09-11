package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.common.UserTree;
import com.keymanager.ckadmin.entity.Organization;
import com.keymanager.monitoring.common.result.Tree;

import java.util.List;

public interface OrganizationService extends IService<Organization> {

    List<Tree> selectUserFulTree ();

    List<UserTree> selectUserFulTree2();
}
