package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.UserRole;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository("userRoleDao2")
public interface UserRoleDao extends BaseMapper<UserRole> {

    List<UserRole> selectByUserId(@Param("userId") Long userId);

    @Select("select fRoleID AS roleId from t_user_role where fUserID = #{userId}")
    @ResultType(Long.class)
    List<Long> selectRoleIdListByUserId(@Param("userId") Long userId);
}
