package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.UserInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 *
 * UserInfo 表数据库控制层接
 *
 */
public interface UserInfoDao2 extends BaseMapper<UserInfo> {

    List<UserInfo> findActiveUsers(@Param("externalLoginName") String externalLoginName);

}