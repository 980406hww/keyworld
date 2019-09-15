package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.Group;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author zhoukai
 * @Date 2019/9/10 17:41
 **/
@Repository("groupDao2")
public interface GroupDao extends BaseMapper<Group> {

    Group findGroupByGroupName (@Param("groupName") String groupName);

    void deleteByGroupName(@Param("groupName") String groupName);
}
