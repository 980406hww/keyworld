package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.keymanager.ckadmin.dao.RoleDao;
import com.keymanager.ckadmin.dao.RoleResourceDao;
import com.keymanager.ckadmin.dao.UserRoleDao;
import com.keymanager.ckadmin.entity.Role;
import com.keymanager.ckadmin.service.RoleService;
import com.keymanager.monitoring.common.result.PageInfo;
import com.keymanager.monitoring.entity.RoleResource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Role 表数据服务层接口实现类
 */
@Service("roleService2")
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    @Resource(name = "roleDao2")
    private RoleDao roleDao;

    @Resource(name = "userRoleDao2")
    private UserRoleDao userRoleDao;

    @Resource(name = "roleResourceDao2")
    private RoleResourceDao roleResourceDao;

    public List<Role> selectAll() {
        EntityWrapper<Role> wrapper = new EntityWrapper<Role>();
        wrapper.orderBy("fSequence");
        return roleDao.selectList(wrapper);
    }


    @Override
    public void updateRoleResource(Long roleId, String resourceIds) {
        // 先删除后添加,有点爆力
        RoleResource roleResource = new RoleResource();
        roleResource.setRoleID(roleId);
        roleResourceDao.delete(new EntityWrapper<RoleResource>(roleResource));

        if (!StringUtils.isBlank(resourceIds)) {
            String[] resourceIdArray = resourceIds.split(",");
            for (String resourceId : resourceIdArray) {
                roleResource = new RoleResource();
                roleResource.setRoleID(roleId);
                roleResource.setResourceID(Long.parseLong(resourceId));
                roleResourceDao.insert(roleResource);
            }
        }
    }

    @Override
    public List<Long> selectResourceIdListByRoleId(Long id) {
        return roleDao.selectResourceIdListByRoleId(id);
    }

    @Override
    public Map<String, Set<String>> selectResourceMapByUserId(Long userId, String version) {
        Map<String, Set<String>> resourceMap = new HashMap<>();
        List<Long> roleIdList = userRoleDao.selectRoleIdListByUserId(userId);
        Set<String> urlSet = new HashSet<>();
        Set<String> roles = new HashSet<>();
        Map<String, Object> condition = new HashMap<>(2);
        condition.put("version", version);
        for (Long roleId : roleIdList) {
            condition.put("id", roleId);
            List<Map<String, String>> resourceList = roleDao.selectResourceListByRoleId(condition);
            if (resourceList != null) {
                for (Map<String, String> map : resourceList) {
                    if (map != null && StringUtils.isNotBlank(map.get("url"))) {
                        urlSet.add(map.get("url"));
                        roles.add(map.get("roleName"));
                    }
                }
            }
            Role role = roleDao.selectById(roleId);
            if (role != null) {
                roles.add(role.getRoleName());
            }
        }
        resourceMap.put("urls", urlSet);
        resourceMap.put("roles", roles);
        return resourceMap;
    }

    @Override
    public Long selectUuidByRoleName(String roleName) {
        return roleDao.selectUuidByRoleName(roleName);
    }

}