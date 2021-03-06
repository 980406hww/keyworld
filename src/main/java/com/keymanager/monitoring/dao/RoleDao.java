package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.Resource;
import com.keymanager.monitoring.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Role 表数据库控制层接口
 */
public interface RoleDao extends BaseMapper<Role> {

    List<Long> selectResourceIdListByRoleId(@Param("id") Long id);

    List<Resource> selectResourceListByRoleIdList(@Param("list") List<Long> list);

    List<Map<String, String>> selectResourceListByRoleId(@Param("condition") Map<String, Object> condition);

    Long selectUuidByRoleName(@Param("roleName") String roleName);
}