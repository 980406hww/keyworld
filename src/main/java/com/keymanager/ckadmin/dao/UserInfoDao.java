package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.UserInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * UserInfo 表数据库控制层接
 */
@Repository("userInfoDao2")
public interface UserInfoDao extends BaseMapper<UserInfo> {

    List<UserInfo> findActiveUsers(@Param("externalLoginName") String externalLoginName);

    Long getUuidByLoginName(@Param("loginName") String loginName);

    List<UserInfo> selectUserInfos (@Param("organizationID") long organizationID);

    UserInfo getUserInfo(@Param("loginName")String loginName);

    List<String> selectUserNames();

    List<String> selectUserLoginNamesByOrganizationName(@Param("organizationName") String organizationName);
}