package com.keymanager.ckadmin.service;


import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.common.result.Menu;
import com.keymanager.ckadmin.entity.Resource;
import java.util.List;

/**
 *
 * Resource 表数据服务层接口
 *
 */
public interface ResourceService extends IService<Resource> {

    List<Menu> selectAuthorizationResource(String loginName, Long parentId);
}