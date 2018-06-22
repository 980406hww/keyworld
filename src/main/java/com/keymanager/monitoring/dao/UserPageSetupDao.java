package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.keymanager.monitoring.entity.UserPageSetup;
import org.apache.ibatis.annotations.Param;


public interface UserPageSetupDao extends BaseMapper<UserPageSetup> {

    UserPageSetup searchUserPageSetup(@Param("loginName")String loginName , @Param("pageUrl") String pageUrl);

    void updateUserPageSetup(@Param("loginName")String loginName , @Param("pageUrl") String pageUrl , @Param("hiddenField") String hiddenField);
}
