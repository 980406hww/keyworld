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

}