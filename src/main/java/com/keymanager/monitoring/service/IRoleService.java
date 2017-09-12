package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.monitoring.common.result.PageInfo;
import com.keymanager.monitoring.entity.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * Role 表数据服务层接口
 *
 */
public interface IRoleService extends IService<Role> {

    void selectDataGrid(PageInfo pageInfo);

    Object selectTree();

    List<Long> selectResourceIdListByRoleId(Long id);

    void updateRoleResource(Long id, String resourceIds);

    Map<String, Set<String>> selectResourceMapByUserId(Long userId);

}