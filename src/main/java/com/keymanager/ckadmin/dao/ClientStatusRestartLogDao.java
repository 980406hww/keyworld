package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.ClientStatusRestartLog;
import org.springframework.stereotype.Repository;

@Repository("clientStatusRestartLogDao2")
public interface ClientStatusRestartLogDao extends BaseMapper<ClientStatusRestartLog> {

}
