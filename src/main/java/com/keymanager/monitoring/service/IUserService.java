package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.monitoring.common.result.PageInfo;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.vo.UserVO;

import java.util.List;

/**
 *
 * User 表数据服务层接口
 *
 */
public interface IUserService extends IService<User> {

    List<UserInfo> selectByLoginName(UserVO userVo);

    void insertByVo(UserVO userVo);

    UserVO selectVoById(Long id);

    void updateByVo(UserVO userVo);

    void updatePwdByUserId(Long userId, String md5Hex);

    void selectDataGrid(PageInfo pageInfo);

    void deleteUserById(Long id);
}