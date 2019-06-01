package com.keymanager.monitoring.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.result.Tree;
import com.keymanager.monitoring.dao.OrganizationDao;
import com.keymanager.monitoring.entity.Organization;
import com.keymanager.monitoring.service.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Organization 表数据服务层接口实现类
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationDao, Organization> implements IOrganizationService {

    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public List<Tree> selectTree() {
        List<Organization> organizationList = selectTreeGrid();
        return this.treeFixDataInfo(organizationList);
    }

    @Override
    public List<Organization> selectTreeGrid() {
        return organizationDao.selectOrganization();
    }

    @Override
    public List<Tree> selectUserFulTree () {
        List<Organization> organizationList = organizationDao.selectOrganizationUserFul();
        return this.treeFixDataInfo(organizationList);
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
}