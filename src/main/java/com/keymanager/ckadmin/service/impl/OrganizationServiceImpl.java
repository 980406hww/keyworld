package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.common.UserTree;
import com.keymanager.ckadmin.dao.OrganizationDao;
import com.keymanager.ckadmin.entity.Organization;
import com.keymanager.ckadmin.service.OrganizationService;
import com.keymanager.monitoring.common.result.Tree;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("organizationService2")
public class OrganizationServiceImpl
        extends ServiceImpl<OrganizationDao, Organization> implements OrganizationService {

    @Resource(name = "organizationDao2")
    private OrganizationDao organizationDao;

    @Override
    public List<Tree> selectUserFulTree () {
        List<Organization> organizationList = organizationDao.selectOrganizationUserFul();
        return this.treeFixDataInfo(organizationList);
    }

    @Override
    public List<UserTree> selectUserFulTree2 () {
        List<Organization> organizationList = new ArrayList<>();
        Organization organization = new Organization();
        organization.setOrganizationName("请选择用户");
        organization.setId(0L);
        organizationList.add(organization);
        organizationList.addAll(organizationDao.selectOrganizationUserFul());
        return this.treeFixDataInfo2(organizationList);
    }

    @Override
    public List<UserTree> selectUserFulTreeByOrganizationId (Integer organizationId){
        List<Organization> organizationList = new ArrayList<>();
        Organization organization = new Organization();
        organization.setOrganizationName("请选择用户");
        organization.setId(0L);
        organizationList.add(organization);
        organizationList.add(organizationDao.selectOrganizationUserFulByOrganizationId(organizationId));
        return this.treeFixDataInfo2(organizationList);
    }

    private List<Tree> treeFixDataInfo(List<Organization> organizationList) {
        List<Tree> trees = new ArrayList<>();
        if (organizationList != null) {
            for (Organization organization : organizationList) {
                Tree tree = new Tree();
                tree.setId(organization.getId());
                tree.setText(organization.getOrganizationName());
                tree.setIconCls(organization.getIcon());
                tree.setPid(organization.getPid());
                trees.add(tree);
            }
        }
        return trees;
    }

    private List<UserTree> treeFixDataInfo2(List<Organization> organizationList) {
        List<UserTree> trees = new ArrayList<>();
        if (organizationList != null) {
            for (Organization organization : organizationList) {
                UserTree tree = new UserTree();
                tree.setId(organization.getId());
                tree.setName(organization.getOrganizationName());
                tree.setAlias("");
                tree.setSpread(Boolean.TRUE);
                trees.add(tree);
            }
        }
        return trees;
    }

}
