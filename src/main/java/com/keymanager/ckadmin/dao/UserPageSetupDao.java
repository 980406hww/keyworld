package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.UserPageSetup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("userPageSetupDao2")
public interface UserPageSetupDao extends BaseMapper<UserPageSetup> {

    UserPageSetup searchUserPageSetup(@Param("loginName")String loginName , @Param("pageUrl") String pageUrl);

    void updateUserPageSetup(@Param("loginName")String loginName , @Param("pageUrl") String pageUrl , @Param("hiddenField") String hiddenField);
}
