package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.Organization;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("organizationDao2")
public interface OrganizationDao extends BaseMapper<Organization> {

    List<Organization> selectOrganizationUserFul ();
}
