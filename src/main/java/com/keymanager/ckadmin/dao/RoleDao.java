package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.Resource;
import com.keymanager.ckadmin.entity.Role;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * Role 表数据库控制层接口
 *
 */
@Repository("roleDao2")
public interface RoleDao extends BaseMapper<Role> {

    List<Long> selectResourceIdListByRoleId(@Param("id") Long id);

    List<Resource> selectResourceListByRoleIdList(@Param("list") List<Long> list);

    List<Map<String, String>> selectResourceListByRoleId(@Param("condition") Map<String, Object> condition);

    Long selectUuidByRoleName(@Param("roleName") String roleName);
}