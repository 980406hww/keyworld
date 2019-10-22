package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.common.result.Menu;
import com.keymanager.ckadmin.dao.ResourceDao;
import com.keymanager.ckadmin.entity.Resource;
import com.keymanager.ckadmin.service.ResourceService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Resource 表数据服务层接口实现类
 */
@Service("resourceService2")
public class ResourceServiceImpl extends ServiceImpl<ResourceDao, Resource> implements ResourceService {

    // 菜单
    private static final int RESOURCE_MENU = 0;

    @javax.annotation.Resource(name = "resourceDao2")
    private ResourceDao resourceDao;

    @Override
    public List<Menu> selectAuthorizationResource(String loginName, Long parentId) {
        List<Menu> menus = new ArrayList<>();
        List<Resource> resources = resourceDao.selectAuthorizationResource(loginName, parentId);
        for (Resource resource : resources) {
            Menu menu = new Menu();
            menu.setTitle(resource.getResourceName());
            menu.setIcon("");
            menu.setSpread(false);
            menu.setHref(resource.getUrl());
            List<Menu> children = selectAuthorizationResource(loginName, resource.getId());
            menu.setChildren(children);
            menus.add(menu);
        }
        return menus;
    }
}