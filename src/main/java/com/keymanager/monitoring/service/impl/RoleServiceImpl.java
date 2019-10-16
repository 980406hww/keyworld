package com.keymanager.monitoring.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.result.PageInfo;
import com.keymanager.monitoring.common.result.Tree;
import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.dao.RoleDao;
import com.keymanager.monitoring.dao.RoleResourceDao;
import com.keymanager.monitoring.dao.UserRoleDao;
import com.keymanager.monitoring.entity.Role;
import com.keymanager.monitoring.entity.RoleResource;
import com.keymanager.monitoring.service.IRoleService;
import com.keymanager.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Role 表数据服务层接口实现类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements IRoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RoleResourceDao roleResourceDao;

    public List<Role> selectAll() {
        EntityWrapper<Role> wrapper = new EntityWrapper<Role>();
        wrapper.orderBy("fSequence");
        return roleDao.selectList(wrapper);
    }

    @Override
    public void selectDataGrid(PageInfo pageInfo) {
        Page<Role> page = new Page<Role>(pageInfo.getNowpage(), pageInfo.getSize());

        EntityWrapper<Role> wrapper = new EntityWrapper<Role>();
        wrapper.orderBy(pageInfo.getSort(), pageInfo.getOrder().equalsIgnoreCase("ASC"));
        selectPage(page, wrapper);

        pageInfo.setRows(page.getRecords());
        pageInfo.setTotal(page.getTotal());
    }

    @Override
    public Object selectTree() {
        List<Tree> trees = new ArrayList<Tree>();
        List<Role> roles = this.selectAll();
        for (Role role : roles) {
            Tree tree = new Tree();
            tree.setId(role.getUuid());
            tree.setText(role.getRoleName());
            trees.add(tree);
        }
        return trees;
    }

    @Override
    public void updateRoleResource(Long roleId, String resourceIds) {
        // 先删除后添加,有点爆力
        RoleResource roleResource = new RoleResource();
        roleResource.setRoleID(roleId);
        roleResourceDao.delete(new EntityWrapper<>(roleResource));

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

}