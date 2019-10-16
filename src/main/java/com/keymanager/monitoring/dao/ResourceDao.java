package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.Resource;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Resource 表数据库控制层接口
 */
public interface ResourceDao extends BaseMapper<Resource> {

    List<Resource> selectAuthorizationMenu(@Param("loginName") String loginName, @Param("version") String version);

    List<Map<String, Object>> selectTreeByPid(@Param("pid") Long pid);

    void updChildVersion(@Param("version") String version, @Param("items") List<Map<String, Object>> children);
}