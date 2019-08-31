package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.common.result.Menu;
import com.keymanager.ckadmin.dao.ResourceDao2;
import com.keymanager.ckadmin.entity.Resource;
import com.keymanager.ckadmin.service.ResourceInterface;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * Resource 表数据服务层接口实现类
 *
 */
@Service("resourceService2")
public class ResourceService2 extends ServiceImpl<ResourceDao2, Resource> implements ResourceInterface {
    private static final int RESOURCE_MENU = 0; // 菜单

    @javax.annotation.Resource(name = "resourceDao2")
    private ResourceDao2 resourceDao2;

    @Override
    public List<Menu> selectAuthorizationResource(String loginName, Long parentId) {
        List<Menu> menus = new ArrayList<>();
        List<Resource> resources = resourceDao2.selectAuthorizationResource(loginName, parentId);
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