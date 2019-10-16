package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.Role;
import com.keymanager.monitoring.common.result.PageInfo;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Role 表数据服务层接口
 */
public interface RoleService extends IService<Role> {

    List<Long> selectResourceIdListByRoleId(Long id);

    void updateRoleResource(Long id, String resourceIds);

    Map<String, Set<String>> selectResourceMapByUserId(Long userId, String version);

    Long selectUuidByRoleName(String departmentManager);
}