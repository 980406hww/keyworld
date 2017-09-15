package com.keymanager.monitoring.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.result.Tree;
import com.keymanager.monitoring.common.shiro.ShiroUser;
import com.keymanager.monitoring.dao.ResourceDao;
import com.keymanager.monitoring.dao.RoleDao;
import com.keymanager.monitoring.dao.RoleResourceDao;
import com.keymanager.monitoring.dao.UserRoleDao;
import com.keymanager.monitoring.entity.Resource;
import com.keymanager.monitoring.service.IResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * Resource 表数据服务层接口实现类
 *
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceDao, Resource> implements IResourceService {
    private static final int RESOURCE_MENU = 0; // 菜单

    @Autowired
    private ResourceDao resourceDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleResourceDao roleResourceDao;
    
    @Override
    public List<Resource> selectAll() {
        EntityWrapper<Resource> wrapper = new EntityWrapper<Resource>();
        wrapper.orderBy("fSequence");
        return resourceDao.selectList(wrapper);
    }
    
    public List<Resource> selectByType(Integer type) {
        EntityWrapper<Resource> wrapper = new EntityWrapper<Resource>();
        Resource resource = new Resource();
        wrapper.setEntity(resource);
        wrapper.addFilter("fResourceType = {0}", type);
        wrapper.orderBy("fSequence");
        return resourceDao.selectList(wrapper);
    }
    
    @Override
    public List<Tree> selectAllMenu() {
        List<Tree> trees = new ArrayList<Tree>();
        // 查询所有菜单
        List<Resource> resources = this.selectByType(RESOURCE_MENU);
        if (resources == null) {
            return trees;
        }
        for (Resource resource : resources) {
            Tree tree = new Tree();
            tree.setId(resource.getId());
            tree.setPid(resource.getParentID());
            tree.setText(resource.getResourceName());
            tree.setIconCls(resource.getIcon());
            tree.setAttributes(resource.getUrl());
            tree.setState(resource.getOpened());
            trees.add(tree);
        }
        return trees;
    }
    
    @Override
    public List<Tree> selectAuthorizationMenu(String loginName) {
        List<Tree> trees = new ArrayList<Tree>();
        // 查询权限菜单
        List<Resource> resources = resourceDao.selectAuthorizationMenu(loginName);
        if (resources == null) {
            return trees;
        }
        for (Resource resource : resources) {
            Tree tree = new Tree();
            tree.setId(resource.getId());
            tree.setPid(resource.getParentID());
            tree.setText(resource.getResourceName());
            tree.setIconCls(resource.getIcon());
            tree.setAttributes(resource.getUrl());
            tree.setState(resource.getOpened());
            trees.add(tree);
        }
        return trees;
    }

    @Override
    public List<Tree> selectAllTree() {
        // 获取所有的资源 tree形式，展示
        List<Tree> trees = new ArrayList<Tree>();
        List<Resource> resources = this.selectAll();
        if (resources == null) {
            return trees;
        }
        for (Resource resource : resources) {
            Tree tree = new Tree();
            tree.setId(resource.getId());
            tree.setPid(resource.getParentID());
            tree.setText(resource.getResourceName());
            tree.setIconCls(resource.getIcon());
            tree.setAttributes(resource.getUrl());
            tree.setState(resource.getOpened());
            trees.add(tree);
        }
        return trees;
    }
    
    @Override
    public List<Tree> selectTree(ShiroUser shiroUser) {
        List<Tree> trees = new ArrayList<Tree>();
        // shiro中缓存的用户角色
        Set<String> roles = shiroUser.getRoles();
        if (roles == null) {
            return trees;
        }
        // 如果有超级管理员权限
        if (roles.contains("admin")) {
            List<Resource> resourceList = this.selectByType(RESOURCE_MENU);
            if (resourceList == null) {
                return trees;
            }
            for (Resource resource : resourceList) {
                Tree tree = new Tree();
                tree.setId(resource.getId());
                tree.setPid(resource.getParentID());
                tree.setText(resource.getResourceName());
                tree.setIconCls(resource.getIcon());
                tree.setAttributes(resource.getUrl());
                tree.setOpenMode(resource.getOpenMode());
                tree.setState(resource.getOpened());
                trees.add(tree);
            }
            return trees;
        }
        // 普通用户
        List<Long> roleIdList = userRoleDao.selectRoleIdListByUserId(shiroUser.getId());
        if (roleIdList == null || roleIdList.isEmpty()) {
            return trees;
        }
        List<Resource> resourceLists = roleDao.selectResourceListByRoleIdList(roleIdList);
        if (resourceLists == null || resourceLists.isEmpty()) {
            return trees;
        }
        for (Resource resource : resourceLists) {
            Tree tree = new Tree();
            tree.setId(resource.getId());
            tree.setPid(resource.getParentID());
            tree.setText(resource.getResourceName());
            tree.setIconCls(resource.getIcon());
            tree.setAttributes(resource.getUrl());
            tree.setOpenMode(resource.getOpenMode());
            tree.setState(resource.getOpened());
            trees.add(tree);
        }
        return trees;
    }

	@Override
	public boolean deleteById(Serializable resourceId) {
		roleResourceDao.deleteByResourceId(resourceId);
		return super.deleteById(resourceId);
	}
}