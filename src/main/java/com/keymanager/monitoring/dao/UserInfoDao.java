package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * UserInfo 表数据库控制层接
 *
 */
public interface UserInfoDao extends BaseMapper<UserInfo> {


    UserVO selectUserVoById(@Param("id") Long id);

    List<Map<String, Object>> selectUserPage(Pagination page, Map<String, Object> params);

    List<UserInfo> searchUsers();

    UserInfo getUserInfo(@Param("loginName")String loginName);

    List<UserInfo> findActiveUsers(@Param("externalLoginName") String externalLoginName);

    Long getUuidByLoginName(@Param("loginName")String loginName);

    List<UserInfo> selectUserInfos (@Param("organizationID") long organizationID);
}