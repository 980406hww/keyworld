package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.keymanager.ckadmin.entity.Resource;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 *
 * Resource 表数据库控制层接口
 *
 */
@Component("resourceDao2")
public interface ResourceDao2 extends BaseMapper<Resource> {

    List<Resource> selectAuthorizationMenu(@Param("loginName") String loginName);

    List<Resource> selectAuthorizationResource(@Param("loginName") String loginName, @Param("parentId") Long parentId);
}