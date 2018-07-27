package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerChargeLog;
import com.keymanager.monitoring.entity.ProjectFollow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectFollowDao extends BaseMapper<ProjectFollow> {

    List<ProjectFollow> findProjectFollows(@Param("customerUuid") Integer customerUuid);

    void deleteProjectFollows(@Param("customerUuid")Integer customerUuid);
}
