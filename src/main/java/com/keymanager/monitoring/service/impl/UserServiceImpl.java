package com.keymanager.monitoring.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.result.PageInfo;
import com.keymanager.monitoring.common.utils.BeanUtils;
import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.dao.UserDao;
import com.keymanager.monitoring.dao.UserInfoDao;
import com.keymanager.monitoring.dao.UserRoleDao;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.entity.UserRole;
import com.keymanager.monitoring.service.IUserService;
import com.keymanager.monitoring.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * User 表数据服务层接口实现类
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {

    @Autowired
    private UserInfoDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;
    
    public List<UserInfo> selectByLoginName(UserVO userVo) {
        User user = new User();
        user.setUserName(userVo.getLoginName());
        EntityWrapper<User> wrapper = new EntityWrapper<User>(user);
        if (null != userVo.getUserUuid()) {
            wrapper.where("id != {0}", userVo.getUserUuid());
        }
        return userDao.searchUsers();
    }

    public void insertByVo(UserVO userVo) {
        User user = BeanUtils.copy(userVo, User.class);
        user.setCreateTime(new Date());
        this.insert(user);

        Long userUuid = user.getUuid();
        String[] roles = userVo.getRoleIds().split(",");
        UserRole userRole = new UserRole();
        for (String string : roles) {
            userRole.setUserID(userUuid);
            userRole.setRoleID(Long.valueOf(string));
            userRoleDao.insert(userRole);
        }
    }

    public UserVO selectVoById(Long id) {
        return userDao.selectUserVoById(id);
    }

    public void updateByVo(UserVO userVo) {
        User user = BeanUtils.copy(userVo, User.class);
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        }
        this.updateById(user);
        
        Long id = userVo.getUserUuid();
        List<UserRole> userRoles = userRoleDao.selectByUserId(id);
        if (userRoles != null && !userRoles.isEmpty()) {
            for (UserRole userRole : userRoles) {
                userRoleDao.deleteById(userRole.getUuid());
            }
        }

        String[] roles = userVo.getRoleIds().split(",");
        UserRole userRole = new UserRole();
        for (String string : roles) {
            userRole.setUserID(id);
            userRole.setRoleID(Long.valueOf(string));
            userRoleDao.insert(userRole);
        }
    }

    @Override
    public void updatePwdByUserId(Long userUuid, String md5Hex) {
        User user = new User();
        user.setUuid(userUuid);
        user.setPassword(md5Hex);
        this.updateById(user);
    }

    @Override
    public void selectDataGrid(PageInfo pageInfo) {
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<Map<String, Object>> list = userDao.selectUserPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
    }

    @Override
    public void deleteUserById(Long id) {
        this.deleteById(id);
        userRoleDao.deleteByUserId(id);
    }

}