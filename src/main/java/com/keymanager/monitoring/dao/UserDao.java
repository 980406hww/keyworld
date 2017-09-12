package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDao extends BaseMapper<User> {

	User getUser(String userID);

	List<User> findActiveUsers();

	UserVO selectUserVoById(@Param("id") Long id);

	List<Map<String, Object>> selectUserPage(Pagination page, Map<String, Object> params);

	List<User> searchUsers();
}
