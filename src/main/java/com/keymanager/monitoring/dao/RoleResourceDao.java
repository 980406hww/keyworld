package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.RoleResource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;

/**
 *
 * RoleResource 表数据库控制层接口
 *
 */
public interface RoleResourceDao extends BaseMapper<RoleResource> {

    @Select("SELECT rr.fUuid AS id FROM t_role r LEFT JOIN t_role_resource_new rr ON r.fUuid = rr.fRoleID WHERE r.id = #{id}")
    Long selectIdListByRoleId(@Param("id") Long id);

    @Delete("DELETE FROM t_role_resource_new WHERE fResourceID  = #{resourceId} OR fParentID = #{resourceId}")
    int deleteByResourceId(@Param("resourceId") Serializable resourceId);

}