package com.keymanager.ckadmin.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.common.UserTree;
import com.keymanager.ckadmin.dao.ConfigDao;
import com.keymanager.ckadmin.dao.UserInfoDao;
import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.service.OrganizationService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.util.Constants;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import com.keymanager.monitoring.common.result.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * User 表数据服务层接口实现类
 */
@Service("userInfoService2")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

    @Resource(name = "userInfoDao2")
    private UserInfoDao userInfoDao;

    @Resource(name = "configDao2")
    private ConfigDao configDao;

    @Resource(name = "organizationService2")
    private OrganizationService organizationService;

    @Override
    public UserInfo getUserInfo(String loginName) {
        return userInfoDao.getUserInfo(loginName);
    }

    @Override
    public List<UserInfo> findActiveUsers() {
        Config config = configDao.getConfig(Constants.CONFIG_TYPE_EXTERNALUSER, Constants.CONFIG_KEY_EXTERNALUSER);
        return userInfoDao.findActiveUsers(config.getValue());
    }

    @Override
    public List<UserInfo> getActiveUsersByAuthority(String loginName){
        Config config = configDao.getConfig(Constants.CONFIG_TYPE_EXTERNALUSER, Constants.CONFIG_KEY_EXTERNALUSER);
        UserInfo userInfo = userInfoDao.getUserInfo(loginName);
        if (userInfo.getDataAuthority().equals("all")){
            return userInfoDao.findActiveUsers(config.getValue());
        }else if (userInfo.getDataAuthority().equals("department")){
            return userInfoDao.findActiveUsersByDepartment(config.getValue(), userInfo.getOrganizationID());
        }else{
            List<UserInfo> selfUser = new ArrayList<>();
            selfUser.add(userInfo);
            return selfUser;
        }
    }

    @Override
    public Long getUuidByLoginName(String loginName) {
        return userInfoDao.getUuidByLoginName(loginName);
    }

    @Override
    public List<Tree> selectUserInfoTrees() {
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
    public List<UserTree> selectUserInfoTrees2 () {
        List<UserTree> trees = organizationService.selectUserFulTree2();
        for (UserTree tree : trees) {
            List<UserInfo> userInfos = userInfoDao.selectUserInfos(tree.getId());
            if (CollectionUtils.isNotEmpty(userInfos)) {
                List<UserTree> userInfoTreeList = new ArrayList<>();
                for (UserInfo userInfo : userInfos) {
                    UserTree userTree = new UserTree();
                    userTree.setId(userInfo.getUuid());
                    userTree.setName(userInfo.getUserName());
                    userTree.setParentTId(tree.getId());
                    userInfoTreeList.add(userTree);
                }
                tree.setChildren(userInfoTreeList);
            }
        }
//        UserTree userTree2 = new UserTree();
//        userTree2.setId(0L);
//        userTree2.setName("请选择用户");
//        userTree2.setAlias("");
//        userTree2.setSpread(Boolean.TRUE);
//        userTree2.setChildren(trees);
//        List<UserTree> list = new ArrayList<>();
//        list.add(userTree2);
//        return list;
        return trees;
    }
    @Override
    public Object selectUserInfoTreesByAuthority(String loginName){
        List<UserTree> trees = new ArrayList<>();
        UserInfo loginUserInfo = userInfoDao.getUserInfo(loginName);
        if (loginUserInfo.getDataAuthority().equals("all")){
            trees = organizationService.selectUserFulTree2();
        }else if (loginUserInfo.getDataAuthority().equals("department")){
            trees = organizationService.selectUserFulTreeByOrganizationId(loginUserInfo.getOrganizationID());
        }
        else{
            UserTree userTree = new UserTree();
            userTree.setId(0L);
            userTree.setName("请选择用户");
            trees.add(userTree);
            return trees;
        }
        for (UserTree tree : trees) {
            List<UserInfo> userInfos = userInfoDao.selectUserInfos(tree.getId());
            if (CollectionUtils.isNotEmpty(userInfos)) {
                List<UserTree> userInfoTreeList = new ArrayList<>();
                for (UserInfo userInfo : userInfos) {
                    UserTree userTree = new UserTree();
                    userTree.setId(userInfo.getUuid());
                    userTree.setName(userInfo.getUserName());
                    userTree.setParentTId(tree.getId());
                    userInfoTreeList.add(userTree);
                }
                tree.setChildren(userInfoTreeList);
            }
        }

        return trees;
    }

    @Override
    public List<String> selectUserNames() {
        return userInfoDao.selectUserNames();
    }

    @Override
    public List<String> selectUserLoginNamesByOrganizationName(String organizationName){
        return userInfoDao.selectUserLoginNamesByOrganizationName(organizationName);
    }
}