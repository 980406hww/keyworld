package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ClientStatusCriteria;
import com.keymanager.monitoring.entity.ClientStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientStatusDao extends BaseMapper<ClientStatus> {

    List<ClientStatus> searchClientStatuses(Page<ClientStatus> page, @Param("clientStatusCriteria") ClientStatusCriteria clientStatusCriteria);

}
