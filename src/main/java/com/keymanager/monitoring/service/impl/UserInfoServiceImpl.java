package com.keymanager.monitoring.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.result.PageInfo;
import com.keymanager.monitoring.common.result.Tree;
import com.keymanager.monitoring.common.utils.BeanUtils;
import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.dao.UserInfoDao;
import com.keymanager.monitoring.dao.UserRoleDao;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.entity.UserRole;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.monitoring.service.IOrganizationService;
import com.keymanager.monitoring.service.IUserInfoService;
import com.keymanager.monitoring.vo.UserVO;
import com.keymanager.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * User 表数据服务层接口实现类
 *
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements IUserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private ConfigService configService;
    @Autowired
    private IOrganizationService organizationService;

    @Override
    public UserInfo getUserInfo(String loginName) {
        return userInfoDao.getUserInfo(loginName);
    }

    @Override
    public List<UserInfo> findActiveUsers() {
        Config config = configService.getConfig(Constants.CONFIG_TYPE_EXTERNALUSER, Constants.CONFIG_KEY_EXTERNALUSER);
        return userInfoDao.findActiveUsers(config.getValue());
    }

    public List<UserInfo> selectByLoginName(UserVO userVo) {
        if(null != userVo.getLoginName()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setLoginName(userVo.getLoginName());
            EntityWrapper<UserInfo> wrapper = new EntityWrapper<UserInfo>(userInfo);
            if (null != userVo.getUserUuid()) {
                wrapper.where("fuuid != {0}", userVo.getUserUuid());
            }
            return this.selectList(wrapper);
        }
        return null;
    }

    public void insertByVo(UserVO userVo) {
        UserInfo userInfo = BeanUtils.copy(userVo, UserInfo.class);
        this.insert(userInfo);

        Long userUuid = userInfo.getUuid();
        String[] roles = userVo.getRoleIds().split(",");
        UserRole userRole = new UserRole();
        for (String string : roles) {
            userRole.setUserID(userUuid);
            userRole.setRoleID(Long.valueOf(string));
            userRoleDao.insert(userRole);
        }
    }

    public UserVO selectVoById(Long id) {
        return userInfoDao.selectUserVoById(id);
    }

    public void updateByVo(UserVO userVo) {
        UserInfo user = BeanUtils.copy(userVo, UserInfo.class);
        user.setUuid(userVo.getUserUuid());
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
    public void updatePwdByUserId(Long userUuid, String md5Hex,String salt) {
        UserInfo user = new UserInfo();
        user.setUuid(userUuid);
        user.setSalt(salt);
        user.setPassword(md5Hex);
        this.updateById(user);
    }

    @Override
    public void selectDataGrid(PageInfo pageInfo) {
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<Map<String, Object>> list = userInfoDao.selectUserPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
    }

    @Override
    public void deleteUserById(Long id) {
        this.deleteById(id);
        userRoleDao.deleteByUserId(id);
    }

    @Override
    public Long getUuidByLoginName(String loginName) {
        return userInfoDao.getUuidByLoginName(loginName);
    }

    @Override
    public List<Tree> selectUserInfoTrees () {
        List<Tree> trees = organizationService.selectUserFulTree();
        for (Tree tree : trees) {
            List<UserInfo> userInfos = userInfoDao.selectUserInfos(tree.getId());
            if (CollectionUtils.isNotEmpty(userInfos)) {
                List<Tree> userInfoTreeList = new ArrayList<>();
                for (UserInfo userInfo : userInfos) {
                    Tree userInfoTree = new Tree();
                    userInfoTree.setId(userInfo.getUuid());
                    userInfoTree.setText(userInfo.getUserName());
                    userInfoTree.setPid(tree.getId());
                    userInfoTreeList.add(userInfoTree);
                }
                tree.setState(0);
                tree.setChildren(userInfoTreeList);
            }
        }
        return trees;
    }

    @Override
    public String getOrganizationNameByLoginName(String loginName) {
        return userInfoDao.getOrganizationNameByLoginName(loginName);
    }
}