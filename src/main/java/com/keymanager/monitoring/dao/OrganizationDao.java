package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.Organization;

import java.util.List;

/**
 * Organization 表数据库控制层接口
 */
public interface OrganizationDao extends BaseMapper<Organization> {

    List<Organization> selectOrganization();

    List<Organization> selectOrganizationUserFul ();
}