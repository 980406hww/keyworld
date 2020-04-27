package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.monitoring.common.result.Tree;
import com.keymanager.ckadmin.common.shiro.ShiroUser;
import com.keymanager.monitoring.entity.Resource;
import java.util.List;

/**
 * Resource 表数据服务层接口
 */
public interface IResourceService extends IService<Resource> {

    List<Resource> selectAll();

    List<Tree> selectAllMenu();

    List<Tree> selectAuthorizationMenu(String loginName, String version);

    List<Tree> selectAllTree();

    List<Tree> selectTree(ShiroUser shiroUser);

    void updResourceById(Resource resource);
}