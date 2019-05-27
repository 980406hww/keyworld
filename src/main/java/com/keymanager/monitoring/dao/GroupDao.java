package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.GroupSettingCriteria;
import com.keymanager.monitoring.entity.Group;
import com.keymanager.monitoring.vo.GroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/4/27 9:41
 **/
public interface GroupDao extends BaseMapper<Group> {

    List<GroupVO> searchGroups (Page<GroupVO> page, @Param("groupSettingCriteria") GroupSettingCriteria groupSettingCriteria);

    void saveGroup (@Param("groupName") String groupName, @Param("terminalType") String terminalType, @Param("createBy") String createBy, @Param("remainingAccount") int remainingAccount, @Param("maxInvalidCount") int maxInvalidCount);

    long lastInsertID ();

    void updateGroupRemainingAccount (@Param("uuid") Long uuid, @Param("remainingAccount") int remainingAccount);

    Group findGroup(@Param("groupName") String groupName, @Param("terminalType") String terminalType);

    List<String> getOptimizationGroups(@Param("terminalType") String terminalType);

    void updateMaxInvalidCount(@Param("uuid") long uuid,@Param("maxInvalidCount") int maxInvalidCount);
}
